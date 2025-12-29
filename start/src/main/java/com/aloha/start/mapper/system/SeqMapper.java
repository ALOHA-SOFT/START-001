package com.aloha.start.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aloha.start.domain.system.Seq;

@Mapper
public interface SeqMapper extends BaseMapper<Seq> {
  
    public List<Seq> list(Map<String, Object> params);

    public List<Seq> listWithParams(Map<String, Object> params);
  
}
