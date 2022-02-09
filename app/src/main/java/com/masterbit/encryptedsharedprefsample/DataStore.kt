package com.masterbit.encryptedsharedprefsample

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import com.google.gson.JsonSyntaxException

interface DataStore {

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Key(val key: String)

    suspend fun <T> restore(clazz: Class<T>): T?

    suspend fun <T: Any> save(data: T?)
}

class SharedPrefDataStore(private val pref: SharedPreferences, private val gsonUtils: GsonUtils) : DataStore {

    @Throws(JsonSyntaxException::class)
    override suspend fun <T> restore(clazz: Class<T>): T? {
        val key = clazz.getAnnotation(DataStore.Key::class.java)?.key
        checkNotNull(key)
        return gsonUtils.deserialize(clazz, pref.getString(key, null))
    }

    override suspend fun <T: Any> save(data: T?) {
        if (data == null) return
        val key = data::class.java.getAnnotation(DataStore.Key::class.java)?.key
        checkNotNull(key)
        pref.edit().putString(key, gsonUtils.serialize(data)).apply()
    }
}