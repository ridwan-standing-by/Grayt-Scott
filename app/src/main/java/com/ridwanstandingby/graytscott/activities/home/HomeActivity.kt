package com.ridwanstandingby.graytscott.activities.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ridwanstandingby.graytscott.R
import com.ridwanstandingby.graytscott.activities.animation.AnimationActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        goToAnimationButton.setOnClickListener { goToAnimationActivity() }
    }

    private fun goToAnimationActivity() {
        val intent = Intent(this, AnimationActivity::class.java)
        startActivity(intent)
    }
}
