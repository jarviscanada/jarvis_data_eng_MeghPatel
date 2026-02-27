package main.java.ca.jrvs.apps.practice;

import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

    // Compile once (performance best practice)
    private static final Pattern JPEG_PATTERN =
            Pattern.compile("^.+\\.(jpg|jpeg)$", Pattern.CASE_INSENSITIVE);

    private static final Pattern IP_PATTERN =
            Pattern.compile("^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.|$)){4}$");

    private static final Pattern EMPTY_PATTERN =
            Pattern.compile("^\\s*$");

    @Override
    public boolean matchJpeg(String filename) {
        if (filename == null) return false;
        return JPEG_PATTERN.matcher(filename).matches();
    }

    @Override
    public boolean matchIp(String ip) {
        if (ip == null) return false;
        return IP_PATTERN.matcher(ip).matches();
    }

    @Override
    public boolean isEmptyLine(String line) {
        if (line == null) return true;
        return EMPTY_PATTERN.matcher(line).matches();
    }
}