import org.junit.Test;

import java.math.BigInteger;
import java.security.MessageDigest;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author hugh
 */
public class MessageDigestTest {

    /**
     * How to generate MD5 hash?
     * http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash
     *
     * MessageDigest is not thread-safe.
     */
    @Test
    public void generateMd5Test() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] source = "source".getBytes("UTF-8");
        byte[] target = md.digest(source);
        
        assertThat(toHex(target), is("36cd38f49b9afa08222c0dc9ebfe35eb"));
    }

    // from string test
    public String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "x", bi);
    }
}
