package net.tobysullivan.untappd

import net.tobysullivan.untappd.json.reads._
import net.tobysullivan.untappd.model.BreweryInfo
import net.tobysullivan.untappd.http._
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object UntappdClient extends UntappdClient {
  private val untappdApiPath = "https://api.untappd.com/v4"

  val httpClient = new HttpClient(untappdApiPath)
}

trait UntappdClient {
  protected val httpClient: HttpClient

  private def getRequest(endpoint: String): Future[JsValue] = {

    val futureResponse = httpClient.get(endpoint)

    futureResponse.map { response =>
      if (response.statusCode != 200)
        throw new UntappdServiceException("Response from Untappd API was an error", response.statusCode)

      response.body \ "response"
    }
  }

  def getBreweryInfo(breweryId: Int): Future[BreweryInfo] = {
    import BreweryInfoReads._

    val endpoint = s"/brewery/info/$breweryId"

    val resp = getRequest(endpoint)

    resp.map(_.as[BreweryInfo])
  }
}

