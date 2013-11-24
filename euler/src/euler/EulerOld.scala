package euler

import org.scalatest.FunSuite
import org.scalatest._
import org.scalatest.BeforeAndAfter
//import org.scalatest.TestDataFixture
import scala.collection.immutable.ListSet
import java.util.Calendar
import java.util.Date
import java.text.SimpleDateFormat
import scala.collection.immutable.TreeSet
import kebra.MyLog._
import language.postfixOps

// scala org.scalatest.tools.Runner -p . -o -s cadransolaire.MainCadranSolaire

// parameters.getValue("45zob").toInt should equal (1)
// projects.map(_.getDatabase.getName).contains("COCO") should be (false)

class EulerOld extends FunSuite with Matchers with BeforeAndAfter {

    before {
        Euler
        println("\n****avant************************************************************")
    }

    after {
        g_t_start = timeStamp(g_t_start,"")
        println("****apres****")
    }

    test("euler1") {
        new Range(1, 1000, 1).toList.filter((i: Int) => (i % 3 == 0) | (i % 5 == 0)).sum should equal(233168)
    }

    test("euler2") {
        val solution = Fibonacci.fib3(4000000).filter(_ % 2 == 0).sum
        println("Euler2 solution: " + solution)
        solution should equal(4613732)
    }

    test("Fibonacci") {
        println(new Range(1, 20, 1).map((i: Int) => (i, Fibonacci.fib(BigInt(i)))))
        Fibonacci.fib(BigInt(25)) should equal(75025)
        Fibonacci.fib10000.apply(25) should equal(75025)
        val f541 = Fibonacci.fib10000.apply(541)
        f541.toString.size should equal(113)
        f541.toString.toList.takeRight(9).map(_.toString.toInt).sorted should equal(new Range(1, 10, 1).toList)
        val f2749 = Fibonacci.fib10000.apply(2749)
        f2749.toString.size should equal(575)
        f2749.toString.toList.take(9).map(_.toString.toInt).sorted should equal(new Range(1, 10, 1).toList)
        Fibonacci.fib3(4000000) should equal(Fibonacci.fib2(40).takeWhile(_ <= 4000000))
    }

    test("euler4") {
        // assumption 6 digits number
        val first = (850 until 1000).reverse.toList.map((i: Int) => {
            val n = i * 1000 + i.toString.reverse.mkString("").toInt
            val div = new EulerDiv(n).primes
            (i, n, div)
        })
        val second = first.filter(_._3.last < 1000).map((t: (Int, Int, List[BigInt])) => (t._1, t._2, t._3, new EulerDivisors(t._3).divisors.filter(_ < 1000)))
        println("**second**")
        second.map(printIt(_))
        val third = second.filter((q: (Int, Int, List[BigInt], List[BigInt])) => q._4.last > q._1)
        println("***third***")
        third.map(printIt(_))

        val solution = third.head._2
        println("Euler4 solution: " + solution)
        solution should equal(906609)
    }

    test("euler6") {
        // http://projecteuler.net/problem=5
        val z10 = (1 until 11).toList
        val sum10 = z10.sum
        val sumSquare10 = sum10 * sum10
        val squareSum10 = z10.map(i => i * i).sum
        printIt(z10, sumSquare10 - squareSum10)
        myAssert2(sumSquare10 - squareSum10, 2640)
        val z100 = (1 until 101).toList
        val sum100 = z100.sum
        val sumSquare100 = sum100 * sum100
        val squareSum100 = z100.map(i => i * i).sum
        val solution = sumSquare100 - squareSum100
        printIt(z100, solution)
        println("euler6 solution: " + solution)
        solution should equal(25164150)
    }

    test("euler8") {
        val mille = List("73167176531330624919225119674426574742355349194934",
            "96983520312774506326239578318016984801869478851843",
            "85861560789112949495459501737958331952853208805511",
            "12540698747158523863050715693290963295227443043557",
            "66896648950445244523161731856403098711121722383113",
            "62229893423380308135336276614282806444486645238749",
            "30358907296290491560440772390713810515859307960866",
            "70172427121883998797908792274921901699720888093776",
            "65727333001053367881220235421809751254540594752243",
            "52584907711670556013604839586446706324415722155397",
            "53697817977846174064955149290862569321978468622482",
            "83972241375657056057490261407972968652414535100474",
            "82166370484403199890008895243450658541227588666881",
            "16427171479924442928230863465674813919123162824586",
            "17866458359124566529476545682848912883142607690042",
            "24219022671055626321111109370544217506941658960408",
            "07198403850962455444362981230987879927244284909188",
            "84580156166097919133875499200524063689912560717606",
            "05886116467109405077541002256983155200055935729725",
            "71636269561882670428252483600823257530420752963450")

        val solution = mille.flatten.sliding(5).filter(_.indexOf("0") < 0).toList.map((l: List[Char]) => l.map(_.toString.toInt).product).max
        println("Euler8 solution: " + solution)
        solution should equal(40824)
    }

    test("euler9") {
        // http://projecteuler.net/problem=9
        // 1ere version iterations=70075, a=200, b=375, c=425, abc=31875000
        // 2eme version iterations=9777, a=200, b=375, c=425, abc=31875000
        var iterations = 0
        val la = new Range(0, 332, 1)
        la.foreach((a: Int) => {
            val lb = new Range(a + 1, (1000 - a) / 2, 1)
            lb.filter(a + _ > 500).foreach((b: Int) => { // a+b > c: iterations=9777, a=200, b=375, c=425, abc=31875000
                val c = 1000 - (a + b)
                assume(a < b)
                assume(b < c)
                iterations += 1
                if (((a * a) + (b * b)) == (c * c)) {
                    println("iterations=" + iterations + ", a=" + a + ", b=" + b + ", c=" + c + ", abc=" + (a * b * c))
                }
            })
        })
    }

    test("euler11") {
        // http://projecteuler.net/problem=11
        val igrid = List(List(8, 2, 22, 97, 38, 15, 0, 40, 0, 75, 4, 5, 7, 78, 52, 12, 50, 77, 91, 8),
            List(49, 49, 99, 40, 17, 81, 18, 57, 60, 87, 17, 40, 98, 43, 69, 48, 4, 56, 62, 0),
            List(81, 49, 31, 73, 55, 79, 14, 29, 93, 71, 40, 67, 53, 88, 30, 3, 49, 13, 36, 65),
            List(52, 70, 95, 23, 4, 60, 11, 42, 69, 24, 68, 56, 1, 32, 56, 71, 37, 2, 36, 91),
            List(22, 31, 16, 71, 51, 67, 63, 89, 41, 92, 36, 54, 22, 40, 40, 28, 66, 33, 13, 80),
            List(24, 47, 32, 60, 99, 3, 45, 2, 44, 75, 33, 53, 78, 36, 84, 20, 35, 17, 12, 50),
            List(32, 98, 81, 28, 64, 23, 67, 10, 26, 38, 40, 67, 59, 54, 70, 66, 18, 38, 64, 70),
            List(67, 26, 20, 68, 2, 62, 12, 20, 95, 63, 94, 39, 63, 8, 40, 91, 66, 49, 94, 21),
            List(24, 55, 58, 5, 66, 73, 99, 26, 97, 17, 78, 78, 96, 83, 14, 88, 34, 89, 63, 72),
            List(21, 36, 23, 9, 75, 0, 76, 44, 20, 45, 35, 14, 0, 61, 33, 97, 34, 31, 33, 95),
            List(78, 17, 53, 28, 22, 75, 31, 67, 15, 94, 3, 80, 4, 62, 16, 14, 9, 53, 56, 92),
            List(16, 39, 5, 42, 96, 35, 31, 47, 55, 58, 88, 24, 0, 17, 54, 24, 36, 29, 85, 57),
            List(86, 56, 0, 48, 35, 71, 89, 7, 5, 44, 44, 37, 44, 60, 21, 58, 51, 54, 17, 58),
            List(19, 80, 81, 68, 5, 94, 47, 69, 28, 73, 92, 13, 86, 52, 17, 77, 4, 89, 55, 40),
            List(4, 52, 8, 83, 97, 35, 99, 16, 7, 97, 57, 32, 16, 26, 26, 79, 33, 27, 98, 66),
            List(88, 36, 68, 87, 57, 62, 20, 72, 3, 46, 33, 67, 46, 55, 12, 32, 63, 93, 53, 69),
            List(4, 42, 16, 73, 38, 25, 39, 11, 24, 94, 72, 18, 8, 46, 29, 32, 40, 62, 76, 36),
            List(20, 69, 36, 41, 72, 30, 23, 88, 34, 62, 99, 69, 82, 67, 59, 85, 74, 4, 36, 16),
            List(20, 73, 35, 29, 78, 31, 90, 1, 74, 31, 49, 71, 48, 86, 81, 16, 23, 57, 5, 54),
            List(1, 70, 54, 71, 83, 51, 54, 69, 16, 92, 33, 48, 61, 43, 52, 1, 89, 19, 67, 48))

        val grid = new Euler11Grid(igrid)

        //	println(grid.grid.mkString("\n"))
        println(grid.get(16, 9).toString)
        println(grid.max + " " + grid.maxi)
        require(grid.max == 94 * 99 * 71 * 61, grid.max + "==" + 94 * 99 * 71 * 61)
        println("*euler11*" + grid.findMax)
        grid.findMax should equal(70600674)
    }

