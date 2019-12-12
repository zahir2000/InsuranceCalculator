package com.zahir.insurancecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: InsuranceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ArrayAdapter.createFromResource(
            this,
            R.array.age_group, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAge.adapter = adapter
        }

        viewModel = ViewModelProviders.of(this).get(InsuranceViewModel::class.java)
        displayResult()

        buttonCalculate.setOnClickListener(this)
        buttonReset.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.buttonCalculate -> {
                if(calculateInsurance() == -1)
                    Toast.makeText(applicationContext, "Please select your gender", Toast.LENGTH_SHORT).show()
                else{
                    viewModel.result = calculateInsurance()
                    makeToast("Calculated")
                    displayResult()
                }
            }

            R.id.buttonReset -> {
                reset()
            }
        }
    }

    private fun displayResult() {
        textViewPremium.text = (getString(R.string.insurance_premium) + " RM${viewModel.result}")
    }

    private fun reset() {
        spinnerAge.setSelection(0)
        radioGroupGender.clearCheck()
        checkBoxSmoker.isChecked = false
        textViewPremium.text = ""
        viewModel.result = 0
        makeToast("Cleared")
    }

    private fun calculateInsurance(): Int{
        val chkSmoker = checkBoxSmoker.isChecked
        val radMale = radioButtonMale.isChecked
        val radFemale = radioButtonFemale.isChecked
        var premium = 0

        if(!radMale and !radFemale)
            return -1

        when(spinnerAge.selectedItemPosition){
            0 -> premium = 60

            1 -> {
                premium = 70

                if(radMale)
                    premium += 50

                if(chkSmoker)
                    premium += 100
            }

            2 -> {
                premium = 90

                if(radMale)
                    premium += 100

                if(chkSmoker)
                    premium += 150
            }

            3 -> {
                premium = 120

                if(radMale)
                    premium += 150

                if(chkSmoker)
                    premium += 200
            }

            4 -> {
                premium = 150

                if(radMale)
                    premium += 200

                if(chkSmoker)
                    premium += 250
            }

            5 -> {
                premium = 150

                if(radMale)
                    premium += 200

                if(chkSmoker)
                    premium += 300
            }
        }
        return premium
    }

    private fun makeToast(text: String){
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
    }
}
