package net.tobysullivan.untappd

class UntappdServiceException(val message: String, val statusCode: Int = 0) extends Exception {
}
