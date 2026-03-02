package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JavaGrepImp implements JavaGrep {

    private static final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String rootPath;
    private String regex;
    private String outFile;

    private Pattern pattern;

    public JavaGrepImp() {}

    public JavaGrepImp(String rootPath, String regex, String outFile) {
        this.rootPath = rootPath;
        this.regex = regex;
        this.outFile = outFile;
        this.pattern = Pattern.compile(regex);
    }

    /**
     * ============ MAIN WORKFLOW ============
     */
    @Override
    public void process() throws IOException {

        logger.info("Starting grep process");

        List<File> files = listFiles(rootPath);

        List<String> matchedLines = new ArrayList<>();

        for (File file : files) {
            List<String> lines = readLines(file);

            for (String line : lines) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }

        writeToFile(matchedLines);

        logger.info("Process completed. Matched {} lines", matchedLines.size());
    }

    /**
     * Recursively list files
     */
    @Override
    public List<File> listFiles(String rootDir) {
        try {
            return Files.walk(Paths.get(rootDir))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error listing files", e);
            return Collections.emptyList();
        }
    }

    /**
     * Read file lines
     */
    @Override
    public List<String> readLines(File inputFile) {

        if (!inputFile.isFile()) {
            throw new IllegalArgumentException("Not a file: " + inputFile);
        }

        try {
            return Files.readAllLines(inputFile.toPath());
        } catch (IOException e) {
            logger.error("Failed reading file {}", inputFile.getName(), e);
            return Collections.emptyList();
        }
    }

    /**
     * Regex matching
     */
    @Override
    public boolean containsPattern(String line) {
        return pattern.matcher(line).find();
    }

    /**
     * Write results
     */
    @Override
    public void writeToFile(List<String> lines) throws IOException {
        Files.write(Paths.get(outFile), lines);
        logger.info("Output written to {}", outFile);
    }

    // ===== getters setters =====

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    /**
     * ============ CLI ENTRY ============
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            logger.error("Usage: JavaGrepImp <rootPath> <regex> <outFile>");
            System.exit(1);
        }

        JavaGrep app = new JavaGrepImp(args[0], args[1], args[2]);

        try {
            app.process();
        } catch (Exception e) {
            logger.error("Application failed", e);
        }
    }
}
