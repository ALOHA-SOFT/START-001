package com.aloha.start.service.inter.admin;

import java.util.List;

import com.aloha.start.domain.admin.Popups;
import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.service.inter.BaseService;
import com.github.pagehelper.PageInfo;

public interface PopupService extends BaseService<Popups> {
  
    PageInfo<Popups> page(QueryParams queryParams);
  
    List<Popups> listByType(String type);
    List<Popups> listByTypeOpen(String type);

    boolean delete(Long no);                                      // no(PK) 삭제
    boolean deleteById(String id);                                // id(PK) 삭제
}
