package euler
import scala.collection.immutable.ListSet
import language.postfixOps

class SuDoKu96(val lines: List[String], val title: String) {
    var history = List[SuDoKuCase]()
    val one2nine = new Range(1, 10, 1).toList
    var cases = List[SuDoKuCase]()

    assume(lines.size == 9)

    lines.zipWithIndex.foreach((line: (String, Int)) => {
        cases = cases ++ line._1.toList.map(_.toString.toInt).toList.zipWithIndex.map(new SuDoKuCase(_, line._2))
    })
    val rangees = cases.grouped(9).toList

    val colonnes = {
        var c = List[List[euler.SuDoKuCase]]()
        val range = new Range(0, 9, 1).toList.foreach((i: Int) => {
            c = c :+ rangees.map(_.apply(i))
        })
        c
    }

    val carres = {
        new Range(0, 3, 1).toList.flatMap((j: Int) => {
            new Range(0, 3, 1).toList.map((i: Int) => {
                val x = rangees.apply(j * 3).grouped(3).toList.apply(i)
                val y = rangees.apply((j * 3) + 1).grouped(3).toList.apply(i)
                val z = rangees.apply((j * 3) + 2).grouped(3).toList.apply(i)
                new SuDoKuCarre(x ++ y ++ z)
            })
        })
    }

    def top3: Int = rangees.head.grouped(3).toList.head.mkString("").toInt

    def forbid = {
        rangees.foreach((rangee: List[SuDoKuCase]) => {
            val li = rangee.map(_.now).distinct
            rangee.foreach((tcase: SuDoKuCase) => tcase.forbid(li))
        })
        colonnes.foreach((rangee: List[SuDoKuCase]) => {
            val li = rangee.map(_.now).distinct
            rangee.foreach((tcase: SuDoKuCase) => tcase.forbid(li))
        })
        carres.foreach((carre: SuDoKuCarre) => {
            val li = carre.cases.map(_.now).distinct
            carre.cases.foreach((tcase: SuDoKuCase) => tcase.forbid(li))
        })
        //		println(carres.map(_.toString2+"\n"))
    }

    def obvious: Boolean = {
        forbid
        carres.foreach(_.obvious)
        forbid
        obviousRangees
        forbid
        obviousColonnes
        forbid
        shouldRangees
        forbid
        shouldColonnes
        forbid
        obviousCases & check
    }

    override def toString: String = {
        var cptRangee = 0
        var s = title + "\n +-----+-----+-----+\n !"
        rangees.foreach((rangee: List[SuDoKuCase]) => {
            var cases = rangee.grouped(3).toList
            cases.foreach((tcases: List[SuDoKuCase]) => {
                s += tcases.mkString(" ") + "!"
            })
            cptRangee += 1
            if (cptRangee % 3 == 0) {
                s += "\n +-----+-----+-----+\n !"
            } else {
                s += "\n !"
            }
        })
        s
    }

    def finished: Boolean = carres.filter(!_.finished).isEmpty

    def check: Boolean = {
        if (!carres.filter(!_.check).isEmpty) {
            false
        } else if (finished) {
            val z = rangees.filter((rangee: List[SuDoKuCase]) => rangee.map(_.now).sorted != new Range(1, 10, 1).toList)
            if (z.isEmpty) {
                true
            } else {
                println("check: " + z)
                false
            }
        } else {
            true
        }
    }

