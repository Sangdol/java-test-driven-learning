import java.util.Map;
import java.util.Properties;

/**
 * https://docs.oracle.com/javase/tutorial/essential/environment/index.html
 *
 * @author hugh
 */
public class ThePlatformEnvironment {
    public static void main(String[] args) {
        printAllSystemPropertiesAsJSON();
        printAllSystemPropertiesInTruncated();
        printAllSystemProperties();

        printAllEnvironmentVariables();
    }

    private static void printAllSystemPropertiesAsJSON() {
        System.out.println(System.getProperties()); // Print as JSON
    }

    private static void printAllSystemPropertiesInTruncated() {
        System.getProperties().list(System.out);
    }

    private static void printAllSystemProperties() {
        Properties props = System.getProperties();
        for (Map.Entry<Object, Object> prop : props.entrySet()) {
            System.out.println(String.format("%s=%s", prop.getKey(), prop.getValue()));
        }
    }

    private static void printAllEnvironmentVariables() {
        for (Map.Entry<String, String> env : System.getenv().entrySet()) {
            System.out.println(String.format("%s=%s", env.getKey(), env.getValue()));
        }
    }
}
