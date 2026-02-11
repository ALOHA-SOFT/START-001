package com.aloha.start.utils;



import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.aloha.start.domain.common.Media;
import com.aloha.start.service.inter.common.MediaService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@CrossOrigin("*")
@RequestMapping("/CHEditor")
@Controller
public class CHEditor {
	
	// 업로드 경로
	@Value("${upload.path}")	
	private String uploadPath;
	
	@Autowired
	private MediaService mediaService;
	
	String SAVE_DIR = "/CHEditor";
	String SAVE_URL = "/CHEditor/attach/";
	
	// @Autowired
	// private FileUtils fileUtils;

	@ResponseBody
	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile uFile, 
	                     @RequestParam(value = "parentId", required = false) String parentId,
	                     HttpServletRequest request, HttpSession session) {
		
		log.info("=================================================================");
		log.info("CHEditor upload START");
		log.info("parentId: {}", parentId);
		log.info("file: {}", uFile.getOriginalFilename());
		log.info("file size: {}KB", uFile.getSize() / 1024);
		
		// parentId 누락 경고
		if (parentId == null || parentId.trim().isEmpty()) {
		    log.warn("⚠️ WARNING: parentId is NULL or EMPTY! Media will NOT be registered in database.");
		    log.warn("This file will be uploaded to local but cannot be deleted when parent entity is deleted.");
		}
		
		try {
		    // 로컬 파일 시스템에 업로드
		    String domain = "CHEditor";
		    
		    // Media 객체 생성 및 업로드
		    Media media = Media.builder()
		        .parentId(parentId != null && !parentId.trim().isEmpty() ? parentId : "temp")
		        .isMain(false)
		        .isThumb(false)
		        .domain(domain)
		        .build();
		    media.setFile(uFile);
		    
		    Media uploadedMedia = mediaService.uploadFile(media);
		    
		    if (uploadedMedia == null) {
		        throw new Exception("-ERR: Failed to upload file");
		    }
		    
		    String fileUrl = uploadedMedia.getContent();
		    log.info("✓ Local upload success: {}", fileUrl);
		    
		    // parentId가 없는 경우 기록만 남김
		    if (parentId == null || parentId.trim().isEmpty()) {
		        log.warn("✗ Media record saved with temp parentId (will need manual cleanup)");
		    } else {
		        log.info("✓ Media record saved - ID: {}, parentId: {}", uploadedMedia.getId(), parentId);
		    }
		    
		    long fileSize = uFile.getSize();
		    String fileName = uFile.getOriginalFilename();
		    
		    String rData = String.format("{\"fileUrl\":\"%s\", \"filePath\":\"%s\", \"fileName\":\"%s\", \"fileSize\":\"%d\"}",
		    							fileUrl, fileUrl, fileName, fileSize);
		    
		    log.info("CHEditor upload END - SUCCESS");
		    log.info("=================================================================");
		    return rData;

		} catch(Exception e) {
		    log.error("CHEditor upload END - FAILED");
		    log.error("Error: {}", e.getMessage(), e);
		    log.info("=================================================================");
		    return String.format("{\"error\":\"%s\"}", e.getMessage());
		}
		
	}
	
	
	// 에디터로 업로드한 이미지 미리보기
	@GetMapping("/attach/{fullName}")
	public ResponseEntity<byte[]> upload(HttpSession session, @PathVariable("fullName") String fullName) throws IOException {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		fullName = uploadPath + SAVE_DIR + "/" + fullName;
		
		try {
			// 확장자
			String formatName = fullName.substring(fullName.lastIndexOf(".") + 1);
			
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			HttpHeaders headers = new HttpHeaders();
			
			try {
				in = new FileInputStream(fullName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 이미지 타입인지
			if( mType != null ) {
				headers.setContentType(mType);
			} else {
				// UID_XXX.PNG
				fullName = fullName.substring(fullName.lastIndexOf("_") + 1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; fullName=\"" + new String(fullName.getBytes("UTF-8"), "ISO-8859") + "\"");
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			if( in != null )
				in.close();
		}
		
		return entity;
		
	}
	
	// 에디터로 업로드한 이미지 미리보기
	@GetMapping("/attach/{userId}/{fullName}")
	public ResponseEntity<byte[]> tempEditorPreview(HttpSession session, @PathVariable("fullName") String fullName
										) throws IOException {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		fullName = uploadPath + SAVE_DIR + "/" + fullName;
		
		try {
			// 확장자
			String formatName = fullName.substring(fullName.lastIndexOf(".") + 1);
			
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			HttpHeaders headers = new HttpHeaders();
			
			try {
				in = new FileInputStream(fullName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 이미지 타입인지
			if( mType != null ) {
				headers.setContentType(mType);
			} else {
				// UID_XXX.PNG
				fullName = fullName.substring(fullName.lastIndexOf("_") + 1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; fullName=\"" + new String(fullName.getBytes("UTF-8"), "ISO-8859") + "\"");
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			if( in != null )
				in.close();
		}
		
		return entity;
		
	}
	
	
	// 에디터로 업로드한 이미지 미리보기
	@GetMapping("/real/{fullName}")
	public ResponseEntity<byte[]> realEditorPreview(HttpSession session, @PathVariable("fullName") String fullName ) throws IOException {
		InputStream in = null;
		ResponseEntity<byte[]> entity = null;
		
		fullName = uploadPath + SAVE_DIR + "/" + fullName;
		
		try {
			// 확장자
			String formatName = fullName.substring(fullName.lastIndexOf(".") + 1);
			
			MediaType mType = MediaUtils.getMediaType(formatName);
			
			HttpHeaders headers = new HttpHeaders();
			
			try {
				in = new FileInputStream(fullName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			// 이미지 타입인지
			if( mType != null ) {
				headers.setContentType(mType);
			} else {
				// UID_XXX.PNG
				fullName = fullName.substring(fullName.lastIndexOf("_") + 1);
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
				headers.add("Content-Disposition", "attachment; fullName=\"" + new String(fullName.getBytes("UTF-8"), "ISO-8859") + "\"");
			}
			
			entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		} finally {
			if( in != null )
				in.close();
		}
		
		return entity;
		
	}
	
	
}