    def solve1(cases2replace: List[SuDoKuCase]): (Int, Boolean) = {
        cases.foreach(_.reset)
        cases2replace.foreach((case2replace: SuDoKuCase) => {
            val y = cases.filter((tcase: SuDoKuCase) => case2replace.row == tcase.row & case2replace.col == tcase.col).head
            y.now = case2replace.now
        })

        println("===== " + title + " =======================================================================")
        //println(" history: "+history.map(_.toString4+" *** "))
        var cpt = 0
        var s = ""
        var sold = ""
        var stop = false
        while ((!finished) & (!stop)) {
            //println("____________________________________________________________")
            val obv = obvious
            s = toString
            //println("1["+cpt+"] "+s)
            if (!obv) {
                println("******** La1! ******** " + obv)
                require(!history.isEmpty)
                stop = true
            } else if (s == sold) {
                println("******** Plan B ********")
                //println(carres.map(_.toString2))
                //println("2["+cpt+"] "+toString)
                cases.foreach(_.wildGuess)
                val dcases = cases.filter(_.lcases.size == 2).flatMap((tcase: SuDoKuCase) => {
                    List(tcase.copy(tcase.lcases.head), tcase.copy(tcase.lcases.last))
                })
                history = history ++ dcases
                stop = true
            }
            if (cpt > 10) {
                require(false, " looping endlessly")
            }
            sold = s
            cpt += 1
        }
        (cpt, check & finished)
    }

    def obviousCases: Boolean = cases.map(_.obvious).filter(_ == false).isEmpty
    /*def obviousCases: Boolean = {
						val z = cases.map(_.obvious)
								println("*5*"+z.filter(_ == false))
								println("*6*"+z.filter(_ == false).isEmpty)
								z.filter(_ == false).isEmpty
					}*/

    def obviousRangees = {
        rangees.foreach((rangee: List[SuDoKuCase]) => {
            one2nine.foreach((i: Int) => {
                val lcases = rangee.filter((tcase: SuDoKuCase) => {
                    (!tcase.cant.contains(i)) & (tcase.now == 0)
                })
                if (lcases.size == 1) {
                    lcases.head.now = i
                }
            })
        })
    }

    def shouldRangees: Boolean = {
        var r = false
        rangees.foreach((rangee: List[SuDoKuCase]) => {
            var carreAllFound = List[SuDoKuCase]()
            var forbidden1 = ListSet[Int]()
            var forbidden2 = ListSet[Int]()
            val gp3s = rangee.grouped(3).toList
            gp3s.foreach((gp3: List[SuDoKuCase]) => {
                if (gp3.filter(_.now != 0).size == 3) {
                    carreAllFound = gp3
                } else if (forbidden1.isEmpty) {
                    forbidden1 = gp3.apply(0).cant intersect gp3.apply(1).cant intersect gp3.apply(2).cant
                } else {
                    forbidden2 = gp3.apply(0).cant intersect gp3.apply(1).cant intersect gp3.apply(2).cant
                }
            })

            forbidden1 = (forbidden1 -- carreAllFound.map(_.now)).filter((i: Int) => !rangee.map(_.now).contains(i))
            forbidden2 = (forbidden2 -- carreAllFound.map(_.now)).filter((i: Int) => !rangee.map(_.now).contains(i))
            if ((!carreAllFound.isEmpty) & (!(forbidden1 ++ forbidden2).isEmpty)) {
                //println("rangee: "+rangee+"\n  carreAllFound: "+carreAllFound+", forbidden1: "+forbidden1+", forbidden2: "+forbidden2)
                gp3s.foreach((gp3: List[SuDoKuCase]) => {
                    if (gp3.filter(_.now != 0).size == 3) {
                        carreAllFound = gp3
                    } else if (!gp3.filter(!_.shouldR.isEmpty).isEmpty) {
                        r = false
                    } else if (gp3.head.cant.intersect(forbidden1).isEmpty) {
                        gp3.apply(0).shouldR = forbidden1.filter(!gp3.apply(0).cant.contains(_))
                        gp3.apply(1).shouldR = forbidden1.filter(!gp3.apply(1).cant.contains(_))
                        gp3.apply(2).shouldR = forbidden1.filter(!gp3.apply(2).cant.contains(_))
                        r = true
                    } else if (gp3.head.cant.intersect(forbidden2).isEmpty) {
                        gp3.apply(0).shouldR = forbidden2.filter(!gp3.apply(0).cant.contains(_))
                        gp3.apply(1).shouldR = forbidden2.filter(!gp3.apply(1).cant.contains(_))
                        gp3.apply(2).shouldR = forbidden2.filter(!gp3.apply(2).cant.contains(_))
                        r = true
                    }
                    //println(gp3.map(_.toString3))
                })

            }
        })
        carres.foreach(_.shouldR2forbid)
        r
    }

