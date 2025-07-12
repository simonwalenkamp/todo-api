package com.example.todoapi.request

import com.example.todoapi.model.Status
import io.circe.Decoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf
import cats.effect.Concurrent

case class UpdateTodoRequest(name: String, status: Status)

object UpdateTodoRequest:
  given Decoder[UpdateTodoRequest] = Decoder.derived[UpdateTodoRequest]
  given [F[_]: Concurrent]: EntityDecoder[F, UpdateTodoRequest] = jsonOf