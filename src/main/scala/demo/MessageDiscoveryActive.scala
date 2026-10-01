package demo

import org.apache.pekko.actor.typed.{ActorRef, Behavior}
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.actor.typed.receptionist.{Receptionist, ServiceKey}
import scala.jdk.CollectionConverters.*

object MessageDiscoveryActive:

  def apply(
             refs: Vector[ActorRef[MessageActor.Command]],
             nextIndex: Int
           ): Behavior[MessageDiscovery.Command] =
    Behaviors.receive { (context, message) =>
      message match

        case MessageDiscovery.SendMessage(message) =>
          if refs.nonEmpty then
            val ref = refs(nextIndex)

            println(s"Sending message to: $ref")

            ref ! MessageActor.ProcessMessage(message)

            val next = (nextIndex + 1) % refs.size

            MessageDiscoveryActive(refs, next)
          else
            println("No MessageActors available")
            Behaviors.same

        case MessageDiscovery.MessageListing(listing) =>
          val newRefs =
            listing
              .getServiceInstances(
                ServiceKey[MessageActor.Command]("message-service")
              )
              .asScala.toVector

          println(s" ak47 Updated MessageActors: ${newRefs.size}")

          MessageDiscoveryActive(newRefs, 0)

        case MessageDiscovery.FindMessageActor =>
          Behaviors.same
    }