    def shouldColonnes: Boolean = {
        var r = false
        colonnes.foreach((colonne: List[SuDoKuCase]) => {
            var carreAllFound = List[SuDoKuCase]()
            var forbidden1 = ListSet[Int]()
            var forbidden2 = ListSet[Int]()
            val gp3s = colonne.grouped(3).toList
            gp3s.foreach((gp3: List[SuDoKuCase]) => {
                if (gp3.filter(_.now != 0).size == 3) {
                    carreAllFound = gp3
                } else if (forbidden1.isEmpty) {
                    forbidden1 = gp3.apply(0).cant intersect gp3.apply(1).cant intersect gp3.apply(2).cant
                } else {
                    forbidden2 = gp3.apply(0).cant intersect gp3.apply(1).cant intersect gp3.apply(2).cant
                }
            })

            forbidden1 = (forbidden1 -- carreAllFound.map(_.now)).filter((i: Int) => !colonne.map(_.now).contains(i))
            forbidden2 = (forbidden2 -- carreAllFound.map(_.now)).filter((i: Int) => !colonne.map(_.now).contains(i))
            if ((!carreAllFound.isEmpty) & (!(forbidden1 ++ forbidden2).isEmpty)) {
                //println("colonne: "+colonne+"\n  carreAllFound: "+carreAllFound+", forbidden1: "+forbidden1+", forbidden2: "+forbidden2)
                gp3s.foreach((gp3: List[SuDoKuCase]) => {
                    if (gp3.filter(_.now != 0).size == 3) {
                        carreAllFound = gp3
                    } else if (!gp3.filter(!_.shouldC.isEmpty).isEmpty) {
                        r = false
                    } else if (gp3.head.cant.intersect(forbidden1).isEmpty) {
                        gp3.apply(0).shouldC = forbidden1.filter(!gp3.apply(0).cant.contains(_))
                        gp3.apply(1).shouldC = forbidden1.filter(!gp3.apply(1).cant.contains(_))
                        gp3.apply(2).shouldC = forbidden1.filter(!gp3.apply(2).cant.contains(_))
                        r = true
                    } else if (gp3.head.cant.intersect(forbidden2).isEmpty) {
                        gp3.apply(0).shouldC = forbidden2.filter(!gp3.apply(0).cant.contains(_))
                        gp3.apply(1).shouldC = forbidden2.filter(!gp3.apply(1).cant.contains(_))
                        gp3.apply(2).shouldC = forbidden2.filter(!gp3.apply(2).cant.contains(_))
                        r = true
                    }
                    //println(gp3.map(_.toString3))
                })

            }
        })
        carres.foreach(_.shouldC2forbid)
        r
    }

    def obviousColonnes = {
        colonnes.foreach((colonne: List[SuDoKuCase]) => {
            one2nine.foreach((i: Int) => {
                val lcases = colonne.filter((tcase: SuDoKuCase) => {
                    (!tcase.cant.contains(i)) & (tcase.now == 0)
                })
                if (lcases.size == 1) {
                    lcases.head.now = i
                }
            })
        })
    }

}

class SuDoKuCarre(val cases: List[SuDoKuCase]) {

    def toString2: String = {
        var cptRangee = 0
        var s = "\n +---------------------+\n"
        cases.grouped(3).toList.foreach((tcases: List[SuDoKuCase]) => {
            s += " !" + tcases.map(_.toString2).mkString(", ") + "!\n"
        })
        s + " +----------------------+\n"

    }

    def obvious = {
        new Range(1, 10, 1).toList.foreach((i: Int) => {
            val lcases = cases.filter(_.now == 0).filter((tcase: SuDoKuCase) => !tcase.cant.contains(i))
            if (lcases.size == 1) {
                lcases.head.now = i
            }
        })
    }

