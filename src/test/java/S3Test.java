import org.junit.Test;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.time.Duration;

public class S3Test {

    @Test
    public void runtimeScopeApacheHttpClient() {
        // ApacheHttpClient is runtime scope so it's not possible to load
        // in production code. why?
        SdkHttpClient httpClient = ApacheHttpClient.builder()
                .connectionTimeout(Duration.ofSeconds(10))
                .socketTimeout(Duration.ofSeconds(10))
                .build();

        S3Client.builder().httpClient(httpClient).build();
    }

    @Test
    public void apiCallTimeout() {
        ClientOverrideConfiguration config = ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofSeconds(30)).build();

        S3Client.builder().overrideConfiguration(config).build();
    }
}
