import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * References
 * - http://www.baeldung.com/jackson
 * - http://www.baeldung.com/jackson-annotations
 *
 * @author hugh
 */
public class JacksonTest {

    @Getter
    @AllArgsConstructor
    static class User {
        @JsonView(Comment.class)
        private int id;

        @JsonView(value = {Comment.Public.class, Comment.class})
        private String name;
    }

    @Getter
    @AllArgsConstructor
    static class Comment {
        static class Public {}

        @JsonView(Comment.Public.class)
        private String text;
        @JsonView(value = {Comment.Public.class, Comment.class})
        private User user;
    }

    /**
     * http://www.baeldung.com/jackson-json-view-annotation
     * http://wiki.fasterxml.com/JacksonJsonViews
     */
    @Test
    public void jsonViewTest() throws Exception {
        User user = new User(1, "SD");
        Comment comment = new Comment("Hello @JsonView!", user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        String result = mapper.writerWithView(Comment.Public.class)
                .writeValueAsString(comment);

        assertThat(result, containsString("SD"));
        assertThat(result, containsString("Hello @JsonView!"));
        assertThat(result, not(containsString("1")));
    }

    /**
     * It seems we don't need extra inner class when we need just one mapping.
     */
    @Test
    public void jsonViewsTest2() throws Exception {
        User user = new User(1, "SD");
        Comment comment = new Comment("Hello @JsonView!", user);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        String result = mapper.writerWithView(Comment.class)
                .writeValueAsString(comment);

        assertThat(result, containsString("1"));
        assertThat(result, containsString("SD"));
        assertThat(result, not(containsString("Hello @JsonView!")));
    }
}
