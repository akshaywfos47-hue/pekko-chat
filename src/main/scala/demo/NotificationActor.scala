package demo

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors

object NotificationActor:
  sealed trait Command
  case class SendNotification(message: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.receiveMessage {
      case SendNotification(message) =>
        println(s"NotificationActor received: $message")
        Behaviors.same
    }