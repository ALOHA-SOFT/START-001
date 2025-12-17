package com.aloha.start.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aloha.start.domain.system.SeqGroups;

@Mapper
public interface SeqGroupsMapper extends BaseMapper<SeqGroups> {
  
    public List<SeqGroups> list(Map<String, Object> params);
  
}
