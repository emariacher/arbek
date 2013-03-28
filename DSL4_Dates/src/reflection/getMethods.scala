package reflection

object getMethods extends App {
  def printMethods[T](t: T) { // requires instance
    val meths = t.getClass.getMethods
    println("\n******************printMethods("+t.getClass.getName+")")
    println(meths take 5 mkString "\n")
  }
  def printMethods1(name: String) { // low-level
    val meths = Class.forName(name).getMethods
    println("\n******************printMethods1("+name+")\n")
    println(meths take 5 mkString "\n")
  }
  def printMethods2[T: Manifest] { // no instance
    val meths = manifest[T].runtimeClass.getMethods
    println("\n******************printMethods2("+manifest[T].runtimeClass.getName+")")
    println(meths take 5 mkString "\n")
  }
  printMethods(Some(""))
  printMethods1("scala.Some")
  printMethods2[Some[_]]
}
