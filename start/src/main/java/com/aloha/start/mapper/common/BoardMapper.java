package com.aloha.start.mapper.common;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.start.domain.common.Board;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface BoardMapper extends BaseMapper<Board> {

  public List<Board> listWithParams(Map<String, Object> params);
  
}
