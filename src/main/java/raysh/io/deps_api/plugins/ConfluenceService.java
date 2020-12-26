package raysh.io.deps_api.plugins;

import com.github.lucapino.confluence.rest.client.api.ClientFactory;
import com.github.lucapino.confluence.rest.client.api.ContentClient;
import com.github.lucapino.confluence.rest.client.api.SpaceClient;
import com.github.lucapino.confluence.rest.client.api.UserClient;
import com.github.lucapino.confluence.rest.client.impl.ClientFactoryImpl;
import com.github.lucapino.confluence.rest.core.api.RequestService;
import com.github.lucapino.confluence.rest.core.api.domain.UserBean;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContentResultsBean;
import com.github.lucapino.confluence.rest.core.api.domain.space.SpaceResultsBean;
import com.github.lucapino.confluence.rest.core.api.misc.ContentType;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ConfluenceService {
    private final ExecutorService executorService;
    private final RequestService requestService;
    private final APIUriProvider apiConfig;

    public ConfluenceService(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
        this.executorService = executorService;
        this.requestService = requestService;
        this.apiConfig = apiConfig;
    }

    public Future<ContentResultsBean> run() {
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, apiConfig);
        UserClient userClient = factory.getUserClient();
        try {
            Future<UserBean> anonymousUser = userClient.getCurrentUser();
            UserBean user = anonymousUser.get();
            System.out.println("Anonymous user: " + user.getDisplayName());
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        SpaceClient spaceClient = factory.getSpaceClient();
        List<String> expands = new ArrayList<String>();
        expands.add("body.view");
        try {
            Future<SpaceResultsBean> spaces = spaceClient.getSpaces(null, null, null, null, expands, 0, 0);
            SpaceResultsBean resultsBean = spaces.get();
            resultsBean.getResults().forEach(space -> {
                System.out.println("Space: " + space.getName());
            });
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Future<ContentResultsBean> contents = null;
        ContentClient contentClient = factory.getContentClient();
        List<String> expandsC = new ArrayList<>();
        expandsC.add("body.view");
        try {
             contents = contentClient.getContent(ContentType.PAGE, null, null, null, null,  expandsC, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
        // return an object that contains details for UserBean, SpaceResultBean as well as ContentResultBean
    }
}
