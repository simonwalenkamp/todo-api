package com.example.todoapi.request

import cats.effect.Concurrent
import io.circe.Decoder
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

case class AddTodoRequest(name: String)

object AddTodoRequest:
  given Decoder[AddTodoRequest] = Decoder.derived[AddTodoRequest]
  given [F[_]: Concurrent]: EntityDecoder[F, AddTodoRequest] = jsonOf