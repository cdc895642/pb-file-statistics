DROP TABLE IF EXISTS "file_statistics" CASCADE;
CREATE TABLE "file_statistics" (
  "id" SERIAL NOT NULL,
  "file_name" VARCHAR(200) NOT NULL,
  "lines_count" INT NOT NULL,
  "avg_count_words_line" INT NOT NULL,
  "min_word_length" INT NOT NULL,
  "max_word_length" INT NOT NULL,
  CONSTRAINT file_statistics_pk PRIMARY KEY ("id")
);
