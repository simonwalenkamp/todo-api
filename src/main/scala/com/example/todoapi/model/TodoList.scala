package com.example.todoapi.model

import java.util.UUID

final case class TodoList(id: UUID, userId: UUID, todos: List[Todo])