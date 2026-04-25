@echo off
chcp 65001 > nul
echo.
echo =============================================
echo   Spring Boot bootWar Build ^& Deploy
echo =============================================
echo.

:: ────────────────────────────────────────────
:: 설정값 (본인 환경에 맞게 수정)
:: ────────────────────────────────────────────
set PROJECT_DIR=%~dp0
set REMOTE_USER=root
set REMOTE_HOST=alohaserver10.cafe24.com
set REMOTE_PATH=/var/www/start/backend
:: 🔐 비밀번호 입력 (안 보이게)
for /f "delims=" %%p in ('powershell -Command "$p = Read-Host ''SSH Password'' -AsSecureString; $BSTR=[System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($p); [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)"') do set REMOTE_PASS=%%p

:: ────────────────────────────────────────────
:: [1] bootWar 빌드
:: ────────────────────────────────────────────
echo [1/3] bootWar 빌드 중...
cd /d %PROJECT_DIR%
call gradlew.bat clean bootWar

if errorlevel 1 (
    echo.
    echo [오류] 빌드 실패! 에러를 확인하세요.
    pause
    exit /b 1
)

echo 빌드 완료 - APP.war 생성됨
echo.

:: ────────────────────────────────────────────
:: [2] APP.war + sh 파일 서버에 업로드
:: ────────────────────────────────────────────
echo [2/3] 파일 업로드 중...

pscp -pw %REMOTE_PASS% build\\libs\\APP.war %REMOTE_USER%@%REMOTE_HOST%:%REMOTE_PATH%/APP.war

if errorlevel 1 (
    echo.
    echo [오류] WAR 업로드 실패! 서버 연결을 확인하세요.
    pause
    exit /b 1
)

pscp -pw %REMOTE_PASS% deploy/start.sh deploy/stop.sh deploy/restart.sh %REMOTE_USER%@%REMOTE_HOST%:%REMOTE_PATH%/

echo 업로드 완료
echo.

:: ────────────────────────────────────────────
:: [3] 실행 권한 부여 + restart.sh 원격 실행
:: ────────────────────────────────────────────
echo [3/3] 서버에서 재시작 중...
plink -pw %REMOTE_PASS% %REMOTE_USER%@%REMOTE_HOST% "chmod +x %REMOTE_PATH%/*.sh && cd %REMOTE_PATH% && bash restart.sh"

if errorlevel 1 (
    echo.
    echo [오류] 재시작 실패! 서버 상태를 확인하세요.
    pause
    exit /b 1
)

:: ────────────────────────────────────────────
:: [4] 완료
:: ────────────────────────────────────────────
echo.
echo =============================================
echo   배포 완료!
echo   로그 확인: ssh %REMOTE_USER%@%REMOTE_HOST%
echo   tail -f %REMOTE_PATH%/log/appwar_*.log
echo =============================================
echo.
pause