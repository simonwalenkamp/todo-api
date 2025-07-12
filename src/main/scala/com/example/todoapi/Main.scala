package com.example.todoapi

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple:
  val run = TodoapiServer.run[IO]
