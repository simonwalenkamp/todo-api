package com.example.todoapi.response

import com.example.todoapi.model.Todo
import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import java.util.UUID

case class TodoResponse(id: UUID, name: String, status: String)

object TodoResponse:
  def fromTodo(todo: Todo): TodoResponse =
    TodoResponse(todo.id, todo.name, todo.status.toString)

  given Encoder[TodoResponse] = Encoder.AsObject.derived[TodoResponse]
  given [F[_]]: EntityEncoder[F, TodoResponse] = jsonEncoderOf
