package test.java.ca.jrvs.apps.grep;

import main.java.ca.jrvs.apps.practice.RegexExc;
import main.java.ca.jrvs.apps.practice.RegexExcImp;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RegexExcImpTest {

    RegexExc regex = new RegexExcImp();

    @Test
    void testMatchJpeg() {
        assertTrue(regex.matchJpeg("photo.jpg"));
        assertTrue(regex.matchJpeg("pic.JPEG"));
        assertFalse(regex.matchJpeg("file.png"));
    }

    @Test
    void testMatchIp() {
        assertTrue(regex.matchIp("192.168.0.1"));
        assertTrue(regex.matchIp("0.0.0.0"));
        assertFalse(regex.matchIp("999.999.999.999"));
    }

    @Test
    void testEmptyLine() {
        assertTrue(regex.isEmptyLine(""));
        assertTrue(regex.isEmptyLine("   "));
        assertFalse(regex.isEmptyLine("hello"));
    }
}
