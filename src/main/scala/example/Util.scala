package example

import org.scalajs.dom
import org.scalajs.dom.document
import Types._
import scala.util.Random

object Util {
  def seq2Color(s: CanvasVec): Color = {
    val Seq(r, g, b, a) = s.take(4)
    Color(r, g, b, a)
  }
  def color2Array(s: Seq[Color]): Array[Int] = {
    s.flatMap(c => Seq(c.r, c.g, c.b, c.a)).toArray
  }

  def canvasToVector(ctx: dom.CanvasRenderingContext2D): CanvasVec =
    ctx.getImageData(0, 0, ctx.canvas.width, ctx.canvas.height).data.toSeq

  def choosePixel(vec: Seq[Color]): Color = {
    vec(Random.nextInt(vec.size))
  }

  def colorDelta(c: Color, d: Color): Double = {
    Math.pow(c.r - d.r, 2) + Math.pow(
      c.g - d.g,
      2
    ) + Math.pow(c.b - d.b, 2) + Math.pow(c.a - d.a, 2)
  }
}
