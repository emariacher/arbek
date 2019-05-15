package kebra

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

/**
 * Created by mariachere on 21.05.2015.
 */
object qqJust4Fun {
  def mjust4Fun(c: Context)(parm: c.Expr[Any]): c.Expr[Unit] = {
    import c.universe._

    val paramRepParm = show(parm.tree)

    c.Expr(q"""func($parm,$paramRepParm)""")
  }

  def just4Fun(parm: Any): Unit = macro mjust4Fun

}

object M1 {
  def apply(f: Any): Any = macro impl
  def impl(c: Context)(f: c.Expr[Any]) = { import c.universe._
    val q"(..$args) => $body" = f.tree
    println(s"Printed at compile time!")
    println(s"args = $args, body = $body")
    c.Expr(q"()")
  }
}
