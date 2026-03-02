package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaGrepLambdaImp extends JavaGrepImp {

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
    public List<String> readLines(File inputFile) {

        try (Stream<String> lines = Files.lines(inputFile.toPath())) {

            return lines.collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Lambda + Stream version of listing files recursively
     */
    @Override
    public List<File> listFiles(String rootDir) {

        try (Stream<java.nio.file.Path> paths = Files.walk(new File(rootDir).toPath())) {

            return paths
                    .filter(Files::isRegularFile)
                    .map(java.nio.file.Path::toFile)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
