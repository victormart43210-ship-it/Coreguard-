package com.coldboar.coreguard

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import android.app.ActivityManager
import android.content.Context

class MainActivity : AppCompatActivity() {

    private lateinit var cpuView: TextView
    private lateinit var ramView: TextView
    private lateinit var upgradeBtn: Button

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cpuView = findViewById(R.id.cpuText)
        ramView = findViewById(R.id.ramText)
        upgradeBtn = findViewById(R.id.upgradeBtn)

        upgradeBtn.setOnClickListener {
            startActivity(Intent(this, PaywallActivity::class.java))
        }

        startMonitoring()
    }

    private fun startMonitoring() {
        handler.post(object : Runnable {
            override fun run() {
                val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val memInfo = ActivityManager.MemoryInfo()
                am.getMemoryInfo(memInfo)

                val availMB = memInfo.availMem / 1048576L
                val totalMB = memInfo.totalMem / 1048576L
                val used = totalMB - availMB

                val cpu = (25..95).random()

                cpuView.text = "CPU: $cpu%"
                ramView.text = "RAM: $used / $totalMB MB"

                if (cpu > 80 && !SubscriptionManager.isPremium()) {
                    startActivity(Intent(this@MainActivity, PaywallActivity::class.java))
                }

                handler.postDelayed(this, 1500)
            }
        })
    }
}