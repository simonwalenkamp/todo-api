package com.example.todoapi.response

import cats.effect.Concurrent
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

import java.util.UUID

case class GetAllTodoListsResponse(todoLists: Array[UUID])

object GetAllTodoListsResponse:
  given Encoder[GetAllTodoListsResponse] = Encoder.AsObject.derived[GetAllTodoListsResponse]
  given [F[_]]: EntityEncoder[F, GetAllTodoListsResponse] = jsonEncoderOf
  given Decoder[GetAllTodoListsResponse] = Decoder.derived[GetAllTodoListsResponse]
  given [F[_] : Concurrent]: EntityDecoder[F, GetAllTodoListsResponse] = jsonOf
