package com.coldboar.coreguard

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PaywallActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paywall)

        findViewById<Button>(R.id.btnLifetime).setOnClickListener {
            SubscriptionManager.currentTier = SubscriptionManager.Tier.LIFETIME
            finish()
        }
    }
}