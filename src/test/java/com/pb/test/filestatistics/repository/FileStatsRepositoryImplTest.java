package com.pb.test.filestatistics.repository;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pb.test.filestatistics.model.FileStatistics;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FileStatsRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;
  @Autowired
  private FileStatsRepositoryImpl fileStatsRepository;

  @Before
  public void setup() {
    fileStatsRepository.setJdbcTemplate(jdbcTemplate);
  }

  @Test
  public void save_validFileStatistics_changeId() {
    //Arrange
    final int EXPECTED_ID = 0;
    final int EXPECTED_TIMES = 1;
    when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
        .thenReturn(1);
    FileStatistics fileStatistics = getFileStatistics(1);

    //Act
    fileStatsRepository.save(fileStatistics);

    //Assert
    assertEquals(EXPECTED_ID, fileStatistics.getId());
    verify(jdbcTemplate,times(EXPECTED_TIMES)).update(any(PreparedStatementCreator.class),any());
  }

  @Test
  public void findAll_twoFileStatsInDb_returnListWithTwoElements() {
    //Arrange
    final int EXPECTED_SIZE = 2;
    final int EXPECTED_TIMES = 1;
    List<FileStatistics> expected = Arrays.asList(
        getFileStatistics(1),
        getFileStatistics(2)
    );
    when(jdbcTemplate.query(any(String.class), any(FileStatRowMapper.class)))
        .thenReturn(expected);

    //Act
    List<FileStatistics> result = fileStatsRepository.findAll();

    //Assert
    assertEquals(EXPECTED_SIZE, result.size());
    assertEquals(expected, result);
    verify(jdbcTemplate,times(EXPECTED_TIMES)).query(any(String.class),any(FileStatRowMapper.class));
  }


  private FileStatistics getFileStatistics(int id) {
    FileStatistics fileStatistics = new FileStatistics();
    fileStatistics.setId(id);
    fileStatistics.setFileName("name");
    fileStatistics.setMaxWordLength(10);
    fileStatistics.setAverageWordsCountInLine(11);
    fileStatistics.setMinWordLength(9);
    fileStatistics.setLinesCount(3);
    return fileStatistics;
  }
}
