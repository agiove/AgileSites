@echo off
if not exist Sites\csinstall.bat goto notfoundsites
if not defined JAVA_HOME goto nojavahome
set PATH="%JAVA_HOME%"\bin;%PATH%
:nojavahome
java -version
if errorlevel 9009 if not errorlevel 9010 goto notfoundjava
set REPLACE=%~dp0
set REPLACE=%REPLACE:\=/%
copy install.ini Sites
del home\*.done
echo >hsql.switch 
java -cp ..\bin\wcs.jar wcs.Replacer ../ "%REPLACE%" <omii.ini >home\ominstallinfo\omii.ini
java -cp ..\bin\wcs.jar wcs.Replacer ../ "%REPLACE%" <omii.ini >Sites\omii.ini
java -cp ..\bin\wcs.jar wcs.Replacer ../ "%REPLACE%" <context.xml >webapps\cs\META-INF\context.xml
java -cp ..\bin\wcs.jar wcs.Unzip Sites\csdt.zip home
cd ..
java -cp bin\wcs.jar wcs.Configurator wcs
cd wcs
cd Sites
call csinstall -silent
goto pause
:notfoundsites
echo Sites installer not found.
echo Please download it from the Oracle website, 
echo then unzip the WCS_Sites*.zip in the wcs folder.
echo It is required the Sites installer in the wcs\Sites folder
goto pause 
:notfoundjava
echo java not found. Please install JDK and set JAVA_HOME environment variable. 
goto pause
:pause
pause
:end