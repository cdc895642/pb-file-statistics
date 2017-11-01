package com.pb.test.filestatistics.validation;

import java.io.File;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
public class AppArgsValidatorImpl implements AppArgsValidator{

  @Override
  public boolean isValid(ApplicationArguments value) {
    return value.getNonOptionArgs().stream().allMatch(
        arg ->
            arg != null
                &&
                !arg.trim().isEmpty()
                &&
                isFilePathValid(arg)
    );
  }

  private boolean isFilePathValid(String path) {
    File file = new File(path.trim());
    return file.isDirectory() && file.exists();
  }
}
