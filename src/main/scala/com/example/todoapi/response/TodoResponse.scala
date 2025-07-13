package com.example.todoapi.response

import cats.effect.Concurrent
import com.example.todoapi.model.Todo
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

import java.util.UUID

case class TodoResponse(id: UUID, name: String, status: String)

object TodoResponse:
  def fromTodo(todo: Todo): TodoResponse =
    TodoResponse(todo.id, todo.name, todo.status.toString)

  given Encoder[TodoResponse] = Encoder.AsObject.derived[TodoResponse]
  given [F[_]]: EntityEncoder[F, TodoResponse] = jsonEncoderOf
  given Decoder[TodoResponse] = Decoder.derived[TodoResponse]
  given [F[_] : Concurrent]: EntityDecoder[F, TodoResponse] = jsonOf