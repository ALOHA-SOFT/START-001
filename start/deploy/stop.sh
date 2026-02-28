#!/bin/bash

# APP.war 프로세스 종료 스크립트
# 실행 중인 java -jar APP.war 프로세스 종료

PIDS=$(ps -ef | grep '[j]ava.*APP.war' | awk '{print $2}')

if [ -z "$PIDS" ]; then
  echo "실행 중인 APP.war 프로세스가 없습니다."
  exit 0
fi

for PID in $PIDS; do
  echo "APP.war 프로세스 종료: $PID"
  kill $PID
  sleep 1
  if ps -p $PID > /dev/null; then
    echo "강제 종료: $PID"
    kill -9 $PID
  fi
  echo "종료 완료: $PID"
  sleep 1
  done

echo "모든 APP.war 프로세스가 종료되었습니다."