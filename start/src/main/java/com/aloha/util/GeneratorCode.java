package com.aloha.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

public class GeneratorCode {
    
    public static void main(String[] args) {
        // 데이터베이스 설정
        String url = "jdbc:mysql://localhost:3306/start?serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true&useSSL=false";
        String username = "aloha";
        String password = "123456";
        
        // 프로젝트 경로
        String projectPath = System.getProperty("user.dir") + "/start";
        String outputDir = projectPath + "/src/main/java";
        String mapperXmlDir = projectPath + "/src/main/resources/mapper";
        
        FastAutoGenerator.create(url, username, password)
            // 전역 설정
            .globalConfig(builder -> {
                builder.author("aloha")                // 작성자
                       .outputDir(outputDir)           // 출력 디렉토리
                       .commentDate("yyyy-MM-dd")      // 주석 날짜 형식
                       .dateType(DateType.TIME_PACK)   // 날짜 타입
                       .disableOpenDir();              // 생성 후 디렉토리 열지 않음
            })
            // 패키지 설정
            .packageConfig(builder -> {
                builder.parent("com.aloha.start")      // 부모 패키지
                       .entity("domain")                // Entity 패키지
                       .mapper("mapper")                // Mapper 패키지
                       .service("service.interface")        // Service 패키지
                       .serviceImpl("service.impl")     // ServiceImpl 패키지
                       .controller("controller")        // Controller 패키지
                       .pathInfo(Collections.singletonMap(OutputFile.xml, mapperXmlDir)); // Mapper XML 위치
            })
            // 전략 설정
            .strategyConfig(builder -> {
                builder.addInclude("users", "products", "orders", "order_items") // 생성할 테이블
                       .addTablePrefix("t_", "c_")      // 테이블 접두사 제거
                       // Entity 설정
                       .entityBuilder()
                       .enableLombok()                  // Lombok 사용
                       .enableTableFieldAnnotation()    // 필드 어노테이션 생성
                       .enableFileOverride()            // 파일 덮어쓰기
                       // Mapper 설정
                       .mapperBuilder()
                       .enableMapperAnnotation()        // @Mapper 어노테이션
                       .enableBaseResultMap()           // BaseResultMap 생성
                       .enableBaseColumnList()          // BaseColumnList 생성
                       .enableFileOverride()            // 파일 덮어쓰기
                       // Service 설정
                       .serviceBuilder()
                       .formatServiceFileName("%sService")      // Service 파일명 형식
                       .formatServiceImplFileName("%sServiceImpl") // ServiceImpl 파일명 형식
                       .enableFileOverride()            // 파일 덮어쓰기
                       // Controller 설정
                       .controllerBuilder()
                       .enableRestStyle()               // REST 스타일
                       .enableFileOverride();           // 파일 덮어쓰기
            })
            // 템플릿 엔진 설정
            .templateEngine(new VelocityTemplateEngine())
            .execute();
        
        System.out.println("코드 생성 완료!");
    }
}