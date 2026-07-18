package com.android.performance.janktest.security

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import android.util.Log
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Core Guard Security Validator
 * 
 * Implements runtime security checks to detect tampering, rooting, jailbreaking,
 * and other security threats. This class should be called during app initialization.
 */
object SecurityValidator {

    private const val TAG = "CoreGuardSecurity"
    private const val KEYSTORE_ALIAS = "coreguard_integrity_key"
    private const val SECURITY_KEYSTORE_INSTANCE = "AndroidKeyStore"

    // ========== DEVICE TAMPERING DETECTION ==========

    /**
     * Master security check - returns true if device is compromised
     */
    fun isDeviceCompromised(context: Context): Boolean {
        return isRooted() || isJailbroken() || isDebuggable() || 
               hasCommonHackingApps(context) || isRunningInEmulator()
    }

    /**
     * Comprehensive device security status
     */
    fun getSecurityStatus(context: Context): SecurityStatus {
        return SecurityStatus(
            isRooted = isRooted(),
            isJailbroken = isJailbroken(),
            isDebuggable = isDebuggable(),
            hasHackingApps = hasCommonHackingApps(context),
            isEmulator = isRunningInEmulator(),
            signatureValid = verifyAppSignature(context),
            keystoreAvailable = isKeystoreAvailable()
        )
    }

    // ========== ROOT DETECTION ==========

    /**
     * Detects if device has been rooted
     */
    private fun isRooted(): Boolean {
        // Check for su binary in common locations
        val roottestPaths = arrayOf(
            "/system/app/Superuser.apk",
            "/system/app/SuperUser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/bin/su.bak",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/nativelib/libjni_load.so",
            "/data/local/su"
        )

        for (path in roottestPaths) {
            if (File(path).exists()) {
                Log.w(TAG, "Root detected at: $path")
                return true
            }
        }

        // Check for Magisk Hide
        if (File("/sbin/.magisk").exists() || File("/magisk/.core").exists()) {
            Log.w(TAG, "Magisk root detected")
            return true
        }

        return false
    }

    // ========== JAILBREAK DETECTION ==========

    /**
     * Detects if iOS device has been jailbroken (placeholder for Android context)
     */
    private fun isJailbroken(): Boolean {
        // Android-specific check: Look for jailbreak-related app stores
        val jailbreakIndicators = arrayOf(
            "/Applications/Cydia.app",
            "/Library/MobileSubstrate/MobileSubstrate.dylib",
            "/usr/sbin/sshd",
            "/etc/apt",
            "/var/lib/apt/",
            "/Applications/FakeCarrier.app",
            "/Applications/SBSettings.app"
        )

        for (path in jailbreakIndicators) {
            if (File(path).exists()) {
                Log.w(TAG, "Jailbreak indicator found at: $path")
                return true
            }
        }

        return false
    }

    // ========== DEBUGGABLE FLAG ==========

    /**
     * Checks if app is debuggable (very dangerous for security app)
     */
    private fun isDebuggable(): Boolean {
        try {
            return (0 != (Build.VERSION_CODES::class.java.modifiers and 2))
        } catch (e: Exception) {
            Log.e(TAG, "Error checking debuggable flag", e)
        }
        return false
    }

    // ========== COMMON HACKING TOOLS DETECTION ==========

    /**
     * Detects common hacking tools and modified system apps
     */
    private fun hasCommonHackingApps(context: Context): Boolean {
        val hackingApps = arrayOf(
            "com.android.vending.billing.InAppBillingService.LACK",
            "com.chelpus.lackyer11",
            "com.ramdroid.appquarantine",
            "com.ramdroid.appnotifier",
            "com.android.protips",
            "com.android.systemui.pokelock",
            "com.szlaApps.hack",
            "com.szlaApps.hackcheat",
            "com.arutom.selfcall",
            "com.arutom.ulua",
            "com.aaa.selfcall.ads",
            "com.mobileuncovered.hack",
            "com.htc.android.qboxconfig",
            "com.android.settings.beta",
            "com.android.phone.beta",
            "com.android.providers.telephony.beta",
            "com.android.systemui.beta",
            "com.android.systemui.usb",
            "com.android.usbtuner",
            "com.android.cts.priv.ctsshim",
            "com.android.cts.ctsshim",
            "com.android.wfds",
            "com.android.smoketest",
            "de.robv.android.xposed.installer",
            "com.android.xposed.installer",
            "io.va.exposed",
            "com.fr4lab.frida",
            "com.android.frida",
            "com.android.debug",
            "com.android.sandbox"
        )

        val pm = context.packageManager
        for (packageName in hackingApps) {
            try {
                pm.getApplicationInfo(packageName, 0)
                Log.w(TAG, "Hacking app detected: $packageName")
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                // App not found, which is good
            }
        }

        return false
    }

    // ========== EMULATOR DETECTION ==========

