package com.example.todoapi

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run: IO[Nothing] = TodoApiServer.run[IO]
