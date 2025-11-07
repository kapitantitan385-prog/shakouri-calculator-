package com.example.shakouricalculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shakouricalculator.databinding.ActivityLcmGcdBinding
import kotlin.math.abs

class LcmGcdActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLcmGcdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLcmGcdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCompute.setOnClickListener {
            val aStr = binding.etA.text.toString()
            val bStr = binding.etB.text.toString()
            if (aStr.isBlank() || bStr.isBlank()) {
                binding.tvResult.text = "لطفاً هر دو عدد را وارد کنید"
                return@setOnClickListener
            }
            try {
                val a = aStr.toLong()
                val b = bStr.toLong()
                val g = gcd(a, b)
                val l = lcm(a, b)
                binding.tvResult.text = "ب.م.م (GCD) = $g\nک.م.م (LCM) = $l"
            } catch (e: Exception) {
                binding.tvResult.text = "خطا: ورودی معتبر نیست"
            }
        }
    }

    private fun gcd(x: Long, y: Long): Long {
        var a = abs(x)
        var b = abs(y)
        while (b != 0L) {
            val t = a % b
            a = b
            b = t
        }
        return a
    }

    private fun lcm(a: Long, b: Long): Long {
        if (a == 0L || b == 0L) return 0L
        return abs(a / gcd(a, b) * b)
    }
}
