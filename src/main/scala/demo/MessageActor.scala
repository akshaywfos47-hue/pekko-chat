package demo

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.cluster.typed.Cluster

// Define a marker trait for Pekko serialization
trait CborSerializable

object MessageActor:
  sealed trait Command extends CborSerializable
  case class ProcessMessage(message: String) extends Command

  def apply(): Behavior[Command] =
    Behaviors.setup { context =>
      val selfAddress = Cluster(context.system).selfMember.address

      Behaviors.receiveMessage {
        case ProcessMessage(message) =>
          context.log.info(s"[(selfAddress] MessageActor (){context.self.path.name}) received: $message")
          Behaviors.same
      }
    }