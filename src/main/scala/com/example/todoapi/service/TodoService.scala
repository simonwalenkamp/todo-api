package com.example.todoapi.service

import cats.Applicative
import com.example.todoapi.model.{Status, Todo, TodoList}
import cats.implicits.catsSyntaxApplicativeId

import java.util.UUID

trait TodoService[F[_]]:
    def getAllTodoLists(userId: UUID): F[Array[UUID]]
    def createTodoList(userId: UUID): F[UUID]
    def getTodoList(userId: UUID, id: UUID): F[TodoList]
    def updateTodo(userId: UUID, todoListId: UUID, id: UUID, name: String, status: Status): F[Todo]
    def deleteTodo(userId: UUID, todoListId: UUID, id: UUID): F[Unit]

object TodoService:
    def impl[F[_]: Applicative]: TodoService[F] = new TodoService[F]:
        // Some dummy UUIDs for testing
        private val dummyUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        private val dummyListId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001")
        private val dummyTodoId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002")

        // Dummy todo and todolist
        private val dummyTodo = Todo(
            id = dummyTodoId,
            name = "Example Todo",
            status = Status.Todo
        )

        private val dummyTodoList = TodoList(
            id = dummyListId,
            userId = dummyUserId,
            todos = List(dummyTodo)
        )

        def getAllTodoLists(userId: UUID): F[Array[UUID]] =
            Array(dummyListId).pure[F]

        def createTodoList(userId: UUID): F[UUID] =
            dummyListId.pure[F]

        def getTodoList(userId: UUID, id: UUID): F[TodoList] =
            dummyTodoList.pure[F]

        def updateTodo(userId: UUID, todoListId: UUID, id: UUID, name: String, status: Status): F[Todo] =
            dummyTodo.copy(name = name, status = status).pure[F]

        def deleteTodo(userId: UUID, todoListId: UUID, id: UUID): F[Unit] =
            ().pure[F]
