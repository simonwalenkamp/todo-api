package com.example.todoapi.response

import com.example.todoapi.model.{Todo, TodoList}
import io.circe.Encoder
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import java.util.UUID

case class GetTodoListResponse(id: UUID, todos: List[TodoResponse])

object GetTodoListResponse:
  given Encoder[GetTodoListResponse] = Encoder.AsObject.derived[GetTodoListResponse]
  given [F[_]]: EntityEncoder[F, GetTodoListResponse] = jsonEncoderOf
  
  def fromTodoList(todoList: TodoList): GetTodoListResponse =
    GetTodoListResponse(
      id = todoList.id,
      todos = todoList.todos.map(TodoResponse.fromTodo)
    )

