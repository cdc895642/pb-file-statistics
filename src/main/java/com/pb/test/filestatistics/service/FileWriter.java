package com.pb.test.filestatistics.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Component;

@Component
public class FileWriter {

  public void write(Path path, byte[] bytes) throws IOException {
    Files.write(path, bytes);
  }

}
