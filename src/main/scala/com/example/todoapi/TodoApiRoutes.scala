package com.example.todoapi

import cats.effect.Concurrent
import cats.syntax.all.*
import com.example.todoapi.request.UpdateTodoRequest
import com.example.todoapi.response.{CreateTodoListResponse, GetAllTodoListsResponse, GetTodoListResponse, TodoResponse}
import com.example.todoapi.service.TodoService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object TodoApiRoutes:

  def TodoRoutes[F[_]: Concurrent](todoService: TodoService[F]): HttpRoutes[F] =  // Changed from Sync to Concurrent
    val dsl = new Http4sDsl[F]{}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / UUIDVar(userId) / "todos" =>
        for {
          todoLists <- todoService.getAllTodoLists(userId)
          resp <- Ok(GetAllTodoListsResponse(todoLists))
        } yield resp

      case POST -> Root / UUIDVar(userId) / "todos" =>
        for {
          id <- todoService.createTodoList(userId)
          resp <- Ok(CreateTodoListResponse(id))
        } yield resp

      case GET -> Root / UUIDVar(userId) / "todos" / UUIDVar(id) =>
        for {
          todoList <- todoService.getTodoList(userId, id)
          resp <- Ok(GetTodoListResponse.fromTodoList(todoList))
        } yield resp

      case req @ PUT -> Root / UUIDVar(userId) / "todos" / UUIDVar(todoListId) / UUIDVar(id) =>
        for {
          updateReq <- req.as[UpdateTodoRequest]
          updated <- todoService.updateTodo(userId, todoListId, id, updateReq.name, updateReq.status)
          resp <- Ok(TodoResponse.fromTodo(updated))
        } yield resp

      case DELETE -> Root / UUIDVar(userId) / "todos" / UUIDVar(todoListId) / UUIDVar(id) =>
        for {
          _ <- todoService.deleteTodo(userId, todoListId, id)
          resp <- NoContent()
        } yield resp
    }