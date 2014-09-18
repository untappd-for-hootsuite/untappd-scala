package net.tobysullivan.untappd.json.reads

import net.tobysullivan.untappd.model.BreweryInfo
import play.api.libs.json._
import play.api.libs.functional.syntax._

object BreweryInfoReads {
  implicit val breweryInfoReads = (
    (__ \ "brewery_id").read[Int] and
      (__ \ "brewery_name").read[String] and
      (__ \ "brewery_label").read[String]
    )(BreweryInfo.apply _)
}
