package com.aloha.start.mapper.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aloha.start.domain.system.CodeGroups;

@Mapper
public interface CodeGroupsMapper extends BaseMapper<CodeGroups> {

    public List<CodeGroups> list(Map<String, Object> params);
  
}
