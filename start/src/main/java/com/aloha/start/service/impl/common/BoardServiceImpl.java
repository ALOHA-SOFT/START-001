package com.aloha.start.service.impl.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aloha.start.domain.common.Board;
import com.aloha.start.domain.common.QueryParams;
import com.aloha.start.mapper.common.BoardMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.common.BoardService;
import com.aloha.start.service.inter.common.MediaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl extends BaseServiceImpl<Board, BoardMapper> implements BoardService {
  
  @Autowired private BoardMapper boardMapper;
  @Autowired private MediaService mediaService;

  @Override
  public PageInfo<Board> page(QueryParams queryParams) {
      int page = (int) queryParams.getPage();
      int size = (int) queryParams.getSize();
      log.info("queryParams : {}", queryParams);
      PageHelper.startPage(page, size);
      Map<String, Object> params = new HashMap<>();
      params.put("queryParams", queryParams);
      List<Board> list = boardMapper.listWithParams(params);
      return new PageInfo<>(list);
  }

  @Override
  public boolean delete(Long no) {
    try {
        // 게시판 정보 조회
        Board board = boardMapper.selectById(no);
        if (board == null) {
            log.warn("Board not found with no: {}", no);
            return false;
        }

        // 파일 삭제
        boolean mediaDeleted = mediaService.deleteByParentId(board.getId());
        if (mediaDeleted) {
            log.info("Associated media deleted for board no: {}", no);
        } else {
            log.warn("Failed to delete associated media for board no: {}", no);
        }
        
        // 게시판 삭제
        int result = boardMapper.deleteById(no);
        if (result > 0) {
            log.info("Board deleted successfully - no: {}", no);
            return true;
        } else {
            log.warn("Failed to delete board - no: {}", no);
            return false;
        }
    } catch (Exception e) {
        log.error("Error deleting board with no: {}", no, e);
        return false;
    }
  }

  @Override
  public boolean deleteById(String id) {
    try {
        // id로 게시판 정보 조회
        QueryWrapper<Board> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Board board = boardMapper.selectOne(queryWrapper);
        
        if (board == null) {
            log.warn("Board not found with id: {}", id);
            return false;
        }

        // 파일 삭제
        boolean mediaDeleted = mediaService.deleteByParentId(id);
        if (mediaDeleted) {
            log.info("Associated media deleted for board id: {}", id);
        } else {
            log.warn("Failed to delete associated media for board id: {}", id);
        }
        
        // 게시판 삭제
        int result = boardMapper.delete(queryWrapper);
        if (result > 0) {
            log.info("Board deleted successfully - id: {}", id);
            return true;
        } else {
            log.warn("Failed to delete board - id: {}", id);
            return false;
        }
    } catch (Exception e) {
        log.error("Error deleting board with id: {}", id, e);
        return false;
    }
  }

}
