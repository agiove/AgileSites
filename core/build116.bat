@echo off
if "%1"=="" goto nofile
set v=11.1.1.6.0
set h=%1\WEB-INF\lib
if not exist %h%\systemtools-1.1.2.jar goto nofile
call mvn install:install-file -Dfile="%h%\cs-core.jar" -DgroupId=com.oracle.sites -DartifactId=cs-core -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\cs.jar" -DgroupId=com.oracle.sites -DartifactId=cs -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\wem-sso-api-1.2.2.jar" -DgroupId=com.oracle.sites -DartifactId=wem-sso-api -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\assetapi.jar" -DgroupId=com.oracle.sites -DartifactId=assetapi -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\assetapi-impl.jar" -DgroupId=com.oracle.sites -DartifactId=assetapi-impl -Dversion=%v% -Dpackaging=jar
call mvn install:install-file -Dfile="%h%\xcelerate.jar" -DgroupId=com.oracle.sites -DartifactId=xcelerate -Dversion=%v% -Dpackaging=jar
:gen
call sbt "core116/sitesTagWrapperGen %1" "core116/publishM2"
goto end
:nofile
echo Please specify the path of an existing directory for WebCenter Sites %v%
:end
