import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.CookieSpecRegistries;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import org.apache.http.pool.PoolStats;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

/**
 * Mock Server
 * - http://www.mock-server.com/
 * - https://www.baeldung.com/mockserver
 *
 * HttpClient
 * - Official: http://hc.apache.org/httpcomponents-client-ga/tutorial/html/
 * - Fluent API: https://hc.apache.org/httpcomponents-client-ga/tutorial/html/fluent.html
 */
public class HttpClientTest {
    private static ClientAndServer mockServer;

    @BeforeClass
    public static void setUp() {
        mockServer = startClientAndServer(1080);
    }

    @AfterClass
    public static void tearDown() {
        mockServer.stop();
    }
    
    @Test
    public void testGet() throws IOException {
        String hello = "Hello, Mock Server";

        new MockServerClient("localhost", 1080)
            .when(HttpRequest.request("/get"))
            .respond(HttpResponse.response().withStatusCode(200).withBody(hello));

        String content = Request.Get("http://localhost:1080/get").execute().returnContent().asString();

        assertThat(content, is(hello));
    }

    public HttpClient httpClient(PoolingHttpClientConnectionManager poolingConnManager, RequestConfig config) {
        String[] datePatterns = {
                DateUtils.PATTERN_RFC1123,
                DateUtils.PATTERN_RFC1036,
                DateUtils.PATTERN_ASCTIME
        };
        DefaultCookieSpecProvider defaultCookieSpecProvider =
                new DefaultCookieSpecProvider(null, null, datePatterns, false);

        RegistryBuilder<CookieSpecProvider> cookieSpecProviderBuilder = CookieSpecRegistries.createDefaultBuilder();
        cookieSpecProviderBuilder.register(CookieSpecs.DEFAULT, defaultCookieSpecProvider);

        return HttpClientBuilder.create()
                .useSystemProperties()
                .setConnectionManager(poolingConnManager)
                .setConnectionTimeToLive(5, SECONDS)
                .setDefaultCookieSpecRegistry(cookieSpecProviderBuilder.build())
                .setDefaultRequestConfig(config)
                .build();
    }

    @Test
    public void shouldConnectionReleasedWhenTimeoutException() {
        String hello = "Hello, Mock Server";

        new MockServerClient("localhost", 1080)
                .when(HttpRequest.request("/get"))
                .respond(HttpResponse.response().withDelay(TimeUnit.SECONDS, 10).applyDelay()
                        .withStatusCode(200).withBody(hello));

        int timeout = 1000;
        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)  // same as read timeout
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout)
                .build();

        HttpClient httpClient = httpClient(poolingConnManager, requestConfig);
        PoolStats stats = poolingConnManager.getTotalStats();
        try {
            HttpGet httpGet = new HttpGet("http://localhost:1080/get");
            HttpClientContext context = HttpClientContext.create();
            httpClient.execute(httpGet, context);
            fail();
        } catch (IOException e) {
            assertThat(stats.getLeased(), is(0));
        }
    }

    @Test
    public void shouldConnectionReleasedWhenRedirectException() throws IOException {
        new MockServerClient("localhost", 1080)
                .when(HttpRequest.request("/forward"))
                .respond(HttpResponse.response()
                        .withStatusCode(302)
                        .withHeader("Location", "http://google.com"));

        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        RequestConfig requestConfig = RequestConfig.custom()
                .setMaxRedirects(1)
                .build();

        HttpClient httpClient = httpClient(poolingConnManager, requestConfig);
        PoolStats stats = poolingConnManager.getTotalStats();
        try {
            HttpGet httpGet = new HttpGet("http://localhost:1080/forward");
            HttpClientContext context = HttpClientContext.create();
            httpClient.execute(httpGet, context);
        } catch (ClientProtocolException e) {
            assertThat(stats.getLeased(), is(0));
        }
    }

    // checkout this http://hc.apache.org/httpcomponents-client-4.3.x/tutorial/html/connmgmt.html#d5e405
    @Test
    public void shouldConnectionNotReleaseWhenInterrupted() throws InterruptedException {
        new MockServerClient("localhost", 1080)
                .when(HttpRequest.request("/get"))
                .respond(HttpResponse.response()
                        .withDelay(TimeUnit.SECONDS, 100)
                        .applyDelay()
                        .withStatusCode(200)
                        .withBody("hi"));
    }
}
