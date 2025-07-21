@echo off
if "%JAVA_HOME%"=="" (
  echo Please set JAVA_HOME to your JDK path.
  exit /b 1
)
call mvnw.cmd spring-boot:run 