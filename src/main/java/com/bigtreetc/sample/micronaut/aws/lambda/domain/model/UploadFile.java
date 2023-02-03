package com.bigtreetc.sample.micronaut.aws.lambda.domain.model;

import com.bigtreetc.sample.micronaut.aws.lambda.base.domain.model.BaseEntityImpl;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.persistence.Column;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@MappedEntity("upload_files")
@Getter
@Setter
public class UploadFile extends BaseEntityImpl {

  private static final long serialVersionUID = -1L;

  @Id @AutoPopulated UUID id;

  // ファイル名
  @Column(name = "file_name")
  String filename;

  // オリジナルファイル名
  @Column(name = "original_file_name")
  String originalFilename;

  // コンテンツタイプ
  String contentType;

  // コンテンツ
  byte[] content;
}
