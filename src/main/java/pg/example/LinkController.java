package pg.example;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import pg.example.model.Link;
import pg.example.model.Node;
import pg.example.service.LinkServiceImpl;
import pg.example.service.NodeServiceImpl;

import javax.inject.Inject;
import java.util.List;

@Controller("/links")
public class LinkController {

    private final LinkServiceImpl crudService;

    @Inject
    public LinkController(LinkServiceImpl crudService) {
        this.crudService = crudService;
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Link> links() {
        return crudService.getLinks();
    }

    @Get("/{linkId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Link link(String linkId) {
        return crudService.getLink(linkId);
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpStatus save(@Body Link link) {
        return crudService.saveLink(link) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @Delete("/{linkId}")
    public HttpStatus delete(String linkId) {
        return crudService.deleteLink(linkId) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }
}