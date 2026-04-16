package com.example.myapplication

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

// FIX 1: Added missing package declaration
// FIX 2: setContentView now uses correct layout name (loadingscreen.xml → R.layout.loadingscreen)
// FIX 3: Intent navigates to MainActivity (Login), not LoginActivity (which doesn't exist)

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loadingscreen)  // FIX: was R.layout.activity_splash (file doesn't exist)

        animateLoadingDots()

        Handler(Looper.getMainLooper()).postDelayed({
            // FIX: Navigate to MainActivity (the login screen)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }

    private fun animateLoadingDots() {
        val dot1: View = findViewById(R.id.dot1)
        val dot2: View = findViewById(R.id.dot2)
        val dot3: View = findViewById(R.id.dot3)

        val animator1 = ObjectAnimator.ofFloat(dot1, "alpha", 1f, 0.4f)
        val animator2 = ObjectAnimator.ofFloat(dot2, "alpha", 0.4f, 1f, 0.4f)
        val animator3 = ObjectAnimator.ofFloat(dot3, "alpha", 0.4f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2, animator3)
        animatorSet.duration = 1200

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatorSet.start()
            }
        })
        animatorSet.start()
    }
}