package com.pb.test.filestatistics.repository;

import com.pb.test.filestatistics.model.FileStatistics;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class FileStatsRepositoryImpl implements FileStatsRepository {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional(readOnly = true)
  @Override
  public List<FileStatistics> findAll() {
    return jdbcTemplate.query("select * from file_statistics",
        new FileStatRowMapper());
  }

  @Transactional
  @Override
  public FileStatistics save(FileStatistics fileStatistics) {
    final String sql = "insert into file_statistics("
        + " file_name, lines_count, avg_count_words_line, min_word_length, max_word_length) "
        + "values(?,?,?,?,?)";
    KeyHolder holder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, fileStatistics.getFileName());
      ps.setInt(2, fileStatistics.getLinesCount());
      ps.setInt(3, fileStatistics.getAverageWordsCountInLine());
      ps.setInt(4, fileStatistics.getMinWordLength());
      ps.setInt(5, fileStatistics.getMaxWordLength());
      return ps;
    }, holder);

    int newFileStatsId = 0;
    if (holder.getKeys() != null) {
      if (holder.getKeys().size() == 1) {
        newFileStatsId = holder.getKey().intValue();
      } else {
        newFileStatsId = (int) holder.getKeys().get("id");
      }
    }
    fileStatistics.setId(newFileStatsId);
    return fileStatistics;
  }
}
