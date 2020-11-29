package pg.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by olesya.daderko on 11/5/18.
 */
public class Link {
    private String id;
    private String json;
    private String parentid;

    @JsonCreator
    public Link(@JsonProperty("id") String id, @JsonProperty("json") String data, @JsonProperty("parentid") String parentid) {
        this.id = id;
        this.json = data;
        this.parentid = parentid;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return json;
    }

    public String getParentid() {
        return parentid;
    }
}
