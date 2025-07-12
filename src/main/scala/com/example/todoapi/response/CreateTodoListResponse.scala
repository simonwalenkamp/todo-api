package com.example.todoapi.response

import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import java.util.UUID

case class CreateTodoListResponse(id: UUID)

object CreateTodoListResponse:
  given Encoder[CreateTodoListResponse] = Encoder.AsObject.derived[CreateTodoListResponse]
  given [F[_]]: EntityEncoder[F, CreateTodoListResponse] = jsonEncoderOf

