package com.aloha.start.controller.common;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aloha.start.domain.common.Media;
import com.aloha.start.service.inter.common.MediaService;
import com.aloha.start.utils.MediaUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/files")
public class FileController {

  @Autowired private MediaService mediaService;

  /**
   * 이미지 출력
   * @param id (media id)
   * @param response
   * @return
   */
  @ResponseBody
  @GetMapping("/img/{id}")
  public void getImage(
    @PathVariable("id") String id,
    HttpServletResponse response
  ) {

    log.info("id: {}", id);
    Media image = mediaService.selectByParentId(id);

    if (image == null) {
      log.error("getImage error: media not found - {}", id);
      return;
    }
    String filePath = image.getPath();
    String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
    String contentType = MediaUtils.getMediaType(ext).toString();
    File imgFile = new File(filePath);
    if (!imgFile.exists()) {
      log.error("getImage error: file not found - {}", filePath);
      return;
    }
    log.info("파일을 찾았습니다. {}", filePath);
    try {
      log.info("filePath: {}, ext: {}, contentType: {}", filePath, ext, contentType);
      response.setContentType(contentType);
      FileInputStream fis = new FileInputStream(imgFile);
      ServletOutputStream sos = response.getOutputStream();
      FileCopyUtils.copy(fis, sos);
      sos.close();
      fis.close();
    } catch (Exception e) {
      log.error("getImage error: {}", e.getMessage());
      return;
    }
    return;
  }
  

  
}


