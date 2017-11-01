package com.pb.test.filestatistics.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pb.test.filestatistics.service.FileStatisticsServiceImpl;
import com.pb.test.filestatistics.validation.AppArgsValidator;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class StatisticsControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private FileStatisticsServiceImpl fileStatisticsService;
  @Mock
  private ApplicationArguments appArgs;
  @Mock
  private AppArgsValidator appArgsValidator;

  @Before
  public void setup() throws IOException {
    fileStatisticsService.setAppArgs(appArgs);
    fileStatisticsService.setAppArgsValidator(appArgsValidator);
  }

  @Sql("/init-test.sql")
  @Sql(scripts = "/clean-test.sql", executionPhase = AFTER_TEST_METHOD)
  @Test
  public void getStatistics_addFileAndEmptyDB_returnListStatsOneElement() throws Exception {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>(){{add("");}});
    when(appArgsValidator.isValid(any())).thenReturn(true);
    final int EXPECTED_SIZE = 1;
    final String TESTED_URL = "/getStatistics";
    final String[] EXPECTED_FILE_NAMES = {"test.txt"};
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        getInputFileByteArray());

    //Act && Assert
    mvc.perform(fileUpload(TESTED_URL).file(file)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
        .andExpect(jsonPath("$[*].id", not(0)))
        .andExpect(jsonPath("$[*].fileName", containsInAnyOrder(EXPECTED_FILE_NAMES)));
  }

  @Sql("/init-test.sql")
  @Sql("/insert-test.sql")
  @Sql(scripts = "/clean-test.sql", executionPhase = AFTER_TEST_METHOD)
  @Test
  public void getStatistics_addFileAndNotEmptyDB_returnListStatsTwoElementa() throws Exception {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>(){{add("");}});
    when(appArgsValidator.isValid(any())).thenReturn(true);
    final int EXPECTED_SIZE = 2;
    final String TESTED_URL = "/getStatistics";
    final String[] EXPECTED_FILE_NAMES = {"test.txt", "test0.txt"};
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        getInputFileByteArray());

    //Act && Assert
    mvc.perform(fileUpload(TESTED_URL).file(file)
    )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(EXPECTED_SIZE)))
        .andExpect(jsonPath("$[*].id", not(0)))
        .andExpect(jsonPath("$[*].fileName", containsInAnyOrder(EXPECTED_FILE_NAMES)));
  }

  @Test(expected = NestedServletException.class)
  public void getStatistics_emptyFile_returnError() throws Exception {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>(){{add("");}});
    when(appArgsValidator.isValid(any())).thenReturn(true);
    final int EXPECTED_HTTP_STATUS = 500;
    final String TESTED_URL = "/getStatistics";
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        new byte[]{});

    //Act && Assert
    mvc.perform(fileUpload(TESTED_URL).file(file)
    )
        .andExpect(status().is(EXPECTED_HTTP_STATUS));
  }

  @Test(expected = NestedServletException.class)
  public void getStatistics_noAppArgs_returnError() throws Exception {
    //Arrange
    when(appArgs.getNonOptionArgs()).thenReturn(new ArrayList<String>());
    when(appArgsValidator.isValid(any())).thenReturn(true);
    final int EXPECTED_HTTP_STATUS = 500;
    final String TESTED_URL = "/getStatistics";
    MockMultipartFile file = new MockMultipartFile("file",
        "test.txt",
        MediaType.TEXT_PLAIN_VALUE,
        getInputFileByteArray());

    //Act && Assert
    mvc.perform(fileUpload(TESTED_URL).file(file)
    )
        .andExpect(status().is(EXPECTED_HTTP_STATUS));
  }

  private byte[] getInputFileByteArray() throws UnsupportedEncodingException {
    String inputFileString="fisrt string aaa\n"
        + "second test string bbbbb\n"
        + "third string c";
    return inputFileString.getBytes();
  }
}
