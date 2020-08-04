package com.appplanet.activitydemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendButton = findViewById<Button>(R.id.activity_button) as Button
        sendButton.setOnClickListener {
            val editText = findViewById<EditText>(R.id.editTextTextPersonName)
            val message = editText.text.toString()
            val intent = Intent(this, SecondActivity::class.java).apply {
                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }
    }
}