package com.example.calc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputField: EditText
    private lateinit var operationField: TextView
    private lateinit var resultField: TextView

    private var currentInput = ""
    private var previousValue: Double? = null
    private var operation: String? = null
    private var isNewInput = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputField = findViewById(R.id.inputField)
        operationField = findViewById(R.id.operationField)
        resultField = findViewById(R.id.resultField)

        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        )

        numberButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { onNumberClick((it as Button).text.toString()) }
        }

        findViewById<Button>(R.id.btnPlus).setOnClickListener { onOperationClick("+") }
        findViewById<Button>(R.id.btnMinus).setOnClickListener { onOperationClick("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { onOperationClick("×") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { onOperationClick("÷") }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
        findViewById<Button>(R.id.btnClearOne).setOnClickListener { deleteLastCharacter() }
        findViewById<Button>(R.id.btnPercent).setOnClickListener { applyPercentage() }

        findViewById<Button>(R.id.btnPlusMinus).setOnClickListener { toggleSign() }
    }

    private fun onNumberClick(number: String) {
        if (isNewInput) {
            currentInput = ""
            isNewInput = false
        }
        currentInput += number
        inputField.setText(currentInput)
    }

    private fun onOperationClick(selectedOperation: String) {
        if (currentInput.isEmpty()) return

        previousValue = currentInput.toDoubleOrNull()
        operation = selectedOperation
        operationField.text = selectedOperation
        resultField.text = currentInput
        currentInput = ""
        inputField.text.clear()
    }

    private fun calculateResult() {
        if (previousValue == null || currentInput.isEmpty() || operation == null) return

        val secondValue = currentInput.toDoubleOrNull() ?: return
        val result = when (operation) {
            "+" -> previousValue!! + secondValue
            "-" -> previousValue!! - secondValue
            "×" -> previousValue!! * secondValue
            "÷" -> if (secondValue != 0.0) previousValue!! / secondValue else "Ошибка"
            else -> return
        }

        resultField.text = result.toString()
        operationField.text = ""
        inputField.text.clear()
        currentInput = result.toString()
        previousValue = null
        operation = null
        isNewInput = true
    }

    private fun clearAll() {
        currentInput = ""
        previousValue = null
        operation = null
        inputField.text.clear()
        operationField.text = ""
        resultField.text = ""
    }

    private fun deleteLastCharacter() {
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            inputField.setText(currentInput)
        }
    }

    private fun applyPercentage() {
        if (currentInput.isNotEmpty()) {
            val percentage = currentInput.toDoubleOrNull()?.div(100)
            if (percentage != null) {
                currentInput = percentage.toString()
                inputField.setText(currentInput)
            }
        }
    }

    private fun toggleSign() {
        if (currentInput.startsWith("-")) {
            currentInput = currentInput.substring(1)
        } else {
            currentInput = "-$currentInput"
        }
        inputField.setText(currentInput)
    }
}