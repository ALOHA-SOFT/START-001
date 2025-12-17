package com.aloha.start.service.inter.system;

import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.Codes;
import com.aloha.start.service.inter.BaseService;
import com.github.pagehelper.PageInfo;

public interface CodesService extends BaseService<Codes> {

    public PageInfo<Codes> page(QueryParams queryParams);

}
