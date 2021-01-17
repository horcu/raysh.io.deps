package raysh.io.deps_api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by olesya.daderko on 11/5/18.
 */
public class Part {
    private String id;
    private String json;

    @JsonCreator
    public Part(@JsonProperty("id") String id, @JsonProperty("json") String data) {
        this.id = id;
        this.json = data;
    }

    public String getId() {
        return id;
    }

    public String getData() {
        return json;
    }

}
