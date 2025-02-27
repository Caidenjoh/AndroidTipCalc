package com.example.firstproject

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var edtBillAmt: EditText
    private lateinit var edtTipPercentage: EditText
    private lateinit var edtNumPeople: EditText
    private lateinit var btnAddPeople: Button
    private lateinit var peopleContainer: LinearLayout
    private lateinit var btnCalculate: Button
    private lateinit var txtResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Find UI elements
        edtBillAmt = findViewById(R.id.edtBillAmt)
        edtTipPercentage = findViewById(R.id.edtTipPercentage)
        edtNumPeople = findViewById(R.id.edtNumPeople)
        btnAddPeople = findViewById(R.id.btnAddPeople)
        peopleContainer = findViewById(R.id.peopleContainer)
        btnCalculate = findViewById(R.id.btnCalculate)
        txtResults = findViewById(R.id.txtResults)

        btnAddPeople.setOnClickListener { addPeopleFields() }
        btnCalculate.setOnClickListener { calculateTip() }
    }

    private fun addPeopleFields() {
        peopleContainer.removeAllViews()
        val numPeople = edtNumPeople.text.toString().toIntOrNull() ?: 0

        for (i in 1..numPeople) {
            val editText = EditText(this).apply {
                hint = "Amount paid by Person $i"
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
                id = View.generateViewId()
            }
            peopleContainer.addView(editText)
        }
    }

    private fun calculateTip() {
        val billAmt = edtBillAmt.text.toString().toDoubleOrNull() ?: 0.0
        val tipPercent = edtTipPercentage.text.toString().toDoubleOrNull() ?: 0.0
        val totalTip = billAmt * (tipPercent / 100)

        val numPeople = edtNumPeople.text.toString().toIntOrNull() ?: 0
        if (numPeople == 0) {
            txtResults.text = "Tip Amount: R${String.format("%.2f", totalTip)}\nTotal: R${String.format("%.2f", billAmt + totalTip)}"
            return
        }

        var totalPaid = 0.0
        val contributions = mutableListOf<Double>()
        for (i in 0 until numPeople) {
            val editText = peopleContainer.getChildAt(i) as EditText
            val amountPaid = editText.text.toString().toDoubleOrNull() ?: 0.0
            contributions.add(amountPaid)
            totalPaid += amountPaid
        }

        if (totalPaid != billAmt) {
            txtResults.text = "Error: The total contributions (R${String.format("%.2f", totalPaid)}) do not match the bill amount (R${String.format("%.2f", billAmt)})."
            return
        }

        val results = StringBuilder("Tip Split Results:\n")
        val totalAmountWithTip = billAmt + totalTip
        for (i in 0 until numPeople) {
            val percentagePaid = contributions[i] / totalPaid
            val tipContribution = totalTip * percentagePaid
            val totalOwed = contributions[i] + tipContribution
            results.append("Person ${i + 1}: Total Owed R${String.format("%.2f", totalOwed)}\n")
        }

        txtResults.text = results.toString()
    }
}

