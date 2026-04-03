package com.aloha.start.mapper.common;

import org.apache.ibatis.annotations.Mapper;

import com.aloha.start.domain.common.SmsTemplate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface SmsTemplateMapper extends BaseMapper<SmsTemplate> {
}
