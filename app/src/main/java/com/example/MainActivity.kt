package com.example

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.databinding.ActivityMainBinding
import com.srtvprateek.jsonrenderview.Config

class MainActivity : AppCompatActivity() {

    private val viewModel: JSONReaderViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.json.applyConfig(
            Config.Builder()
                .keyObjectColor(Color.BLACK)
                .valueNumberColor(Color.MAGENTA)
                .build()
        )
        viewModel.jsonString.removeObserver(parsedAttrObserver)
        viewModel.jsonString.observe(this, parsedAttrObserver)

        viewModel.parseJson(assets.open("array.json"))
    }

    private val parsedAttrObserver = Observer<String> {
        binding.json.bind(it)
    }
}