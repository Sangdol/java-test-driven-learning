import com.google.common.collect.Lists;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * @author hugh
 */
public class StringTest {

    @Test
    public void characterTest() {
        Character a = 'a';
        Character b = 'b';

       assertThat(a + b, is(195));

       String str = "";
       str += a;
       str += b;
       assertThat(str, is("ab"));
       
       char ca = a;
       assertThat(ca, is('a'));
    }

    /**
     * http://stackoverflow.com/questions/11700320/is-string-literal-pool-a-collection-of-references-to-the-string-object-or-a-col/11701016#11701016
     */
    @Test
    public void stringLiteralPoolTest() throws Exception {
        String a = "a";
        String b = "a";

        // Same literal
        assertThat(a.equals(b), is(true));
        assertThat(a.hashCode() == b.hashCode(), is(true));
        assertThat(System.identityHashCode(a) == System.identityHashCode(b), is(true));

        a = "a";
        b = new String("a");

        // Same literal
        assertThat(a.equals(b), is(true));
        assertThat(a.hashCode() == b.hashCode(), is(true));
        assertThat(System.identityHashCode(a) == System.identityHashCode(b), is(false));

        a = "a";
        b = "?a".substring(1);

        // Different literal
        assertThat(a.equals(b), is(true));
        assertThat(a.hashCode() == b.hashCode(), is(true));
        assertThat(System.identityHashCode(a) == System.identityHashCode(b), is(false));

        a = "a";
        b = "?a".substring(1).intern();

        // Same literal
        assertThat(a.equals(b), is(true));
        assertThat(a.hashCode() == b.hashCode(), is(true));
        assertThat(System.identityHashCode(a) == System.identityHashCode(b), is(true));
    }
    
    @Test
    public void trimTest() throws Exception {
        assertThat(" abc ".trim(), is("abc"));
    }

    /**
     * http://stackoverflow.com/questions/7569335/reverse-a-string-in-java
     */
    @Test
    public void reverseTest() throws Exception {
        assertThat(new StringBuilder("abc").reverse().toString(), is("cba"));
    }

    @Test
    public void replaceTest() throws Exception {
        assertThat("abc1".replace("1", "2"), is("abc2")); // Literal
        assertThat("abc11".replaceFirst("\\d", "2"), is("abc21")); // Regex
        assertThat("abc11".replaceAll("\\d", "2"), is("abc22")); // Regex
    }
    
    @Test
    public void charSequenceTest() throws Exception {
        CharSequence cs = "str";
        assertThat(cs.charAt(0), is('s'));

        cs = new StringBuilder("str");
        assertThat(cs.charAt(0), is('s'));

        cs = new StringBuffer("str");
        assertThat(cs.charAt(0), is('s'));
        
        assertThat(cs.length(), is(3));
        assertThat(cs.toString(), is("str"));
    }

    /**
     * Why length() is a method instead of a field?
     * http://stackoverflow.com/questions/8720220/why-is-string-length-a-method
     */
    @Test
    public void lengthTest() throws Exception {
        assertThat("abc".length(), is(3));
    }

    @Test
    public void charsTest() throws Exception {
        char[] chars = "abc0".toCharArray();
        assertThat(chars[0], is('a'));
        assertThat(chars[2], is('c'));
        assertThat(chars[3] - '0', is(0));

        assertThat(new String(chars), is("abc0"));
        assertThat(String.valueOf(chars), is("abc0"));
    }

    @Test
    public void base64Test() throws Exception {
        byte[] bytes = new byte[] {1, 2, 3, 4, 100};

        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = "AQIDBGQ=";
        assertThat(encoder.encodeToString(bytes), is(encoded));

        Base64.Decoder decoder = Base64.getDecoder();
        assertThat(decoder.decode(encoded), is(bytes));
    }

    /**
     * http://stackoverflow.com/a/943963/524588
     */
    @Test
    public void bytesToHexStringTest() throws Exception {
        byte[] bytes = new byte[] {1, 2};
        BigInteger bi = new BigInteger(1, bytes);

        assertThat(String.format("%0" + (bytes.length << 1) + "X", bi), is("0102"));

        // explanation tests
        assertThat(bi, is(BigInteger.valueOf(258)));
        assertThat(bytes.length << 1, is(4));
        assertThat("%0" + (bytes.length << 1) + "X", is("%04X"));
    }

    /**
     * http://stackoverflow.com/a/14552724/524588
     * http://stackoverflow.com/a/5942951/524588
     */
    @Test
    public void bytesToHexAndReverseTest() throws Exception {
        byte[] bytes = {(byte)0, (byte)0, (byte)134, (byte)1, (byte)61};
        String hex = "000086013D";
        assertThat(DatatypeConverter.printHexBinary(bytes), is(hex));
        assertThat(DatatypeConverter.parseHexBinary(hex), is(bytes));
    }

    @Test
    public void hexFormatTest() throws Exception {
        BigInteger bi = new BigInteger(new byte[]{10, 2});
        // % - Begin format specifier
        // 0 - Flag (0 flag specifies that 0 is the padding character)
        // 4 - Width
        // x - Conversion (Hex in lowercase)
        assertThat(String.format("%04x", bi), is("0a02"));
        assertThat(String.format("%04X", bi), is("0A02"));

        assertThat(String.format("%4X", bi), is(" A02"));
        assertThat(String.format("%14X", bi), is("           A02"));
        assertThat(String.format("%014X", bi), is("00000000000A02"));
    }

    /**
     * http://stackoverflow.com/questions/1751844/java-convert-liststring-to-a-joind-string
     */
    @Test
    public void joinTest() throws Exception {
        assertThat(String.join(", ", Lists.newArrayList("1", "2")), is("1, 2"));
        assertThat(String.join(", ", Lists.newArrayList("1", null)), is("1, null"));
    }

    @Test
    public void listToString() throws Exception {
        List<Integer> ints = Lists.newArrayList(1, 2);

        assertThat(ints.toString(), is("[1, 2]"));
    }

    /**
     * http://stackoverflow.com/questions/18571223/how-to-convert-java-string-into-byte
     */
    @Test
    public void stringToByteArraysTest() throws Exception {
        String ab = "ab";

        assertTrue(Arrays.equals(ab.getBytes(), new byte[]{97, 98}));
    }

    /**
     * http://stackoverflow.com/questions/5673059/converting-byte-array-to-string-java
     */
    @Test
    public void byteArrayToStringTest() throws Exception {
        byte a = 'a';
        byte b = 'b';

        assertThat((int) a, is(97));
        assertThat((int) b, is(98));

        byte[] bytes = new byte[]{97, 98};

        assertThat(new String(bytes, "UTF-8"), is("ab"));
    }

    @Test
    public void comparisonTest() throws Exception {
        assertThat("10:10".compareTo("09:10"), is(greaterThan(0)));
    }

    @Test
    public void formatTest() {
        // http://examples.javacodegeeks.com/core-java/lang/string/java-string-format-example/
        assertThat(String.format("%s", 1), is("1"));
        assertThat(String.format("%s", -1), is("-1"));
        assertThat(String.format("%s", 1.1), is("1.1"));

        // https://docs.oracle.com/javase/tutorial/essential/io/formatting.html
        assertThat(String.format("%1$s %1$s", "reuse"), is("reuse reuse"));
        // 1$: argument index, +0: flags, 20: width, .10: precision, f: conversion
        assertThat(String.format("%f, %1$+020.10f", Math.PI), is("3.141593, +00000003.1415926536"));

        assertThat("Print Long with %d.", String.format("%d", 100_000_000_000_000L), is("100000000000000"));

    }
}
