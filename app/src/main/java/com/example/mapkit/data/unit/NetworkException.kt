package com.example.mapkit.data.unit


class ServerException(message: String) : Exception(message)

class NetworkException(message: String) : Exception(message)
class NoConnectionException(message: String) : Exception(message)

class GlobalException(message: String, val statusCode: Int) :
    Exception("$message code = $statusCode")

class TokenWrongException(message: String) : Exception(message)
class TokenWrongLogoutException(message: String) : Exception(message)
class Token20403Exception(message: String) : Exception(message)
class Token20400Exception(message: String) : Exception(message)
class NeedPassChangeException(message: String) : Exception(message)
class AppUpdateException(massage: String) : Exception(massage)
class InfoForUser(massage: String) : Exception(massage)
