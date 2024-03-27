package com.intermeet.android.Home_Page

//import HomePage_Test
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.intermeet.android.R

/*class test_to_home : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_creation)

        ButtonFunc()
    }

    private fun ButtonFunc(){
        val nextButton : Button = findViewById(R.id.next_button)
        nextButton.setOnClickListener{
            val intent = Intent(this, HomePage_Test2::class.java)
            startActivity(intent)
        }
    }
}*/

class Test_to_home : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_to_home)

        ButtonFunc()
    }

    private fun ButtonFunc(){
        val nextButton : Button = findViewById(R.id.next_button)
        nextButton.setOnClickListener{
            val intent = Intent(this, HomePage_Test2::class.java)
            startActivity(intent)
        }
    }
}
