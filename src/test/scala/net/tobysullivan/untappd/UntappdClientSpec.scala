package net.tobysullivan.untappd

import com.ning.http.client.AsyncHttpClient
import net.tobysullivan.untapped.UntappdClient
import org.scalatest.{FlatSpec, Matchers}
import org.mockito.Mockito._

class UntappdClientSpec extends FlatSpec with Matchers with UntappdClient {
  val httpClient = mock(classOf[AsyncHttpClient])

  "getBreweryInfo" should "return a BreweryInfo instance" in {
    // TODO
  }
}
