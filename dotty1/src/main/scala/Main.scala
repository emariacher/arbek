@main def hello: Unit = 
  println("Hello world!")
  println(msg)
  println(s.split(" ").toList)

def msg = "I was compiled by Scala 3. :)"
def s = "zorglub bulgroz"
