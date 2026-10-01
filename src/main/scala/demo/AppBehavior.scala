package demo

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.cluster.ClusterEvent
import org.apache.pekko.cluster.typed.{Cluster, Subscribe}
import org.apache.pekko.actor.typed.receptionist.{Receptionist, ServiceKey}
import scala.concurrent.duration.*

object AppBehavior:
  def apply(): Behavior[Nothing] =
    Behaviors.setup { context =>
      val cluster = Cluster(context.system)

      println(s"Node address: ${cluster.selfMember.address}")
      println(s"Cluster members: ${cluster.state.members}")

      val listener = context.spawn(
        Behaviors.receiveMessage[ClusterEvent.ClusterDomainEvent] { event =>
          println(s"Cluster event: $event")
          println(s"Current members: ${cluster.state.members}")
          Behaviors.same
        },
        "clusterListener"
      )

      cluster.subscriptions ! Subscribe(
        listener,
        classOf[ClusterEvent.MemberEvent]
      )

      //creating actors 
      val messageActor = context.spawn(MessageActor(), "messageActor")
      //multiple actors with one behavior
      val messageActor1 = context.spawn(MessageActor(), "messageActor1")
      val messageActor2 = context.spawn(MessageActor(), "messageActor2")
      val messageActor3 = context.spawn(MessageActor(), "messageActor3")
      
      val notificationActor = context.spawn(NotificationActor(), "notificationActor")
      val chatActor = context.spawn(ChatActor(), "chatActor")

      // cfeating keys 
      val messageKey = ServiceKey[MessageActor.Command]("message-service")
      val notificationKey = ServiceKey[NotificationActor.Command]("notification-service")
      val chatKey = ServiceKey[ChatActor.Command]("chat-service")

      
      // registrating with receptionist
      context.system.receptionist ! Receptionist.Register(messageKey, messageActor)
      context.system.receptionist ! Receptionist.Register(notificationKey, notificationActor)
      context.system.receptionist ! Receptionist.Register(chatKey, chatActor)

      //multiple instances with only one key 
      context.system.receptionist ! Receptionist.Register(messageKey, messageActor1)
      context.system.receptionist ! Receptionist.Register(messageKey, messageActor2)
      context.system.receptionist ! Receptionist.Register(messageKey, messageActor3)

      val messageDiscovery = context.spawn(MessageDiscovery(messageKey), "messageDiscovery")
     // messageDiscovery ! MessageDiscovery.FindMessageActor
   

      context.scheduleOnce(
        1.second,
        messageDiscovery,
        MessageDiscovery.FindMessageActor
      )

      context.scheduleOnce(
        3.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 1")
      )

      context.scheduleOnce(
        4.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 2")
      )

      context.scheduleOnce(
        5.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 3")
      )

      context.scheduleOnce(
        6.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 4")
      )

      context.scheduleOnce(
        7.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 5")
      )

      context.scheduleOnce(
        8.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 6")
      )

      context.scheduleOnce(
        9.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 7")
      )

      context.scheduleOnce(
        10.seconds,
        messageDiscovery,
        MessageDiscovery.SendMessage("Hello 8")
      )

      Behaviors.empty
    }