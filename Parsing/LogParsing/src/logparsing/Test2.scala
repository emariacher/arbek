package logparsing

class Test2(val name: String, val result: String, val logs: List[LineTest2]) {
    if (!logs.isEmpty) {
        println("logs ["+logs+"]")
    }
    val size = logs.length
    val name2 = name.replaceAll("\\.", "_")
    var path = name.split("\\.").toList

    override def toString: String = "--{"+name+": "+result+", "+size+"}--"

    def isVoid = size == 0

    def toStringHtml: String = { // for hierarchical menu
        var s = "<div id=\""+name2+"\">\n"
        s += "<h2>"+name+" : "+result+"</h2>\n<table border=\"0\">"
        s += lines.map(_.toStringHtml).mkString("\n  ", "\n  ", "\n")
        s += "</table>\n</div>\n"
        s
    }

    def lines: List[LineTest2] = { // beautify log by removing adjacent duplicate timestamps and sourcefile names
        logs.sliding(2).toList.reverse.map(z => {
            if (z.head.timeStamp == z.last.timeStamp) {
                z.last.timeStamp = new TimeStamp
            }
            if (z.head.origin == z.last.origin) {
                z.last.origin = null
            }
        })
        logs
    }
}

