package com.aloha.start.service.inter.system;

import com.github.pagehelper.PageInfo;
import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.CodeGroups;
import com.aloha.start.service.inter.BaseService;

public interface CodeGroupsService extends BaseService<CodeGroups> {

    public PageInfo<CodeGroups> page(QueryParams queryParams);

}
