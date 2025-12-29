package com.aloha.start.service.impl.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.CodeGroups;
import com.aloha.start.mapper.system.CodeGroupsMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.system.CodeGroupsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CodeGroupsServiceImpl extends BaseServiceImpl<CodeGroups, CodeGroupsMapper> implements CodeGroupsService {

    @Autowired private CodeGroupsMapper mapper;

    @Override
    public PageInfo<CodeGroups> page(QueryParams queryParams) {
        log.info("## 코드그룹 목록 조회 ##");

        int page = queryParams.getPage();
        int size = queryParams.getSize();
        log.info("queryParams : {}", queryParams);

        applySearchFilter(queryParams);

        PageHelper.startPage(page, size);

        Map<String, Object> params = new HashMap<>(2);
        params.put("queryParams", queryParams);

        List<CodeGroups> list = mapper.listWithParams(params);
        return new PageInfo<>(list);
    }

    private void applySearchFilter(QueryParams queryParams) {
        Map<String, Object> p = queryParams.getParams();
        
        // 검색 파라미터 정규화
        normalizeSearchParam(p, "groupId");
        normalizeSearchParam(p, "groupName");
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
