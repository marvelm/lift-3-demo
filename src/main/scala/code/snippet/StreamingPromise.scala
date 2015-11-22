package code.snippet

import code.lib.util.Languages
import net.liftweb.http.{RoundTripInfo, S}
import net.liftweb.http.js.JsCmds._

import scala.Stream._
import scala.xml.NodeSeq

object StreamingPromise {

  def render(in: NodeSeq): NodeSeq = {
    // If an exception is thrown during the save, the client automatically
    // gets a Failure
    def doFind(param: String): Stream[String] = {
      val words = Languages.l.filter(_.toLowerCase startsWith param.toLowerCase).sorted
      from(1) take words.size map (num => {
        Thread.sleep(1000)
        words(num - 1)
      }): Stream[String]

    }

    // Associate the server functions with client-side functions
    for (sess <- S.session) {
      val script = JsCrVar("streamingPromise",
        sess.buildRoundtrip(List[RoundTripInfo]("find" -> doFind _)))
      S.appendGlobalJs(script)
    }

    in
  }
}