    test("euler12") {
        // http://projecteuler.net/problem=12
        var z1 = new Euler12deadend(100, 1, 1, 0)
        val z2 = new Triangle(80)
        val z3 = new Triangle2(3240)
        z1.max should equal(z2.length)
        z2.length should equal(z3.length)

        new Triangle2(4400)
        z1 = new Euler12deadend(20000, 1, 1, 0)
        println(new Triangle(List(2, 2, 2).product))
        println(new Triangle(List(3, 5).product))
        println(new Triangle(List(2, 2, 2, 2, 5).product))
        println(new Triangle(List(2, 3, 2, 3).product))
        z1.max >= 500 should be(true)
        /*		   12375 76576500: 479 575 11 List(2, 2, 3, 3, 5, 5, 5, 7, 11, 13, 17) List(1, 2, 3, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 17, 18, 20, 21, 22, 25, 26, 28, 30, 33, 34, 35, 36, 39, 42, 44, 45, 50, 51, 52, 55, 60, 63, 65, 66, 68, 70, 75, 77, 78, 84, 85, 90, 91, 99, 100, 102, 105, 110, 117, 119, 125, 126, 130, 132, 140, 143, 150, 153, 154, 156, 165, 170, 175, 180, 182, 187, 195, 198, 204, 210, 220, 221, 225, 231, 234, 238, 250, 252, 255, 260, 273, 275, 286, 300, 306, 308, 315, 325, 330, 340, 350, 357, 364, 374, 375, 385, 390, 396, 420, 425, 429, 442, 450, 455, 462, 468, 476, 495, 500, 510, 525, 546, 550, 561, 572, 585, 595, 612, 630, 650, 660, 663, 693, 700, 714, 715, 748, 750, 765, 770, 780, 819, 825, 850, 858, 875, 884, 900, 910, 924, 935, 975, 990, 1001, 1020, 1050, 1071, 1092, 1100, 1105, 1122, 1125, 1155, 1170, 1190, 1260, 1275, 1287, 1300, 1309, 1326, 1365, 1375, 1386, 1428, 1430, 1500, 1530, 1540, 1547, 1575, 1625, 1638, 1650, 1683, 1700, 1716, 1750, 1785, 1820, 1870, 1925, 1950, 1980, 1989, 2002, 2100, 2125, 2142, 2145, 2210, 2244, 2250, 2275, 2310, 2340, 2380, 2431, 2475, 2550, 2574, 2618, 2625, 2652, 2730, 2750, 2772, 2805, 2860, 2925, 2975, 3003, 3060, 3094, 3150, 3250, 3276, 3300, 3315, 3366, 3465, 3500, 3570, 3575, 3740, 3825, 3850, 3900, 3927, 3978, 4004, 4095, 4125, 4250, 4284, 4290, 4420, 4500, 4550, 4620, 4641, 4675, 4862, 4875, 4950, 5005, 5100, 5148, 5236, 5250, 5355, 5460, 5500, 5525, 5610, 5775, 5850, 5950, 6006, 6188, 6300, 6375, 6435, 6500, 6545, 6630, 6732, 6825, 6930, 7140, 7150, 7293, 7650, 7700, 7735, 7854, 7875, 7956, 8190, 8250, 8415, 8500, 8580, 8925, 9009, 9100, 9282, 9350, 9625, 9724, 9750, 9900, 9945, 10010, 10500, 10710, 10725, 11050, 11220, 11375, 11550, 11700, 11781, 11900, 12012, 12155, 12375, 12750, 12870, 13090, 13260, 13650, 13860, 13923, 14025, 14300, 14586, 14625, 14875, 15015, 15300, 15470, 15708, 15750, 16380, 16500, 16575, 16830, 17017, 17325, 17850, 17875, 18018, 18564, 18700, 19125, 19250, 19500, 19635, 19890, 20020, 20475, 21420, 21450, 21879, 22100, 22750, 23100, 23205, 23375, 23562, 24310, 24750, 25025, 25500, 25740, 26180, 26775, 27300, 27625, 27846, 28050, 28875, 29172, 29250, 29750, 30030, 30940, 31500, 32175, 32725, 33150, 33660, 34034, 34125, 34650, 35700, 35750, 36036, 36465, 38250, 38500, 38675, 39270, 39780, 40950, 42075, 42900, 43758, 44625, 45045, 45500, 46410, 46750, 47124, 48620, 49500, 49725, 50050, 51051, 53550, 53625, 55250, 55692, 56100, 57750, 58500, 58905, 59500, 60060, 60775, 64350, 65450, 66300, 68068, 68250, 69300, 69615, 70125, 71500, 72930, 75075, 76500, 77350, 78540, 81900, 82875, 84150, 85085, 86625, 87516, 89250, 90090, 92820, 93500, 98175, 99450, 100100, 102102, 102375, 107100, 107250, 109395, 110500, 115500, 116025, 117810, 121550, 125125, 128700, 130900, 133875, 136500, 139230, 140250, 145860, 150150, 153153, 154700, 160875, 163625, 165750, 168300, 170170, 173250, 178500, 180180, 182325, 193375, 196350, 198900, 204204, 204750, 210375, 214500, 218790, 225225, 232050, 235620, 243100, 248625, 250250, 255255, 267750, 278460, 280500, 294525, 300300, 303875, 306306, 321750, 327250, 331500, 340340, 346500, 348075, 364650, 375375, 386750, 392700, 409500, 420750, 425425, 437580, 450450, 464100, 490875, 497250, 500500, 510510, 535500, 546975, 580125, 589050, 607750, 612612, 643500, 654500, 696150, 729300, 750750, 765765, 773500, 841500, 850850, 900900, 911625, 981750, 994500, 1021020, 1093950, 1126125, 1160250, 1178100, 1215500, 1276275, 1392300, 1472625, 1501500, 1531530, 1701700, 1740375, 1823250, 1963500, 2127125, 2187900, 2252250, 2320500, 2552550, 2734875, 2945250, 3063060, 3480750, 3646500, 3828825, 4254250, 4504500, 5105100, 5469750, 5890500, 6381375, 6961500, 7657650, 8508500, 10939500, 12762750, 15315300, 19144125, 25525500, 38288250)

  76576500: 575 */

    }

    test("euler13") {
        // http://projecteuler.net/problem=13
        val l = List(BigInt.apply("37107287533902102798797998220837590246510135740250"),
            BigInt.apply("46376937677490009712648124896970078050417018260538"),
            BigInt.apply("74324986199524741059474233309513058123726617309629"),
            BigInt.apply("91942213363574161572522430563301811072406154908250"),
            BigInt.apply("23067588207539346171171980310421047513778063246676"),
            BigInt.apply("89261670696623633820136378418383684178734361726757"),
            BigInt.apply("28112879812849979408065481931592621691275889832738"),
            BigInt.apply("44274228917432520321923589422876796487670272189318"),
            BigInt.apply("47451445736001306439091167216856844588711603153276"),
            BigInt.apply("70386486105843025439939619828917593665686757934951"),
            BigInt.apply("62176457141856560629502157223196586755079324193331"),
            BigInt.apply("64906352462741904929101432445813822663347944758178"),
            BigInt.apply("92575867718337217661963751590579239728245598838407"),
            BigInt.apply("58203565325359399008402633568948830189458628227828"),
            BigInt.apply("80181199384826282014278194139940567587151170094390"),
            BigInt.apply("35398664372827112653829987240784473053190104293586"),
            BigInt.apply("86515506006295864861532075273371959191420517255829"),
            BigInt.apply("71693888707715466499115593487603532921714970056938"),
            BigInt.apply("54370070576826684624621495650076471787294438377604"),
            BigInt.apply("53282654108756828443191190634694037855217779295145"),
            BigInt.apply("36123272525000296071075082563815656710885258350721"),
            BigInt.apply("45876576172410976447339110607218265236877223636045"),
            BigInt.apply("17423706905851860660448207621209813287860733969412"),
            BigInt.apply("81142660418086830619328460811191061556940512689692"),
            BigInt.apply("51934325451728388641918047049293215058642563049483"),
            BigInt.apply("62467221648435076201727918039944693004732956340691"),
            BigInt.apply("15732444386908125794514089057706229429197107928209"),
            BigInt.apply("55037687525678773091862540744969844508330393682126"),
            BigInt.apply("18336384825330154686196124348767681297534375946515"),
            BigInt.apply("80386287592878490201521685554828717201219257766954"),
            BigInt.apply("78182833757993103614740356856449095527097864797581"),
            BigInt.apply("16726320100436897842553539920931837441497806860984"),
            BigInt.apply("48403098129077791799088218795327364475675590848030"),
            BigInt.apply("87086987551392711854517078544161852424320693150332"),
            BigInt.apply("59959406895756536782107074926966537676326235447210"),
            BigInt.apply("69793950679652694742597709739166693763042633987085"),
            BigInt.apply("41052684708299085211399427365734116182760315001271"),
            BigInt.apply("65378607361501080857009149939512557028198746004375"),
            BigInt.apply("35829035317434717326932123578154982629742552737307"),
            BigInt.apply("94953759765105305946966067683156574377167401875275"),
            BigInt.apply("88902802571733229619176668713819931811048770190271"),
            BigInt.apply("25267680276078003013678680992525463401061632866526"),
            BigInt.apply("36270218540497705585629946580636237993140746255962"),
            BigInt.apply("24074486908231174977792365466257246923322810917141"),
            BigInt.apply("91430288197103288597806669760892938638285025333403"),
            BigInt.apply("34413065578016127815921815005561868836468420090470"),
            BigInt.apply("23053081172816430487623791969842487255036638784583"),
            BigInt.apply("11487696932154902810424020138335124462181441773470"),
            BigInt.apply("63783299490636259666498587618221225225512486764533"),
            BigInt.apply("67720186971698544312419572409913959008952310058822"),
            BigInt.apply("95548255300263520781532296796249481641953868218774"),
            BigInt.apply("76085327132285723110424803456124867697064507995236"),
            BigInt.apply("37774242535411291684276865538926205024910326572967"),
            BigInt.apply("23701913275725675285653248258265463092207058596522"),
            BigInt.apply("29798860272258331913126375147341994889534765745501"),
            BigInt.apply("18495701454879288984856827726077713721403798879715"),
            BigInt.apply("38298203783031473527721580348144513491373226651381"),
            BigInt.apply("34829543829199918180278916522431027392251122869539"),
            BigInt.apply("40957953066405232632538044100059654939159879593635"),
            BigInt.apply("29746152185502371307642255121183693803580388584903"),
            BigInt.apply("41698116222072977186158236678424689157993532961922"),
            BigInt.apply("62467957194401269043877107275048102390895523597457"),
            BigInt.apply("23189706772547915061505504953922979530901129967519"),
            BigInt.apply("86188088225875314529584099251203829009407770775672"),
            BigInt.apply("11306739708304724483816533873502340845647058077308"),
            BigInt.apply("82959174767140363198008187129011875491310547126581"),
            BigInt.apply("97623331044818386269515456334926366572897563400500"),
            BigInt.apply("42846280183517070527831839425882145521227251250327"),
            BigInt.apply("55121603546981200581762165212827652751691296897789"),
            BigInt.apply("32238195734329339946437501907836945765883352399886"),
            BigInt.apply("75506164965184775180738168837861091527357929701337"),
            BigInt.apply("62177842752192623401942399639168044983993173312731"),
            BigInt.apply("32924185707147349566916674687634660915035914677504"),
            BigInt.apply("99518671430235219628894890102423325116913619626622"),
            BigInt.apply("73267460800591547471830798392868535206946944540724"),
            BigInt.apply("76841822524674417161514036427982273348055556214818"),
            BigInt.apply("97142617910342598647204516893989422179826088076852"),
            BigInt.apply("87783646182799346313767754307809363333018982642090"),
            BigInt.apply("10848802521674670883215120185883543223812876952786"),
            BigInt.apply("71329612474782464538636993009049310363619763878039"),
            BigInt.apply("62184073572399794223406235393808339651327408011116"),
            BigInt.apply("66627891981488087797941876876144230030984490851411"),
            BigInt.apply("60661826293682836764744779239180335110989069790714"),
            BigInt.apply("85786944089552990653640447425576083659976645795096"),
            BigInt.apply("66024396409905389607120198219976047599490197230297"),
            BigInt.apply("64913982680032973156037120041377903785566085089252"),
            BigInt.apply("16730939319872750275468906903707539413042652315011"),
            BigInt.apply("94809377245048795150954100921645863754710598436791"),
            BigInt.apply("78639167021187492431995700641917969777599028300699"),
            BigInt.apply("15368713711936614952811305876380278410754449733078"),
            BigInt.apply("40789923115535562561142322423255033685442488917353"),
            BigInt.apply("44889911501440648020369068063960672322193204149535"),
            BigInt.apply("41503128880339536053299340368006977710650566631954"),
            BigInt.apply("81234880673210146739058568557934581403627822703280"),
            BigInt.apply("82616570773948327592232845941706525094512325230608"),
            BigInt.apply("22918802058777319719839450180888072429661980811197"),
            BigInt.apply("77158542502016545090413245809786882778948721859617"),
            BigInt.apply("72107838435069186155435662884062257473692284509516"),
            BigInt.apply("20849603980134001723930671666823555245252804609722"),
            BigInt.apply("53503534226472524250874054075591789781264330331690"))
        println(l.sum)
    }

