package com.coldboar.coreguard

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var cpuView: TextView
    private lateinit var ramView: TextView
    private lateinit var statusView: TextView
    private val handler = Handler(Looper.getMainLooper())
    private var monitoring = false
    private var paywallVisible = false

    private val monitorTask = object : Runnable {
        override fun run() {
            if (!monitoring) return

            val memoryInfo = ActivityManager.MemoryInfo()
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.getMemoryInfo(memoryInfo)

            val totalMb = memoryInfo.totalMem / (1024L * 1024L)
            val availableMb = memoryInfo.availMem / (1024L * 1024L)
            val usedMb = (totalMb - availableMb).coerceIn(0L, totalMb)
            val simulatedCpu = (25..95).random()

            cpuView.text = getString(R.string.cpu_simulated_format, simulatedCpu)
            ramView.text = getString(R.string.ram_format, usedMb, totalMb)

            if (simulatedCpu > 80 && !SubscriptionManager.isPremium() && !paywallVisible) {
                openPaywall()
            }
            handler.postDelayed(this, 1500L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SubscriptionManager.initialize(this)

        cpuView = findViewById(R.id.cpuText)
        ramView = findViewById(R.id.ramText)
        statusView = findViewById(R.id.statusText)
        findViewById<Button>(R.id.upgradeBtn).setOnClickListener { openPaywall() }
    }

    override fun onStart() {
        super.onStart()
        monitoring = true
        paywallVisible = false
        handler.removeCallbacks(monitorTask)
        handler.post(monitorTask)
    }

    override fun onStop() {
        monitoring = false
        handler.removeCallbacks(monitorTask)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        if (::statusView.isInitialized) {
            statusView.text = getString(R.string.monitoring_status)
        }
    }

    private fun openPaywall() {
        if (!paywallVisible) {
            paywallVisible = true
            startActivity(Intent(this, PaywallActivity::class.java))
        }
    }
}
