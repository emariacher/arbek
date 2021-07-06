package carte

import carte.Tableaux._

class ListCarres {
  var lc: List[Carre] = List[Carre]()

  def getNextRowCol: (Int, Int) = {
    if (lc.isEmpty) {
      (0, 0)
    } else {
      var lastRow = lc.last.row
      var lastCol = lc.last.col
      if (lastCol == tbx.maxCol - 1) {
        lastCol = 0
        lastRow += 1
      } else {
        lastCol += 1
      }
      require(lastRow < tbx.maxRow && lastCol < tbx.maxCol, "lastRow<tbx.maxRow && lastCol<tbx.maxCol: " + (lastRow, lastCol))
      (lastRow, lastCol)
    }

  }
}