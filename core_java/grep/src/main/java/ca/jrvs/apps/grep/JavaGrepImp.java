package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

        try (Stream<File> files = listFiles(rootPath);
             Stream<String> matchedLines = files
                     .flatMap(this::safeReadLines)
                     .filter(this::containsPattern)) {
            writeToFile(matchedLines);
        }
    }

    /**
     * Recursively list files
     */
    @Override
    public Stream<File> listFiles(String rootDir) throws IOException {
        return Files.walk(Paths.get(rootDir))
                .filter(Files::isRegularFile)
                .map(Path::toFile);
    }

    /**
     * Read file lines
     */
    @Override
    public Stream<String> readLines(File inputFile) throws IOException {

        if (!inputFile.isFile()) {
            throw new IllegalArgumentException("Not a file: " + inputFile);
        }

        return Files.lines(inputFile.toPath());
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
    public void writeToFile(Stream<String> lines) throws IOException {
        Path outputPath = Paths.get(outFile);
        Path parent = outputPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        long matchedCount = 0L;
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
            Iterator<String> iterator = lines.iterator();
            while (iterator.hasNext()) {
                writer.write(iterator.next());
                writer.newLine();
                matchedCount++;
            }
        }

        logger.info("Process completed. Matched {} lines", matchedCount);
        logger.info("Output written to {}", outFile);
    }

    protected Stream<String> safeReadLines(File inputFile) {
        try {
            return readLines(inputFile);
        } catch (IOException e) {
            logger.error("Failed reading file {}", inputFile.getAbsolutePath(), e);
            return Stream.empty();
        }
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
