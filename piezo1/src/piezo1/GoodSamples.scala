package piezo1

class GoodSamples(l: List[(Double, Int)], goodSampleRoots: List[(Int, Int)], mirror: Boolean) {
    var normalizedGoodSamples = List.empty[Sample]

    if (!goodSampleRoots.isEmpty) {
        val goodSamples = goodSampleRoots.map(z => l.slice(z._1, z._1 + z._2))
        normalizedGoodSamples = goodSamples.map(new Sample(_, mirror))
    }
}
