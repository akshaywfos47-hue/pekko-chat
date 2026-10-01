package demo

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object ChatActor:
  sealed trait Command
  case class ChatMessage(message: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.receiveMessage {
      case ChatMessage(message) =>
        println(s"ChatActor received: $message")
        Behaviors.same
    }