import org.junit.Test;
import sun.security.rsa.RSASignature;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class SignatureTest {
    private byte[] data1 = new byte[] {'a', 'b'};
    private byte[] data2 = new byte[] {'c', 'd'};


    /**
     * Generating and Verifying Signatures
     * https://docs.oracle.com/javase/tutorial/security/apisign/index.html
     */
    @Test
    public void generatingAndVerifyingSignatureTest() throws Exception {
        KeyPair keyPair = generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        byte[] realSig = getOrigSig(privateKey);
        PublicKey decodedPublicKey = encodeAndDecodePublicKey(publicKey);
        Signature sig = generateVerifyingSig(decodedPublicKey);

        assertTrue(Arrays.equals(publicKey.getEncoded(), decodedPublicKey.getEncoded()));

        boolean verifies = sig.verify(realSig);
        assertTrue(verifies);
    }

    /**
     * https://docs.oracle.com/javase/tutorial/security/apisign/step2.html
     */
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
//        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);

        return keyGen.generateKeyPair();
    }

    /**
     * https://docs.oracle.com/javase/tutorial/security/apisign/step3.html
     */
    private byte[] getOrigSig(PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeyException, SignatureException {

        // SHA1 - message digest algorithm
        // DSA - signature algorithm
        Signature dsa = Signature.getInstance("SHA1withRSA");
//        Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
        dsa.initSign(privateKey);
        dsa.update(data1);
        dsa.update(data2);
        return dsa.sign();
    }

    /**
     * https://docs.oracle.com/javase/tutorial/security/apisign/vstep2.html
     */
    private PublicKey encodeAndDecodePublicKey(PublicKey publicKey) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeySpecException {

        byte[] encodedPublicKey = publicKey.getEncoded();
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
        return keyFactory.generatePublic(publicKeySpec);
    }

    /**
     * https://docs.oracle.com/javase/tutorial/security/apisign/vstep4.html
     */
    private Signature generateVerifyingSig(PublicKey decodedPublicKey) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeyException, SignatureException {

        Signature sig = Signature.getInstance("SHA1withRSA");
//        Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
        sig.initVerify(decodedPublicKey);
        sig.update(data1);
        sig.update(data2);

        return sig;
    }

    @Test
    public void keyPairTest() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        assertThat(privateKey.getFormat(), is("PKCS#8"));
        assertThat(publicKey.getFormat(), is("X.509"));

//        assertThat(privateKey.getAlgorithm(), is("RSA"));
//        assertThat(publicKey.getAlgorithm(), is("RSA"));
//        assertThat(privateKey.getAlgorithm(), is("DSA"));
//        assertThat(publicKey.getAlgorithm(), is("DSA"));

        assertThat(privateKey.isDestroyed(), is(false));

        Base64.Encoder encoder = Base64.getEncoder();
        assertThat(encoder.encodeToString(privateKey.getEncoded()), is(not("")));
        assertThat(encoder.encodeToString(publicKey.getEncoded()), is(not("")));

        System.out.println(encoder.encodeToString(privateKey.getEncoded()));
        System.out.println();
        System.out.println(encoder.encodeToString(publicKey.getEncoded()));
    }

    /**
     * http://stackoverflow.com/questions/361975/setting-the-default-java-character-encoding
     */
    @Test
    public void fileEncodingTest() throws Exception {
        assertThat(System.getProperty("file.encoding"), is("UTF-8"));
    }

}
