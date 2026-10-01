package demo

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.receptionist.{Receptionist, ServiceKey}
import scala.jdk.CollectionConverters.*

object MessageDiscovery:
  sealed trait Command
  case object FindMessageActor extends Command
  case class MessageListing(listing: Receptionist.Listing) extends Command
  case class SendMessage(message: String) extends Command

  def apply(messageKey: ServiceKey[MessageActor.Command]): Behavior[Command] =
    Behaviors.setup { context =>
      val listingAdapter =
        context.messageAdapter[Receptionist.Listing](MessageListing.apply)

      Behaviors.receiveMessage {
        case FindMessageActor =>
          context.system.receptionist ! Receptionist.Subscribe(messageKey, listingAdapter)
          Behaviors.same

        case MessageListing(listing) =>
          val refs =
            listing.getServiceInstances(messageKey).asScala.toVector
          println(s"Found ${refs.size} MessageActors")
          MessageDiscoveryActive(refs, 0)

        case SendMessage(message) =>
          // We will add selection logic here next
          println("MessageActors not discovered yet")
          Behaviors.same
      }
    }