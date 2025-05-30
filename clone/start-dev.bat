@echo off
REM 현재 배치파일이 있는 경로를 변수에 저장
set SCRIPT_DIR=%~dp0

REM 백엔드 경로 (예: clone 폴더 위치)
set BACKEND_DIR=%SCRIPT_DIR%

REM 프론트엔드 경로 (백엔드 위치에서 상대경로)
set FRONTEND_DIR=%BACKEND_DIR%src\main\frontend\front

REM 백엔드 실행
cd /d "%BACKEND_DIR%"
start cmd /k "gradlew bootRun"

REM 5초 대기
timeout /t 5

REM 프론트엔드 실행
cd /d "%FRONTEND_DIR%"
start cmd /k "npm start"
