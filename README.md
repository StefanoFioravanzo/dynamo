# Dynamo



### Messages

ActorSystem to Node:

- **StartJoinMessage**: Tell to node that it has to join the system with given remote reference

Node to Node:

- **PeerListMessage** (Request/Response): A node asks for the list of actors present in the system
- **RequestInitItemsMessage** (Request/Response): A new node asks to its clockwise neighbor for the data it is responsible for

- **HelloMatesMessage**: A node has received the list of Nodes present in the system and the data it is responsible for, so it announces itself to the community
- **ByeMatesMessage**: tell nodes that the sender wants to leave the system. Have to replicate the data it was storing accordingly.

- **ReadOperationMessage**: This message is used for 4 different use cases
    - the client requests a read to a node (with specific item's key)
    - a node requests a read to another node (with specific item's key)
    - a node responds to node to read request
    - a node responds to client to read request
    
 
