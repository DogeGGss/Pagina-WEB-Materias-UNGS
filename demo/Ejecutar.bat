@echo off
echo Compilando proyecto...
call mvnw.cmd clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Error al compilar el proyecto
    pause
    exit /b 1
)
echo.
echo Ejecutando aplicacion...
call mvnw.cmd spring-boot:run
pause