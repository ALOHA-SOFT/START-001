# START 프로젝트 분석서

## 1. 프로젝트 개요

| 항목 | 내용 |
|------|------|
| **프로젝트명** | start |
| **그룹** | com.aloha |
| **Java 버전** | 17 |
| **빌드 도구** | Gradle |
| **패키징** | WAR |
| **데이터베이스** | MySQL (`start`) |

---

## 2. 기술 스택

### Backend
| 기술 | 버전 | 용도 |
|------|------|------|
| **Spring Boot** | 3.5.11 | 애플리케이션 프레임워크 |
| **Spring Security** | 6.x | 인증/인가 (폼 로그인, Remember-Me, 역할 기반 접근 제어) |
| **MyBatis** | 3.0.3 | SQL Mapper |
| **MyBatis Plus** | 3.5.15 | ORM 확장 (BaseMapper, ServiceImpl, Code Generator) |
| **MySQL** | - | RDBMS |
| **Thymeleaf** | - | 서버사이드 템플릿 엔진 |
| **Thymeleaf Layout Dialect** | - | 레이아웃 상속 지원 |
| **Thymeleaf Extras SpringSecurity6** | - | 템플릿 내 Security 표현식 |
| **PageHelper** | 2.1.0 | MyBatis 페이지네이션 |
| **Lombok** | - | 보일러플레이트 코드 감소 |
| **Spring Mail** | - | 이메일 발송 (Gmail SMTP) |
| **Spring WebFlux (WebClient)** | - | 비동기 HTTP 클라이언트 |
| **Jackson** | - | JSON 직렬화/역직렬화 |
| **Commons FileUpload** | 1.5 | 파일 업로드 처리 |
| **Commons IO** | 2.15.1 | I/O 유틸리티 |
| **Velocity Engine** | 2.3 | MyBatis Plus 코드 생성기 템플릿 |
| **BCrypt** | - | 비밀번호 암호화 |

### Frontend
| 기술 | 용도 |
|------|------|
| **Bootstrap 5** | UI 프레임워크 |
| **jQuery** | DOM 조작 및 AJAX 통신 |
| **SweetAlert2 (Swal)** | 알림창/토스트 UI |
| **CHEditor** | 리치 텍스트 에디터 |
| **Pace.js** | 페이지 로딩 프로그레스 바 |

---

## 3. MVC 아키텍처 패턴

### 3.1 전체 구조
```
┌─────────────────────────────────────────────────────────────┐
│  Client (Browser)                                           │
├─────────────┬───────────────────────────────────────────────┤
│  Thymeleaf  │  REST API (JSON)                              │
│  Templates  │  /api/**                                      │
├─────────────┼───────────────────────────────────────────────┤
│  Controller │  @RestController (API)                        │
│  (View)     │  /api/users/**, /api/admin/**, /api/common/** │
├─────────────┴───────────────────────────────────────────────┤
│  Service Layer (Interface + Impl)                           │
│  BaseService<E> → BaseServiceImpl<E, M>                     │
├─────────────────────────────────────────────────────────────┤
│  Mapper Layer (MyBatis Plus BaseMapper + XML)                │
├─────────────────────────────────────────────────────────────┤
│  MySQL Database                                             │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 패키지 구조
```
com.aloha.start
├── StartApplication.java          # Spring Boot 진입점
├── ServletInitializer.java        # WAR 배포 지원
├── config/                        # 설정
│   ├── SecurityConfig.java        # Spring Security 설정
│   ├── WebConfig.java             # MVC 리소스 핸들러 설정
│   └── DDLConfig.java             # DDL 관리 설정
├── controller/                    # 뷰 컨트롤러 (Thymeleaf)
│   ├── HomeController.java        # / , /login, /join
│   ├── admin/                     # /admin/** (관리자 페이지)
│   ├── common/                    # 공통 (파일 서빙 등)
│   └── users/                     # /my/** (마이페이지)
├── api/                           # REST API 컨트롤러
│   ├── admin/                     # /api/admin/**
│   ├── common/                    # /api/common/**
│   ├── system/                    # /api/admin/seq, codes 등
│   ├── users/                     # /api/users/**
│   └── SampleApi.java             # /api/sample
├── service/
│   ├── inter/                     # 서비스 인터페이스
│   │   ├── BaseService.java       # 제네릭 CRUD 인터페이스
│   │   ├── admin/                 # PopupService
│   │   ├── common/                # BoardService, MediaService, EmailService
│   │   ├── system/                # Codes, CodeGroups, Seq, SeqGroups
│   │   ├── users/                 # UserService
│   │   └── sample/                # SampleService
│   └── impl/                     # 서비스 구현체
│       ├── BaseServiceImpl.java   # 제네릭 CRUD 구현 (MyBatis Plus ServiceImpl)
│       ├── admin/
│       ├── common/
│       ├── system/
│       ├── users/                 # CustomDetailsService (Spring Security)
│       └── sample/
├── mapper/                        # MyBatis Mapper 인터페이스
│   ├── admin/                     # PopupMapper
│   ├── common/                    # BoardMapper, MediaMapper
│   ├── system/                    # CodesMapper, CodeGroupsMapper, SeqMapper, SeqGroupsMapper
│   ├── users/                     # UserMapper, UserAuthMapper
│   └── SampleMapper.java
├── domain/                        # 엔티티/DTO
│   ├── Base.java                  # 공통 Base 엔티티
│   ├── Sample.java
│   ├── admin/                     # Popups
│   ├── common/                    # Board, Media, Files, Pagination, QueryParams
│   ├── system/                    # Codes, CodeGroups, Seq, SeqGroups
│   └── users/                     # Users, UserAuth, CustomUser
├── security/
│   └── handler/                   # Security 핸들러 (6개)
└── utils/                         # 유틸리티
    ├── FileUtils.java
    ├── MediaUtils.java
    └── CHEditor.java

