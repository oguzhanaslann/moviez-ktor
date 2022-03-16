@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  com.oguzhanaslann.movies-server-ktor startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and COM_OGUZHANASLANN_MOVIES_SERVER_KTOR_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\com.oguzhanaslann.movies-server-ktor-0.0.1.jar;%APP_HOME%\lib\koin-ktor-3.1.5.jar;%APP_HOME%\lib\ktor-serialization-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-html-builder-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-server-netty-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-server-host-common-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-auth-jwt-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-auth-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-server-core-jvm-1.6.7.jar;%APP_HOME%\lib\kotlinx-html-jvm-0.7.3.jar;%APP_HOME%\lib\exposed-dao-0.37.3.jar;%APP_HOME%\lib\exposed-jdbc-0.37.3.jar;%APP_HOME%\lib\koin-core-jvm-3.1.5.jar;%APP_HOME%\lib\kotlin-stdlib-jdk8-1.6.10.jar;%APP_HOME%\lib\logback-classic-1.2.11.jar;%APP_HOME%\lib\ktor-client-cio-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-client-serialization-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-client-logging-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-client-json-jvm-1.6.7.jar;%APP_HOME%\lib\exposed-core-0.37.3.jar;%APP_HOME%\lib\ktor-client-core-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-http-cio-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-http-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-network-tls-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-network-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-utils-jvm-1.6.7.jar;%APP_HOME%\lib\ktor-io-jvm-1.6.7.jar;%APP_HOME%\lib\kotlin-stdlib-jdk7-1.6.10.jar;%APP_HOME%\lib\kotlin-reflect-1.6.10.jar;%APP_HOME%\lib\kotlinx-serialization-json-jvm-1.3.0.jar;%APP_HOME%\lib\kotlinx-serialization-core-jvm-1.3.0.jar;%APP_HOME%\lib\kotlin-stdlib-1.6.10.jar;%APP_HOME%\lib\slf4j-api-1.7.32.jar;%APP_HOME%\lib\kotlinx-coroutines-core-jvm-1.6.0.jar;%APP_HOME%\lib\kotlinx-coroutines-jdk8-1.6.0.jar;%APP_HOME%\lib\config-1.4.1.jar;%APP_HOME%\lib\jansi-2.4.0.jar;%APP_HOME%\lib\kotlin-stdlib-common-1.6.10.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\java-jwt-3.13.0.jar;%APP_HOME%\lib\jwks-rsa-0.17.0.jar;%APP_HOME%\lib\netty-codec-http2-4.1.69.Final.jar;%APP_HOME%\lib\alpn-api-1.1.3.v20160715.jar;%APP_HOME%\lib\netty-transport-native-kqueue-4.1.69.Final.jar;%APP_HOME%\lib\netty-transport-native-epoll-4.1.69.Final.jar;%APP_HOME%\lib\logback-core-1.2.11.jar;%APP_HOME%\lib\annotations-13.0.jar;%APP_HOME%\lib\junit-4.10.jar;%APP_HOME%\lib\jackson-databind-2.11.3.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\guava-30.0-jre.jar;%APP_HOME%\lib\netty-codec-http-4.1.69.Final.jar;%APP_HOME%\lib\netty-handler-4.1.69.Final.jar;%APP_HOME%\lib\netty-codec-4.1.69.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.69.Final.jar;%APP_HOME%\lib\netty-transport-4.1.69.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.69.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.69.Final.jar;%APP_HOME%\lib\netty-common-4.1.69.Final.jar;%APP_HOME%\lib\hamcrest-core-1.1.jar;%APP_HOME%\lib\jackson-annotations-2.11.3.jar;%APP_HOME%\lib\jackson-core-2.11.3.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.5.0.jar;%APP_HOME%\lib\error_prone_annotations-2.3.4.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar


@rem Execute com.oguzhanaslann.movies-server-ktor
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %COM_OGUZHANASLANN_MOVIES_SERVER_KTOR_OPTS%  -classpath "%CLASSPATH%" io.ktor.server.netty.EngineMain %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable COM_OGUZHANASLANN_MOVIES_SERVER_KTOR_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%COM_OGUZHANASLANN_MOVIES_SERVER_KTOR_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
