package demo

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object MessageActor:
  sealed trait Command
  case class ProcessMessage(message: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.receiveMessage {
      case ProcessMessage(message) =>
        println(s"MessageActor received: $message")
        Behaviors.same
    }