package com.pb.test.filestatistics.service;

import com.pb.test.filestatistics.model.FileStatistics;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Interface that provides the methods needed to specify the actions with FileStatistics objects
 */
public interface FileStatisticsService {

  /**
   * Calculate statistics from inputFile
   *
   * @param inputFile file that contains text for processing
   */
  FileStatistics calculateStatistics(MultipartFile inputFile) throws IOException;

  /**
   * Get all file statistics from the database
   *
   * @return List of FileStatistics objects that contains all data about  file statistics from the
   * database
   */
  List<FileStatistics> getStatistics();

  /**
   * Save information from FileStatistics to the database
   *
   * @param fileStatistics object of FileStatistics class for save
   * @return saved object of FileStatistics class
   */
  FileStatistics saveStatistics(FileStatistics fileStatistics);

  /**
   * Save file in system if it possible
   * @param inputFile file for save
   * @throws IOException
   */
  Path saveFile(MultipartFile inputFile) throws IOException;
}
