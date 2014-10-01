package net.tobysullivan.untappd

import java.io.{InputStreamReader, BufferedReader}

import net.tobysullivan.untappd.http.{HttpResponse, HttpClient}
import org.scalatest.{FlatSpec, Matchers}
import org.mockito.Mockito._

import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent._

class UntappdClientSpec extends FlatSpec with Matchers with UntappdClient {
  val httpClient = mock(classOf[HttpClient])

  "getBreweryInfo" should "return a BreweryInfo instance" in {
    val mockPayload = getClass.getResource("mock-responses/get-brewery-info-1009.json")
    val loadedResponse: String = scala.io.Source.fromURL(mockPayload).mkString

    val parsedResponse = Json.parse(loadedResponse)

    when(httpClient.get("/brewery/info/1009")).thenReturn(Future(new HttpResponse(200, parsedResponse)))

    val futureBreweryInfo = this.getBreweryInfo(1009)

    val breweryInfo = Await.result(futureBreweryInfo, 1 second)

    breweryInfo.breweryId shouldBe 1009
    breweryInfo.breweryName shouldBe "Port Brewing Company"
    breweryInfo.breweryLabel startsWith "https://"
  }
}
