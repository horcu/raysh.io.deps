package pg.example.service;

import pg.example.model.Link;
import pg.example.model.Node;

import java.util.List;

/**
 * Created by olesya.daderko on 11/5/18.
 */
public interface LinkService {

    List<Link> getLinks();
    Link getLink(String linkId);
    boolean saveLink(Link link);
    boolean deleteLink(String linkId);
}
