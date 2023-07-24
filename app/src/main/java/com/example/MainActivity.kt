package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.databinding.ActivityMainBinding
import com.srtvprateek.jsonrenderview.Config
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.json.applyConfig(
            Config.Builder()
                .keyObjectColor(Color.BLACK)
                .valueNumberColor(Color.MAGENTA)
                .build()
        )
        Thread {
            val inputStream: InputStream?
            try {
                inputStream = assets.open("object.json")
                val length = inputStream.available()
                val buffer = ByteArray(length)
                inputStream.read(buffer)
                val result = String(buffer, Charsets.UTF_8)
                inputStream.close()
                runOnUiThread { binding.json.bind(result) }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }
}