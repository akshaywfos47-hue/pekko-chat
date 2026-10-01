package demo

import org.apache.pekko.actor.typed.ActorSystem

object DistributedMessagingApp:
  def main(args: Array[String]): Unit =
    ActorSystem(AppBehavior(), "ClusterSystem")