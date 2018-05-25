PATH=%PATH%;D:\eric\programf\javacc-3.2\bin;C:\j2sdk1.4.2_05\bin;P:\output\WS\UML-RapidPrototyping\javacc;D:\SandBox\CaliberTools\NiceXLSCheckListTable;C:\Program Files\Seapine\Surround SCM;C:\Program Files\Seapine\Surround SCM\jre\bin;D:\SandBox\CaliberTools\NiceXLSCheckListTable\classes
set CLASSPATH=P:\output\WS\UML-RapidPrototyping\javacc;D:\eric\programf\javacc-3.2\bin\lib;D:\SandBox\CaliberTools\NiceXLSCheckListTable;D:\SandBox\CaliberTools\NiceXLSCheckListTable\classes;C:\Program Files\Seapine\Surround SCM
java -jar D:\eric\programf\saxon\saxon8.jar -t %1 D:\SandBox\CaliberTools\NiceXLSCheckListTable\calibertable3.xsl > %1.xml
pause
