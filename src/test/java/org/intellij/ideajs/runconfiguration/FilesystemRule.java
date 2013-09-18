package org.intellij.ideajs.runconfiguration;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;


public class FilesystemRule implements TestRule {
    private final File tmpDir;

    public FilesystemRule(File root) {
        tmpDir = new File(root, "." + UUID.randomUUID().toString());
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                tmpDir.mkdirs();
                base.evaluate();
                Files.walkFileTree(tmpDir.toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.deleteIfExists(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.deleteIfExists(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            }
        };
    }

    public String root() {
        try {
            return tmpDir.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File file(String name) {
        return file(name, "");
    }

    public File file(String name, String content) {
        File file = new File(tmpDir, name);
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new RuntimeException("Could not create file: " + file);
            }
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    public String path(String name) {
        return new File(tmpDir, name).getPath();
    }

    public String fullPath(String name) {
        try {
            return new File(tmpDir, name).getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
