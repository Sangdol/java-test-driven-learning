package accesslevel.subpackage;

import accesslevel.AccessControlTest;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class AccessControlSub1Test {

    private AccessControlTest parentPackage = new AccessControlTest();

    int packageLevel() {
        return 0;
    }

    public int publicLevel() {
        return 0;
    }

    @Test
    public void accessLevelTest() throws Exception {
        // Cannot access to the package-private method of the parent package.
//        assertThat(top.packageLevel(), is());
        assertThat(parentPackage.publicLevel(), is(0));
    }

}
