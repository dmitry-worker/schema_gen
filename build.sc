import mill._, scalalib._

object gen extends ScalaModule {

  def mainClass = Some("gen.Main")

  def scalaVersion = "2.12.13"

  def ivyDeps = Agg(
    ivy"com.chuusai::shapeless:2.3.7",
    ivy"io.circe::circe-core:0.14.1",
    ivy"io.circe::circe-generic:0.14.1",
    ivy"io.circe::circe-parser:0.14.1",
    ivy"com.softwaremill.magnolia::magnolia-core:1.0.0-M2"
  )

  def scalacOptions = Seq(
    "-unchecked",
    "-deprecation",
    "-encoding", "utf8",
    "-feature",
    "-language:higherKinds",
    "-Ypartial-unification"
  )

  def scalacPluginIvyDeps = super.scalacPluginIvyDeps() ++ Agg(
    ivy"org.scalamacros:::paradise:2.1.1"
  )

  object test extends Tests {
    def ivyDeps = Agg(ivy"com.lihaoyi::utest:0.7.4")
    def testFrameworks = Seq("utest.runner.Framework")
  }

}
