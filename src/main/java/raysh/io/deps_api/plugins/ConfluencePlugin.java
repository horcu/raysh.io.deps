package raysh.io.deps_api.plugins;

import com.github.lucapino.confluence.rest.client.example.PropertiesFileStore;
import com.github.lucapino.confluence.rest.core.api.domain.content.ContentResultsBean;
import com.github.lucapino.confluence.rest.core.impl.APIAuthConfig;
import com.github.lucapino.confluence.rest.core.impl.APIUriProvider;
import com.github.lucapino.confluence.rest.core.impl.HttpAuthRequestService;

import javax.inject.Singleton;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static raysh.io.deps_api.misc.HttpAuthProperties.*;

@Singleton
public class ConfluencePlugin {

   public ConfluencePlugin(){}

    public Future<ContentResultsBean> run(){
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(100);

            APIAuthConfig conf = loadAuthConfig();
            HttpAuthRequestService requestService = new HttpAuthRequestService();
            requestService.connect(new URI(conf.getBaseUrl()), conf.getUser(), conf.getPassword());

            APIUriProvider uriProvider = new APIUriProvider(new URI(conf.getBaseUrl()));

            ConfluenceService confluenceService = new ConfluenceService(executorService, requestService, uriProvider);
            return confluenceService
                    .run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private APIAuthConfig loadAuthConfig() throws Exception {
        HashMap<String, String> defaultProperties = getDefaultProperties();
        PropertiesFileStore store = new PropertiesFileStore("target/example-httpauth.properties", defaultProperties);
        Map<String, String> props = store.getProperties();
        return new APIAuthConfig(props.get(BASE_URL), props.get(USER), props.get(PASSWORD));
    }

    private HashMap<String, String> getDefaultProperties() {
        HashMap<String, String> defaultProperties = new HashMap<>();
        defaultProperties.put(BASE_URL, "https://rayshio.atlassian.net/wiki/");
        defaultProperties.put(USER, "peez@raysh.io");
        defaultProperties.put(PASSWORD, "bgZ0EUAyeTCwZE4YfaxsA0BD");
        return defaultProperties;
    }

}
