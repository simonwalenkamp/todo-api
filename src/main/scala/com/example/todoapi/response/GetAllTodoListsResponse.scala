package com.example.todoapi.response

import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import java.util.UUID

case class GetAllTodoListsResponse(todoLists: Array[UUID])

object GetAllTodoListsResponse:
  given Encoder[GetAllTodoListsResponse] = Encoder.AsObject.derived[GetAllTodoListsResponse]
  given [F[_]]: EntityEncoder[F, GetAllTodoListsResponse] = jsonEncoderOf

