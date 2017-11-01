package com.pb.test.filestatistics.repository;

import com.pb.test.filestatistics.model.FileStatistics;
import java.util.List;

/**
 * Base interface for DAO layer for operations that related to the file statistics
 */
public interface FileStatsRepository {

  /**
   * Get all file statistics from the database
   * @return all file statistics from the database
   */
  List<FileStatistics> findAll();

  /**
   * Save data from fileStatistics to the database
   * @param fileStatistics object of FileStatistics class for save
   * @return saved object of FileStatistics
   */
  FileStatistics save(FileStatistics fileStatistics);
}
