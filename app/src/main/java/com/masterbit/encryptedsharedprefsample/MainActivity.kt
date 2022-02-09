package com.masterbit.encryptedsharedprefsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataStore: DataStore = SharedPrefDataStore(
            pref = EncryptedSharedPreferences
                .create("datastore", MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC), this, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM),
            GsonUtils
        )

        findViewById<Button>(R.id.save).setOnClickListener {
            val value = findViewById<EditText>(R.id.et).text.toString()
            launch(Dispatchers.IO) {
                dataStore.save(Data(value))
            }
        }

        findViewById<Button>(R.id.restore).setOnClickListener {
            launch {
                val data = withContext(Dispatchers.IO) {
                    dataStore.restore(Data::class.java)
                }
                findViewById<TextView>(R.id.resultTv).text = data?.value ?: "null"
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()
}

@DataStore.Key("plain_text")
class Data(val value: String)