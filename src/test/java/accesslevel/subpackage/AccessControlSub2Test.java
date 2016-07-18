package accesslevel.subpackage;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class AccessControlSub2Test {

    private AccessControlSub1Test sub1 = new AccessControlSub1Test();

    @Test
    public void accessLevelTest() throws Exception {
        assertThat(sub1.packageLevel(), is(0));
        assertThat(sub1.publicLevel(), is(0));
    }

}
