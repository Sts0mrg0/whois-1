package net.ripe.db.whois.api;

import net.ripe.db.whois.api.httpserver.Audience;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.DeserializationConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.Before;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public abstract class AbstractRestClientTest extends AbstractIntegrationTest {
    protected Client client;

    protected String apiKey;

    @Before
    public void setUpClient() throws Exception {
        final JacksonJaxbJsonProvider jsonProvider = new JacksonJaxbJsonProvider();
        jsonProvider.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, false);
        jsonProvider.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .register(jsonProvider)
                .build();
    }

    protected WebTarget createStaticResource(final Audience audience, final String path) {
       return client.target(String.format("http://localhost:%s/%s", getPort(audience), path));
    }

    protected void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    protected WebTarget createResource(final Audience audience, final String path) {
        return client.target(String.format("http://localhost:%s/%s?apiKey=%s", getPort(audience), path, apiKey));
    }

    protected WebTarget createResourceGet(final Audience audience, final String pathAndParams) {
        return client.target(String.format("http://localhost:%s/%s&apiKey=%s", getPort(audience), pathAndParams, apiKey));
    }

    protected WebTarget createResource(final Audience audience, final String path, final String param) {
        return client.target(String.format("http://localhost:%s/%s/%s?apiKey=%s", getPort(audience), path, encode(param), apiKey));
    }

    protected String encode(final String param) {
        return encode(param, "UTF-8");
    }

    protected String encode(final String param, final String encoding) {
        try {
            return URLEncoder.encode(param, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
