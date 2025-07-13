package com.example.todoapi.response

import cats.effect.Concurrent
import com.example.todoapi.model.{Todo, TodoList}
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

import java.util.UUID

case class GetTodoListResponse(id: UUID, todos: List[TodoResponse])

object GetTodoListResponse:
  given Encoder[GetTodoListResponse] = Encoder.AsObject.derived[GetTodoListResponse]
  given [F[_]]: EntityEncoder[F, GetTodoListResponse] = jsonEncoderOf
  given Decoder[GetTodoListResponse] = Decoder.derived[GetTodoListResponse]
  given [F[_] : Concurrent]: EntityDecoder[F, GetTodoListResponse] = jsonOf

  def fromTodoList(todoList: TodoList): GetTodoListResponse =
    GetTodoListResponse(
      id = todoList.id,
      todos = todoList.todos.map(TodoResponse.fromTodo)
    )

