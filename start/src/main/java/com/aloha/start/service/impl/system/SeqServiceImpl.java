package com.aloha.start.service.impl.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.Seq;
import com.aloha.start.mapper.system.SeqMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.system.SeqService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SeqServiceImpl extends BaseServiceImpl<Seq, SeqMapper> implements SeqService {

    @Autowired private SeqMapper mapper;

    @Override
    public PageInfo<Seq> page(QueryParams queryParams) {
        log.info("## 시퀀스 목록 조회 ##");

        int page = queryParams.getPage();
        int size = queryParams.getSize();
        log.info("queryParams : {}", queryParams);

        applySearchFilter(queryParams);

        PageHelper.startPage(page, size);

        Map<String, Object> params = new HashMap<>(2);
        params.put("queryParams", queryParams);

        List<Seq> list = mapper.listWithParams(params);
        return new PageInfo<>(list);
    }

    private void applySearchFilter(QueryParams queryParams) {
        Map<String, Object> p = queryParams.getParams();
        
        // 검색 파라미터 정규화
        normalizeSearchParam(p, "seqId");
        normalizeSearchParam(p, "seqName");
        normalizeSearchParam(p, "groupId");
        normalizeSearchParam(p, "enabled");
    }

    private void normalizeSearchParam(Map<String, Object> p, String key) {
        Object obj = p.get(key);
        if (!hasText(obj)) {
            p.remove(key);
            return;
        }
        p.put(key, obj.toString().trim());
    }

    private boolean hasText(Object obj) {
        return obj != null && !obj.toString().trim().isEmpty();
    }
}
