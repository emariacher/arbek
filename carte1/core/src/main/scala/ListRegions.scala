package carte

class ListRegions {
  var lr: List[Region] = List[Region]()

  def add(r: Region): Unit = {
    lr = lr :+ r
  }

}
