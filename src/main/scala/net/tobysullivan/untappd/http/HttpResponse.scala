package net.tobysullivan.untappd.http

import play.api.libs.json.JsValue

class HttpResponse(val statusCode: Int, val body: JsValue) {

}