    ignore("euler14") {
        // http://projecteuler.net/problem=14
        val treize = new ItemSyracuse(13)
        treize.process(new FunctionSyracuse(3), 40)
        println(treize.i + " " + treize.tempsDeVol + " " + treize.iteration + treize.getCycle.toString)
        //	treize.tempsDeVol should equal (10)

        var maxes = List(new ItemSyracuse(0), new ItemSyracuse(0))
        val start = 830001
        val end = 1000001
        var i = start
        while (i < end) {
            if (i % 10000 == 1) {
                println("  " + i)
            }
            val syr = new ItemSyracuse(i)
            syr.process(new FunctionSyracuse(3), 0)
            if (syr.tempsDeVol > (maxes.last.tempsDeVol - 2)) {
                maxes = maxes :+ syr
                println(syr.bi + " " + syr.tempsDeVol + " " + syr.tempsDeVolEnAltitude)
            }
            i += 2 // +2 OK if number found > 500000 and has at least one more iteration than max found < 500000
        }
        println("from " + start + " to " + end + ": " + maxes.map(_.tempsDeVol).max)

        var zob = new ItemSyracuse(837799)
        zob.process(new FunctionSyracuse(3), 0)
        println(zob.bi + " " + zob.tempsDeVol + " " + zob.tempsDeVolEnAltitude)
        zob = new ItemSyracuse(626331)
        zob.process(new FunctionSyracuse(3), 0)
        println("euler14 " + zob.bi + " " + zob.tempsDeVol + " " + zob.tempsDeVolEnAltitude)

    }

    test("euler15") {
        // http://projecteuler.net/problem=15
        /*euler15permut("ab")
		euler15permut("aabb")
		euler15permut("aaabbb")
		euler15permut("aaaabbbb")
		euler15permut("aaaaabbbbb")*/
        euler15_4
    }

    def euler15permut(s: String) = {
        val p = s.permutations.toList
        println(s + " " + s.size + " " + (new EulerFactorielle(s.size)).f + " " + p.size + " " + p)
        println(((new EulerFactorielle(s.size)).f / ((new EulerFactorielle(s.size / 2)).f * (new EulerFactorielle(s.size / 2)).f)))
    }

    def euler15_2(i: Int): BigInt = {
        val result = ((new EulerFactorielle(i)).f / ((new EulerFactorielle(i / 2)).f * (new EulerFactorielle(i / 2)).f))
        println("-2- " + i + " " + result)
        result
    }

    def euler15_3(i: Int): BigInt = {
        assume(i % 2 == 0, i)
        val r = (new Range(1, i + 1, 1)).toList.map(BigInt(_))
        val rd2 = (new Range(1, (i / 2) + 1, 1)).toList.map(BigInt(_))
        val tr = r filterNot (rd2 contains)
        val result = tr.product / rd2.product
        //println("-3- "+i+" "+result)
        result
    }

    def euler15_4 = {
        (new Range(2, 42, 2)).foreach((i: Int) => {
            //		euler15_2(i)
            euler15_3(i)
            println(euler15_3(i).toFloat / euler15_3(i - 2).toFloat)
        })

    }

    test("euler16") {
        Euler.powl(2, 15).toString.map(_.toString.toInt).sum should equal(26)
        Euler.powl(2, 1000).toString.map(_.toString.toInt).sum should equal(1366)
    }

    test("euler18") {
        // http://projecteuler.net/problem=18
        val grid2 = new Euler18Grid(List(List(3),
            List(7, 4),
            List(2, 4, 6),
            List(8, 5, 9, 3)))
        grid2.findMax should equal(23)
        grid2.get(3, 0).sum should equal(20)

        val igrid = List(List(75),
            List(95, 64),
            List(17, 47, 82),
            List(18, 35, 87, 10),
            List(20, 4, 82, 47, 65),
            List(19, 1, 23, 75, 3, 34),
            List(88, 2, 77, 73, 7, 63, 67),
            List(99, 65, 4, 28, 6, 16, 70, 92),
            List(41, 41, 26, 56, 83, 40, 80, 70, 33),
            List(41, 48, 72, 33, 47, 32, 37, 16, 94, 29),
            List(53, 71, 44, 65, 25, 43, 91, 52, 97, 51, 14),
            List(70, 11, 33, 28, 77, 73, 17, 78, 39, 68, 17, 57),
            List(91, 71, 52, 38, 17, 14, 91, 43, 58, 50, 27, 29, 48),
            List(63, 66, 4, 68, 89, 53, 67, 30, 73, 16, 69, 87, 40, 31),
            List(4, 62, 98, 27, 23, 9, 70, 98, 73, 93, 38, 53, 60, 4, 23))

        val grid = new Euler18Grid(igrid)

        println("*euler18*" + grid.findMax)
        grid.findMax should equal(1074)
    }

    test("euler20") {
        // http://projecteuler.net/problem=20
        var i = 1
        var fact: BigInt = 1
        var factold: BigInt = 0
        while (i <= 100) {
            factold = fact
            fact = fact * i
            fact / factold should equal(i)
            if (fact % 10 == 0) {
                fact = fact / 10
            }
            //println(i+" "+fact)
            i += 1
        }

        val digits = fact.toString.toList.map(_.toString.toInt)
        println(digits.grouped(10).mkString("\n  "))
        val sumdigits = digits.sum
        println("euler20 " + sumdigits)
        sumdigits should equal(648)
    }

    test("euler28") {
        // http://projecteuler.net/problem=28
        val eu = new Euler28
        eu.total should equal(669171001)

    }

    test("euler36") {
        // http://projecteuler.net/problem=36
        val eu = new Euler36(1000000)
        eu.sum should equal(872187)
        require(eu.isPalindrome(585))
        require(!eu.isPalindrome(11))
        require(!eu.isPalindrome(15))
        require(eu.isPalindrome(9))
    }

    test("euler52") {
        val dix_16 = new Range(10, 17, 1).toList
        val one2two = new Range(1, 3, 1).toList
        val x = new Range(1, 100000, 1).toList.flatMap((i: Int) => {
            val zu = dix_16.map((s: Int) => {
                val bi = BigInt.apply(s.toString + i.toString)
                val z = one2two.map(_ * bi)
                val y = z.map(_.toString.toList.sorted.mkString(""))
                //println(bi+" "+z+" "+y)
                (bi, z, y)
            })
            if (i % 10000 == 0) {
                println("\n" + i)
            } else if (i % 1000 == 0) {
                print(".")
            }
            zu
        }).filter((l: (BigInt, List[BigInt], List[String])) => {
            l._3.head == l._3.tail.head
        })
        //println("\n  "+x.mkString("\n  "))

        val z = x.groupBy { _._3.head }.toList
        //println("\n  "+z.map((w: (String, List[(scala.math.BigInt, List[scala.math.BigInt], List[String])])) => "\n    "+w._1+" - "+w._2.size+" "+w._2.map(_._1)))
        // entre 10 et 166 sinon pas meme nombre de digits quand x6
        val k = "124578".permutations.toList.filter((s: String) => {
            !dix_16.filter((si: Int) => s.indexOf(si.toString) == 0).isEmpty
        })
        //println("\n  "+k.size+" "+k)
        val y = x.map(_._3.head).distinct
        //println("\n  "+y)
        // apparemment il faut des nombres dont tous les chiffres sont differents
        // quand un nombre marche, certaines de ses permutations marchent aussi
        val one2six = new Range(1, 7, 1).toList
        val z2 = z.flatMap((w: (String, List[(scala.math.BigInt, List[scala.math.BigInt], List[String])])) => w._2.map(_._1.toString))
        val y2 = z2.filter((s: String) => {
            val bi = BigInt.apply(s.toString)
            val z = one2six.map(_ * bi)
            val y = z.map(_.toString.toList.sorted.mkString(""))
            //println(bi+" "+z+" "+y+" "+(y.head==y.tail.head)+" "+(y.head==y.apply(1))+" "+(y.head==y.apply(2))+" "+(y.head==y.apply(3))+" "+(y.head==y.apply(4))+" "+(y.head==y.apply(5)))	
            y.distinct.size == 1
        })
        val sol = y2.min
        println("Euler52 solution[" + sol + "]: ")
        sol should equal("142857")
    }

