package com.pb.test.filestatistics.service;

import com.pb.test.filestatistics.model.FileStatistics;
import com.pb.test.filestatistics.repository.FileStatsRepository;
import com.pb.test.filestatistics.validation.AppArgsValidator;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStatisticsServiceImpl implements FileStatisticsService {

  private ApplicationArguments appArgs;
  private AppArgsValidator appArgsValidator;
  private FileStatsRepository fileStatsRepository;
  private FileWriter fileWriter;

  @Value("${app.args.validation.error}")
  private String argsValidationMessage;
  @Value("${app.args.validation.file.empty}")
  private String emptyFileValidationMessage;
  @Value("${app.args.validation.error}")
  private String noWordsValidationMessage;

  public void setFileWriter(FileWriter fileWriter) {
    this.fileWriter = fileWriter;
  }

  @Autowired
  public void setFileStatsRepository(
      FileStatsRepository fileStatsRepository) {
    this.fileStatsRepository = fileStatsRepository;
  }

  @Autowired
  public void setAppArgs(ApplicationArguments appArgs) {
    this.appArgs = appArgs;
  }

  @Autowired
  public void setAppArgsValidator(AppArgsValidator appArgsValidator) {
    this.appArgsValidator = appArgsValidator;
  }

  @Override
  public FileStatistics calculateStatistics(MultipartFile inputFile) throws IOException {
    if (!appArgsValidator.isValid(appArgs)) {
      throw new ValidationException(argsValidationMessage);
    }
    saveFile(inputFile);
    return proccessFile(inputFile);
  }

  private FileStatistics proccessFile(MultipartFile inputFile) throws IOException {
    InputStream inputStream = inputFile.getInputStream();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    String line;
    List<String> fileContent = new ArrayList<>();
    while ((line = bufferedReader.readLine()) != null) {
      fileContent.add(line);
    }
    if (fileContent.size() == 0) {
      throw new ValidationException(emptyFileValidationMessage);
    }
    FileStatistics fileStatistics = getFileStatistics(fileContent, inputFile.getOriginalFilename());
    return fileStatistics;
  }

  private FileStatistics getFileStatistics(List<String> fileContent,
      String originalFilename) {
    FileStatistics fileStatistics = new FileStatistics();
    fileStatistics.setFileName(originalFilename);
    fileStatistics.setLinesCount(fileContent.size());
    List<String> words = fileContent.stream().
        flatMap(line -> Arrays.stream(line.split("\\p{IsWhite_Space}+")))
        .map(word -> word.replaceAll("[^\\p{IsAlphabetic}]", ""))
        .filter(word->!word.isEmpty())
        .collect(Collectors.toList());
    if (words.isEmpty()){
      throw new ValidationException(noWordsValidationMessage);
    }
    fileStatistics
        .setAverageWordsCountInLine((int) (words.stream().count() / fileContent.size()));
    fileStatistics.setMaxWordLength(
        words.stream().max(Comparator.comparing(String::length)).get().length());
    fileStatistics.setMinWordLength(
        words.stream().min(Comparator.comparing(String::length)).get().length());
    return fileStatistics;
  }

  @Override
  public Path saveFile(MultipartFile inputFile) throws IOException {
    String filePath = appArgs.getNonOptionArgs().get(0);
    byte[] bytes = inputFile.getBytes();
    if (bytes.length==0 || filePath.isEmpty()){
      return null;
    }
    String separator = filePath.trim().endsWith(File.separator) ? "" : File.separator;
    Path path = Paths.get(filePath + separator + inputFile.getOriginalFilename());
    fileWriter.write(path, bytes);
    return path;
  }

  @Override
  public List<FileStatistics> getStatistics() {
    return fileStatsRepository.findAll();
  }

  @Override
  public FileStatistics saveStatistics(FileStatistics fileStatistics) {
    return fileStatsRepository.save(fileStatistics);
  }
}
