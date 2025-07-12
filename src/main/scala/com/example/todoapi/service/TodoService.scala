package com.example.todoapi.service

import cats.Monad
import cats.implicits.catsSyntaxApplicativeId
import cats.syntax.all.*
import com.example.todoapi.model.{Status, Todo, TodoList}
import com.example.todoapi.repository.TodoRepository

import java.util.UUID

trait TodoService[F[_]]:
    def getAllTodoLists(userId: UUID): F[Either[Error, Array[UUID]]]
    def createTodoList(userId: UUID): F[Either[Error, UUID]]
    def getTodoList(userId: UUID, id: UUID): F[Either[Error, TodoList]]
    def updateTodo(userId: UUID, todoListId: UUID, id: UUID, name: String, status: Status): F[Either[Error, Todo]]
    def deleteTodo(userId: UUID, todoListId: UUID, id: UUID): F[Either[Error, Unit]]
    def addTodo(userId: UUID, todoListId: UUID, name: String): F[Either[Error, UUID]]

object TodoService:
    def impl[F[_]: Monad](repository: TodoRepository[F]): TodoService[F] = new TodoService[F]:
        private def withUser[A](userId: UUID)(f: UUID => F[Either[Error, A]]): F[Either[Error, A]] = {
            repository.getUser(userId).flatMap {
                case Some(userFound) => f(userFound)
                case None => Left(Error("user not found")).pure[F]
            }
        }

        private def withUserForTodoList[A](userId: UUID, todoListId: UUID)(f: ((UUID, TodoList))
          => F[Either[Error, A]]): F[Either[Error, A]] = {
            withUser(userId) { user =>
                repository.getTodoList(todoListId).flatMap {
                    case Some(list) if list.userId == user => f(user, list)
                    case Some(_) => Left(Error("user does not have access to todo list")).pure[F]
                    case None => Left(Error("todo list not found")).pure[F]
                }
            }
        }

        override def getAllTodoLists(userId: UUID): F[Either[Error, Array[UUID]]] = {
            withUser(userId) { user =>
                repository.getAllTodoLists(user).map(Right(_))
            }
        }

        override def createTodoList(userId: UUID): F[Either[Error, UUID]] = {
            withUser(userId) { user =>
                repository.createTodoList(user).map(Right(_))
            }
        }

        override def getTodoList(userId: UUID, id: UUID): F[Either[Error, TodoList]] = {
            withUserForTodoList(userId, id) { (_, list) =>
                list.pure[F].map(Right(_))
            }
        }

        override def updateTodo(userId: UUID, todoListId: UUID, id: UUID, name: String, status: Status): F[Either[Error, Todo]] = {
            withUserForTodoList(userId, todoListId) { (_, list) =>
                list.todos.find(_.id == id) match {
                    case Some(_) =>
                        repository.updateTodo(todoListId, id, name, status).map {
                            case Some(updated) => Right(updated)
                            case None          => Left(Error("could not update todo"))
                        }
                    case None =>
                        Left(Error("todo not found")).pure[F]
                }
            }
        }

        override def deleteTodo(userId: UUID, todoListId: UUID, id: UUID): F[Either[Error, Unit]] = {
            withUserForTodoList(userId, todoListId) { (_, list) =>
              repository.deleteTodo(list.id, id).map(Right(_))
            }
        }

        override def addTodo(userId: UUID, todoListId: UUID, name: String): F[Either[Error, UUID]] = {
            withUserForTodoList(userId, todoListId) { (_, list) =>
              repository.addTodo(list.id, name).map {
                  case Some(newTodoId) => Right(newTodoId)
                  case None => Left(Error("could not add todo"))
              }
            }
        }