    test("euler57") {
            def sq3(n: Int): (BigInt, BigInt) = {
                n match {
                    case 0 => (3, 2)
                    case 1 => (7, 5)
                    case _ => {
                        val prev = sq3(n - 1)
                        val pprev = sq3(n - 2)
                        ((prev._1 * 2) + pprev._1, (prev._2 * 2) + pprev._2)
                    }
                }
            }

            def sq4(prev: ((BigInt, BigInt), (BigInt, BigInt))): ((BigInt, BigInt), (BigInt, BigInt)) = {
                (((prev._1._1 * 2) + prev._2._1, (prev._1._2 * 2) + prev._2._2), prev._1)
            }

        var sq4z = sq4(sq3(1), sq3(0))
        var z = new Range(0, 999, 1).map((i: Int) => {
            sq4z = sq4(sq4z)
            sq4z._1
        }).filter((c: (BigInt, BigInt)) => c._1.toString.size > c._2.toString.size)

        println("Solution euler57: " + z.size)
        z.size should equal(153)
    }

    test("euler67") {
        // http://projecteuler.net/problem=67

        var prevRow = List[EulerCell]()
        val it = scala.io.Source.fromFile("out\\triangleEuler68.txt").getLines
        while (it.hasNext) {
            val line = it.next
            val row = line.split(" ").toList.map(_.toInt).zipWithIndex.map((z: (Int, Int)) => new EulerCell(z._1, 0, z._2))
            row.foreach((cell: EulerCell) => {
                val c = cell.c
                var getNorthWest = EulerGrid.InvalidCell
                prevRow.find((cellP: EulerCell) => cellP.c == c) match {
                    case Some(z) => getNorthWest = z
                    case _       =>
                }
                var getNorthEast = EulerGrid.InvalidCell
                prevRow.find((cellP: EulerCell) => cellP.c == c - 1) match {
                    case Some(z) => getNorthEast = z
                    case _       =>
                }
                cell.sum = List(getNorthWest.sum, getNorthEast.sum).max + cell.i
                //println(cell+ " " + cell.sum)
            })
            prevRow = row
        }
        val solution = prevRow.map((cell: EulerCell) => cell.sum).max
        println("*euler67*solution=" + solution)
        solution should equal(7273)
    }

    test("euler71") {
        // http://projecteuler.net/problem=71
        /*val euler71 = new Range(12,35,1).toList.map((num: Int) => new Euler71(num))
				val groupby = euler71.groupBy(_.ZeSolution.r).toList.sortBy{_._1}
		println(groupby.map((z: (Double, List[euler.Euler71])) => z._2.size+" "+z+"\n"))
		//		println(euler71.mkString("\n"))*/
        println(new Fraction(3, 7))
        val num = 1000000
        val i = num - 12
        val n = 5 + (i / 7) * 3
        println(n)
        n should equal(428570)
    }

    test("euler92") {
        val euf = new FunctionEuler92
        euf.process(new Item(BigInt(44))).bi should equal(32)

        println(EulerFactorielle.combinations(30, 2))
        val lc = "012345678901234567890123456789".combinations(3).toList
        val all = lc.flatMap((s: String) => s.permutations.toList)
        println("all.size: " + all.size)
        val lc7 = "0123456789012345678901234567890123456789012345678901234567890123456789".combinations(7).toList
        println("lc7.size: " + lc7.size)
        var l89 = ListSet[BigInt](89)
        var i89 = 0
        lc7.foreach((s: String) => {
            //println(s)
            val eui = new ItemEuler92(s.toInt)
            eui.process(euf, 0)
            val iter = eui.iteration.map(_.bi)
            if (!(iter intersect l89.toList).isEmpty) {
                //println(" "+i89)
                i89 += s.permutations.toList.size
            }
        })
        println("euler92 i89: " + i89)
        i89 should equal(8581146)
    }

