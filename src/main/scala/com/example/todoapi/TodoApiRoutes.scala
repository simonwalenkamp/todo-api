package com.example.todoapi

import cats.effect.Concurrent
import cats.syntax.all.*
import com.example.todoapi.request.{AddTodoRequest, UpdateTodoRequest}
import com.example.todoapi.response.{CreateTodoListResponse, CreateTodoResponse, GetAllTodoListsResponse, GetTodoListResponse, TodoResponse}
import com.example.todoapi.service.TodoService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object TodoApiRoutes:

  def TodoRoutes[F[_]: Concurrent](todoService: TodoService[F]): HttpRoutes[F] =  // Changed from Sync to Concurrent
    val dsl = new Http4sDsl[F]{}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / UUIDVar(userId) / "todos" =>
        todoService.getAllTodoLists(userId).flatMap {
          case Right(todoLists) => Ok(GetAllTodoListsResponse(todoLists))
          case Left(error) => NotFound(error.getMessage)
        }

      case POST -> Root / UUIDVar(userId) / "todos" =>
        todoService.createTodoList(userId).flatMap( {
          case Right(id) => Created(CreateTodoListResponse(id))
          case Left(error) => NotFound(error.getMessage)
        })

      case GET -> Root / UUIDVar(userId) / "todos" / UUIDVar(id) =>
        todoService.getTodoList(userId, id).flatMap( {
          case Right(todoList) => Ok(GetTodoListResponse.fromTodoList(todoList))
          case Left(error) => NotFound(error.getMessage)
        })

      case req @ POST -> Root / UUIDVar(userId) / "todos" / UUIDVar(listId) =>
        req.as[AddTodoRequest].flatMap { addRequest =>
          todoService.addTodo(userId, listId, addRequest.name).flatMap {
            case Right(id) => Created(CreateTodoResponse(id))
            case Left(error) => NotFound(error.getMessage)
          }
        }

      case req @ PUT -> Root / UUIDVar(userId) / "todos" / UUIDVar(listId) / UUIDVar(todoId) =>
        req.as[UpdateTodoRequest].flatMap { updateRequest =>
          todoService.updateTodo(userId, listId, todoId, updateRequest.name, updateRequest.status).flatMap {
            case Right(todo) => Ok(TodoResponse.fromTodo(todo))
            case Left(error) => NotFound(error.getMessage)
          }
        }

      case DELETE -> Root / UUIDVar(userId) / "todos" / UUIDVar(todoListId) / UUIDVar(id) =>
        todoService.deleteTodo(userId, todoListId, id).flatMap( {
          case Right(_) => NoContent()
          case Left(error) => NotFound(error.getMessage)
        })
    }