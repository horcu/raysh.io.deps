package pg.example.service;

import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.pgclient.data.Json;
import io.reactiverse.reactivex.pgclient.PgClient;
import io.reactiverse.reactivex.pgclient.PgIterator;
import io.reactiverse.reactivex.pgclient.PgPool;
import io.reactiverse.reactivex.pgclient.PgRowSet;
import io.reactiverse.reactivex.pgclient.Row;
import io.reactiverse.reactivex.pgclient.Tuple;
import io.vertx.core.json.JsonObject;
import org.json.JSONObject;
import pg.example.model.Node;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class NodeServiceImpl implements NodeService {
    private static final long serialVersionUID=1L;
    public static Logger logger= Logger.getLogger("global");

    private PgPool client;

    public NodeServiceImpl() {
        PgPoolOptions options = new PgPoolOptions()
                .setPort(5432)
                .setHost("localhost")
                .setDatabase("deps")
                .setUser("deps")
                .setPassword("timbub12")
                .setMaxSize(5);
        client = PgClient.pool(options);
    }

    @Override
    public List<Node> getNodes() {
        List<Node> nodes = new LinkedList<>();
        PgIterator pgIterator = client.rxPreparedQuery("select * from nodes;").blockingGet().iterator();
        while (pgIterator.hasNext()) {
            Row row = pgIterator.next();

            String id = row.getString("id");
            String jsonData = row.getString("data");
            logger.info("id: " + id);
            logger.info("json: " + jsonData);
            nodes.add(new Node(row.getString("id"),  row.getString("json"), row.getString("parentid")));
        }
        return nodes;
    }

    @Override
    public Node getNode(String nodeId) {
        List<Node> nodes = new LinkedList<>();
        PgIterator pgIterator = client.rxPreparedQuery("SELECT * from nodes WHERE id=$1", Tuple.of((String) nodeId))
                .blockingGet().iterator();
        while (pgIterator.hasNext()) {
            Row row = pgIterator.next();

            nodes.add(new Node(row.getString("id"), row.getString("json"), row.getString("parentid")));
        }
        return nodes.iterator().next();
    }

    @Override
    public boolean saveNode(Node node) {
        String sqlQuery = "INSERT INTO nodes (id, json) VALUES ($1, $2) ON CONFLICT (id) DO UPDATE SET json = $2";
        logger.info(sqlQuery);
        logger.info("saving: " + node.getData());

       client.preparedQuery(sqlQuery, Tuple.of(node.getId(), node.getData()), ar -> {
            if (ar.succeeded()) {
                PgRowSet rows = ar.result();
                logger.info("saved node(s) successfully");
                System.out.println(rows.rowCount());
            } else {
                logger.info("couldn't save node(s) successfully");
                System.out.println("Failure: " + ar.cause().getMessage());
            }
        });
        return true;
    }

    @Override
    public boolean deleteNode(String nodeId) {
        client.preparedQuery("DELETE FROM nodes WHERE id=$1", Tuple.of((String) nodeId),  ar -> {
            if (ar.succeeded()) {
                PgRowSet rows = ar.result();
                System.out.println(rows.rowCount());
            } else {
                System.out.println("Failure: " + ar.cause().getMessage());
            }
        });
        return true;
    }
}
