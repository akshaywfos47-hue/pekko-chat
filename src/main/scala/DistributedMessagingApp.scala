import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.cluster.ClusterEvent
import org.apache.pekko.cluster.typed.{Cluster, Subscribe}

object DistributedMessagingApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem(Behaviors.empty, "ClusterSystem")

    val cluster = Cluster(system)

    println(s"Node address: ${cluster.selfMember.address}")
    println(s"Cluster members: ${cluster.state.members}")

    val listener = system.systemActorOf(
      Behaviors.receiveMessage[ClusterEvent.ClusterDomainEvent] { event =>
        println(s"Cluster event: $event")
        println(s"Current members: ${cluster.state.members}")
        Behaviors.same
      },
      "clusterListener"
    )

    cluster.subscriptions ! Subscribe(listener, classOf[ClusterEvent.MemberEvent])
  }
}