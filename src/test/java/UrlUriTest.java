import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UrlUriTest {

    @Test(expected = MalformedURLException.class)
    public void testMalformedURLExceptionURL() throws MalformedURLException {
        new URL("test");
    }

    @Test
    public void testURL() throws MalformedURLException {
        URL url = new URL("http://abc.com");
        assertThat(url.getHost(), is("abc.com"));
    }

    @Test
    public void testURI() {
        URI uri = URI.create("path");
        assertThat(uri.getHost(), is(nullValue()));
        assertThat(uri.getScheme(), is(nullValue()));
        assertThat(uri.getPath(), is("path"));
        assertThat(uri.toString(), is("path"));

        URI emptyUri = URI.create("");
        assertThat(emptyUri.getHost(), is(nullValue()));
        assertThat(emptyUri.getScheme(), is(nullValue()));
        assertThat(emptyUri.getPath(), is(""));
        assertThat(emptyUri.toString(), is(""));

        URI newUri = URI.create("https://google.com/" + uri.getPath());
        assertThat(newUri.getHost(), is("google.com"));
        assertThat(newUri.getScheme(), is("https"));
        assertThat(newUri.getPath(), is("/path"));
        assertThat(newUri.toString(), is("https://google.com/path"));
    }

}
