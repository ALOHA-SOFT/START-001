package com.aloha.start.service.impl.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.SeqGroups;
import com.aloha.start.mapper.system.SeqGroupsMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.system.SeqGroupsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeqGroupsServiceImpl extends BaseServiceImpl<SeqGroups, SeqGroupsMapper> implements SeqGroupsService {

    @Autowired 
    private SeqGroupsMapper seqGroupsMapper;

    @Override
    public PageInfo<SeqGroups> page(QueryParams queryParams) {
        int page = (int) queryParams.getPage();
        int size = (int) queryParams.getSize();
        log.info("queryParams : {}", queryParams);
        PageHelper.startPage(page, size);
        Map<String, Object> params = new HashMap<>();
        params.put("queryParams", queryParams);
        List<SeqGroups> list = seqGroupsMapper.list(params);
        return new PageInfo<>(list);
    }

}
