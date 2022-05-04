package com.oguzhanaslann.base

import java.lang.Exception

const val ONE_MINUTE_MILLIS = 60000
const val ONE_HOUR_MINUTES = 60

class ResultNotFoundException(message: String = "") : Exception(message)