package com.aloha.start.service.impl.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.aloha.start.service.inter.common.SMSService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SMSServiceImpl implements SMSService {

    @Value("${aligo.user_id}") String userId;
    @Value("${aligo.api-key}") String apiKey;
    @Value("${aligo.sender}") String sender;

    @Override
    public Map<String, Object> send(MultiValueMap<String, String> param) {
        try {
            final String encodingType = "utf-8";
            final String boundary = "____boundary____";

            String sms_url = "https://apis.aligo.in/send/";

            Map<String, String> sms = new HashMap<>();
            String rdate = param.getFirst("rdate") == null ? "" : param.getFirst("rdate");
            String rtime = param.getFirst("rtime") == null ? "" : param.getFirst("rtime");
            String testmodeYn = param.getFirst("testmode_yn") == null ? "N" : param.getFirst("testmode_yn");

            sms.put("user_id", userId);                                       // ✅(필수) SMS 아이디
            sms.put("key", apiKey);                                           // ✅(필수) 인증키
            sms.put("msg", param.getFirst("msg") );                       // ✅(필수) 문자 메시지                          
            sms.put("receiver", param.getFirst("receiver"));             // ✅(필수) 받는 번호
            sms.put("sender", sender);                                        // ✅(필수) 보내는 번호
            sms.put("rdate", rdate);                                              // 예약일자 20231225
            sms.put("rtime", rtime);                                              // 예약시간 1230 (현재시간 10분이후부터가능)
            sms.put("testmode_yn", testmodeYn);                               // 테스트 모드 여부
            String title = param.getFirst("title") == null ? "세상에이런트럭" : param.getFirst("title");
            sms.put("title", title);


            // 💎 이미지 첨부 관련
            String image = "";
            //image = "/tmp/pic_57f358af08cf7_sms_.jpg";

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();

            builder.setBoundary(boundary);
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName(encodingType));

            for (Iterator<String> i = sms.keySet().iterator(); i.hasNext(); ) {
                String key = i.next();
                builder.addTextBody(key, sms.get(key), ContentType.create("Multipart/related", encodingType));
            }

            File imageFile = new File(image);
            if (image != null && image.length() > 0 && imageFile.exists()) {
                builder.addPart("image", new FileBody(imageFile, ContentType.create("application/octet-stream"), imageFile.getName()));
            }

            HttpEntity entity = builder.build();

            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(sms_url);      // POST 방식의 요청 객체
            post.setEntity(entity);

            HttpResponse res = client.execute(post);    // 요청 보냄 (request)
                                                        // HttpResponse 응답 객체
            StringBuilder result = new StringBuilder();
            if (res != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), encodingType));
                String buffer;
                while ((buffer = in.readLine()) != null) {
                    result.append(buffer);
                }
                in.close();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = objectMapper.readValue(result.toString(), Map.class);
            return resultMap;
            /*
             * {"result_code":"1","message":"success","msg_id":"678458794","success_cnt":1,"error_cnt":0,"msg_type":"SMS"}
             * ➡ Map<String, Object> 으로 반환
             */

        } catch (Exception e) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("error", e.getMessage());
            return resultMap;
        }
    }
    
}
