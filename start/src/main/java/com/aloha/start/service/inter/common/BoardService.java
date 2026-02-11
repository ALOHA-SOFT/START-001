package com.aloha.start.service.inter.common;

import com.aloha.start.domain.common.Board;
import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.service.inter.BaseService;
import com.github.pagehelper.PageInfo;

public interface BoardService extends BaseService<Board> {

  PageInfo<Board> page(QueryParams queryParams);

  boolean delete(Long no);                                      // no(PK) 삭제
  boolean deleteById(String id);                                // id(PK) 삭제
  
}
