package logparsing

object BeLeConv {
  def read16(hex: List[Int], startIndex: Int, mask: Int): Int = (hex.apply(startIndex) + (hex.apply(startIndex + 1) * 256)) & mask
  def read16(hex: List[Int], startIndex: Int): Int = read16(hex, startIndex, 0xffff)
  def read16(hex: List[Int]): Int = read16(hex, 0, 0xffff)
  def read32(hex: List[Int], startIndex: Int): Int = read16(hex, startIndex) + (read16(hex, startIndex + 2) * 65536)
  def read32(hex: List[Int]): Int = read16(hex) + (read16(hex,2) * 65536)
}