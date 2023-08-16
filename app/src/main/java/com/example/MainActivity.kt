package com.example

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.example.databinding.ActivityMainBinding
import com.jsonrenderview.Config

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

        binding.searchEdit.doOnTextChanged { text, _, _, _ ->
            if(text.toString().isEmpty()) {
                binding.json.search.clear()
                binding.searchCount.visibility = GONE
            } else {
                val count = binding.json.search.highlight(text.toString())
                binding.searchCount.visibility = VISIBLE
                binding.searchCount.text = count.toString()
            }
        }
        binding.searchNext.setOnClickListener { binding.json.search.navigateNext() }
        binding.searchPrevious.setOnClickListener { binding.json.search.navigatePrevious() }

        viewModel.parseJson(assets.open("object.json"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private val parsedAttrObserver = Observer<String> {
        binding.json.bind(it)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_expand -> binding.json.action.expand()
            R.id.action_collapse -> binding.json.action.collapse()
        }
        return super.onOptionsItemSelected(item)
    }
}
