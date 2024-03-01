package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
//import kotlinx.android.synthetic.main.activity_main.*
import com.example.myapplication.helperFunc.DatePickerHelper
import java.util.*

class BirthdayActivity : AppCompatActivity(){
    lateinit var datePicker: DatePickerHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday)

        datePicker = DatePickerHelper(this, true)

        val selectDate: Button = findViewById(R.id.btSelectDate)
        selectDate.setOnClickListener {
            showDatePickerDialog()
        }
    }
    private fun showDatePickerDialog() {
        val cal = Calendar.getInstance()
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val m = cal.get(Calendar.MONTH)
        val y = cal.get(Calendar.YEAR)
        datePicker.showDialog(d, m, y, object : DatePickerHelper.Callback {
            override fun onDateSelected(dayofMonth: Int, month: Int, year: Int) {
                val dayStr = if (dayofMonth < 10) "0${dayofMonth}" else "${dayofMonth}"
                val mon = month + 1
                val monthStr = if (mon < 10) "0${mon}" else "${mon}"
                val tvDate : TextView = findViewById(R.id.tvDate)
                tvDate.text = "${dayStr}-${monthStr}-${year}"
            }
        })
    }
}