    test("euler96") {
        var sudokus = List[SuDoKu96]()
        var lines = List[String]()
        var title = ""
        val it = scala.io.Source.fromFile("out\\sudoku96.txt").getLines
        while (it.hasNext) {
            val line = it.next
            if (line.indexOf("Grid") == 0) {
                if (!lines.isEmpty) {
                    sudokus = sudokus :+ new SuDoKu96(lines, title)
                    lines = List[String]()
                }
                title = line
            } else {
                lines = lines :+ line
            }
        }
        sudokus = sudokus :+ new SuDoKu96(lines, title)
        sudokus.foreach((sudoku: SuDoKu96) => {
            //println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
            println("%%%%%%%% " + sudoku.title + " %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
            //println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%")
            var cpt = 0
            var historyIndex = -1
            var r = (0, false)
            while ((!r._2) & (cpt < 8)) {
                var historyIn = sudoku.history
                var historyOut =
                    if (historyIn.isEmpty) {
                        historyIn
                    } else {
                        historyIndex += 1
                        List(historyIn.apply(historyIndex))
                    }
                r = sudoku.solve1(historyOut)
                println("r: " + r + " historyIndex: " + historyIndex)
                cpt += 1
            }
            println("(!r._2)&(cpt<8) " + (!r._2) + (cpt < 8))
            println("cpt: " + cpt)
            require(sudoku.finished)
            require(sudoku.check)
        })
        val top3 = sudokus.map(_.top3)
        println(top3)
        println("Solution Euler96 Sudoku: " + top3.sum)
        top3.sum should equal(24702)
    }

    test("euler104bis") {
        print("Computing Fibonacci 10000 ")
        //println(Fibonacci.fib10000.take(600).zipWithIndex.mkString("\n"))
        val one2nine = new Range(1, 10, 1).toList
        var result = BigInt(0)
        var prev = BigInt(1)
        var pprev = BigInt(0)
        var cpt = BigInt(2)
        var found = false
        var firstfound = false
        var lastfound = false

        var prev2 = (BigInt(0), BigInt(0))
        var pprev2 = (BigInt(0), BigInt(0))
        var result2 = (BigInt(0), BigInt(0))
        var cpt2 = 0
        val limit2 = 40
        var wasSize = 0

        while (!found) {
            var s = ""
            var sl = 0
            if (cpt2 <= 2) {
                result = prev + pprev
                if (cpt % 100 == 0) {
                    print(".")
                    if (cpt <= 10000) {
                        result should equal(Fibonacci.fib10000.apply(cpt.toInt))
                    }
                }
                s = result.toString
                sl = s.size
                pprev = prev
                prev = result

            }
            if (cpt % 100 == 0) {
                print(".")
            }
            if ((sl > 100) & (cpt2 == 0)) {
                pprev2 = (BigInt.apply(s.substring(0, limit2)), BigInt.apply(s.substring(sl - 9)))
                println("\n pp: " + pprev2 + " " + s.size)
                wasSize = s.size
                cpt2 = 1
            } else if (cpt2 == 1) {
                prev2 = (BigInt.apply(s.substring(0, limit2)), BigInt.apply(s.substring(sl - 9)))
                println(" p: " + prev2 + " " + s.size)
                s.size should equal(wasSize)
                cpt2 = 2
            } else if (cpt2 >= 2) {
                result2 = (prev2._1 + pprev2._1, prev2._2 + pprev2._2)
                val sl2 = result2._2.toString.size
                if (result2._2.toString.substring(sl2 - 9).toList.map(_.toString.toInt).sorted == one2nine) {
                    println("\nlast2: " + cpt + " _1: " + result2._1.toString.size + " _2: " + result2._2.toString.size)
                    firstfound = true
                }
                if (result2._1.toString.substring(0, 9).toList.map(_.toString.toInt).sorted == one2nine) {
                    println("\nfirst2: " + cpt + " _1: " + result2._1.toString.size + " _2: " + result2._2.toString.size)
                    lastfound = true
                }

                pprev2 = prev2
                prev2 = result2
                if ((pprev2.toString.size == prev2.toString.size) & (prev2._1.toString.size > 100)) {
                    pprev2 = (BigInt.apply(pprev2._1.toString.substring(0, limit2)), BigInt.apply(pprev2._2.toString.substring(pprev2._2.toString.size - 10)))
                    prev2 = (BigInt.apply(prev2._1.toString.substring(0, limit2)), BigInt.apply(prev2._2.toString.substring(prev2._2.toString.size - 10)))
                    print("+")
                }
                cpt2 += 1
            }

            cpt += 1
            found = firstfound & lastfound
            firstfound = false
            lastfound = false
        }
        println("Solution euler104: " + (cpt - 1))
        (cpt - 1) should equal(329468)

    }

    ignore("euler104") {
        print("Computing Fibonacci 10000 ")
        //println(Fibonacci.fib10000.take(600).zipWithIndex.mkString("\n"))
        val one2nine = new Range(1, 10, 1).toList
        var result = BigInt(0)
        var prev = BigInt(1)
        var pprev = BigInt(0)
        var cpt = BigInt(2)
        var found = false
        var firstfound = false
        var lastfound = false
        while (!found) {
            result = prev + pprev
            if (cpt % 100 == 0) {
                print(".")
                if (cpt <= 10000) {
                    result should equal(Fibonacci.fib10000.apply(cpt.toInt))
                }
            }
            val s = result.toString
            val sl = s.size
            if (sl > 10) {
                if (s.substring(sl - 9).toList.map(_.toString.toInt).sorted == one2nine) {
                    println("\nlast: " + cpt + " " + sl)
                    firstfound = true
                }
                if (s.substring(0, 9).toList.map(_.toString.toInt).sorted == one2nine) {
                    println("\nfirst: " + cpt + " " + sl)
                    lastfound = true
                }
            }
            cpt += 1
            pprev = prev
            prev = result
            found = firstfound & lastfound
            firstfound = false
            lastfound = false
        }
        println("Solution euler104: " + (cpt - 1))
    }

    test("euler119") {
        val powers = new Range(1, 10, 1).toList
        val range = new Range(2, 100, 1).toList.map((i: Int) => {
            powers.map((pow: Int) => Euler.powl(i, pow))
        })
        //println(range.mkString("\n"))
        val w = range.flatMap((l: List[BigInt]) => {
            l.tail.filter((bi: BigInt) => bi.toString.toList.map(_.toString.toInt).sum == l.head)
        }).sorted.zipWithIndex
        w.apply(1)._1 should equal(BigInt.apply("512"))
        w.apply(9)._1 should equal(BigInt.apply("614656"))
        println("Solution euler119: " + w.apply(29))
        w.apply(29)._1 should equal(BigInt.apply("248155780267521"))
    }

    test("Binomiale") {

            def sumEq(g: Gauss, perms: List[List[Int]], i: Int): Double = {
                val l = perms.filter(_.sum == i)
                val theor = (l.size.toDouble / perms.size.toDouble)
                println("sumEq(" + i + ") " + l.size + " " + l + " " + l.size + "/" + perms.size + ": %4.3f".format(theor))
                val inGaussian = g.find(g.gauss, (theor, g.mu, (4.0 * g.sigma) + g.mu))
                println("  " + l.size + "/" + perms.size + ": %4.3f".format(theor) + " should: " + inGaussian._2)
                inGaussian._2
            }

        val gauss1 = new Gauss(1.0, 0.0)
        gauss1.printTable(List(gauss1.gauss, gauss1.PHI))
        gauss1.dessine(List(gauss1.gauss, gauss1.PHI), new Range(0, 40, 1), 0.1)

        println("************* 4 des a 2 faces")
        var n = 4
        var binom = new Binomiale(0.5, n)
        binom.printTable(List(binom.gauss, binom.PHI))
        var combis = List(0, 1, 0, 1, 0, 1, 0, 1).combinations(n).toList
        var perms = combis.flatMap(_.permutations)
        println("combis: " + combis.size + " " + combis)
        println("perms: " + perms.size + " " + perms)
        sumEq(binom, perms, 2)
        Math.abs(sumEq(binom, perms, 3) - 3.0) < 0.04 should be(true)
        Math.abs(sumEq(binom, perms, 4) - 4.0) < 0.08 should be(true)

        // http://math.univ-lille1.fr/~suquet/ens/ICP/Cmd060902.pdf
        val binom2 = new Binomiale(0.5, 50)
        binom2.dessine(List(binom.gauss), new Range(20, 80, 1), 0.5)

        println("************* 3 des a 3 faces 0,1,2")
        n = 3
        var mu = 1.0
        val gauss2 = new Gauss(math.sqrt(2), n * mu)
        gauss2.printTable(List(gauss2.gauss, binom.PHI))
        combis = List(0, 1, 2, 0, 1, 2, 0, 1, 2).combinations(n).toList
        perms = combis.flatMap(_.permutations)
        println("combis: " + combis.size + " " + combis)
        println("perms: " + perms.size + " " + perms)
        Math.abs(sumEq(gauss2, perms, 4) - 4.0) < 0.04 should be(true)
        Math.abs(sumEq(gauss2, perms, 5) - 5.0) < 0.08 should be(true)
        Math.abs(sumEq(gauss2, perms, 6) - 6.0) < 0.16 should be(true)
        gauss2.dessine(List(gauss2.gauss), new Range(-30, 90, 1), 0.1)

        println("************* 3 des a 6 faces 1,2,3,4,5,6")
        n = 3
        mu = 3.5
        val gauss3 = new Gauss(math.sqrt(8), n * mu)
        gauss3.printTable(List(gauss3.gauss, binom.PHI))
        combis = List(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6).combinations(n).toList
        perms = combis.flatMap(_.permutations)
        println("combis: " + combis.size + " " + combis)
        println("perms: " + perms.size + " " + perms)
        sumEq(gauss3, perms, 15)
        gauss3.dessine(List(gauss3.gauss), new Range(10, 80, 1), 0.2)
        Euler.waiting(10000)

        GaussPhi.getVal(1.73) should equal(0.95818)
        GaussPhi.getVal(1.746) should equal(0.959592)
    }

    test("euler148") {
        val power7 = new Range(2, 13, 1).toList.map((i: Int) => Euler.powl(7, i)).filter(_ < Euler.powl(10, 11))
        println(power7.size + " " + power7)
        val r312 = new Range(3, 12, 1).toList
        var prevdec = List[BigInt](0, 0, 0, 0)

            def fzob7(z: BigInt, i: Int): BigInt = {
                var n = BigInt(0)
                if (z % 7 == 0) {
                    n = z - 1
                } else {
                    n = z
                }
                val d = n / 7
                val nzob = n / power7.apply(i - 2)
                nzob should equal(0)
                val dec = (new Range(2, i + 1, 1).map((i: Int) => zob7(n, i)).sum) * (nzob + 1) + nzob
                val start = d * 7
                //println("fzob7 i: "+i+" z: "+z+" n: "+n+" dec: "+dec+" start: "+start+" nzob: "+nzob)
                start - ((z - start) * dec)
            }

            def zob7(z: BigInt, i: Int): BigInt = {
                if (i == 2) {
                    (z / 7) % 7
                } else if (z < power7.apply(i - 3)) {
                    0
                } else {
                    //val p2dp3 =  ((z%power7.apply(i-2))/power7.apply(i-3))
                    val p2dp3 = ((z / power7.apply(i - 3)) % 7)
                    if (p2dp3 == 0) {
                        0
                    } else {
                        //println("  zob7 i: "+i+" z: "+z+" result: "+((z%power7.apply(i-2))/power7.apply(i-3))+" * ( 1 + "+(new Range(2,i,1).map((i: Int) => zob7(z,i)))+" ) ")
                        p2dp3 * (1 + new Range(2, i, 1).map((i: Int) => zob7(z, i)).sum)
                    }
                }
            }

            def zob7b(z: BigInt, i: Int): BigInt = {
                if (z < power7.apply(i - 3)) {
                    0
                } else {
                    val p2dp3 = ((z / power7.apply(i - 3)) % 7)
                    if (p2dp3 == 0) {
                        0
                    } else {
                        p2dp3 * (1 + new Range(2, i, 1).map((i: Int) => zob7(z, i)).sum)
                    }
                }
            }

            def fzob49(n: BigInt): BigInt = {
                val dec0 = rzob(n)
                val decdiff = List(BigInt(1)) ++ dec0.tail
                val dec2to6 = (dec0 zip decdiff).map((c: (BigInt, BigInt)) => (c._1 + c._2, c._1 + (2 * c._2), c._1 + (3 * c._2), c._1 + (4 * c._2), c._1 + (5 * c._2), c._1 + (6 * c._2)))
                val decs = List(dec0, dec2to6.map(_._1), dec2to6.map(_._2), dec2to6.map(_._3), dec2to6.map(_._4), dec2to6.map(_._5), dec2to6.map(_._6))
                //println("     n: %5d".format(n)+" "+decs.map((l: List[BigInt]) => (7*(n+(l.head*7)-1)) - l.sum*28))
                //decsg = decsg :+ (n,decs.map((l: List[BigInt]) => (7*(n+(l.head*7)-1)) - l.sum*28))
                //dex1 = dex1 :+ dec0
                /*val dec0b = getNext(n,prevdec)
					prevdec = dec0b
					println("%4d ".format(n)+dec0.take(4).reverse.map(("%4d".format(_)))+" "+dec0b.take(4).reverse.map(("%4d".format(_)))+
					" "+(dec0.take(4)==dec0b))*/
                decs.map((l: List[BigInt]) => (7 * (n + (l.head * 7) - 1)) - l.sum * 28).sum
            }

            def rzob(n: BigInt): List[BigInt] = {

                val z3 = zob7b(n, 3)
                var result = List[BigInt](0, z3)
                val z4 = {
                    val p2dp3 = ((n / power7.apply(1)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3)
                    }
                }
                result = result :+ z4
                val z5 = {
                    val p2dp3 = ((n / power7.apply(2)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4)
                    }
                }
                result = result :+ z5
                val z6 = {
                    val p2dp3 = ((n / power7.apply(3)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5)
                    }
                }
                result = result :+ z6
                val z7 = {
                    val p2dp3 = ((n / power7.apply(4)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5 + z6)
                    }
                }
                result = result :+ z7
                val z8 = {
                    val p2dp3 = ((n / power7.apply(5)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5 + z6 + z7)
                    }
                }
                result = result :+ z8
                val z9 = {
                    val p2dp3 = ((n / power7.apply(6)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5 + z6 + z7 + z8)
                    }
                }
                result = result :+ z9
                val z10 = {
                    val p2dp3 = ((n / power7.apply(7)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5 + z6 + z7 + z8 + z9)
                    }
                }
                result = result :+ z10
                val z11 = {
                    val p2dp3 = ((n / power7.apply(8)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5 + z6 + z7 + z8 + z9 + z10)
                    }
                }
                result = result :+ z11
                val z12 = {
                    val p2dp3 = ((n / power7.apply(9)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5 + z6 + z7 + z8 + z9 + z10 + z11)
                    }
                }
                result = result :+ z12
                val z13 = {
                    val p2dp3 = ((n / power7.apply(10)) % 7)
                    if (p2dp3 == 0) {
                        BigInt(0)
                    } else {
                        p2dp3 * (1 + z3 + z4 + z5 + z6 + z7 + z8 + z9 + z10 + z11 + z12)
                    }
                }
                result = result :+ z13
                /*result = result.padTo(10,BigInt(0))
					//println(List(BigInt(0)) ++ r312.map((i: Int) => zob7b(n,i))+" "+result)
					result should equal(List(BigInt(0)) ++ r312.map((i: Int) => zob7b(n,i)))
			List(BigInt(0)) ++ r312.map((i: Int) => zob7b(n,i))*/
                result
            }

            def getSolution2(bi: BigInt): BigInt = {
                prevdec = List[BigInt](0, -1, 0, 0)
                var cpt = BigInt(1)
                var sum = BigInt(0)
                val plus = (49 * 48 / 2)
                while (cpt < bi - 48) {
                    sum += ((cpt * 49) + plus) - fzob49(cpt)
                    //println("*49* %5d: ".format(cpt)+ " %5d".format(fzob49(cpt)))
                    if (cpt % 10000 == 0) {
                        print("\n" + cpt + ": " + sum + " ")
                    } else if (cpt % 1000 == 0) {
                        print(".")
                    }
                    cpt += 49
                }
                while (cpt < bi + 1) {
                    sum += cpt - fzob7(cpt, 11)
                    //println("---- %5d: ".format(cpt)+ " %5d".format(fzob7(cpt,11)))
                    if (cpt % 10000 == 0) {
                        print("\n" + cpt + ": " + sum + " ")
                    } else if (cpt % 1000 == 0) {
                        print(".")
                    }
                    cpt += 1
                }

                println("\nEuler148b2 solution[" + bi + "]: " + sum + "\n***************************")
                sum
            }

        val tp7 = new TrianglePascal(7).triangle
        println(tp7.mkString("\n"))
        val tp100 = new TrianglePascal(100).triangle
        val tp100f = tp100.flatten
        tp100f.size should equal(5050)
        tp100f.filter(_ % 7 != 0).size should equal(2361)

        val tp100fg = tp100f.groupBy { (bi: BigInt) => bi }.toList.map((c: (BigInt, List[BigInt])) => (c._1, c._2.size)).sortBy { _._2 }
        tp100fg.filter(_._1 % 7 != 0).map(_._2).sum should equal(2361)

        getSolution2(100) should equal(2361)

        //getSolution2(100000) should equal(2361)
        getSolution2(1000000) should equal(BigInt.apply("14938429440"))
        getSolution2(Euler.powl(10, 9)) should equal(BigInt.apply("2129970655314432"))
    }

    ignore("euler164") {
            def chk(s: String): Boolean = s.map(_.toString.toInt).sliding(3).toList.filter(_.sum > 9).isEmpty

            def getDeux(s: String): (List[Euler164], List[Euler164]) = {
                val ldeux = s.combinations(2).toList.filter(chk(_)).flatMap(_.permutations).sorted.map((s: String) => new Euler164(s))
                ldeux.foreach((eu1: Euler164) => {
                    eu1.next = ldeux.filter((eu2: Euler164) => chk(eu1 + eu2)).map(new Euler164(_))
                })
                //println("ldeux: "+ldeux.size+"\n  "+ldeux.mkString("\n  "))
                val ldeux1 = ldeux.filter((eu1: Euler164) => eu1.deux.indexOf("0") != 0)
                (ldeux, ldeux1)
            }

            def getDeuxbis(ldeux: List[Euler164], ldeux1: List[Euler164]): (List[Euler164], List[Euler164]) = {
                val ldeuxbis = ldeux.map((eu1: Euler164) => {
                    val eu3 = new Euler164(eu1)
                    eu3.next = eu1.next
                    eu3.cpt = ldeux.filter((eu2: Euler164) => eu2.next.contains(eu1)).map(_.cpt).sum
                    eu3
                })
                //println("ldeuxbis: "+ldeuxbis.size+"\n  "+ldeuxbis.mkString("\n  ")+"\n"+ldeuxbis.map(_.cpt).sum)
                val ldeuxbis1 = ldeux.map((eu1: Euler164) => {
                    val eu3 = new Euler164(eu1)
                    eu3.next = eu1.next
                    eu3.cpt = ldeux1.filter((eu2: Euler164) => eu2.next.contains(eu1)).map(_.cpt).sum
                    eu3
                })

                (ldeuxbis, ldeuxbis1)
            }

        //chk("123423") should be (true)
        chk("123432") should be(false)

        /*val range = new Range(100,1000000,1).toList.map(_.toString).filter((s: String) => chk(s)).groupBy{_.size}.toList.sortBy{_._1}
		println("range: "+range.map((c: (Int, List[java.lang.String])) => "  "+c._1+" "+c._2.size+" "+c._2+"\n"))
		//,   6 574 List(100000, 100001

		val range2 = "12340123401201201201".combinations(6).flatMap(_.permutations).toList.sorted.filter((s: String) => chk(s)&s.indexOf("0")!=0)
		println("rng2: "+range2.size+" "+range2+"\n")*/

        val ldeuxa = getDeux("01234012")
        val ldeuxb = getDeuxbis(ldeuxa._1, ldeuxa._2)
        val ldeuxbis1 = ldeuxb._2

        // combinations of 4 numbers XXXX
        //println("ldeuxbis1: "+ldeuxbis1.size+"\n  "+ldeuxbis1.mkString("\n  ")+"\n"+ldeuxbis1.map(_.cpt).sum)
        //ldeuxbis1.map(_.cpt).sum should equal (990) 
        val ldeuxc = getDeuxbis(ldeuxb._1, ldeuxb._2)
        val ldeuxbis6 = ldeuxc._2
        //println("ldeuxbis6: "+ldeuxbis6.size+"\n  "+ldeuxbis6.mkString("\n  ")+"\n"+ldeuxbis6.map(_.cpt).sum)

        var ldeux = getDeux("012345678901234")
        var ldeuxbis = getDeuxbis(ldeux._1, ldeux._2)
        val range2a10 = new Range(2, 10, 1).toList.foreach((i: Int) => {
            ldeuxbis = getDeuxbis(ldeuxbis._1, ldeuxbis._2)
            //println("ldeuxbis["+(2*(i+1))+"]: "+ldeuxbis._2.size+"\n  "+ldeuxbis._2.mkString("\n  ")+"\n"+ldeuxbis._2.map(_.cpt).sum)
        })
        println("euler164 solution: " + ldeuxbis._2.map(_.cpt).sum)
        ldeuxbis._2.map(_.cpt).sum should equal(BigInt.apply("378158756814587"))
    }

    test("euler240_15") {
        val eu = new Euler240(5, 6, 3, 15)
        println("mainList: " + eu.mainList.size + "\n" + eu.mainList.mkString("\n"))
        println("otherList:\n" + eu.otherList(3).mkString("\n"))
        eu.getSolution should equal(1111)
        eu.getSolution2 should equal(1111)
    }

    ignore("euler240_70") {
        val eu = new Euler240(20, 12, 10, 70)
        println("mainList: " + eu.mainList.size + "\n" + eu.mainList.map(_.min).mkString("\n"))
        eu.getSolution
    }

    test("euler345") {

            def euler345Filter(lp: List[(Int, Int)], el345: euler345, threshold: Int) = lp.filter(el345.getCell(_) > threshold)

            def euler345doZeJob(el345: euler345, threshold: Int) = {
                println("********** " + threshold)
                //var linput = euler345Filter(el345.getAllCells, el345, threshold)
                var linput = el345.getAllCells
                var result = List.empty[(Int, Int, (Int, Int))]
                var already = List.empty[(Int, Int)]
                var check = List.empty[euler345Square]
                var cpt = 6
                var zeResult = List.empty[(Int, Int)]
                do {
                    result = euler345getZeResult(linput, el345)
                    zeResult = result.map(_._3) ++ already
                    check = euler345checkZeJob(zeResult, el345)
                    var topOfZeList = result.head._3
                    already = already ++ List(topOfZeList)
                    println("  topOfZeList: " + topOfZeList + ", occurences: " + result.head._2)
                    linput = linput.filter((p: (Int, Int)) => p._1 != topOfZeList._1 & p._2 != topOfZeList._2)
                    cpt -= 1
                    //} while(cpt>0)
                } while (!check.isEmpty)
                val solution5 = zeResult.map(el345.getCell(_)).sum
                System.err.println(threshold + " -> " + zeResult + ": " + solution5)
                solution5
            }

            def euler345getZeResult(lp: List[(Int, Int)], el345: euler345) = {
                val allCombis = lp.combinations(2).toList
                val realSquareCombis = allCombis.filter(euler345.isRealSquare(_)).map(new euler345Square(_))
                print("  " + allCombis.size + " " + realSquareCombis.size)
                val bests = realSquareCombis.map(_.best(el345))
                val sorted = bests.map(_.back2pair).flatten.groupBy { (p: (Int, Int)) => p }.toList.sortBy { _._2.size }.map((z: ((Int, Int), List[(Int, Int)])) =>
                    (el345.getCell(z._1), z._2.size, z._2)).map((z: (Int, Int, List[(Int, Int)])) => (z._1, z._2, z._3.head)).reverse
                var result = List(sorted.head)
                sorted.takeWhile((y: (Int, Int, (Int, Int))) => {
                    if (!result.map(_._3._1).contains(y._3._1) & !result.map(_._3._2).contains(y._3._2)) {
                        result = result :+ y
                    }
                    result.size < el345.size
                })
                //println("  "+result.size+" "+result.mkString("\n"))
                result
            }

            def euler345findSwapped(l1: List[(Int, Int)], l2: List[(Int, Int)]) = {
                val l1Combis = l1.combinations(2).toList.filter(euler345.isRealSquare(_)).map(new euler345Square(_))
                val l2Combis = l2.combinations(2).toList.filter(euler345.isRealSquare(_)).map(new euler345Square(_))
                val l2CombisSwapped = l2Combis.map(_.swap)
                val x = l1Combis intersect (l2Combis ++ l2CombisSwapped)
                println(x)
                val result = x.map(_.back2pair).flatten
                println(result)
                result
            }

            def euler345find4Corners(l: List[(Int, Int)], el345: euler345): List[euler345Square] = {
                val lCombis = l.combinations(2).toList.filter(euler345.isRealSquare(_)).map(new euler345Square(_))
                //println(lCombis)
                val found4Corners = lCombis.filter((es: euler345Square) => lCombis.contains(es.swap))
                val bests = ListSet[euler345Square]() ++ found4Corners.map(_.best(el345))
                val result = found4Corners filterNot (bests.toList contains)
                //println(result)
                result
            }

            def euler345checkZeJob(lp: List[(Int, Int)], el345: euler345): List[euler345Square] = {
                val allCombis = lp.combinations(2).toList.map(new euler345Square(_))
                val bests = allCombis.map(_.best(el345))
                val sorted = bests.map(_.back2pair).flatten.groupBy { (p: (Int, Int)) => p }.toList.sortBy { _._2.size }.map((z: ((Int, Int), List[(Int, Int)])) =>
                    (el345.getCell(z._1), z._2.size, z._2)).map((z: (Int, Int, List[(Int, Int)])) => z._3.head).sortBy { euler345.hardSort(_) }
                val lps = lp.sortBy { euler345.hardSort(_) }
                if (sorted != lps) {
                    println("  NO GOOD! " + lps + " != " + sorted)
                    euler345find4Corners(sorted, el345)
                } else {
                    println("  GOOD! " + lps + " != " + sorted)
                    List.empty[euler345Square]
                }
            }

        val ll5 = List(List(7, 53, 183, 439, 863),
            List(497, 383, 563, 79, 973),
            List(287, 63, 343, 169, 583),
            List(627, 343, 773, 959, 943),
            List(767, 473, 103, 699, 303))

        val ll15 = List(List(7, 53, 183, 439, 863, 497, 383, 563, 79, 973, 287, 63, 343, 169, 583),
            List(627, 343, 773, 959, 943, 767, 473, 103, 699, 303, 957, 703, 583, 639, 913),
            List(447, 283, 463, 29, 23, 487, 463, 993, 119, 883, 327, 493, 423, 159, 743),
            List(217, 623, 3, 399, 853, 407, 103, 983, 89, 463, 290, 516, 212, 462, 350),
            List(960, 376, 682, 962, 300, 780, 486, 502, 912, 800, 250, 346, 172, 812, 350),
            List(870, 456, 192, 162, 593, 473, 915, 45, 989, 873, 823, 965, 425, 329, 803),
            List(973, 965, 905, 919, 133, 673, 665, 235, 509, 613, 673, 815, 165, 992, 326),
            List(322, 148, 972, 962, 286, 255, 941, 541, 265, 323, 925, 281, 601, 95, 973),
            List(445, 721, 11, 525, 473, 65, 511, 164, 138, 672, 18, 428, 154, 448, 848),
            List(414, 456, 310, 312, 798, 104, 566, 520, 302, 248, 694, 976, 430, 392, 198),
            List(184, 829, 373, 181, 631, 101, 969, 613, 840, 740, 778, 458, 284, 760, 390),
            List(821, 461, 843, 513, 17, 901, 711, 993, 293, 157, 274, 94, 192, 156, 574),
            List(34, 124, 4, 878, 450, 476, 712, 914, 838, 669, 875, 299, 823, 329, 699),
            List(815, 559, 813, 459, 522, 788, 168, 586, 966, 232, 308, 833, 251, 631, 107),
            List(813, 883, 451, 509, 615, 77, 281, 613, 459, 205, 380, 274, 302, 35, 805))

        val ell5 = new euler345(ll5)
        evaluating { ell5.MSum(List((1, 1), (1, 2))) } should produce[java.lang.IllegalArgumentException]
        evaluating { ell5.MSum(List((2, 2), (1, 2))) } should produce[java.lang.IllegalArgumentException]
        ell5.MSum(List((0, 0), (1, 1))) should equal(390)
        new euler345Square(List((0, 0), (1, 1))).best(ell5) should equal(new euler345Square(List((0, 1), (1, 0))))
        new euler345Square(List((0, 0), (1, 1))).best(ell5).back2pair should equal(List((0, 1), (1, 0)))
        new euler345Square(List((0, 0), (1, 1))).swap should equal(new euler345Square(List((0, 1), (1, 0))))
        new euler345Square(List((0, 0), (1, 1))).swap.back2pair should equal(List((0, 1), (1, 0)))
        new euler345Square(List((0, 0), (1, 1))).swap.swap should equal(new euler345Square(List((0, 0), (1, 1))))
        new euler345Square(List((0, 0), (1, 1))) should not equal (new euler345Square(List((0, 1), (1, 0))))

        new euler345Square(List((3, 2), (2, 3))).back2pair should equal(List((2, 3), (3, 2)))
        new euler345Square(List((2, 3), (3, 2))).back2pair should equal(List((2, 3), (3, 2)))

        euler345checkZeJob(List((0, 4), (4, 0), (2, 2), (1, 1), (3, 3)), ell5) should equal(List.empty[euler345Square])

        var solution5 = euler345doZeJob(ell5, 0)
        solution5 should equal(3315)
        val solution = euler345doZeJob(new euler345(ll15), 0)
        println("Euler345 solution: " + solution)
        solution should equal(13938)

    }

    test("test de fin") {
        println("That's all folks!")
    }

}

class PlusUnSurI extends Function((it: Item) => {
    /*  println("before+1/i "+it)
  println("after+1/i "+new Item(it.i+1,it.d + (1.0/it.i),true))*/
    new Item(it.bi + 1, it.i + 1, it.d + (1.0 / it.i), true)
}, (it: Item) => new Item(it.bi + 1, it.i + 1, it.d, true), "+1/i")

class NoFilter extends Function((it: Item) => new Item(true), "NoFilter")

class Euler36(val limit: Int) {
    //  val rangeOdd = new Range(1,10,2)
    val range = new Range(1, limit, 2)
    val sum = range.filter(isPalindrome(_)).sum
    println("sum=" + sum)
    println(range.filter(isPalindrome(_)))
    println("sum=" + sum)

