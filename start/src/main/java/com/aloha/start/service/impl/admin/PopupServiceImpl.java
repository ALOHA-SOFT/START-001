package com.aloha.start.service.impl.admin;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.start.domain.admin.Popups;
import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.mapper.admin.PopupMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.admin.PopupService;
import com.aloha.start.service.inter.common.MediaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PopupServiceImpl extends BaseServiceImpl<Popups, PopupMapper> implements PopupService {

    @Autowired private PopupMapper popupMapper;
    @Autowired private MediaService mediaService;

    @Override
    public PageInfo<Popups> page(QueryParams queryParams) {
        int page = (int) queryParams.getPage();
        int size = (int) queryParams.getSize();
        log.info("queryParams : {}", queryParams);
        PageHelper.startPage(page, size);
        Map<String, Object> params = new HashMap<>();
        params.put("queryParams", queryParams);
        List<Popups> list = popupMapper.listWithParams(params);
        return new PageInfo<>(list);
    }

    @Override
    public List<Popups> listByType(String type) {
        QueryWrapper<Popups> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return popupMapper.selectList(queryWrapper);
    }

    @Override
    public List<Popups> listByTypeOpen(String type) {
        QueryWrapper<Popups> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.eq("is_show", true);
        queryWrapper.le("started_at", LocalDateTime.now());
        queryWrapper.ge("ended_at", LocalDateTime.now());
        return popupMapper.selectList(queryWrapper);
    }

    @Override
    public boolean delete(Long no) {
        try {
            // 팝업 정보 조회
            Popups popup = popupMapper.selectById(no);
            if (popup == null) {
                log.warn("Popup not found with no: {}", no);
                return false;
            }
            
            // 팝업에 연결된 미디어 삭제 (URL 기준)
            if (popup.getUrl() != null && !popup.getUrl().trim().isEmpty()) {
                boolean mediaDeleted = mediaService.deleteMediaByUrl(popup.getUrl());
                if (mediaDeleted) {
                    log.info("Associated media deleted for popup no: {}", no);
                } else {
                    log.warn("Failed to delete associated media for popup no: {}", no);
                }
            }
            
            // 팝업 삭제
            int result = popupMapper.deleteById(no);
            if (result > 0) {
                log.info("Popup deleted successfully - no: {}", no);
                return true;
            } else {
                log.warn("Failed to delete popup - no: {}", no);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting popup with no: {}", no, e);
            return false;
        }
    }

    @Override
    public boolean deleteById(String id) {
        try {
            // id로 팝업 정보 조회
            QueryWrapper<Popups> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            Popups popup = popupMapper.selectOne(queryWrapper);
            
            if (popup == null) {
                log.warn("Popup not found with id: {}", id);
                return false;
            }
            
            // 팝업에 연결된 미디어 삭제 (URL 기준)
            if (popup.getUrl() != null && !popup.getUrl().trim().isEmpty()) {
                boolean mediaDeleted = mediaService.deleteMediaByUrl(popup.getUrl());
                if (mediaDeleted) {
                    log.info("Associated media deleted for popup id: {}", id);
                } else {
                    log.warn("Failed to delete associated media for popup id: {}", id);
                }
            }
            
            // 팝업 삭제
            int result = popupMapper.delete(queryWrapper);
            if (result > 0) {
                log.info("Popup deleted successfully - id: {}", id);
                return true;
            } else {
                log.warn("Failed to delete popup - id: {}", id);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting popup with id: {}", id, e);
            return false;
        }
    }

    
}
