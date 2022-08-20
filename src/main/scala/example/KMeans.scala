package example

trait KMeans {
  val alpha = 10
  def kmeans(x: Seq[Color], nk: Int): Seq[Color] = {
    var oldK: Seq[Color] = Seq()
    var initialK = Seq.fill(nk)(Util.choosePixel(x))
    var diff = 99999
    var belongMap: Seq[Color] = Seq()

    while (diff > alpha) {
      // println(s"initialK $initialK")
      // let x belong to ks
      belongMap = x.map { pix =>
        initialK.minBy(k => Util.colorDelta(k, pix))
      }
      // println(s"gr# ${grouped.size} bl# ${belongMap.size}")
      val zipped = x zip belongMap
      oldK = initialK
      initialK = initialK map { k =>
        val belongingToK = zipped.filter(_._2 == k)
        // println(s"belK# ${belongingToK.size}")
        Util.seq2Color(
          belongingToK
            .map(pair => Seq(pair._1.r, pair._1.g, pair._1.b, pair._1.a))
            .transpose
            .map(_.sum / x.size)
        )
      }
      // fit initialK to existing pixel
      initialK = initialK.map(k => x.minBy(Util.colorDelta(_, k)))

      // get delta
      diff = (oldK zip initialK).map { case (c, d) =>
        Util.colorDelta(c, d).toInt
      }.sum / x.size

      if (initialK.distinct.size < initialK.size) { diff = 0 }

      println(s"diff: $diff")
    }

    belongMap
  }

}
