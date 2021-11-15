package com.membara.entertaiment.dakonthegame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.membara.entertaiment.dakonthegame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onClick()
    }

    private fun onClick() {
        binding.btnPlay.setOnClickListener {
            intent = Intent(this, PlayActivity::class.java)
            startActivity(intent)
        }

        binding.btnHelp.setOnClickListener {
            intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }
}