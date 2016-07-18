package accesslevel;

import accesslevel.subpackage.AccessControlSub1Test;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * https://docs.oracle.com/javase/tutorial/java/javaOO/accesscontrol.html
 *
 * @author hugh
 */
public class AccessControlTest {

    private AccessControlSub1Test sub = new AccessControlSub1Test();

    @SuppressWarnings("unused")
    int packageLevel() {
        return 0;
    }

    public int publicLevel() {
        return 0;
    }

    @Test
    public void accessLevelTest() throws Exception {
        // Cannot access to the package-private method of a child package.
//        assertThat(aTop.packageLevel(), is(0));
        assertThat(sub.publicLevel(), is(0));
    }

}
