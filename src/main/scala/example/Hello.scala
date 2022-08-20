package example

import scala.scalajs.js.JSConverters

object Hello extends App with KMeans {
  import org.scalajs.dom

  val fileElemt: dom.HTMLInputElement =
    dom.document.querySelector("#file").asInstanceOf[dom.HTMLInputElement]
  val origImgElem: dom.HTMLImageElement =
    dom.document.querySelector("img").asInstanceOf[dom.HTMLImageElement]
  val origCanvasElem: dom.HTMLCanvasElement =
    dom.document.querySelector("#orig").asInstanceOf[dom.HTMLCanvasElement]
  val resultCanvasElem: dom.HTMLCanvasElement =
    dom.document.querySelector("#result").asInstanceOf[dom.HTMLCanvasElement]
  val convertButton: dom.HTMLButtonElement =
    dom.document.querySelector("#conv").asInstanceOf[dom.HTMLButtonElement]
  val clusterRange: dom.HTMLInputElement =
    dom.document.querySelector("#k").asInstanceOf[dom.HTMLInputElement]
  val clusterRangeLabel: dom.HTMLLabelElement =
    dom.document.querySelector("#klabel").asInstanceOf[dom.HTMLLabelElement]
  val downScaleRange: dom.HTMLInputElement =
    dom.document.querySelector("#ds").asInstanceOf[dom.HTMLInputElement]
  val downScaleRangeLabel: dom.HTMLLabelElement =
    dom.document.querySelector("#dslabel").asInstanceOf[dom.HTMLLabelElement]

  val origCtx: dom.CanvasRenderingContext2D =
    origCanvasElem
      .getContext("2d")
      .asInstanceOf[dom.CanvasRenderingContext2D]
  val resultCtx: dom.CanvasRenderingContext2D = resultCanvasElem
    .getContext("2d")
    .asInstanceOf[dom.CanvasRenderingContext2D]

  fileElemt
    .addEventListener(
      "change",
      (_: Any) => {
        println("changed")
        val fr = new dom.FileReader()
        fr.onload = (ev) => {
          origImgElem.src = fr.result.toString()

        }
        fr.readAsDataURL(fileElemt.files.item(0))
      }
    )

  origImgElem.onload = (_: dom.Event) => {
    println("drawing")
    // transform canvas
    redraw(downScaleRange.value.toInt)
  }

  downScaleRange.onchange = (_: dom.Event) => {
    downScaleRangeLabel.innerText = s"Downscale (${downScaleRange.value}x)"
    redraw(downScaleRange.value.toInt)
  }

  clusterRange.onchange = (_: dom.Event) => {
    clusterRangeLabel.innerText = s"Color size (${clusterRange.value})"
  }

  val redraw = (downScaleFactor: Int) => {
    origCanvasElem.height = origImgElem.naturalHeight / downScaleFactor
    origCanvasElem.width = origImgElem.naturalWidth / downScaleFactor
    resultCanvasElem.height = origImgElem.naturalHeight / downScaleFactor
    resultCanvasElem.width = origImgElem.naturalWidth / downScaleFactor
    origCtx.imageSmoothingEnabled = false;
    origCtx.drawImage(
      origImgElem,
      0,
      0,
      origCanvasElem.width,
      origCanvasElem.height
    )
  }

  convertButton.addEventListener(
    "click",
    (_: dom.Event) => {
      import scalajs.js.JSConverters._
      println("convert")
      val vec = Util.canvasToVector(origCtx)
      val result: Array[Int] =
        Util.color2Array(
          kmeans(
            vec.grouped(4).map(Util.seq2Color).toSeq,
            clusterRange.value.toInt
          )
        )

      val buf = resultCtx.createImageData(
        resultCanvasElem.width,
        resultCanvasElem.height
      )
      for (n <- 0 until result.length) {
        buf.data(n) = result(n)
      }

      resultCtx.putImageData(buf, 0, 0)
    }
  )

  println("ok")
}
