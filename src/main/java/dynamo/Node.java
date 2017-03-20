package dynamo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dynamo.messages.StartJoinMessage;

/**
 * Created by StefanoFioravanzo on 15/03/2017.
 */
public class Node {
    public static void main(String[] args){

        String remoteIp = null;
        String remotePort = null;
        Integer localId = null;

        // First we parse the arguments given from CLI
        // If JOIN we have to create a new actor and contact
        // the given remotePath to join the distributed storage system.

        // If RECOVER we will do some recovery action

        // we use a simple if-else parsing since we have a small
        // number of possible input arguments

        String error_msg = "Exactly three parameters are needed!\n" +
                "There are two options available:\n" +
                "\t-java dynamo.Node join remote_ip remote_port local_id\n" +
                "\t-java dynamo.Node recover remote_ip remote_port local_id";
        // Really there is a third option with just one argument (id) for the first actor of dynamo.

        if (args.length > 4 || args.length < 1){
            throw new IllegalArgumentException(error_msg);
        }

        // here goes the logic for joining a new node
        if (args[0].equals("join") || args[0].equals("start")){
            boolean join = false;
            if (args[0].equals("join")){
                join = true;
            }

            // TODO: Have to generate here a unique id key for the node?

            if (join){
                remoteIp = args[1];
                remotePort = args[2];
                localId = Integer.parseInt(args[3]);
            } else{
                localId = Integer.parseInt(args[1]);
            }

            Config myConfig = ConfigFactory.load("application");
            Config custom = ConfigFactory.parseString("akka.remote.netty.tcp.hostname = localhost, akka.remote.netty.tcp.port = " + (10000 + localId));

            ActorSystem system = ActorSystem.create("dynamo", custom.withFallback(myConfig));
            ActorRef localNode = null;

            // Get replication parameters from config file.
            Integer n = myConfig.getInt("dynamo.replication.N");
            Integer r = myConfig.getInt("dynamo.replication.R");
            Integer w = myConfig.getInt("dynamo.replication.W");

            if (r + w < n){
                // Illegal
                throw new IllegalArgumentException("R + W is less than N.");
            }

            // Can extend here the crete call with arguments to the
            // constructor of the dynamo.Node class
            localNode = system.actorOf(Props.create(NodeActor.class, localId, n, r, w), "node");
            System.out.println("Node started and waiting for messages (id : " + localId + ")");

            if (join){
                localNode.tell(new StartJoinMessage(remoteIp, remotePort), null);
            }

        }

        if (args[0].equals("recover")) {
            throw new IllegalArgumentException("Not yet implemented.");
            // TODO
        }else{
            // throw new IllegalArgumentException("Argument not recognized.");
        }
    }
}