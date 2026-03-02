package ca.jrvs.apps.practice;

/**
 * Regex utility interface
 * Defines methods to validate common patterns using regex.
 */
public interface RegexExc {

    /**
     * Return true if filename extension is jpg or jpeg (case insensitive)
     * Example: image.jpg, photo.JPEG
     */
    boolean matchJpeg(String filename);

    /**
     * Return true if IP address is valid IPv4
     * Example: 192.168.0.1
     */
    boolean matchIp(String ip);

    /**
     * Return true if line is empty or only whitespace
     */
    boolean isEmptyLine(String line);
}