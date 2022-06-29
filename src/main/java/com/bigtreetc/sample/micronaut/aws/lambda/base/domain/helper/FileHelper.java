package com.bigtreetc.sample.micronaut.aws.lambda.base.domain.helper;

import io.micronaut.http.server.types.files.StreamedFile;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;

/** ファイルアップロード */
@Singleton
public class FileHelper {

  /**
   * ファイルの一覧を取得します。
   *
   * @param location
   * @return
   */
  public List<Path> listAllFiles(Path location) {
    try (val walk = Files.walk(location, 1)) {
      return walk.filter(path -> !path.equals(location))
          .map(location::relativize)
          .collect(Collectors.toList());
    } catch (IOException e) {
      throw new IllegalArgumentException("failed to list uploaded files. ", e);
    }
  }

  /**
   * ファイルを読み込みます。
   *
   * @param location
   * @param filename
   * @return
   */
  public StreamedFile loadFile(Path location, String filename) {
    try {
      val resolvedPath = location.resolve(filename);
      return new StreamedFile(resolvedPath.toUri().toURL());
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(
          "malformed Url resource. [location=%s, filename=%s]"
              .formatted(location.toString(), filename),
          e);
    }
  }
}
