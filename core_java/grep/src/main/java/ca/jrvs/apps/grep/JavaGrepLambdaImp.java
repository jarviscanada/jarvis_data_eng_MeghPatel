package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public JavaGrepLambdaImp() {
    }

    public JavaGrepLambdaImp(String rootPath, String regex, String outFile) {
        super(rootPath, regex, outFile);
    }

    /**
     * CLI entry
     */
    public static void main(String[] args) {

        if (args.length != 3) {
            throw new IllegalArgumentException(
                    "Usage: java JavaGrepLambdaImp <regex> <rootPath> <outFile>");
        }

        JavaGrepLambdaImp app = new JavaGrepLambdaImp();

        app.setRegex(args[0]);
        app.setRootPath(args[1]);
        app.setOutFile(args[2]);

        try {
            app.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lambda + Stream version of reading file
     */
    @Override
    public Stream<String> readLines(File inputFile) throws IOException {
        return Files.lines(inputFile.toPath());
    }

    /**
     * Lambda + Stream version of listing files recursively
     */
    @Override
    public Stream<File> listFiles(String rootDir) throws IOException {
        return Files.walk(new File(rootDir).toPath())
                .filter(Files::isRegularFile)
                .map(java.nio.file.Path::toFile);
    }
}
