package com.example.acpia.core.security

import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

object SecurityCryptoManager {

    private const val HASH_ALGORITHM = "SHA-256"

    /**
     * Computes the SHA-256 hash string for raw byte data or text.
     */
    fun computeSha256(input: String): String {
        return computeSha256(input.toByteArray(Charsets.UTF_8))
    }

    fun computeSha256(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance(HASH_ALGORITHM)
        val hashBytes = digest.digest(bytes)
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Verifies if a given hash matches the newly computed hash for the payload.
     */
    fun verifyIntegrity(payload: String, expectedHash: String): Boolean {
        val computed = computeSha256(payload)
        return computed.equals(expectedHash.trim(), ignoreCase = true)
    }

    /**
     * Creates an immutable Chain-of-Custody seal for evidence tracking.
     */
    fun generateChainOfCustodySeal(
        evidenceId: String,
        officerBadge: String,
        timestamp: String,
        action: String
    ): String {
        val rawEnvelope = "EVID::$evidenceId||OFFICER::$officerBadge||TIME::$timestamp||ACT::$action||SECRET::KERALA_CYBERDOME_SECURE_VAULT_2026"
        return "SEAL_" + computeSha256(rawEnvelope).take(24).uppercase(Locale.ROOT)
    }

    /**
     * Generates a tamper-proof audit record hash linking previous state.
     */
    fun generateAuditHash(
        id: String,
        timestamp: String,
        badge: String,
        action: String,
        prevHash: String = "GENESIS_ROOT_HASH"
    ): String {
        val payload = "$id|$timestamp|$badge|$action|$prevHash"
        return computeSha256(payload)
    }

    /**
     * Returns current formatted timestamp for forensic logs (ISO-like standard).
     */
    fun getFormattedTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'IST'", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getShortTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }
}
