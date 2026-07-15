package com.coldboar.coreguard

object SubscriptionManager {

    enum class Tier {
        FREE, PRO, PLUS, LIFETIME
    }

    var currentTier: Tier = Tier.FREE

    fun isPremium(): Boolean {
        return currentTier != Tier.FREE
    }
}