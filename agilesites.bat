@echo off
if exist build.sbt goto run
echo "To start, copy build.sbt.sbt to build.sbt and EDIT IT."
echo "Reading the README.md and the documentation does not hurt, too." 
goto end 
:run
set SCRIPT_DIR=%~dp0
java -Xms256M -Xmx1024M -Xss1M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=384M -Dsbt.boot.directory=project\boot -jar "%SCRIPT_DIR%bin\sbt-launch.jar" %*
:end
