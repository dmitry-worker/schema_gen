## Intro

Scala generator for OpenRPC.
Built on magnolia-macros and circe.

Assuming that each method consists of 
`case class Request` and `case class Response`

It is able to derive schema for the following:
* Product (`case class`es) 
* CoProduct (`sealed trait` hierarchies) 
* Array (`Seq[_]`)
* Optional parameters (`Option[_]`)

**See `Main.scala` for a working example**


## Run

Just invoke `mill gen.run`

Then check the result on the https://playground.open-rpc.org/
