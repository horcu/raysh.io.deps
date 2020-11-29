package pg.example.service;

import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.reactivex.pgclient.*;
import pg.example.model.Link;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class LinkServiceImpl implements LinkService {

    private PgPool client;

    public LinkServiceImpl() {
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
    public List<Link> getLinks() {
        List<Link> links = new LinkedList<>();
        PgIterator pgIterator = client.rxPreparedQuery("SELECT * from links").blockingGet().iterator();
        while (pgIterator.hasNext()) {
            Row row = pgIterator.next();

            links.add(new Link(row.getString("id"), row.getString("json"), row.getString("parentid")));
        }
        return links;
    }

    @Override
    public Link getLink(String linkId) {
        List<Link> links = new LinkedList<>();
        PgIterator pgIterator = client.rxPreparedQuery("SELECT * from links WHERE id=$1", Tuple.of((String) linkId))
                .blockingGet().iterator();
        while (pgIterator.hasNext()) {
            Row row = pgIterator.next();

            links.add(new Link(row.getString("id"), row.getString("json"), row.getString("parentid")));
        }
        return links.iterator().next();
    }

    @Override
    public boolean saveLink(Link link) {
        String sqlQuery = "INSERT INTO links (id, json) VALUES ($1, $2) ON CONFLICT (id) DO UPDATE SET json = $2";
        client.preparedQuery(sqlQuery, Tuple.of(link.getId(), link.getLink()), ar -> {
            if (ar.succeeded()) {
                PgRowSet rows = ar.result();
                System.out.println(rows.rowCount());
            } else {
                System.out.println("Failure: " + ar.cause().getMessage());
            }
        });
        return true;
    }

    @Override
    public boolean deleteLink(String linkId) {
        client.preparedQuery("DELETE FROM links WHERE id=$1", Tuple.of((String) linkId),  ar -> {
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
