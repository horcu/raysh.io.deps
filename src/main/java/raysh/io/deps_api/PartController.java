package raysh.io.deps_api;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import raysh.io.deps_api.service.PartServiceImpl;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Controller("/parts")
public class PartController {
    private static final long serialVersionUID=1L;
    public static Logger logger= Logger.getLogger("global");
    private final PartServiceImpl crudService;

    @Inject
    public PartController(PartServiceImpl crudService) {
        this.crudService = crudService;
    }

    @Get("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<raysh.io.deps_api.model.Part> parts() {
        return crudService.getParts();
    }

    @Get("/{partId}")
    @Produces(MediaType.APPLICATION_JSON)
    public raysh.io.deps_api.model.Part user(String partId) {
        return crudService.getPart(partId);
    }

    @Post("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public HttpStatus save(@Body raysh.io.deps_api.model.Part part) {
        logger.info("got in!");
        return crudService.savePart(part) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @Delete("/{partId}")
    public HttpStatus delete(String partId) {
        logger.info("got in!");
        return crudService.deletePart(partId) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }
}