package com.ianarbuckle.gymplanner.utils

import kotlinx.serialization.json.Json

inline fun <reified T> Json.prettyPrintBody(body: T): String = this.encodeToString(body)
