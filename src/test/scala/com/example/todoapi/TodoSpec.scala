package com.example.todoapi

import cats.effect.IO
import com.example.todoapi.service.TodoService
import org.http4s.*
import org.http4s.implicits.*
import munit.CatsEffectSuite

class TodoSpec extends CatsEffectSuite:

  test("Get all todo lists for a user returns status code 200") {
    assertIO(getAllTodosResponse.map(_.status) ,Status.Ok)
  }

  private[this] val getAllTodosResponse: IO[Response[IO]] = 
    val getAllTodoLists = Request[IO](Method.GET, uri"/550e8400-e29b-41d4-a716-446655440000/todos")
    val todoService = TodoService.impl[IO]
    TodoApiRoutes.TodoRoutes(todoService).orNotFound(getAllTodoLists)
