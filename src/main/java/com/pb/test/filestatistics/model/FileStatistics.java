package com.pb.test.filestatistics.model;

/**
 * Class that represent file statistics values
 */
public class FileStatistics {

  private int id;
  private String fileName;
  private int linesCount;
  private int averageWordsCountInLine;
  private int maxWordLength;
  private int minWordLength;

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getLinesCount() {
    return linesCount;
  }

  public void setLinesCount(int linesCount) {
    this.linesCount = linesCount;
  }

  public int getAverageWordsCountInLine() {
    return averageWordsCountInLine;
  }

  public void setAverageWordsCountInLine(int averageWordsCountInLine) {
    this.averageWordsCountInLine = averageWordsCountInLine;
  }

  public int getMaxWordLength() {
    return maxWordLength;
  }

  public void setMaxWordLength(int maxWordLength) {
    this.maxWordLength = maxWordLength;
  }

  public int getMinWordLength() {
    return minWordLength;
  }

  public void setMinWordLength(int minWordLength) {
    this.minWordLength = minWordLength;
  }
}
