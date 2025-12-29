package com.aloha.start.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aloha.start.domain.system.Codes;

@Mapper
public interface CodesMapper extends BaseMapper<Codes> {
  
    public List<Codes> list(Map<String, Object> params);

    public List<Codes> listWithParams(Map<String, Object> params);
  
}
