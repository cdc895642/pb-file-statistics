package com.pb.test.filestatistics.validation;

import org.springframework.boot.ApplicationArguments;

/**
 * Represents methods for validate application argument values
 */
public interface AppArgsValidator {

  /**
   * Check if ApplicationArguments object has valid value
   * @param value ApplicationArguments object for check
   * @return true if value passed the check, otherwise return false
   */
  boolean isValid(ApplicationArguments value);
}
