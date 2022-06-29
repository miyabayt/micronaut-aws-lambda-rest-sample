package com.bigtreetc.sample.micronaut.aws.lambda.base.web.controller.api.response;

import io.micronaut.data.model.Page;

public interface PageableApiResponse extends ListApiResponse {

  int getPage();

  int getTotalPages();

  void setPage(int page);

  void setTotalPages(int totalPages);

  default PageableApiResponse success(Page<?> page) {
    this.setPage(page.getPageNumber());
    this.setTotalPages(page.getTotalPages());
    this.setCount(page.getTotalSize());
    this.setData(page.getContent());
    this.success();
    return this;
  }
}
