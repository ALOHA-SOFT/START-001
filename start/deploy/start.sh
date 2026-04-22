#!/bin/bash

# 현재 스크립트가 위치한 경로로 이동
cd "$(dirname "$0")"

# 로그 디렉토리 생성
LOG_DIR="../log"
mkdir -p "$LOG_DIR"
LOG_FILE="$LOG_DIR/appwar_$(date +%Y%m%d_%H%M%S).log"

# JAVA_HOME이 설정되어 있지 않으면 java 명령어 사용
JAVA_CMD=${JAVA_HOME:-}/bin/java
if [ ! -x "$JAVA_CMD" ]; then
  JAVA_CMD=java
fi

# JVM 메모리 제한 (최소 128MB, 최대 256MB)
JAVA_OPTS="-Xms128m -Xmx256m"

# APP.war 파일을 백그라운드에서 실행하고 로그 저장
$JAVA_CMD $JAVA_OPTS -jar APP.war > "$LOG_FILE" 2>&1 &