com.aloha.util
└── GeneratorCode.java             # MyBatis Plus 코드 생성기
```

---

## 4. 데이터베이스 설계 (ERD)

### 4.1 테이블 구조

#### 사용자 관련
| 테이블 | 설명 | 주요 컬럼 |
|--------|------|-----------|
| `users` | 사용자 | no(PK), id(UK), username(UK), password, name, tel, email, enabled |
| `user_auth` | 사용자 권한 | no(PK), id(UK), user_no(FK→users), username, auth, name |

#### 콘텐츠 관련
| 테이블 | 설명 | 주요 컬럼 |
|--------|------|-----------|
| `board` | 게시판 | no(PK), id(UK), type, title, content, views, seq |
| `popups` | 팝업 | no(PK), id(UK), type, name, url, link, seq, content, started_at, ended_at, is_show |
| `media` | 미디어/파일 | no(PK), id(UK), parent_id(FK), is_main, type(ENUM), content, path, name |

#### 시스템 관련
| 테이블 | 설명 | 주요 컬럼 |
|--------|------|-----------|
| `code_groups` | 코드 그룹 | no(PK), id(UK), name, description |
| `codes` | 코드 | no(PK), id(UK), code_group_no(FK), name, value, code, seq |
| `seq_groups` | 시퀀스 그룹 | no(PK), id(UK), code, name, value, step |
| `seq` | 시퀀스 | no(PK), id(UK), seq_group_no(FK), code, value, date |
| `sample` | 샘플 | no(PK), id(UK), name |

### 4.2 공통 컬럼 규약
- `no` : BIGINT AUTO_INCREMENT (PK)
- `id` : VARCHAR(64) UUID (UK, 비즈니스 식별자)
- `created_at` : TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- `updated_at` : TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

### 4.3 테이블 관계
```
users (1) ──── (N) user_auth        [CASCADE DELETE]
code_groups (1) ── (N) codes          [CASCADE DELETE]
seq_groups (1) ─── (N) seq            [CASCADE DELETE]
board (1) ──────── (N) media          [parentId 로 연결, 서비스에서 CASCADE 처리]
popups (1) ─────── (N) media          [parentId 로 연결, 서비스에서 CASCADE 처리]
```

---

## 5. 주요 기능 모듈

### 5.1 인증/보안 (Spring Security)
- **폼 로그인** : `/login` → `CustomDetailsService` → `LoginSuccessHandler` / `LoginFailureHandler`
- **Remember-Me** : `PersistentTokenBasedRememberMeServices` (30일, DB 저장)
- **역할 기반 접근 제어** :
  - `/admin/**` → `ROLE_ADMIN`
  - `/users/**`, `/my/**` → `ROLE_USER`
  - 그 외 → `permitAll`
- **비밀번호 암호화** : BCryptPasswordEncoder
- **CSRF** : 활성화 (일부 예외 경로)
- **핸들러** : LoginSuccess, LoginFailure, CustomAuthenticationSuccess, CustomAuthenticationEntryPoint, CustomLogoutSuccess, CustomAccessDenied, CustomRememberMe

### 5.2 사용자 관리
- 회원가입 (UUID 생성, 비밀번호 암호화, ROLE_USER 자동 부여)
- 로그인 / 로그아웃
- 아이디 찾기 (이름 + 이메일)
- 비밀번호 찾기 (임시 비밀번호 이메일 발송)
- 마이페이지 (프로필 조회/수정/탈퇴)

### 5.3 관리자 기능 (`/admin`)
- 사용자 목록 관리 (페이징, 검색)
- 팝업 관리 (CRUD, 기간 설정, 공개 여부)
- 게시판 관리 (공지사항, FAQ, 이용안내, 자료실)
- 코드/코드그룹 관리
- 시퀀스/시퀀스그룹 관리

### 5.4 파일/미디어 관리
- 파일 업로드 (로컬 파일시스템, UUID 파일명)
- 미디어 메타데이터 DB 저장
- 미디어 타입 : 이미지, 동영상, 임베드
- 부모 엔티티 연동 (`parentId`)
- 연쇄 삭제 (부모 삭제 시 미디어 함께 삭제)

### 5.5 이메일 서비스
- 텍스트 메일 발송
- HTML 메일 발송
- 임시 비밀번호 메일 (스타일 포함 HTML 템플릿)

### 5.6 페이지네이션
- PageHelper 기반
- `QueryParams` DTO : page, size, search, sortBy[], sortOrder[], filter, params
- `Pagination` 헬퍼 : start, end, first, last, prev, next 계산

---

## 6. REST API 엔드포인트

### 6.1 사용자 API
| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/api/users/user` | 사용자 목록 (페이징) |
| GET | `/api/users/user/{id}` | 사용자 상세 조회 |
| POST | `/api/users/user` | 사용자 등록 |
| PUT | `/api/users/user` | 사용자 수정 |
| GET | `/api/users/auth` | 권한 목록 |
| POST | `/api/users/auth` | 권한 등록 |
| PUT | `/api/users/auth` | 권한 수정 |

### 6.2 관리자 API
| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/api/admin/popup` | 팝업 목록 |
| GET | `/api/admin/popup/{id}` | 팝업 상세 |
| POST | `/api/admin/popup` | 팝업 등록 |
| PUT | `/api/admin/popup` | 팝업 수정 |

### 6.3 공통 API
| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/api/common/board` | 게시판 목록 |
| GET | `/api/common/board/{id}` | 게시글 상세 |
| POST | `/api/common/board` | 게시글 등록 |
| PUT | `/api/common/board` | 게시글 수정 |
| GET | `/api/common/media` | 미디어 목록 |
| POST | `/api/common/media` | 미디어 등록 |
| POST | `/api/common/media/upload` | 파일 업로드 |
| PUT | `/api/common/media` | 미디어 수정 |

### 6.4 시스템 API
| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET/POST/PUT | `/api/admin/seq` | 시퀀스 CRUD |
| GET/POST/PUT | `/api/admin/seqgroups` | 시퀀스 그룹 CRUD |
| GET/POST/PUT | `/api/admin/codes` | 코드 CRUD |
| GET/POST/PUT | `/api/admin/codegroups` | 코드 그룹 CRUD |

---

## 7. 공통 함수 (JavaScript)

### 7.1 AJAX 통신 (`ajax.js`) — jQuery 기반
```javascript
/**
 * 공통 AJAX 요청 함수
 * - CSRF 토큰 자동 처리 (meta 태그에서 추출)
 * - FormData / JSON 자동 판별
 * - GET/DELETE : 쿼리 파라미터 전송
 * - POST/PUT   : JSON.stringify 또는 FormData 전송
 *
 * @param {Object} obj - { url, type, data, success, error }
 * @returns {Promise} 응답 데이터 또는 "FAIL"
 */
