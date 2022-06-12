package com.lada.vicinity

import java.util.*

data class Model (
    var name: String,
    var creationDate: Date = Calendar.getInstance().time,
    var updateTime: Date = Calendar.getInstance().time
)