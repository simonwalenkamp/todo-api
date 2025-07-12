package com.example.todoapi.model

import io.circe.{Decoder, Encoder}

enum Status {
  case Todo, Done
}

object Status {
  given Decoder[Status] = Decoder.decodeString.emap {
    case "Todo" => Right(Status.Todo)
    case "Done" => Right(Status.Done)
    case other => Left(s"Invalid status: $other. Expected: Todo or Done")
  }

  given Encoder[Status] = Encoder.encodeString.contramap {
    case Status.Todo => "Todo"
    case Status.Done => "Done"
  }
}