$ajax({ url, type, data, success, error })
```

### 7.2 알림창 (`alert.js`) — SweetAlert2 기반

| 함수 | 설명 | 반환 |
|------|------|------|
| `$alert(title, text, icon)` | 기본 알림창 | Promise |
| `$alert_(title, text, icon)` | 콜백 지원 알림창 (객체 파라미터 지원) | Promise |
| `$alertHTML(title, html, icon)` | HTML 지원 알림창 | Promise |
| `$confirm(title, text, icon, confirmBtn, cancelBtn)` | 확인/취소 다이얼로그 | Promise |
| `$confirmDeny(title, text, icon, confirmBtn, denyBtn, cancelBtn)` | 확인/거부/취소 다이얼로그 | Promise |
| `$confirmHTML(title, html, icon, confirmBtn, cancelBtn)` | HTML 확인 다이얼로그 | Promise |
| `$toast(obj)` | 토스트 알림 (자동 닫힘) | void |
| `$toast_(obj)` | 토스트 알림 (콜백 지원) | Promise |
| `alertLogin()` | 로그인 필요 알림 (회원가입/로그인 선택) | void |

### 7.3 공통 유틸 (`common.js`)
```javascript
/**
 * 로그아웃 함수 - 현재 경로를 redirect 파라미터로 전달
 */
