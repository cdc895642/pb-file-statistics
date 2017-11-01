package com.pb.test.filestatistics.repository;

import com.pb.test.filestatistics.model.FileStatistics;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class FileStatRowMapper implements RowMapper<FileStatistics> {

  @Override
  public FileStatistics mapRow(ResultSet resultSet, int rowNum) throws SQLException {
    FileStatistics fileStatistics=new FileStatistics();
    fileStatistics.setId(resultSet.getInt("id"));
    fileStatistics.setFileName(resultSet.getString("file_name"));
    fileStatistics.setLinesCount(resultSet.getInt("lines_count"));
    fileStatistics.setAverageWordsCountInLine(resultSet.getInt("avg_count_words_line"));
    fileStatistics.setMinWordLength(resultSet.getInt("min_word_length"));
    fileStatistics.setMaxWordLength(resultSet.getInt("max_word_length"));
    return fileStatistics;
  }
}
