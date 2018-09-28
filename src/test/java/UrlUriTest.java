import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UrlUriTest {

    @Test
    public void testUriBuilder() throws URISyntaxException {
        String url = new URIBuilder()
                .setScheme("http")
                .setHost("apache.org")
                .setPath("/shindig")
                .addParameter("hello world", "foo&bar")
                .setFragment("foo")
                .toString();
        
        assertThat(url, is("http://apache.org/shindig?hello+world=foo%26bar#foo"));

        url = new URIBuilder("https://abc.com/hey")
                .addParameter("encode", "?")
                .addParameter("null1", null)
                .addParameter("null2", null)
                .toString();
        
        assertThat(url, is("https://abc.com/hey?encode=%3F&null1&null2"));
    }

    @Test
    public void testUrlEncoder() throws UnsupportedEncodingException {
        assertThat(URLEncoder.encode("abc?=& äö", "UTF-8"), is("abc%3F%3D%26+%C3%A4%C3%B6"));
    }

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
