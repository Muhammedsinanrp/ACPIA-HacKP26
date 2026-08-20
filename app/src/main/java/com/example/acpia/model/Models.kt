package com.example.acpia.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

enum class CaseStage(val label: String) {
    INTAKE("Intake & FIR"),
    FORENSIC_ANALYSIS("Forensic Analysis"),
    SUSPECT_MAPPING("Suspect Correlation"),
    CHARGE_SHEET("Charge Sheet Preparation"),
    COURT_TRIAL("Court Ready / Trial")
}

enum class CasePriority(val label: String, val colorHex: Long) {
    CRITICAL("Critical", 0xFFFF4D4F),
    HIGH("High", 0xFFFF7A45),
    MEDIUM("Medium", 0xFFFFA940),
    LOW("Low", 0xFF52C41A)
}

data class CustodyRecord(
    val timestamp: String,
    val officerName: String,
    val badgeNumber: String,
    val action: String,
    val location: String,
    val hashSignature: String
)

data class Evidence(
    val id: String,
    val caseId: String,
    val name: String,
    val kind: String,
    val count: String,
    val status: String, // "Verified", "Under Review", "Flagged"
    val type: String,   // "MESSAGE", "IMAGE", "DEVICE", "LOG", "NETWORK_PCAP", "CRYPTO_LEDGER"
    val sha256Hash: String,
    val fileSizeBytes: Long = 0L,
    val seizureLocation: String = "",
    val verifiedByOfficer: String = "",
    val custodyLogs: List<CustodyRecord> = emptyList()
)

data class Case(
    val id: String,
    val firNumber: String,
    val title: String,
    val status: String, // "Active", "Under Review", "Escalated", "Closed"
    val priority: CasePriority = CasePriority.HIGH,
    val stage: CaseStage = CaseStage.FORENSIC_ANALYSIS,
    val platform: String = "",
    val reportedAt: String = "",
    val hashMatches: Int = 0,
    val hashDbStatus: String = "Clean",
    val locationHint: String = "",
    val department: String = "Cyber Crime Police Station, Thiruvananthapuram",
    val notes: String = "",
    val assignedTo: String = "Insp. R. Chandran (Badge #KL-CY-409)",
    val suspectCount: Int = 1,
    val events: List<Event> = emptyList(),
    val evidence: List<Evidence> = emptyList(),
    val entities: List<String> = emptyList()
)

data class Event(
    val t: String,
    val label: String,
    val category: String = "INVESTIGATION"
)

data class Stat(
    val label: String,
    val value: String,
    val detail: String,
    val color: Long
)

data class Service(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val endpoint: String = ""
)

data class QuickLink(
    val label: String,
    val value: String,
    val icon: ImageVector
)

data class EntityNode(
    val id: String,
    val label: String,
    val type: String, // "SUSPECT", "PHONE", "IP", "WALLET", "DEVICE"
    val riskScore: Int, // 1 - 100
    val xRatio: Float,
    val yRatio: Float
)

data class EntityLink(
    val fromId: String,
    val toId: String,
    val relationship: String
)

data class AuditLogEntry(
    val id: String,
    val timestamp: String,
    val officerName: String,
    val badgeNumber: String,
    val actionType: String, // "EVIDENCE_ACCESS", "HASH_VERIFICATION", "CASE_UPDATE", "EXPORT_REPORT"
    val details: String,
    val tamperProofHash: String
)

data class AiInsight(
    val id: String,
    val category: String,
    val headline: String,
    val confidence: Float,
    val summary: String,
    val recommendedAction: String
)

data class CyberLawReference(
    val section: String,
    val title: String,
    val description: String,
    val punishment: String
)
