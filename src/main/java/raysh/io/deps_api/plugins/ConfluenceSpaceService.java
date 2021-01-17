package raysh.io.deps_api.plugins;

import com.github.lucapino.confluence.rest.client.api.ClientFactory;
import com.github.lucapino.confluence.rest.client.api.SpaceClient;
import com.github.lucapino.confluence.rest.client.impl.ClientFactoryImpl;
import com.github.lucapino.confluence.rest.core.api.RequestService;
import com.github.lucapino.confluence.rest.core.api.domain.space.SpaceResultsBean;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ConfluenceSpaceService {
    private final ExecutorService executorService;
    private final RequestService requestService;
    private final APIUriProvider apiConfig;

    public ConfluenceSpaceService(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
        this.executorService = executorService;
        this.requestService = requestService;
        this.apiConfig = apiConfig;
    }

    public Future<SpaceResultsBean> run(String spaceId) {
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, apiConfig);
        SpaceClient spaceClient = factory.getSpaceClient();
        Future<SpaceResultsBean> spaces = null;
        List<String> expands = new ArrayList<String>();
        SpaceResultsBean resultsBean = null;
        expands.add("body.view");
        try {
            spaces = spaceClient.getSpaces(null, null, null, null, expands, 0, 0);
             resultsBean = spaces.get();
            resultsBean.getResults().forEach(space -> {
                System.out.println("Space: " + space.getName());
            });
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return spaces;
    }
}
