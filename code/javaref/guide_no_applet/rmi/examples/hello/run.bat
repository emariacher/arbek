@echo off
set TOPDIR=..\..

rem  This BAT script explains and carries out the process of compiling
rem  and running the hello example.  The important commands are
rem  summarized here:
rem
rem    rmiregistry
rem    javac -d ..\.. *.java
rem    rmic -d ..\.. examples.hello.HelloImpl
rem    java -Djava.rmi.server.codebase="your_url" examples.hello.HelloImpl
rem    appletviewer index.html

echo ************************************************************************
echo *
echo * This script goes through the complete process of compiling and
echo * running the hello example on your local machine.

echo *
echo * To compile and run server, temporarily add the root directory
echo * of these classes (..\..) to CLASSPATH...
echo *
@echo on
set ORIGCLASSPATH=%CLASSPATH%
set CLASSPATH=%TOPDIR%;%CLASSPATH%
@echo off

echo *
echo * Start the RMI registry...
echo *
@echo on
start /min rmiregistry
@echo off

echo *
echo * Compile all Java sources files...
echo *
@echo on
javac -d %TOPDIR% *.java
@echo off

echo *
echo * Run rmic to generate stub and skeleton classes for HelloImpl...
echo *
@echo on
rmic -d %TOPDIR% examples.hello.HelloImpl
@echo off

echo *
echo * Start the server examples.hello.HelloImpl...
echo *
@echo on
start java examples.hello.HelloImpl
@echo off

echo *
echo * Please wait until a message appears in the server process's window
echo * indicating that it has been bound to the registry.
echo *
pause

echo *
echo * Restore the original CLASSPATH at this point, so that the appletviewer
echo * will not have any of these example classes locally available through
echo * CLASSPATH.  Therefore, it must load them through the codebase...
echo *
@echo on
set CLASSPATH=%ORIGCLASSPATH%
@echo off

echo *
echo * Start the appletviewer...
echo *
@echo on
appletviewer index.html
@echo off

echo *
echo * If all the preceding steps were successful, the registry and server
echo * processes are still running.  You can manually destroy these windows
echo * to clean up before running this script again.
echo *

