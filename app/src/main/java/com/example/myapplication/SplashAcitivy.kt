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

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Animate loading dots
        animateLoadingDots()

        // Simulate loading time (replace with your actual loading logic)
        Handler(Looper.getMainLooper()).postDelayed({
            // Navigate to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Close splash screen
        }, 3000) // 3 seconds delay
    }

    private fun animateLoadingDots() {
        val dot1: View = findViewById(R.id.dot1)
        val dot2: View = findViewById(R.id.dot2)
        val dot3: View = findViewById(R.id.dot3)

        // Infinite loading animation
        val animator1 = ObjectAnimator.ofFloat(dot1, "alpha", 1f, 0.4f)
        val animator2 = ObjectAnimator.ofFloat(dot2, "alpha", 0.4f, 1f, 0.4f)
        val animator3 = ObjectAnimator.ofFloat(dot3, "alpha", 0.4f, 1f)

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(animator1, animator2, animator3)
        animatorSet.duration = 1200

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animatorSet.start() // Loop infinitely
            }
        })
        animatorSet.start()
    }
}