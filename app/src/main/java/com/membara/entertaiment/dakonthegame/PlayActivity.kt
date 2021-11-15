package com.membara.entertaiment.dakonthegame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.membara.entertaiment.dakonthegame.databinding.ActivityPlayBinding

class PlayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}