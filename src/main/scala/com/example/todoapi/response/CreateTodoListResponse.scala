package com.example.todoapi.response

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

import java.util.UUID

case class CreateTodoListResponse(id: UUID)

object CreateTodoListResponse:
  given Encoder[CreateTodoListResponse] = Encoder.AsObject.derived[CreateTodoListResponse]
  given [F[_]]: EntityEncoder[F, CreateTodoListResponse] = jsonEncoderOf
  given Decoder[CreateTodoListResponse] = Decoder.derived[CreateTodoListResponse]
  given [F[_] : Concurrent]: EntityDecoder[F, CreateTodoListResponse] = jsonOf
