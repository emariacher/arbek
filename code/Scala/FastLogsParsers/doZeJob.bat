cls
@setlocal
echo on
SET PATH=.;.\bin;C:\Z\scala-2.1.0-RC5\bin;C:\Z\scala-2.1.0-RC5\lib;C:\Z\Javalibs\akka-2.1.0\lib\akka;%PATH%
SET CLASSPATH=%PATH%;%CLASSPATH%
echo %PATH%
echo %CLASSPATH%
scala.bat fastlogsparsers.ControllerTestsTraces
@endlocal
pause
