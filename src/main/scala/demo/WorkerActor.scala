package demo

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object WorkerActor:
  sealed trait Command
  case class Work(message: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.receiveMessage {
      case Work(message) =>
        println(s"Worker received: $message")
        Behaviors.same
    }