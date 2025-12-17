package com.aloha.start.service.impl.system;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.Codes;
import com.aloha.start.mapper.system.CodesMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.system.CodesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodesServiceImpl extends BaseServiceImpl<Codes, CodesMapper> implements CodesService {
    @Autowired
    private CodesMapper codesMapper;

    @Override
    public PageInfo<Codes> page(QueryParams queryParams) {
        int page = (int) queryParams.getPage();
        int size = (int) queryParams.getSize();
        log.info("queryParams : {}", queryParams);
        PageHelper.startPage(page, size);
        Map<String, Object> params = new HashMap<>();
        params.put("queryParams", queryParams);
        List<Codes> list = codesMapper.list(params);
        return new PageInfo<>(list);
    }

}
