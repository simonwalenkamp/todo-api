package com.example.todoapi.response

import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import java.util.UUID

case class CreateTodoResponse(id: UUID)

object CreateTodoResponse:
  given Encoder[CreateTodoResponse] = Encoder.AsObject.derived[CreateTodoResponse]
  given [F[_]]: EntityEncoder[F, CreateTodoResponse] = jsonEncoderOf

