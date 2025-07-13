package com.example.todoapi.request

import cats.effect.Concurrent
import com.example.todoapi.model.Status
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

case class UpdateTodoRequest(name: String, status: Status)

object UpdateTodoRequest:
  given Decoder[UpdateTodoRequest] = Decoder.derived[UpdateTodoRequest]
  given [F[_]: Concurrent]: EntityDecoder[F, UpdateTodoRequest] = jsonOf
  given Encoder[UpdateTodoRequest] = Encoder.AsObject.derived[UpdateTodoRequest]
  given [F[_]]: EntityEncoder[F, UpdateTodoRequest] = jsonEncoderOf