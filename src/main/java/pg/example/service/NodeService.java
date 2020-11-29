package pg.example.service;

import pg.example.model.Node;

import java.util.List;

/**
 * Created by olesya.daderko on 11/5/18.
 */
public interface NodeService {

    List<Node> getNodes();
    Node getNode(String nodeId);
    boolean saveNode(Node node);
    boolean deleteNode(String nodeId);
}
