package com.example.shakouricalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.shakouricalculator.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentExpression = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // open LCM/GCD screen
        binding.btnLcmGcd.setOnClickListener {
            startActivity(Intent(this, LcmGcdActivity::class.java))
        }

        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )

        for ((i, btn) in numberButtons.withIndex()) {
            btn.setOnClickListener { appendToExpression(i.toString()) }
        }

        binding.btnDot.setOnClickListener { appendToExpression(".") }
        binding.btnPlus.setOnClickListener { appendToExpression("+") }
        binding.btnMinus.setOnClickListener { appendToExpression("-") }
        binding.btnMul.setOnClickListener { appendToExpression("*") }
        binding.btnDiv.setOnClickListener { appendToExpression("/") }

        binding.btnClear.setOnClickListener {
            currentExpression = ""
            updateDisplay()
        }
        binding.btnBack.setOnClickListener {
            if (currentExpression.isNotEmpty()) currentExpression = currentExpression.dropLast(1)
            updateDisplay()
        }
        binding.btnEquals.setOnClickListener {
            try {
                val result = evalExpression(currentExpression)
                currentExpression = result.toString()
            } catch (e: Exception) {
                currentExpression = "Error"
            }
            updateDisplay()
        }
    }

    private fun appendToExpression(s: String) {
        currentExpression += s
        updateDisplay()
    }

    private fun updateDisplay() {
        binding.tvDisplay.text = currentExpression
    }

    // Very simple expression evaluator (supports + - * / and decimals)
    private fun evalExpression(expr: String): Double {
        // This is a basic implementation using JavaScript engine is not available here,
        // so we implement a simple shunting-yard + RPN evaluator for safety.
        val tokens = tokenize(expr)
        val rpn = toRPN(tokens)
        return evalRPN(rpn)
    }

    private fun tokenize(s: String): List<String> {
        val toks = mutableListOf<String>()
        var i = 0
        while (i < s.length) {
            val c = s[i]
            when {
                c.isDigit() || c == '.' -> {
                    val sb = StringBuilder()
                    while (i < s.length && (s[i].isDigit() || s[i] == '.')) {
                        sb.append(s[i]); i++
                    }
                    toks.add(sb.toString())
                    continue
                }
                c in "+-*/" -> {
                    toks.add(c.toString())
                }
            }
            i++
        }
        return toks
    }

    private fun precedence(op: String) = when(op) {
        "+","-" -> 1
        "*","/" -> 2
        else -> 0
    }

    private fun toRPN(tokens: List<String>): List<String> {
        val output = mutableListOf<String>()
        val ops = ArrayDeque<String>()
        for (t in tokens) {
            if (t.toDoubleOrNull() != null) output.add(t)
            else if (t in listOf("+","-","*","/")) {
                while (ops.isNotEmpty() && precedence(ops.last()) >= precedence(t)) {
                    output.add(ops.removeLast())
                }
                ops.addLast(t)
            }
        }
        while (ops.isNotEmpty()) output.add(ops.removeLast())
        return output
    }

    private fun evalRPN(rpn: List<String>): Double {
        val st = ArrayDeque<Double>()
        for (t in rpn) {
            val n = t.toDoubleOrNull()
            if (n != null) st.addLast(n)
            else {
                val b = st.removeLast()
                val a = st.removeLast()
                val res = when(t) {
                    "+" -> a + b
                    "-" -> a - b
                    "*" -> a * b
                    "/" -> a / b
                    else -> 0.0
                }
                st.addLast(res)
            }
        }
        return if (st.isNotEmpty()) st.last() else 0.0
    }
}
