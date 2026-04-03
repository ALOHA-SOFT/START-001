package com.aloha.start.api.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aloha.start.domain.common.SmsTemplate;
import com.aloha.start.domain.users.Users;
import com.aloha.start.service.inter.common.SMSService;
import com.aloha.start.service.inter.common.SmsTemplateService;
import com.aloha.start.service.inter.users.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin/sms")
public class AdminSmsApi {

    @Autowired private SMSService smsService;
    @Autowired private SmsTemplateService smsTemplateService;
    @Autowired private UserService userService;

    @Value("${aligo.sender}") private String sender;

    /**
     * 문자 발송 (단일/단체)
     * receivers: 콤마 구분 전화번호 목록
     * msg: 문자 내용
     */
    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody Map<String, String> request) {
        try {
            String receivers = request.get("receivers");
            String msg = request.get("msg");

            if (receivers == null || receivers.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "수신번호를 입력해주세요."));
            }
            if (msg == null || msg.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "문자 내용을 입력해주세요."));
            }

            // 번호에서 숫자만 추출 후 콤마 구분 유지
            String[] phoneArr = receivers.split(",");
            List<String> results = new ArrayList<>();
            int successCount = 0;
            int errorCount = 0;

            for (String phone : phoneArr) {
                String cleanPhone = phone.trim().replaceAll("[^0-9]", "");
                if (cleanPhone.isEmpty()) continue;

                MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
                param.add("receiver", cleanPhone);
                param.add("msg", msg);

                try {
                    Map<String, Object> result = smsService.send(param);
                    if ("1".equals(String.valueOf(result.get("result_code")))) {
                        successCount++;
                        results.add(cleanPhone + ": 성공");
                    } else {
                        errorCount++;
                        results.add(cleanPhone + ": 실패 - " + result.get("message"));
                    }
                } catch (Exception e) {
                    errorCount++;
                    results.add(cleanPhone + ": 오류 - " + e.getMessage());
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", successCount > 0);
            response.put("message", "발송완료 (성공: " + successCount + ", 실패: " + errorCount + ")");
            response.put("successCount", successCount);
            response.put("errorCount", errorCount);
            response.put("details", results);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("관리자 SMS 발송 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "문자 발송 중 오류가 발생했습니다."));
        }
    }

    /**
     * 회원 검색 (이름 또는 전화번호)
     */
    @GetMapping("/users")
    public ResponseEntity<?> searchUsers(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        try {
            List<Users> allUsers = userService.list();
            List<Map<String, String>> result = new ArrayList<>();
            for (Users u : allUsers) {
                if (keyword.isEmpty() ||
                    (u.getName() != null && u.getName().contains(keyword)) ||
                    (u.getTel() != null && u.getTel().contains(keyword)) ||
                    (u.getUsername() != null && u.getUsername().contains(keyword))) {
                    Map<String, String> m = new HashMap<>();
                    m.put("name", u.getName());
                    m.put("tel", u.getTel());
                    m.put("username", u.getUsername());
                    result.add(m);
                }
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("회원 검색 오류: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    // -------- 자주 쓰는 문자 템플릿 CRUD --------

    @GetMapping("/templates")
    public ResponseEntity<?> getTemplates() {
        try {
            List<SmsTemplate> list = smsTemplateService.list();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PostMapping("/templates")
    public ResponseEntity<?> createTemplate(@RequestBody SmsTemplate template) {
        try {
            boolean result = smsTemplateService.insert(template);
            return ResponseEntity.ok(Map.of("success", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/templates/{id}")
    public ResponseEntity<?> updateTemplate(@PathVariable("id") String id, @RequestBody SmsTemplate template) {
        try {
            template.setId(id);
            boolean result = smsTemplateService.updateById(template);
            return ResponseEntity.ok(Map.of("success", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/templates/{id}")
    public ResponseEntity<?> deleteTemplate(@PathVariable("id") String id) {
        try {
            boolean result = smsTemplateService.deleteById(id);
            return ResponseEntity.ok(Map.of("success", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