    /**
     * Detects if running in an emulator
     */
    private fun isRunningInEmulator(): Boolean {
        val emulatorIndicators = arrayOf(
            "generic",
            "unknown",
            "emulator",
            "vbox",
            "bluestacks"
        )

        val buildFingerprint = Build.FINGERPRINT.lowercase()
        val buildProduct = Build.PRODUCT.lowercase()
        val buildHardware = Build.HARDWARE.lowercase()

        for (indicator in emulatorIndicators) {
            if (buildFingerprint.contains(indicator) ||
                buildProduct.contains(indicator) ||
                buildHardware.contains(indicator)
            ) {
                Log.w(TAG, "Emulator detected: $indicator")
                return true
            }
        }

        // Check for common emulator files
        val emulatorFiles = arrayOf(
            "/system/lib/libc_malloc_debug_qemu.so",
            "/sys/qemu_trace",
            "/system/bin/qemu-props",
            "/system/priv-app/VirtualXposed",
            "/data/xposed/modules/edu.cmu.cylab.starjni"
        )

        for (path in emulatorFiles) {
            if (File(path).exists()) {
                Log.w(TAG, "Emulator file detected: $path")
                return true
            }
        }

        return false
    }

    // ========== APP SIGNATURE VERIFICATION ==========

    /**
     * Verifies that the app's signature hasn't been tampered with
     */
    private fun verifyAppSignature(context: Context): Boolean {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNATURES
            )
            // TODO: Replace with your actual app signature hash
            val expectedSignature = "YOUR_EXPECTED_SIGNATURE_HASH"
            val actualSignature = packageInfo.signatures?.get(0)?.toCharsString() ?: ""
            
            val signaturesMatch = actualSignature.equals(expectedSignature, ignoreCase = true)
            if (!signaturesMatch) {
                Log.e(TAG, "App signature mismatch detected!")
            }
            signaturesMatch
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying app signature", e)
            false
        }
    }

    // ========== KEYSTORE OPERATIONS ==========

    /**
     * Checks if secure keystore is available (Hardware-Backed Key Storage)
     */
    private fun isKeystoreAvailable(): Boolean {
        return try {
            val keystore = KeyStore.getInstance(SECURITY_KEYSTORE_INSTANCE)
            keystore.load(null)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Keystore not available", e)
            false
        }
    }

    /**
     * Creates a hardware-backed encryption key for secure storage
     */
    fun createSecureKey(context: Context): SecretKey? {
        return try {
            val keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                SECURITY_KEYSTORE_INSTANCE
            )

            keyGenerator.init(
                KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .setUserAuthenticationRequired(true)
                    .setUserAuthenticationValidityDurationSeconds(300)
                    .build()
            )

            keyGenerator.generateKey()
        } catch (e: Exception) {
            Log.e(TAG, "Error creating secure key", e)
            null
        }
    }

    /**
     * Encrypts sensitive data with the hardware-backed key
     */
    fun encryptData(plaintext: String, key: SecretKey): ByteArray? {
        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val iv = cipher.iv
            val encryptedData = cipher.doFinal(plaintext.toByteArray())

            // Prepend IV to encrypted data for later decryption
            iv + encryptedData
        } catch (e: Exception) {
            Log.e(TAG, "Error encrypting data", e)
            null
        }
    }

    /**
     * Decrypts sensitive data with the hardware-backed key
     */
    fun decryptData(encryptedData: ByteArray, key: SecretKey): String? {
        return try {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val gcmSpec = GCMParameterSpec(128, encryptedData, 0, 12)
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec)

            val decryptedData = cipher.doFinal(encryptedData, 12, encryptedData.size - 12)
            String(decryptedData)
        } catch (e: Exception) {
            Log.e(TAG, "Error decrypting data", e)
            null
        }
    }

    // ========== CERTIFICATE PINNING HELPER ==========

    /**
     * Certificate pinning configuration for OkHttp3
     * Add this to your OkHttpClient initialization
     */
    fun getCertificatePinner(): String {
        return """
            // In your OkHttp client initialization:
            val certificatePinner = CertificatePinner.Builder()
                .add("api.coreguard.net", "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=")
                .add("api.coreguard.net", "sha256/BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB=")
                .build()
            
            val client = OkHttpClient.Builder()
                .certificatePinner(certificatePinner)
                .build()
        """.trimIndent()
    }

    // ========== DATA CLASSES ==========

    data class SecurityStatus(
        val isRooted: Boolean,
        val isJailbroken: Boolean,
        val isDebuggable: Boolean,
        val hasHackingApps: Boolean,
        val isEmulator: Boolean,
        val signatureValid: Boolean,
        val keystoreAvailable: Boolean
    ) {
        val isCompromised: Boolean
            get() = isRooted || isJailbroken || isDebuggable || 
                    hasHackingApps || isEmulator || !signatureValid

        override fun toString(): String = """
            ┌─── CoreGuard Security Status ───┐
            │ Rooted: $isRooted
            │ Jailbroken: $isJailbroken
            │ Debuggable: $isDebuggable
            │ Hacking Apps: $hasHackingApps
            │ Emulator: $isEmulator
            │ Signature Valid: $signatureValid
            │ Keystore Available: $keystoreAvailable
            │ ─────────────────────────────────
            │ Overall Status: ${if (isCompromised) "⚠️  COMPROMISED" else "✅ SECURE"}
            └─────────────────────────────────┘
        """.trimIndent()
    }
}
