#!/bin/bash

# 현재 스크립트가 있는 절대 경로
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 백엔드 위치 (예: 스크립트 위치에서 한 단계 위의 clone 폴더)
BACKEND_DIR="$SCRIPT_DIR"

# 프론트엔드 위치 (백엔드 위치에서 상대경로로 지정)
FRONTEND_DIR="$BACKEND_DIR/src/main/frontend/front"

# 백엔드 실행
cd "$BACKEND_DIR" || exit
./gradlew bootRun &

# 5초 대기
sleep 5

# 프론트엔드 실행
cd "$FRONTEND_DIR" || exit
npm start
