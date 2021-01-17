package raysh.io.deps_api.plugins;

import com.github.lucapino.confluence.rest.client.api.ClientFactory;
import com.github.lucapino.confluence.rest.client.api.ContentClient;
import com.github.lucapino.confluence.rest.client.impl.ClientFactoryImpl;
import com.github.lucapino.confluence.rest.core.api.RequestService;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContentBean;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ConfluencePageService {
    private final ExecutorService executorService;
    private final RequestService requestService;
    private final APIUriProvider apiConfig;

    public ConfluencePageService(ExecutorService executorService, RequestService requestService, APIUriProvider apiConfig) {
        this.executorService = executorService;
        this.requestService = requestService;
        this.apiConfig = apiConfig;
    }

    public Future<ContentBean> run(String pageId) {
        ClientFactory factory = new ClientFactoryImpl(executorService, requestService, apiConfig);
        Future<ContentBean> content = null;
        ContentClient contentClient = factory.getContentClient();
        ContentBean cb = null;
        List<String> expandsC = new ArrayList<>();
        expandsC.add("body.view");
        try {
            content = contentClient.getContentById(pageId.toString(), 0, expandsC);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
