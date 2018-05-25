package kbdmatrix

import scala.math.Ordering

class CompareRowThenCol extends Ordering[KbdKey] {
	def compare(k1: KbdKey, k2: KbdKey) = k1.value-k2.value
}