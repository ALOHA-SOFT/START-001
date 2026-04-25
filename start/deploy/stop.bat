@echo off
chcp 65001 > nul
set REMOTE_USER=root
set REMOTE_HOST=alohaserver10.cafe24.com
set REMOTE_PATH=/var/www/start/backend
:: 🔐 비밀번호 입력 (안 보이게)
for /f "delims=" %%p in ('powershell -Command "$p = Read-Host ''SSH Password'' -AsSecureString; $BSTR=[System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($p); [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)"') do set REMOTE_PASS=%%p

echo [STOP] Spring Boot 앱 종료 중...
plink -pw %REMOTE_PASS% %REMOTE_USER%@%REMOTE_HOST% "cd %REMOTE_PATH% && bash stop.sh"
echo 완료!
pause