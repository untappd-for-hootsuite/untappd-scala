package net.tobysullivan.untappd

import com.ning.http.client.AsyncHttpClient
import net.tobysullivan.untappd.json.reads._
import net.tobysullivan.untappd.model.BreweryInfo
import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

object UntappdClient extends UntappdClient {
  val httpClient = new AsyncHttpClient()
}

trait UntappdClient {
  private val untappdApiPath = "https://api.untappd.com/v4"

  protected val httpClient: AsyncHttpClient

  private def getRequest(endpoint: String): Future[JsValue] = {
    val f = httpClient.prepareGet(untappdApiPath + endpoint).execute()

    future {
      val resp = f.get()

      if (resp.getStatusCode != 200)
        throw new UntappdServiceException("Response from Untappd API was an error", resp.getStatusCode)

      Json.parse(resp.getResponseBody) \ "meta" \ "response"
    }
  }

  def getBreweryInfo(breweryId: Int): Future[BreweryInfo] = {
    import BreweryInfoReads._

    val endpoint = s"/brewery/info/$breweryId"

    val resp = getRequest(endpoint)

    resp.map(_.as[BreweryInfo])
  }
}

