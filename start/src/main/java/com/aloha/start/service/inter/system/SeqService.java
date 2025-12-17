package com.aloha.start.service.inter.system;

import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.domain.system.Seq;
import com.aloha.start.service.inter.BaseService;
import com.github.pagehelper.PageInfo;

public interface SeqService extends BaseService<Seq> {

    public PageInfo<Seq> page(QueryParams queryParams);
}
