package com.example.bluetoothdevicelistandapicall.userinterface

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.bluetoothdevicelistandapicall.R
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewPager = findViewById(R.id.viewPager) as ViewPager
        var tablayout = findViewById(R.id.tablayout) as TabLayout

        val fragmentAdapter = FragmentAdapter(supportFragmentManager)
        fragmentAdapter.addFragment(BluetoothListFragment(),"BT Device List")
        fragmentAdapter.addFragment(MainFragment(),"List From API")

        viewPager.adapter = fragmentAdapter
        tablayout.setupWithViewPager(viewPager)

    }

}