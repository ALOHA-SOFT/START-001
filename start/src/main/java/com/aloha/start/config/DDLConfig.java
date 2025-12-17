package com.aloha.start.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.ddl.IDdl;

@Component
public class DDLConfig implements IDdl {

  @Autowired
  private DataSource dataSource;

  @Override
  public void runScript(Consumer<DataSource> consumer) {
    try (Connection conn = dataSource.getConnection()) {
      // 데이터베이스 연결 인코딩 설정
      try (Statement stmt = conn.createStatement()) {
        stmt.execute("SET NAMES utf8mb4");
        stmt.execute("SET CHARACTER SET utf8mb4");
      }
      
      // SQL 파일들을 UTF-8로 읽어서 실행
      for (String sqlFile : getSqlFiles()) {
        executeSqlFile(conn, sqlFile);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void executeSqlFile(Connection conn, String sqlFilePath) throws Exception {
    System.out.println("Executing SQL file: " + sqlFilePath);
    ClassPathResource resource = new ClassPathResource(sqlFilePath);
    
    try (InputStream is = resource.getInputStream();
         InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
         BufferedReader br = new BufferedReader(isr);
         Statement stmt = conn.createStatement()) {
      
      // 각 파일 실행 전 인코딩 재설정
      stmt.execute("SET NAMES utf8mb4");
      stmt.execute("SET CHARACTER SET utf8mb4");
      
      StringBuilder sqlBuilder = new StringBuilder();
      String line;
      
      while ((line = br.readLine()) != null) {
        line = line.trim();
        
        // 주석과 빈 줄 건너뛰기
        if (line.isEmpty() || line.startsWith("--")) {
          continue;
        }
        
        sqlBuilder.append(line).append(" ");
        
        // 세미콜론으로 끝나면 SQL 문 실행
        if (line.endsWith(";")) {
          String sql = sqlBuilder.toString().trim();
          if (!sql.isEmpty()) {
            // 마지막 세미콜론 제거
            sql = sql.substring(0, sql.length() - 1);
            stmt.execute(sql);
          }
          sqlBuilder.setLength(0);
        }
      }
    }
    System.out.println("Completed SQL file: " + sqlFilePath);
  }

  @Override
  public List<String> getSqlFiles() {
    return List.of("db/DDL.sql", "db/data/users.sql");
  }
  
}
