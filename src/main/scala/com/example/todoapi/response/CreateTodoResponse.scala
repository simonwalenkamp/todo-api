package com.example.todoapi.response

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

import java.util.UUID

case class CreateTodoResponse(id: UUID)

object CreateTodoResponse:
  given Encoder[CreateTodoResponse] = Encoder.AsObject.derived[CreateTodoResponse]
  given [F[_]]: EntityEncoder[F, CreateTodoResponse] = jsonEncoderOf
  given Decoder[CreateTodoResponse] = Decoder.derived[CreateTodoResponse]
  given [F[_] : Concurrent]: EntityDecoder[F, CreateTodoResponse] = jsonOf