    def isPalindrome(n: Int): Boolean = {
        val s10 = "%d".format(n).toList
        if (s10 == s10.reverse) {
            var m = n
            var s2 = List[String]()
            while (m > 0) {
                if (m % 2 == 0) {
                    s2 = s2 :+ "0"
                } else {
                    s2 = s2 :+ "1"
                }
                m = m >> 1
            }
            //println(n+" ["+s2.toString+"-"+s2.reverse.toString+"]"+(s2.toString == s2.reverse.toString))
            s2 == s2.reverse
        } else {
            false
        }
    }
}

class Euler12deadend(limit: Int, start: Int, inc: Int, sumi: BigInt) {
    val range = new Range(start, limit, inc).toList
    var sum = sumi
    var max = sum
    var summax = sum

    println(this.getClass.getName + "(" + limit + "," + start + "," + inc + ")*************")
    range.takeWhile((i: Int) => {
        inc match {
            case 1 => sum = sum + i
            case 2 => sum = sum + (2 * i) + 1
            case _ => require(false)
        }

        val div = new EulerDiv(sum)
        val divs = new EulerDivisors(div.primes)
        max = if (divs.divisors.size > max) {
            summax = sum
            //println("   "+i+" "+sum+": "+max+" "+divs.divisors.size+" "+div.primes.size+" "+div.primes+" "+divs.divisors)
            //			println("               "+new Triangle2(sum))
            divs.divisors.size
        } else {
            max
        }
        /*		if(EulerPrime.isPrime(sum)) {
		  println("\n"+sum)
		} else {
		  print(".")
		}*/
        if (max > 500) {
            false
        } else {
            true
        }

    })
    println("\n  " + summax + ": " + max)
}

class Euler71(num: Int) {
    assume(num >= 8)
    val troisSurSept = 3.toDouble / 7.toDouble
    var l = List[Fraction]()
    val rangeD = new Range(2, num + 1, 1).toList
    rangeD.foreach((d: Int) => {
        val rangeN = new Range(2, Math.min(num / 2, d), 1).toList
        rangeN.takeWhile((n: Int) => {
            if (n.toDouble / d.toDouble <= troisSurSept) {
                val fraction = new Fraction(n, d)
                if (fraction.isReduced) {
                    l = l :+ fraction
                }
                true
            } else {
                false
            }
        })
    })
    l = l.sortBy { _.r }
    println(num + " " + l)
    val ZeSolution = l.dropRight(1).last
    require(getSolution == ZeSolution.n, num + " " + getSolution + "==" + ZeSolution.n)

