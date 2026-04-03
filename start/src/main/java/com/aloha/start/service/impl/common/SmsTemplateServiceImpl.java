package com.aloha.start.service.impl.common;

import org.springframework.stereotype.Service;

import com.aloha.start.domain.common.SmsTemplate;
import com.aloha.start.mapper.common.SmsTemplateMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.common.SmsTemplateService;

@Service
public class SmsTemplateServiceImpl extends BaseServiceImpl<SmsTemplate, SmsTemplateMapper> implements SmsTemplateService {
}
