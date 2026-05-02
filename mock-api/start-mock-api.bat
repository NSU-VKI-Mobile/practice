@echo off
set "JAVA_EXE=C:\Program Files\Android\Android Studio\jbr\bin\java.exe"
set "JAVAC_EXE=C:\Program Files\Android\Android Studio\jbr\bin\javac.exe"
set "SCRIPT_DIR=%~dp0"

if not exist "%JAVAC_EXE%" (
  echo Android Studio JBR was not found: %JAVAC_EXE%
  exit /b 1
)

"%JAVAC_EXE%" -encoding UTF-8 "%SCRIPT_DIR%MockApiServer.java" || exit /b 1

"%JAVA_EXE%" -cp "%SCRIPT_DIR%" MockApiServer
