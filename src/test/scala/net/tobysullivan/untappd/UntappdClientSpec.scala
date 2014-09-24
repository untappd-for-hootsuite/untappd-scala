package net.tobysullivan.untappd

import net.tobysullivan.untappd.http.{HttpResponse, HttpClient}
import org.scalatest.{FlatSpec, Matchers}
import org.mockito.Mockito._

import play.api.libs.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent._

class UntappdClientSpec extends FlatSpec with Matchers with UntappdClient {
  val httpClient = mock(classOf[HttpClient])

  def wrapResponse(response: String): JsValue = Json.parse(
    s"""
      | {
      |   "meta": {
      |     "code": 200,
      |     "response_time": {
      |       "time": 0.20,
      |       "measure": "seconds"
      |     }
      |   },
      |   "notifications": {
      |     "type": "notifications",
      |     "unread_count": {
      |       "comments": 0,
      |       "toasts": 0,
      |       "friends": 0,
      |       "news": 0
      |     }
      |   },
      |   "response": {
      |     $response
      |   }
      | }
    """.stripMargin)

  "getBreweryInfo" should "return a BreweryInfo instance" in {
    val contact =
      """
        | {
        |   "twitter":"lostabbey",
        |   "facebook":"http://www.facebook.com/pages/Port-Brewing-The-Lost-abbey/75438839471?ref=nf",
        |   "url":"http://www.portbrewing.com/"
        | }
      """.stripMargin
    
    val claimedStatus =
      """
        | {
        |   "is_claimed":false,
        |   "claimed_slug":"",
        |   "follow_status":false,
        |   "follower_count":0,
        |   "uid":0
        | }
      """.stripMargin

    val beerList =
      """
        | {
        |   "count":15,
        |   "items":[
        |     {
        |       "has_had":false,
        |       "total_count":4176,
        |       "beer":{
        |         "bid":5809,
        |         "beer_name":"Mongo Double IPA",
        |         "beer_label":"https://untappd.s3.amazonaws.com/site/beer_logos/beer-portMongo.jpg",
        |         "beer_style":"Imperial / Double IPA",
        |         "beer_abv":8.5,
        |         "auth_rating":0,
        |         "wish_list":false,
        |         "beer_description":"A Double IPA brewed in memory of the brew cat. In honor of Mongo, Mike Rodriguez made his first Double IPA, as he loved the cat and wanted to honor him with this beer. This 8.5% abv beer was made with Columbus hops (Mongoâ€™s Birthname), Cascade for the Mama cat, and Simocoe because that is Mikeâ€™s favorite hop.",
        |         "rating_score":3.98
        |       },
        |       "brewery":{
        |         "brewery_id":1009,
        |         "brewery_name":"Port Brewing Company",
        |         "brewery_label":"https://untappd.s3.amazonaws.com/site/brewery_logos/brewery-PortBrewingCompany_1009.jpeg",
        |         "country_name":"United States",
        |         "contact":{
        |           "twitter":"lostabbey",
        |           "facebook":"http://www.facebook.com/pages/Port-Brewing-The-Lost-abbey/75438839471?ref=nf",
        |           "url":"http://www.portbrewing.com/"
        |         },
        |         "location":{
        |           "brewery_city":"San Marcos",
        |           "brewery_state":"CA",
        |           "lat":33.1413,
        |           "lng":-117.15
        |         }
        |       },
        |       "friends":{
        |         "count":0,
        |         "items":[ ]
        |       }
        |     }
        |   ]
        | }
      """.stripMargin
    
    val mockedResponse = wrapResponse(
      s"""
        | "brewery": {
        |   "brewery_id": 1009,
        |   "brewery_name":"Port Brewing Company",
        |   "brewery_label":"https://untappd.s3.amazonaws.com/site/brewery_logos/brewery-PortBrewingCompany_1009.jpeg",
        |   "country_name":"United States",
        |   "brewery_in_production":0,
        |   "claimed_status": $claimedStatus,
        |   "beer_count":114,
        |   "contact": $contact,
        |   "brewery_type":"Micro Brewery",
        |   "location":{
        |     "brewery_address":"155 Mata Way, Suite 104",
        |     "brewery_city":"San Marcos",
        |     "brewery_state":"CA",
        |     "brewery_lat":33.1413,
        |     "brewery_lng":-117.15
        |   },
        |   "rating":{
        |     "count":16828,
        |     "rating_score":3.715
        |   },
        |   "owners": {
        |     "count": 1,
        |     "items": [
        |       {
        |         "brewery_id": 157,
        |         "brewery_name": "Brewery name"
        |       }
        |     ]
        |   },
        |   "brewery_description":"",
        |   "stats":{
        |     "total_count":33062,
        |     "unique_count":10170,
        |     "monthly_count":41442,
        |     "weekly_count":12358,
        |     "user_count":17
        |   },
        |   "media": [ ],
        |   "checkins": [ ],
        |   "beer_list":$beerList
        | }
      """.stripMargin)

    when(httpClient.get("/brewery/info/1009")).thenReturn(Future(new HttpResponse(200, mockedResponse)))

    val futureBreweryInfo = this.getBreweryInfo(1009)

    val breweryInfo = Await.result(futureBreweryInfo, 1 second)

    breweryInfo.breweryId shouldBe 1009
    breweryInfo.breweryName shouldBe "Port Brewing Company"
    breweryInfo.breweryLabel startsWith "https://"

  }
}
