package com.masterbit.encryptedsharedprefsample

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

object GsonUtils {

    @Throws(JsonSyntaxException::class)
    fun <T> deserialize(clazz: Class<T>, json: String?): T? {
        return if (json.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(json, clazz)
        }
    }

    fun <T> serialize(data: T?): String? {
        return when {
            data != null -> {
                Gson().toJson(data)
            }
            else -> {
                null
            }
        }
    }
}