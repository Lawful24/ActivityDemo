package com.appplanet.activitydemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragment_container, MainFragment())
        transaction.commit()
    }

    override fun passDataCom(editText_input: String) {
        val bundle = Bundle()
        bundle.putString("msg_txt", editText_input)

        val transaction = this.supportFragmentManager.beginTransaction()
        val msgFragment = MessageFragment()
        msgFragment.arguments = bundle

        transaction.replace(R.id.fragment_container, msgFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}