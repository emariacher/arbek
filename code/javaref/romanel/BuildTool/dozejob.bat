D:
cd D:\SandBox\SeapineTools\BuildTool
PATH=%PATH%;D:\eric\programf\javacc-3.2\bin;C:\j2sdk1.4.2_05\bin;P:\output\WS\UML-RapidPrototyping\javacc;D:\SandBox\SeapineTools\BuildTool;C:\Program Files\Seapine\Surround SCM
set CLASSPATH=P:\output\WS\UML-RapidPrototyping\javacc;D:\eric\programf\javacc-3.2\bin\lib;D:\SandBox\SeapineTools\BuildTool;C:\Program Files\Seapine\Surround SCM
REM jar cmf MANIFEST.MF BuildRelease.jar *.class
java -jar BuildRelease.jar %1 product CDBU1_CDsim 172.17.16.20:4900
REM java BuildRelease %1 product CDBU1_CDsim 172.17.16.20:4900
PAUSE
