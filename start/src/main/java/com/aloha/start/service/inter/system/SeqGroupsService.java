package com.aloha.start.service.inter.system;

import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.SeqGroups;
import com.aloha.start.service.inter.BaseService;
import com.github.pagehelper.PageInfo;

public interface SeqGroupsService extends BaseService<SeqGroups> {

    public PageInfo<SeqGroups> page(QueryParams queryParams);
}
