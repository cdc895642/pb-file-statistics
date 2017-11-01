package com.pb.test.filestatistics.controller;

import com.pb.test.filestatistics.model.FileStatistics;
import com.pb.test.filestatistics.service.FileStatisticsService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Main controller of app.
 * Rest service that receive file for processing and return file statistics result
 */
@RestController
public class StatisticsController {

  private FileStatisticsService fileStatisticsService;

  @Autowired
  public void setFileStatisticsService(
      FileStatisticsService fileStatisticsService) {
    this.fileStatisticsService = fileStatisticsService;
  }

  /**
   * Receive file for processing and return all file statistics from the DB
   * @param uploadfile file for processing
   * @return all file statistics from the DB
   * @throws IOException
   */
  @PostMapping("/getStatistics")
  public List<FileStatistics> getStatistics(@RequestParam("file") MultipartFile uploadfile)
      throws IOException {
    FileStatistics fileStatistics=fileStatisticsService.calculateStatistics(uploadfile);
    fileStatisticsService.saveStatistics(fileStatistics);
    List<FileStatistics> result=fileStatisticsService.getStatistics();
    return result;
  }
}
