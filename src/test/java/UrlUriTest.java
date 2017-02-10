import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UrlUriTest {

    @Test
    public void testURI() {
        URI uri = URI.create("path");
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getScheme(), is(nullValue()));
        assertThat(uri.getPath(), is("path"));
        assertThat(uri.toString(), is("path"));

        URI newUri = URI.create("https://google.com/" + uri.getPath());
        assertThat(newUri.getHost(), is("google.com"));
        assertThat(newUri.getScheme(), is("https"));
        assertThat(newUri.getPath(), is("/path"));
        assertThat(newUri.toString(), is("https://google.com/path"));
    }

}
