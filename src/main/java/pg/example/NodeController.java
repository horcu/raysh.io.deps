package pg.example;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.HttpStatus;
import io.vertx.core.json.Json;
import pg.example.model.Node;
import pg.example.service.NodeServiceImpl;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Controller("/nodes")
public class NodeController {
    private static final long serialVersionUID=1L;
    public static Logger logger= Logger.getLogger("global");
    private final NodeServiceImpl crudService;

    @Inject
    public NodeController(NodeServiceImpl crudService) {
        this.crudService = crudService;
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Node> nodes() {
        return crudService.getNodes();
    }

    @Get("/{nodeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Node user(String nodeId) {
        return crudService.getNode(nodeId);
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpStatus save(@Body Node node) {
        logger.info("got in!");
        return crudService.saveNode(node) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @Delete("/{nodeId}")
    public HttpStatus delete(String nodeId) {
        return crudService.deleteNode(nodeId) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }
}