    override def toString: String = num + " " + ZeSolution

    def getSolution: Int = {
        require(num >= 12)
        val i = num - 12
        val n = 5 + (i / 7) * 3
        val d = 12 + (i / 7) * 7
        n
    }
}

class Fraction(val n: Int, val d: Int) {
    val r = (1.0 * n) / (1.0 * d)
    var reduced: Fraction = _
    val dprimes = new EulerDiv(d).primes :+ BigInt(d)
    val nprimes = new EulerDiv(n).primes :+ BigInt(n)
    val intersect = dprimes.intersect(nprimes)
    val isReduced = intersect.isEmpty
    var pgcd = 1
    if (isReduced) {
        reduced = this
    } else {
        pgcd = intersect.product.toInt
        reduced = new Fraction(n / pgcd, d / pgcd)
    }
    override def toString: String = {
        if (isReduced) {
            "[" + n + "/" + d + "=%2.5f]".format(r)
        } else {
            "[[" + n + "/" + d + "=%2.5f]->".format(r) + reduced.toString + "]"
        }
    }
}

object Euler131 {
    val limit = 1000000
    val cubes = new Range(1, limit, 1).toList.map((i: Int) => BigInt(i) * BigInt(i) * BigInt(i))
    val diffcubes = new TreeSet[BigInt]() ++ cubes.sliding(2).map((z: List[BigInt]) => z.last - z.head).toList
    val invalid = BigInt(0)
    val betaCeilFuncMaxN = new BetaFunction(0.015, 0)
    val ceilFuncMaxN = new Function(betaCeilFuncMaxN)
    val betaCeilFuncMaxN2 = new BetaFunction(0.008, 0)

}

class Euler131 extends EulerPrime(Euler131.limit, 1000) {

    var l = List[Euler131PN]()
    var l2 = List[BigInt]()

