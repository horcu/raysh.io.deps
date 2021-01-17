package raysh.io.deps_api.service;

import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.reactivex.pgclient.*;
import raysh.io.deps_api.model.Part;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@Singleton
public class PartServiceImpl implements PartService {
    private static final long serialVersionUID=1L;
    public static Logger logger= Logger.getLogger("global");

    private PgPool client;

    public PartServiceImpl() {
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
    public List<Part> getParts() {
        List<Part> parts = new LinkedList<>();
        PgIterator pgIterator = client.rxPreparedQuery("select * from parts;").blockingGet().iterator();
        while (pgIterator.hasNext()) {
            Row row = pgIterator.next();

            String id = row.getString("id");
            String jsonData = row.getString("data");
            logger.info("id: " + id);
            logger.info("json: " + jsonData);
            parts.add(new Part(row.getString("id"),  row.getString("json")));
        }
        return parts;
    }

    @Override
    public Part getPart(String partId) {
        List<Part> part = new LinkedList<>();
        PgIterator pgIterator = client.rxPreparedQuery("SELECT * from parts WHERE id=$1", Tuple.of((String) partId))
                .blockingGet().iterator();
        while (pgIterator.hasNext()) {
            Row row = pgIterator.next();

            part.add(new Part(row.getString("id"), row.getString("json")));
        }
        return part.iterator().next();
    }

    @Override
    public boolean savePart(Part part) {
        String sqlQuery = "INSERT INTO parts (id, json) VALUES ($1, $2) ON CONFLICT (id) DO UPDATE SET json = $2";
        logger.info(sqlQuery);
        logger.info("saving: " + part.getData());

       client.preparedQuery(sqlQuery, Tuple.of(part.getId(), part.getData()), ar -> {
            if (ar.succeeded()) {
                PgRowSet rows = ar.result();
                logger.info("saved part(s) successfully");
                System.out.println(rows.rowCount());
            } else {
                logger.info("couldn't save part(s) successfully");
                System.out.println("Failure: " + ar.cause().getMessage());
            }
        });
        return true;
    }

    @Override
    public boolean deletePart(String partId) {
        client.preparedQuery("DELETE FROM parts WHERE id=$1", Tuple.of((String) partId),  ar -> {
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
