#!/bin/bash

# APP.war 재시작 스크립트 (stop.sh → start.sh 순서로 실행)

cd "$(dirname "$0")"

echo "=== APP.war 재시작 시작 ==="

# 기존 프로세스 종료
bash stop.sh

# 프로세스 완전 종료 대기
sleep 2

# 새 프로세스 시작
bash start.sh

echo "=== APP.war 재시작 완료 ==="
