package com.example.todoapi.request

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class AddTodoRequest(name: String)

object AddTodoRequest:
  given Decoder[AddTodoRequest] = Decoder.derived[AddTodoRequest]
  given [F[_]: Concurrent]: EntityDecoder[F, AddTodoRequest] = jsonOf
  given Encoder[AddTodoRequest] = Encoder.AsObject.derived[AddTodoRequest]
  given [F[_]]: EntityEncoder[F, AddTodoRequest] = jsonEncoderOf