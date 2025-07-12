package com.example.todoapi.model

import java.util.UUID

final case class Todo(id: UUID, name: String, status: Status)