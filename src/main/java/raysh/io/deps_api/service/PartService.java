package raysh.io.deps_api.service;

import raysh.io.deps_api.model.Part;

import java.util.List;

public interface PartService {

    List<Part> getParts();
    Part getPart(String partId);
    boolean savePart(Part part);
    boolean deletePart(String partId);
}
