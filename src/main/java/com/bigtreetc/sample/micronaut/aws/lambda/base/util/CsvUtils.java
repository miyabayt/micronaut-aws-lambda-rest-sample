package com.bigtreetc.sample.micronaut.aws.lambda.base.util;

import static com.fasterxml.jackson.dataformat.csv.CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import java.io.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

/** TSVファイル出力ユーティリティ */
@Slf4j
public class CsvUtils {

  private static final int OUTPUT_BYTE_ARRAY_INITIAL_SIZE = 4096;

  public static final String CHARSET__WINDOWS_31J = "Windows-31J";

  private static final CsvMapper csvMapper = createCsvMapper();

  /**
   * CSVマッパーを生成する。
   *
   * @return
   */
  private static CsvMapper createCsvMapper() {
    val mapper = new CsvMapper();
    mapper.configure(ALWAYS_QUOTE_STRINGS, true);
    mapper.findAndRegisterModules();
    return mapper;
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   */
  public static ByteArrayOutputStreamEx writeCsv(Class<?> clazz, Object data) {
    return write(clazz, data, CHARSET__WINDOWS_31J, ',');
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   */
  public static ByteArrayOutputStreamEx writeCsv(Class<?> clazz, Object data, String charsetName) {
    return write(clazz, data, charsetName, ',');
  }

  /**
   * TSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @throws Exception
   */
  public static ByteArrayOutputStreamEx writeTsv(Class<?> clazz, Object data) {
    return write(clazz, data, CHARSET__WINDOWS_31J, '\t');
  }

  /**
   * TSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   * @throws Exception
   */
  public static ByteArrayOutputStreamEx writeTsv(Class<?> clazz, Object data, String charsetName) {
    return write(clazz, data, charsetName, '\t');
  }

  /**
   * CSVファイルを出力します。
   *
   * @param clazz
   * @param data
   * @param charsetName
   * @param delimiter
   * @return
   * @throws Exception
   */
  @SneakyThrows
  private static ByteArrayOutputStreamEx write(
      Class<?> clazz, Object data, String charsetName, char delimiter) {
    // CSVヘッダをオブジェクトから作成する
    val schema = csvMapper.schemaFor(clazz).withHeader().withColumnSeparator(delimiter);

    // 書き出し
    val outputStream = new ByteArrayOutputStreamEx(OUTPUT_BYTE_ARRAY_INITIAL_SIZE);
    try (Writer writer = new OutputStreamWriter(outputStream, charsetName)) {
      csvMapper.writer(schema).writeValue(writer, data);
    }
    return outputStream;
  }

  public static class ByteArrayOutputStreamEx extends ByteArrayOutputStream {
    public ByteArrayOutputStreamEx(int size) {
      super(size);
    }

    public InputStream toInputStream() {
      return new ByteArrayInputStream(this.buf, 0, this.count);
    }
  }
}