    def getSolution = {
        premiersForCompute.foreach(doZeJob2(_))
        computePrime((index: Int, range: TreeSet[BigInt]) => {
            range.foreach(doZeJob2(_))
            0
        })
        println("*****" + l.size + "\n" + l.mkString("\n") + "\n" + l.size)
        println("*****" + l2.size + "\n" + l2.mkString("\n") + "\n" + l2.size)
    }

    def doZeJob2(p: BigInt) = {
        if (Euler131.diffcubes.contains(p)) {
            l2 = l2 :+ p
        }
    }

    def doZeJob(p: Int) = {
        var cubes = new TreeSet[BigInt]() ++ Euler131.cubes
        if (!l.isEmpty) {
            val prev = l.last
            cubes = new TreeSet[BigInt]() ++ Euler131.cubes.splitAt(Euler131.cubes.indexOf(prev.n + prev.p))._2
        }

        cubes.find((z: BigInt) => {
            cubes.contains(z + BigInt(p))
        }) match {
            case Some(z) => {
                val pn = new Euler131PN(BigInt(p), z, cubes)
                println(pn)
                l = l :+ pn
            }
            case _ =>
        }
        /*		val ceil = ((Euler131.betaCeilFuncMaxN2.func(p)*p)+20)

		val cubes2 = cubes.filter((z: BigInt) => {
			Euler131.cubes.contains(z+BigInt(p))
		})


		val lsize = l.size

		cubes2.takeWhile((y: BigInt) => {
			val pn = new Euler131PN(BigInt(p),y)
			var found = pn.perfect != Euler131.invalid
			if (found) {
				println(pn)
				if(!l.isEmpty) {
					println("    "+pn.getDerivee(l.last))
				} 
				l = l :+ pn
			} else if(y.toDouble>ceil) {
			  println("            p: "+p+" n: "+y+" > ceil: "+ceil)
			  found = true
			}
			!found
		})

	if(l.size==lsize & !cubes2.isEmpty) {
	  println(println(" +1+ p: "+p+" - ceil: "+ceil+" "+cubes.head+" "+cubes2.size))
	} else 

	if(cubes2.size==1) {
	  val pn = new Euler131PN(BigInt(p),cubes2.head)
	  println(pn)
	  l = l :+ pn
	} else 
	if(cubes2.size>1) {
	  println(println(" +2+ p: "+p+" - ceil: "+ceil+" "+cubes.head+" "+cubes2.size+"**********************************************"))
	} */
    }
}

class Euler131PN(val p: BigInt, val n: BigInt, val cubes: TreeSet[BigInt]) {
    val n3n2p = new Function((it: Item) => new Item((it.bi * it.bi) * (p + it.bi)), "n3n2p[" + p + "]")
    var perfect = Euler131.invalid
    cubes.find((z: BigInt) => n3n2p.process(new Item(n)).bi == z) match {
        case Some(x) => perfect = x
        case _       =>
    }

    def getDerivee(prev: Euler131PN): Double = {
        (n - prev.n).toDouble / (p - prev.p).toDouble
    }

    override def toString: String = "[p: " + p + " n: " + n + " <" + (new EulerDiv(n).primes) + "> ne2*(n+p: " + (n + p) + " <" + (new EulerDiv(n + p).primes) + ">)=" + perfect + " <" + (new EulerDiv(perfect).primes) + ">]"
}

class Euler164(val deux: String) {
    def this(eu: Euler164) = this(eu.deux)

    var cpt = BigInt(1)
    var next = List[Euler164]()

    def +(p: Euler164): String = deux + p.deux
    override def toString: String = deux + " " + cpt.toString + " " + next.map(_.toString2)
    def toString2: String = deux
    override def hashCode: Int = toString2.hashCode
    override def equals(o: Any): Boolean = toString2 == o.asInstanceOf[Euler164].toString2

}

class Euler240(val numDices: Int, val numSides: Int, val topDices: Int, val sumDices: Int) {
    val sides = new Range(1, numSides + 1, 1).toList

    def getSolution2: BigInt = {
        val factNumDices = new EulerFactorielle(numDices).f
        val bymins = mainList.groupBy(_.min).toList.sortBy { _._1 }
        var solution = BigInt(0)
        bymins.foreach((bymin: (Int, List[List[Int]])) => {
            val byCountMins = bymin._2.groupBy { _.filter(_ == bymin._1).size }.toList.sortBy { _._1 }
            println("min: " + bymin._1)
            byCountMins.foreach((bycountmin: (Int, List[List[Int]])) => {
                val size = bycountmin._2.size
                println("  count: " + bycountmin._1 + " size: " + size + " head: " + bycountmin._2.head)
                val otherLists = otherList(bymin._1)
                val v = otherLists.map((l2: List[Int]) => {
                    val l3 = bycountmin._2.head ++ l2
                    val ls3 = l3.distinct
                    val cls3f = ls3.map((i: Int) => l3.count(_ == i)).map((i: Int) => new EulerFactorielle(i).f).toList.filter(_ != 1).sorted
                    val p3t = factNumDices / cls3f.product
                    /*val p3 = l3.permutations.toList
						println("  l3: "+l3+" "+p3.size+" vs "+p3t+" "+ls3+" "+cls3f+" "+p3)*/
                    //println("    l3: "+l3+" "+p3t+" "+ls3+" "+cls3f+" "+p3t+"="+factNumDices+"/"+cls3f.product+" "+size)
                    p3t
                })
                println("  v: " + v)
                solution += v.sum * size
            })
        })
        println("Euler240 solution: " + solution)
        solution
    }

    def getSolution: BigInt = {
        val factNumDices = new EulerFactorielle(numDices).f
        val z = mainList.flatMap((l: List[Int]) => {
            println("l: " + l)
            val otherLists = otherList(l.min)
            otherLists.map((l2: List[Int]) => {
                val l3 = l ++ l2
                val ls3 = l3.distinct
                val cls3f = ls3.map((i: Int) => l3.count(_ == i)).map((i: Int) => new EulerFactorielle(i).f).toList
                val p3t = factNumDices / cls3f.product
                /*val p3 = l3.permutations.toList
						println("  l3: "+l3+" "+p3.size+" vs "+p3t+" "+ls3+" "+cls3f+" "+p3)*/
                //println("  l3: "+l3+" "+p3t+" "+ls3+" "+cls3f+" "+p3t+"="+factNumDices+"/"+cls3f.product)
                p3t
            })
        })
        val solution = z.sum
        println("Euler240 solution: " + solution)
        solution
    }

    def otherList(limit: Int): List[List[Int]] = {
        val l = new Range(1, limit + 1, 1).toList
        val range = new Range(0, numDices - topDices, 1).toList
        var l3 = List[Int]()
        range.foreach((i: Int) => l3 = l3 ++ l)
        l3.combinations(numDices - topDices).toList
    }

    val mainList = {
        val startList =
            if (sumDices == 15) {
                List(3, 4, 5, 5, 5, 6, 6)
            } else if (sumDices == 70) {
                //max five 12s, max four 1s
                //max six 11s, max five 2s
                //max six 10s, max six 3s
                //max sept 9s, max sept 4s
                //max huit 8s, max sept 5s
                //max dix 7s, max huit 6s
                List(1, 1, 1, 1) ++
                    List(2, 2, 2, 2, 2) ++
                    List(3, 3, 3, 3, 3, 3) ++
                    List(4, 4, 4, 4, 4, 4, 4) ++
                    List(5, 5, 5, 5, 5, 5, 5) ++
                    List(6, 6, 6, 6, 6, 6, 6, 6) ++
                    List(7, 7, 7, 7, 7, 7, 7, 7, 7, 7) ++
                    List(8, 8, 8, 8, 8, 8, 8, 8) ++
                    List(9, 9, 9, 9, 9, 9, 9) ++
                    List(10, 10, 10, 10, 10, 10) ++
                    List(11, 11, 11, 11, 11, 11) ++
                    List(12, 12, 12, 12, 12)
            } else {
                require(false, "not supported")
                List[Int]()
            }
        startList.combinations(topDices).toList.filter(_.sum == sumDices)
    }
}

class euler345Square(val l: List[(Int, Int)]) {
    euler345.check2(l)
    val back2pair = l.sortBy { euler345.hardSort(_) }

    def swap = new euler345Square(List((back2pair.apply(0)._1, back2pair.apply(1)._2), (back2pair.apply(1)._1, back2pair.apply(0)._2)))
    def best(el345: euler345) = if (MSum(el345) > swap.MSum(el345)) this else swap
    def MSum(el345: euler345) = back2pair.map((rc: (Int, Int)) => el345.ll.apply(rc._1).apply(rc._2)).sum

    override def toString = "<" + back2pair.head + "," + back2pair.last + ">"
    override def equals(x: Any): Boolean = toString.equals(x.asInstanceOf[euler345Square].toString)
    override def hashCode: Int = toString.hashCode
}

object euler345 {
    def check2(l: List[(Int, Int)]): List[(Int, Int)] = {
        assert(l.size == 2)
        assert((ListSet[Int]() ++ l.map(_._1)).size == 2)
        assert((ListSet[Int]() ++ l.map(_._2)).size == 2)
        l
    }

    def isRealSquare(l: List[(Int, Int)]): Boolean = {
        assert(l.size == 2)
        l.head._1 != l.last._1 & l.head._2 != l.last._2
    }

    def hardSort(p: (Int, Int)): Int = p._1 * 100 + p._2

}

class euler345(val ll: List[List[Int]]) {
    val size = ll.size
    require(size == ll.map(_.size).max)
    require(size == ll.map(_.size).min)

    lazy val getAllCells = {
        val r = 0 until size
        List(r.map((i: Int) => r.map((j: Int) => (i, j)))).flatten.flatten
    }

    def getCell(rc: (Int, Int)) = ll.apply(rc._1).apply(rc._2)

    def MSum(choix: List[(Int, Int)]): Int = {
        require((ListSet[Int]() ++ choix.map(_._1)).size == choix.size)
        require((ListSet[Int]() ++ choix.map(_._2)).size == choix.size)
        choix.map((rc: (Int, Int)) => ll.apply(rc._1).apply(rc._2)).sum
    }

}
