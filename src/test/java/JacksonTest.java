import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * References
 * - http://www.baeldung.com/jackson
 * - http://www.baeldung.com/jackson-annotations
 *
 * codehaus vs. fasterxml
 * - codehaus is older version
 * - Jackson has moved from Codehaus to Github when releasing Jackson 2.
 * - http://stackoverflow.com/questions/30782706/org-codehaus-jackson-versus-com-fasterxml-jackson-core
 *
 * @author hugh
 */
public class JacksonTest {
    private ObjectMapper mapper = new ObjectMapper();

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

        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);

        String result = mapper.writerWithView(Comment.class)
                .writeValueAsString(comment);

        assertThat(result, containsString("1"));
        assertThat(result, containsString("SD"));
        assertThat(result, not(containsString("Hello @JsonView!")));
    }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Naming {
        int ageAge;
    }

    /**
     * Why is this not working with Jackson 2.7+?
     * http://stackoverflow.com/questions/35471256/how-to-set-naming-strategy-to-objectmapper-on-jackson-2-7
     */
    @Ignore
    @Test
    public void namingStrategyTest() throws Exception {
        Naming naming = new Naming(1);
        assertThat(mapper.writeValueAsString(naming), is("{\"ageAge\":1}"));

        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        assertThat(mapper.writeValueAsString(naming), is("{\"age_age\":1}"));
        assertThat(mapper.readValue("{\"ageAge\":1}", Naming.class).getAgeAge(), is(1));
    }

    @Test
    public void jsonNodeTest() throws Exception {
        JsonNode node = mapper.readTree("{\"ageAge\":1}");
        assertThat(node.get("ageAge").asInt(), is(1));

        node = mapper.readTree("{\"parent\": {\"child\": {\"age\": 1}}}");
        assertThat(node.get("age"), is(nullValue()));
        assertThat(node.get("parent").get("child").get("age").asInt(), is(1));

        node = mapper.readTree("[{\"age\": 1}, {\"age\": 2}]");
        assertThat(node.get(0).get("age").asInt(), is(1));
    }
}
