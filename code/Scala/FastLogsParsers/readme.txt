Eric Mariacher 19feb2013
needs the following libraries Scala2.10 + akka-actor_2.10-2.1.0.jar + config-1.0.0.jar

you shall have a directory structure where a "out" directory is created at the same level than the "src" directory
in the out directory, create a "log" directory

ControllerTestsTraces.scala parse 1 log file:
 * right click and save link "ctrl_test_g30_trunk_wimoR2_release_wes_1307c058" in main FAST Browser window: http://phonak.fast-cluster.com/fasthosting/phonak/viewer/results/list?category=726&order_by=startedat_dsc&page=1&perpage=50&type=all
 * double click doZeJob.bat
 * a dos window will open and then the html output in your default browser

 * you can also copy/paste line from nunit and then double click doZeJob.bat
 
FastLogsBrowser.scala parse all logs and try to find fatal messages which contains "0x08"
 * FAST Browser window: http://phonak.fast-cluster.com/fasthosting/phonak/viewer/results/list?category=726&order_by=startedat_dsc&page=1&perpage=50&type=all
 * find fatal messages which contains "0x08" 
 
