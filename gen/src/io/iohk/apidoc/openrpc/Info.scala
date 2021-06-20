package io.iohk.apidoc.openrpc

case class Info(
  version: String,
  title: String,
  description: String,
  termsOfService: Option[String],
  contact: Option[Info.Contact],
  license: Option[Info.License]
)

object Info {

  val empty = Info(
    version = "",
    title = "",
    description = "",
    termsOfService = None,
    contact = None,
    license = None
  )
  
  case class Contact(
    name: String,
    email: String,
    url: Option[String]
  )
  
  case class License(
    name: String,
    url: Option[String]
  )

}