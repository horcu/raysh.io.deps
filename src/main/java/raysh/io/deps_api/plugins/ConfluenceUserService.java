package raysh.io.deps_api.plugins;

import com.github.lucapino.confluence.rest.client.api.ClientFactory;
import com.github.lucapino.confluence.rest.client.api.UserClient;
import com.github.lucapino.confluence.rest.client.impl.ClientFactoryImpl;
import com.github.lucapino.confluence.rest.core.api.RequestService;
import com.github.lucapino.confluence.rest.core.api.domain.UserBean;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ConfluenceUserService {
    private final ExecutorService executorService;
    private final RequestService requestService;
    private final APIUriProvider apiConfig;

    public ConfluenceUserService(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
        this.executorService = executorService;
        this.requestService = requestService;
        this.apiConfig = apiConfig;
    }

    public Future<UserBean> run() {
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, apiConfig);
        UserClient userClient = factory.getUserClient();
        UserBean user = null;
        Future<UserBean> anonymousUser = null;
        try {
            anonymousUser = userClient.getCurrentUser();
            user = anonymousUser.get();
            System.out.println("Anonymous user: " + user.getDisplayName());
        } catch (URISyntaxException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return anonymousUser;
    }
}
