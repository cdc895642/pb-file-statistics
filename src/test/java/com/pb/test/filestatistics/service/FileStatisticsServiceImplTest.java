package com.pb.test.filestatistics.service;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pb.test.filestatistics.model.FileStatistics;
import com.pb.test.filestatistics.repository.FileStatsRepositoryImpl;
import com.pb.test.filestatistics.validation.AppArgsValidator;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.ValidationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FileStatisticsServiceImplTest {

  @Mock
  private FileStatsRepositoryImpl fileStatsRepository;
  @Mock
  private ApplicationArguments appArgs;
  @Mock
  private AppArgsValidator appArgsValidator;
  @Mock
  private FileWriter fileWriter;
  @InjectMocks
  private FileStatisticsServiceImpl fileStatisticsService;
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Before
  public void setup() throws IOException {
    MockitoAnnotations.initMocks(this);
    doNothing().when(fileWriter).write(any(), any());
  }

  @Test (expected = ValidationException.class)
  public void calculateStatistics_noWords_exceptionThrown() throws IOException {
    //Arrange
    when(appArgsValidator.isValid(any())).thenReturn(false);
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "12345".getBytes());

    //Act
    FileStatistics fileStatistics = fileStatisticsService.calculateStatistics(file);
  }

  @Test (expected = ValidationException.class)
  public void calculateStatistics_emptyFile_exceptionThrown() throws IOException {
    //Arrange
    when(appArgsValidator.isValid(any())).thenReturn(false);
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        new byte[]{});

    //Act
    FileStatistics fileStatistics = fileStatisticsService.calculateStatistics(file);
  }

  @Test (expected = ValidationException.class)
  public void calculateStatistics_appArgsInvalid_exceptionThrown() throws IOException {
    //Arrange
    when(appArgsValidator.isValid(any())).thenReturn(false);
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        getInputFileByteArray());

    //Act
    FileStatistics fileStatistics = fileStatisticsService.calculateStatistics(file);
  }

  @Test
  public void calculateStatistics_correctValues_getStats() throws IOException {
    //Arrange
    when(appArgsValidator.isValid(any())).thenReturn(true);
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      //add("d:\\folder\\");
      add(temporaryFolder.newFolder().getPath());
    }});
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        getInputFileByteArray());

    //Act
    FileStatistics fileStatistics = fileStatisticsService.calculateStatistics(file);

    //Assert
    assertEquals(fileStatistics.getId(), 0);
    assertEquals(fileStatistics.getFileName(), "test.txt");
    assertEquals(fileStatistics.getAverageWordsCountInLine(), 3);
    assertEquals(fileStatistics.getLinesCount(), 3);
    assertEquals(fileStatistics.getMinWordLength(), 1);
    assertEquals(fileStatistics.getMaxWordLength(), 7);
  }

  @Test
  public void saveFile_emptyFile_returnNull() throws IOException {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      add("d:\\folder\\");
    }});
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        new byte[]{});

    //Act
    Path path = fileStatisticsService.saveFile(file);

    //Assert
    assertNull(path);
  }

  @Test
  public void saveFile_folderFinishedSeparator_returnPath() throws IOException {
    //Arrange
//    String expectedPath = "d:\\folder\\test.txt";
      String newFolder = temporaryFolder.newFolder().getPath();
      String expectedPath = newFolder + File.separator+"test.txt";
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      add(newFolder+File.separator);
    }});
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        getInputFileByteArray());

    //Act
    Path path = fileStatisticsService.saveFile(file);

    //Assert
    assertEquals(expectedPath, path.toString());
  }

  @Test
  public void saveFile_folderNotFinishedSeparator_returnPath() throws IOException {
    //Arrange
    //String expectedPath = "d:\\folder\\test.txt";
    String newFolder = temporaryFolder.newFolder().getPath();
    String expectedPath = newFolder + File.separator+"test.txt";

    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>() {{
      add(newFolder);
    }});
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        getInputFileByteArray());

    //Act
    Path path = fileStatisticsService.saveFile(file);

    //Assert
    assertEquals(expectedPath, path.toString());
  }

  @Test
  public void getStatistics_notEmptyDB_returnListResult() {
    //Arrange
    final int EXPECTED_SIZE = 2;
    final int EXPECTED_TIMES = 1;
    List<FileStatistics> expected = Arrays.asList(
        getFileStatistics(1),
        getFileStatistics(2)
    );
    when(fileStatsRepository.findAll())
        .thenReturn(expected);

    //Act
    List<FileStatistics> result = fileStatisticsService.getStatistics();

    //Assert
    assertEquals(EXPECTED_SIZE, result.size());
    assertEquals(expected, result);
    verify(fileStatsRepository, times(EXPECTED_TIMES)).findAll();
  }

  @Test
  public void saveStatistics_notEmptyStats_saveStats() {
    //Arrange
    final int EXPECTED_TIMES = 1;
    FileStatistics expected = getFileStatistics(1);
    when(fileStatsRepository.save(any(FileStatistics.class)))
        .thenReturn(expected);

    //Act
    FileStatistics result = fileStatisticsService.saveStatistics(expected);

    //Assert
    assertEquals(expected, result);
    verify(fileStatsRepository, times(EXPECTED_TIMES)).save(any());
  }

  private FileStatistics getFileStatistics(int id) {
    FileStatistics fileStatistics = new FileStatistics();
    fileStatistics.setId(id);
    fileStatistics.setFileName("name");
    return fileStatistics;
  }

  private byte[] getInputFileByteArray() throws UnsupportedEncodingException {
    String inputFileString = "test fisrt string aaa\n"
        + "second string bbbbb\n"
        + "third testing string c";
    return inputFileString.getBytes();
  }
}
