package com.aloha.start.service.impl.common;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.start.domain.common.Media;
import com.aloha.start.mapper.common.MediaMapper;
import com.aloha.start.service.impl.BaseServiceImpl;
import com.aloha.start.service.inter.common.MediaService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MediaServiceImpl extends BaseServiceImpl<Media, MediaMapper> implements MediaService {

  @Autowired private MediaMapper mapper;

  @Value("${upload.path}")
  private String uploadPath;
  
  @Override
  public Media uploadFile(Media media) {
    if (media == null || media.getFile() == null) {
      log.warn("No file to upload");
      return null;
    }
    
    MultipartFile file = media.getFile();
    log.info("Uploading file: {}", file.getOriginalFilename());

    try {
      // 도메인 추출 (parentId에서 products, reviews 등 판별)
      String domain = extractDomainFromParentId(media.getDomain());
      
      // 업로드 디렉토리 생성
      File uploadDir = new File(uploadPath, domain);
      if (!uploadDir.exists()) {
        uploadDir.mkdirs();
        log.info("Created upload directory: {}", uploadDir.getAbsolutePath());
      }
      
      // 고유한 파일명 생성
      String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
      
      // 파일 저장
      File destFile = new File(uploadDir, uniqueFileName);
      FileCopyUtils.copy(file.getBytes(), destFile);
      log.info("File saved to: {}", destFile.getAbsolutePath());
      
      // 파일 타입 감지
      String fileType = determineFileType(file.getContentType(), file.getOriginalFilename());
      
      // Set media properties
      media.setContent("/upload/" + domain + "/" + uniqueFileName);  // 웹 접근 URL
      media.setName(uniqueFileName);
      media.setOriginName(file.getOriginalFilename());
      media.setPath(destFile.getAbsolutePath());  // 실제 파일 시스템 경로
      media.setType(fileType);

      // Save to database
      this.save(media);
      log.info("File uploaded successfully: {}", media.getContent());
      
      return media;
    } catch (Exception e) {
      log.error("Error uploading file: {}", file.getOriginalFilename(), e);
      return null;
    }
  }

  @Override
  public Media uploadFile(MultipartFile file, String parentId) {
    if (file == null || file.isEmpty()) {
      log.warn("No file to upload");
      return null;
    }
    Media media = new Media();
    media.setFile(file);
    media.setParentId(parentId);
    return uploadFile(media);
  }

  
  
  private String generateUniqueFileName(String originalFilename) {
    String uid = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    return uid + "_" + originalFilename;
  }

  @Override
  public Media uploadFile(MultipartFile file, String parentId, Boolean isMain, Boolean isThumb, Integer thumbSeq) {
    if (file == null || file.isEmpty()) {
      log.warn("No file to upload");
      return null;
    }
    Media media = new Media();
    media.setFile(file);
    media.setParentId(parentId);
    media.setIsMain(isMain);
    media.setIsThumb(isThumb);
    media.setThumbSeq(thumbSeq);
    return uploadFile(media);
  }

  @Override
  public Media selectByParentId(String parentId) {
    if (parentId == null || parentId.trim().isEmpty()) {
      log.warn("Parent ID is null or empty");
      return null;
    }
    try {
      QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("parent_id", parentId);
      return mapper.selectOne(queryWrapper);
    } catch (Exception e) {
      log.error("Error selecting media by parent ID: {}", parentId, e);
      return null;
    }
  }
  
  @Override
  public List<Media> selectAllByParentId(String parentId) {
    if (parentId == null || parentId.trim().isEmpty()) {
      log.warn("Parent ID is null or empty");
      return null;
    }
    try {
      QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("parent_id", parentId);
      return mapper.selectList(queryWrapper);
    } catch (Exception e) {
      log.error("Error selecting all media by parent ID: {}", parentId, e);
      return null;
    }
  }

  @Override
  public boolean deleteMediaById(String id) {
    log.info("===========================================================");
    log.info("id = {}", id);
    if (id == null || id.trim().isEmpty()) {
      log.warn("Media ID is null or empty");
      return false;
    }
    try {
      QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("id", id);
      Media media = mapper.selectOne(queryWrapper);
      if (media == null) {
        log.warn("No media found with ID: {}", id);
        return false;
      }
      
      // 로컬 파일 삭제
      if (media.getPath() != null) {
        File file = new File(media.getPath());
        if (file.exists()) {
          if (file.delete()) {
            log.info("File deleted from filesystem: {}", media.getPath());
          } else {
            log.warn("Failed to delete file from filesystem: {}", media.getPath());
          }
        }
      }
      
      // Delete record from database
      int rows = mapper.delete(queryWrapper);
      if (rows > 0) {
        log.info("Media record deleted from database: ID={}", id);
        return true;
      } else {
        log.warn("Failed to delete media record from database: ID={}", id);
        return false;
      }
    } catch (Exception e) {
      log.error("Error deleting media by ID: {}", id, e);
      return false;
    }

  }

  /**
   * domain 추출 (parentId에서 products, reviews 등 판별)
   */
  private String extractDomainFromParentId(String domain) {
    if (domain == null) {
      return "others";
    }
    return domain;
  }

  /**
   * 파일 타입 감지
   */
  private String determineFileType(String contentType, String filename) {
    // Content Type이 있는 경우
    if (contentType != null) {
      if (contentType.startsWith("image/")) {
        return "이미지";
      } else if (contentType.startsWith("video/")) {
        return "동영상";
      }
    }
    
    // 파일 확장자로 판별
    if (filename != null) {
      String lowerFilename = filename.toLowerCase();
      if (lowerFilename.matches(".*\\.(jpg|jpeg|png|gif|bmp|webp|svg)$")) {
        return "이미지";
      } else if (lowerFilename.matches(".*\\.(mp4|avi|mov|wmv|flv|webm|mkv)$")) {
        return "동영상";
      }
    }
    
    // 기본값
    return "이미지";
  }

  @Override
  public boolean deleteMediaByUrl(String url) {
    log.info("Deleting media by URL: {}", url);
    if (url == null || url.trim().isEmpty()) {
      log.warn("URL is null or empty");
      return false;
    }
    try {
      QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("content", url);
      List<Media> mediaList = mapper.selectList(queryWrapper);
      
      if (mediaList == null || mediaList.isEmpty()) {
        log.warn("No media found with URL: {}", url);
        return false;
      }
      
      boolean allDeleted = true;
      for (Media media : mediaList) {
        boolean deleted = deleteMediaById(media.getId());
        if (!deleted) {
          allDeleted = false;
          log.warn("Failed to delete media with ID: {}", media.getId());
        }
      }
      
      return allDeleted;
    } catch (Exception e) {
      log.error("Error deleting media by URL: {}", url, e);
      return false;
    }
  }

  @Override
  public boolean deleteByParentId(String id) {
    log.info("Deleting media by Parent ID: {}", id);
    if (id == null || id.trim().isEmpty()) {
      log.warn("Parent ID is null or empty");
      return false;
    }
    try {
      QueryWrapper<Media> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("parent_id", id);
      List<Media> mediaList = mapper.selectList(queryWrapper);
      
      if (mediaList == null || mediaList.isEmpty()) {
        log.warn("No media found with Parent ID: {}", id);
        return false;
      }
      
      boolean allDeleted = true;
      for (Media media : mediaList) {
        boolean deleted = deleteMediaById(media.getId());
        if (!deleted) {
          allDeleted = false;
          log.warn("Failed to delete media with ID: {}", media.getId());
        }
      }
      
      return allDeleted;
    } catch (Exception e) {
      log.error("Error deleting media by Parent ID: {}", id, e);
      return false;
    }
  }

  
}
