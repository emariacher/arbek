package fastlogsparsers

import java.io.File
import scala.swing.TextField

object OneTestSummary extends App {
	var L = MyLog.newMyLog(this.getClass.getName,new File("out\\zorg"),"html")
			L.createGui(List(("testfilter","",new TextField)))					
			val zfilter = L.Gui.getAndClose.head._2
			L = MyLog.newMyLog(this.getClass.getName,new File("out\\"+zfilter),"html")
			new InspectMutipleLog(RunUtil.toHtmlFilterTests, zfilter, "get a unified view for ")
}