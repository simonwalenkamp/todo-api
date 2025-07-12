package com.example.todoapi.repository

import com.example.todoapi.model.{Status, Todo, TodoList}
import cats.effect.Sync
import cats.implicits.*

import java.util.UUID
import scala.collection.mutable

class TodoRepository[F[_]: Sync]:

  private val users: mutable.Set[UUID] = mutable.Set(
    UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
    UUID.fromString("67e55044-10b1-426f-9247-bb680e5fe0c8")
  )

  private val todoLists: mutable.Map[UUID, TodoList] = mutable.Map.empty

  def getUser(userId: UUID): F[Option[UUID]] = {
    users.find(_ == userId).pure[F]
  }
    
  def getAllTodoLists(userId: UUID): F[Array[UUID]] =
    todoLists.values.filter(_.userId == userId).map(_.id).toArray.pure[F]
      
  def createTodoList(userId: UUID): F[UUID] =
    for {
      newListId <- Sync[F].delay(UUID.randomUUID())
      _ = todoLists.put(newListId, TodoList(newListId, userId, List.empty))
    } yield newListId

  def getTodoList(listId: UUID): F[Option[TodoList]] =
    todoLists.get(listId).pure[F]

  def updateTodo(todoListId: UUID, todoId: UUID, name: String, status: Status): F[Option[Todo]] = {
    todoLists.get(todoListId).flatMap { list =>
      list.todos.find(_.id == todoId).map { _ =>
        val updatedTodo = Todo(todoId, name, status)
        val updatedTodos = list.todos.map {
          case todo if todo.id == todoId => updatedTodo
          case todo => todo
        }
        val _ = todoLists.put(todoListId, list.copy(todos = updatedTodos))
        updatedTodo
      }
    }.pure[F]
  }

  def deleteTodo(todoListId: UUID, todoId: UUID): F[Unit] =
    todoLists.get(todoListId) match
      case Some(list) =>
        val updatedTodos = list.todos.filterNot(_.id == todoId)
        val _ = todoLists.put(todoListId, list.copy(todos = updatedTodos))
        ().pure[F]
      case None => ().pure[F]

  def addTodo(todoListId: UUID, name: String): F[Option[UUID]] =
    todoLists.get(todoListId) match {
      case Some(list) =>
        for {
          newTodoId <- Sync[F].delay(UUID.randomUUID())
          newTodo = Todo(newTodoId, name, Status.Todo)
          updatedList = list.copy(todos = list.todos :+ newTodo)
          _ = todoLists.put(todoListId, updatedList)
        } yield Some(newTodoId)

      case None => None.pure[F]
    }