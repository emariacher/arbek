package logparsing

class DetectBoot(val lt: List[Test2]) {
    val limite = 2 // a partir de combien de tests ayant la meme ligne au debut considere t'on que c'est un duplicate?
    var lineNumber = 0
    var ltl = lt
    do {
        ltl = ltl.filter(_.size > lineNumber)
        var lapplies = ltl.groupBy(_.logs.apply(lineNumber).msg).filter(_._2.size >= limite)
        println("lineNumber: "+lineNumber+"\n  "+lapplies.map(g => g._1+" "+g._2.size).mkString("\n  "))
        ltl = lapplies.flatMap(_._2).toList
        ltl.foreach(_.logs.apply(lineNumber).duplicate=true)
        lineNumber += 1
    } while (!ltl.isEmpty)
}