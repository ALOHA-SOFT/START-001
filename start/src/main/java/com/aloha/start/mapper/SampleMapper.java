package com.aloha.start.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aloha.start.domain.Sample;

@Mapper
public interface SampleMapper extends BaseMapper<Sample> {
  
  
}
