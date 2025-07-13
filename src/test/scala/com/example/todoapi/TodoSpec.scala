package com.example.todoapi

import cats.effect.IO
import com.example.todoapi.model.Status
import com.example.todoapi.repository.TodoRepository
import com.example.todoapi.request.{AddTodoRequest, UpdateTodoRequest}
import com.example.todoapi.response.{CreateTodoListResponse, CreateTodoResponse, GetAllTodoListsResponse, GetTodoListResponse}
import com.example.todoapi.service.TodoService
import munit.CatsEffectSuite
import org.http4s.{Status as HttpStatus, *}
import org.http4s.circe.CirceEntityCodec.*
import org.http4s.implicits.*

import java.util.UUID

class TodoSpec extends CatsEffectSuite:

  private val userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
  private val repository = new TodoRepository[IO]
  private val todoService = TodoService.impl[IO](repository)
  private val routes = TodoApiRoutes.TodoRoutes[IO](todoService).orNotFound

  test("Complete todo list workflow") {
    for {
      // Get all todo lists - should be empty initially
      emptyResponse <- routes.run(
        Request[IO](Method.GET, uri"/" / userId.toString / "todos")
      )
      emptyLists <- emptyResponse.as[GetAllTodoListsResponse]
      _ <- IO(assertEquals(emptyLists.todoLists.length, 0))

      // Create a new todo list
      createListResponse <- routes.run(
        Request[IO](Method.POST, uri"/" / userId.toString / "todos")
      )
      _ <- IO(assertEquals(createListResponse.status, HttpStatus.Created))
      createdList <- createListResponse.as[CreateTodoListResponse]
      todoListId = createdList.id

      // Verify the list appears in getAllTodoLists
      listsResponse <- routes.run(
        Request[IO](Method.GET, uri"/" / userId.toString / "todos")
      )
      lists <- listsResponse.as[GetAllTodoListsResponse]
      _ <- IO(assertEquals(lists.todoLists.length, 1))
      _ <- IO(assertEquals(lists.todoLists(0), todoListId))

      // Add three todos
      createTodo1 <- routes.run(
        Request[IO](Method.POST, uri"/" / userId.toString / "todos" / todoListId.toString)
          .withEntity(AddTodoRequest("First todo"))
      )
      todo1 <- createTodo1.as[CreateTodoResponse]
      
      createTodo2 <- routes.run(
        Request[IO](Method.POST, uri"/" / userId.toString / "todos" / todoListId.toString)
          .withEntity(AddTodoRequest("Second todo"))
      )
      todo2 <- createTodo2.as[CreateTodoResponse]
      
      createTodo3 <- routes.run(
        Request[IO](Method.POST, uri"/" / userId.toString / "todos" / todoListId.toString)
          .withEntity(AddTodoRequest("Third todo"))
      )
      todo3 <- createTodo3.as[CreateTodoResponse]

      // Edit second todo
      _ <- routes.run(
        Request[IO](Method.PUT, uri"/" / userId.toString / "todos" / todoListId.toString / todo2.id.toString)
          .withEntity(UpdateTodoRequest("Updated second todo", Status.Done))
      )

      // Get the list and verify the edit
      listResponse <- routes.run(
        Request[IO](Method.GET, uri"/" / userId.toString / "todos" / todoListId.toString)
      )
      todoList <- listResponse.as[GetTodoListResponse]
      _ <- IO(assertEquals(todoList.todos.length, 3))
      _ <- IO(assertEquals(todoList.todos(1).name, "Updated second todo"))
      _ <- IO(assertEquals(todoList.todos(1).status, "Done"))

      // Delete third todo
      deleteResponse <- routes.run(
        Request[IO](Method.DELETE, uri"/" / userId.toString / "todos" / todoListId.toString / todo3.id.toString)
      )
      _ <- IO(assertEquals(deleteResponse.status, HttpStatus.NoContent))

      // Verify list now has only two todos
      finalListResponse <- routes.run(
        Request[IO](Method.GET, uri"/" / userId.toString / "todos" / todoListId.toString)
      )
      finalList <- finalListResponse.as[GetTodoListResponse]
      _ <- IO(assertEquals(finalList.todos.length, 2))
      _ <- IO(assertEquals(finalList.todos.map(_.id).toSet, Set(todo1.id, todo2.id)))
    } yield ()
  }