logout()
```

---

## 8. 뷰 (Thymeleaf 템플릿)

### 8.1 레이아웃 구조
```
templates/
├── UI/
│   ├── layouts/
│   │   ├── admin/
│   │   │   └── admin_layout.html          # 관리자 레이아웃
│   │   └── user/
│   │       ├── main_layout.html           # 메인 레이아웃
│   │       └── my_layout.html             # 마이페이지 레이아웃
│   └── fragments/
│       ├── admin/                          # 관리자 프래그먼트 (header, sidebar, script, meta, link 등)
│       ├── common/
│       │   └── page.html                  # 페이지네이션 컴포넌트
│       └── user/                          # 사용자 프래그먼트 (header, footer, sidebar, popup, script 등)
├── index.html                             # 홈페이지
├── error/
│   ├── 403.html                           # 접근 거부
│   ├── 404.html                           # 페이지 없음
│   ├── 500.html                           # 서버 오류
│   └── exception.html                     # 예외 처리
└── page/
    ├── login.html                         # 로그인
    ├── join.html                          # 회원가입
    ├── find-id.html                       # 아이디 찾기
    ├── find-pw.html                       # 비밀번호 찾기
    ├── info.html                          # 정보 페이지
    ├── editor.html                        # 에디터 페이지
    ├── orders.html                        # 주문 페이지
    ├── my/
    │   ├── my.html                        # 마이페이지
    │   ├── edit.html                      # 정보 수정
    │   └── delete.html                    # 회원 탈퇴
    ├── admin/
    │   ├── admin.html                     # 관리자 대시보드
    │   ├── users/      (list, create, update)
    │   ├── popups/     (list, create, update)
    │   ├── board/      (list, create, update)
    │   ├── seq/        (list, create, update)
    │   ├── seqgroups/  (list, create, update)
    │   ├── codes/      (list, create, update)
    │   └── codegroups/ (list, create, update)
    └── terms/
        ├── 이용약관.html
        ├── 개인정보수집및이용(필수).html
        ├── 개인정보수집및이용(선택).html
        └── 마케팅및광고활용동의.html
```

---

## 9. 설정 프로파일

### 9.1 프로파일 구성
| 파일 | 용도 |
|------|------|
| `application.properties` | 공통 설정 (포트, 프로파일 선택) |
| `application-local.properties` | 로컬 개발 환경 (DB, 파일 경로) |
| `application-server.properties` | 서버 배포 환경 |
| `application-common.properties` | 공통 설정 (MyBatis, 페이징, 로깅, 메일) |

### 9.2 주요 설정
```properties
# MyBatis Plus
mybatis-plus.mapper-locations=classpath:mybatis/mapper/**/**.xml
mybatis-plus.type-aliases-package=com.aloha.start.domain
mybatis-plus.global-config.db-config.id-type=auto
mybatis-plus.global-config.db-config.logic-delete-value=1

# PageHelper
pagehelper.helperDialect=mysql
pagehelper.reasonable=true

# 파일 업로드
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=200MB
upload.path=C:/start/upload    (로컬 기준)
```

---

## 10. 배포

### 10.1 배포 스크립트
- `deploy/start.sh` : 애플리케이션 시작
- `deploy/stop.sh` : 애플리케이션 중지

### 10.2 패키징
- WAR 파일 빌드 (`war` 플러그인)
- `ServletInitializer` 를 통한 외부 톰캣 배포 지원
- `spring-boot-starter-tomcat` (providedRuntime)

---

## 11. 정적 리소스

| 경로 | 내용 |
|------|------|
| `static/assets/` | Bootstrap CSS/JS, 플러그인, 폰트, 이미지 |
| `static/css/` | 커스텀 스타일 (admin.css, reset.css, style.css) |
| `static/js/common/` | 공통 JS (ajax.js, alert.js, common.js) |
| `static/CHEditor/` | CHEditor 리치 텍스트 에디터 |
| `static/fonts/` | 커스텀 폰트 |
| `static/img/` | 커스텀 이미지 |
| `static/robots.txt` | 검색엔진 크롤링 설정 |
| `static/sitemap.xml` | 사이트맵 |