    def shouldR2forbid = {
        val shouldCases = cases.filter(!_.shouldR.isEmpty)
        if (!shouldCases.isEmpty) {
            val should = shouldCases.head.shouldR
            val notShouldCases = cases filterNot (shouldCases contains)
            notShouldCases.foreach((tcase: SuDoKuCase) => tcase.cant = tcase.cant ++ should)
        }
    }

    def shouldC2forbid = {
        val shouldCases = cases.filter(!_.shouldC.isEmpty)
        if (!shouldCases.isEmpty) {
            val should = shouldCases.head.shouldC
            val notShouldCases = cases filterNot (shouldCases contains)
            notShouldCases.foreach((tcase: SuDoKuCase) => tcase.cant = tcase.cant ++ should)
        }
    }

    override def toString: String = {
        var cptRangee = 0
        var s = "\n +-----+\n"
        cases.grouped(3).toList.foreach((tcases: List[SuDoKuCase]) => {
            s += " !" + tcases.mkString(" ") + "!\n"
        })
        s + " +-----+\n"
    }

    def has(i: Int): Boolean = cases.map(_.now).contains(i)

    def finished: Boolean = cases.filter(_.now == 0).isEmpty

    def check: Boolean = {
        if (finished) {
            val c = cases.map(_.now).sorted
            if (c == new Range(1, 10, 1).toList) {
                true
            } else {
                println("check no good1: " + toString + " " + c)
                false
            }
        } else {
            val c = cases.map(_.now).filter(_ != 0)
            if (c.size == (ListSet[Int]() ++ c).size) {
                true
            } else {
                println("check no good2: " + toString + " " + c)
                false
            }
        }
    }
}

class SuDoKuCase(val init: ((Int, Int), Int)) {
    val one2nine = ListSet[Int]() ++ new Range(1, 10, 1).toList
    val row = init._2
    val col = init._1._2
    var now = init._1._1
    var cant = ListSet[Int]()
    var shouldR = ListSet[Int]()
    var shouldC = ListSet[Int]()
    var lcases = ListSet[Int]()

    def forbid(li: List[Int]) = {
        cant = cant ++ li.filterNot(_ == now)
    }

    def copy(forceNow: Int): SuDoKuCase = {
        val zecopy = new SuDoKuCase(init)
        zecopy.now = forceNow
        zecopy
    }

    def reset = {
        now = init._1._1
        cant = ListSet[Int]()
        shouldR = ListSet[Int]()
        shouldC = ListSet[Int]()
        lcases = ListSet[Int]()
    }

    def obvious: Boolean = {
        var r = true
        new Range(1, 10, 1).toList.takeWhile((i: Int) => {
            val lcases = one2nine -- cant
            if (now != 0) {
                //println(" 1now: "+now)
            } else if (lcases.size == 1) {
                now = lcases.head
                //println("  r"+row+"c"+col+" 2cant: "+cant+"  lcases: "+lcases+" now: "+now+" return: "+r)
            } else if (lcases.isEmpty) {
                println("****Obvious Error! r" + row + "c" + col + " cant: " + cant + " now: " + now + " lcases: " + lcases + " return: " + r)
                r = false
            }
            r
        })
        //println(" r"+row+"c"+col+"  return: "+r)
        r
    }

    def wildGuess = {
        if (now == 0) {
            lcases = one2nine -- cant
            //println("wildGuess! r"+row+"c"+col+" "+toString2+" cant: "+cant+" now: "+now+" lcases: "+lcases)
        }
    }

    def toString2: String = {
        if (now != 0) { now.toString } else { " r" + row + "c" + col + " cant: " + cant.mkString("") }
    }

    def toString3: String = {
        if (now != 0) { now.toString } else { toString2 + " shouldR: " + shouldR.mkString("") + " shouldC: " + shouldC.mkString("") }
    }

    def toString4: String = {
        " r" + row + "c" + col + " " + now.toString
    }

    override def toString: String = {
        if (now == 0) { " " } else { now.toString }
    }
}

