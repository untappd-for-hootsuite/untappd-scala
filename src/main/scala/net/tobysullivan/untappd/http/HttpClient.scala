package net.tobysullivan.untappd.http

import com.ning.http.client.AsyncHttpClient
import net.tobysullivan.untappd.UntappdServiceException
import play.api.libs.json.{Json, JsNull, JsValue}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._

class HttpClient(val baseUrl: String) {

  private val ningClient = new AsyncHttpClient()

  def get(endpoint: String, params: Map[String, String] = Map()): Future[HttpResponse] = {
    val f = ningClient.prepareGet(baseUrl + endpoint).execute()

    future {
      val resp = f.get()

      val json = Json.parse(resp.getResponseBody)

      new HttpResponse(resp.getStatusCode, json)
    }
  }

  def post(endpoint: String, content: JsValue, params: Map[String, String] = Map()): Future[HttpResponse] = ???
}
