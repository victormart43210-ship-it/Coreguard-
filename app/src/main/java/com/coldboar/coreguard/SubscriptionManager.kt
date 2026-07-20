package com.coldboar.coreguard

import android.content.Context

/** Local demo entitlement. Replace with verified Google Play Billing state before release. */
object SubscriptionManager {
    enum class Tier { FREE, PRO, PLUS, LIFETIME }

    private const val PREFS = "coreguard_entitlements"
    private const val TIER_KEY = "tier"
    private var currentTier: Tier = Tier.FREE

    fun initialize(context: Context) {
        val stored = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(TIER_KEY, Tier.FREE.name)
        currentTier = runCatching { Tier.valueOf(stored ?: Tier.FREE.name) }
            .getOrDefault(Tier.FREE)
    }

    fun setTier(context: Context, tier: Tier) {
        currentTier = tier
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(TIER_KEY, tier.name)
            .apply()
    }

    fun currentTier(): Tier = currentTier
    fun isPremium(): Boolean = currentTier != Tier.FREE
}
