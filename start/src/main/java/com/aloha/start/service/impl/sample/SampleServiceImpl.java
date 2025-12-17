package com.aloha.start.service.impl.sample;

import org.springframework.stereotype.Service;

import com.aloha.start.domain.Sample;
import com.aloha.start.mapper.SampleMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.sample.SampleService;

import groovy.util.logging.Slf4j;

@Slf4j
@Service
public class SampleServiceImpl extends BaseServiceImpl<Sample, SampleMapper> implements SampleService {

  
}
