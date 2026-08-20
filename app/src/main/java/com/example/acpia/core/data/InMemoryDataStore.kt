package com.example.acpia.core.data

import com.example.acpia.core.security.SecurityCryptoManager
import com.example.acpia.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class InMemoryDataStore : ICaseRepository, IEvidenceRepository, IAuditRepository, IEntityGraphRepository, IAiAssistantRepository {

    // -------------------------------------------------------------
    // Initial Seed Data
    // -------------------------------------------------------------
    private val initialCases = listOf(
        Case(
            id = "ACPIA-2026-014",
            firNumber = "FIR No. 84/2026 u/s 66D, 419, 420 IPC",
            title = "Multi-State Instant Loan App & Extortion Syndicate",
            status = "Active",
            priority = CasePriority.CRITICAL,
            stage = CaseStage.FORENSIC_ANALYSIS,
            platform = "Android APK / Telegram / Cloud Server",
            reportedAt = "2026-08-18 09:14 IST",
            hashMatches = 18,
            hashDbStatus = "Interpol Blacklist Alert Match",
            locationHint = "Kochi & Bengaluru Call Hubs",
            department = "Cyber Crime Police Station, Kochi Central",
            assignedTo = "Insp. R. Chandran (Badge #KL-CY-409)",
            suspectCount = 4,
            notes = "Targeting victims across Ernakulam and Kozhikode with rogue loan apps harvesting contact books and blackmailing.",
            events = listOf(
                Event("09:14", "Case intake and chain-of-custody seal created", "INTAKE"),
                Event("09:41", "Evidence ingestion and metadata extraction complete", "FORENSIC"),
                Event("10:02", "Entity graph expanded with 3 new SIM correlations", "CORRELATION"),
                Event("10:20", "AI investigator flagged a shared Mule Bank IFSC", "AI_ALERT")
            ),
            entities = listOf("Suspect: A. Sharma", "UPI: quickpay99@ibl", "IMEI: 864920194820194", "IP: 103.24.18.91")
        ),
        Case(
            id = "ACPIA-2026-009",
            firNumber = "FIR No. 52/2026 u/s 66C, 66E IT Act",
            title = "SIM-Box Bypass & OTP Interception Gateway",
            status = "Under Review",
            priority = CasePriority.HIGH,
            stage = CaseStage.SUSPECT_MAPPING,
            platform = "GSM Gateway / VoIP Gateway",
            reportedAt = "2026-08-15 14:30 IST",
            hashMatches = 7,
            hashDbStatus = "Telecom Dept Carrier Match",
            locationHint = "Kozhikode Coastal Belt",
            department = "Cyber Operations Wing, Kozhikode",
            assignedTo = "DySP M. Suresh (Badge #KL-CY-102)",
            suspectCount = 2,
            notes = "Illegal VoIP termination gateway operating 64-port SIM boxes without license.",
            events = listOf(
                Event("14:30", "Seizure of 64-port GSM Gateway hardware", "SEIZURE"),
                Event("16:00", "Dumped flash memory extracted via JTAG", "FORENSIC"),
                Event("18:15", "Correlated with overseas call routing server", "ANALYSIS")
            ),
            entities = listOf("Suspect: Unknown Operator", "Gateway IP: 45.112.90.12", "Tower ID: KL-KKD-401")
        ),
        Case(
            id = "ACPIA-2026-003",
            firNumber = "FIR No. 19/2026 u/s 43, 66 IT Act & 384 IPC",
            title = "Healthcare Diagnostic Lab Ransomware Incident",
            status = "Escalated",
            priority = CasePriority.CRITICAL,
            stage = CaseStage.INTAKE,
            platform = "Windows Server 2022 / RDP",
            reportedAt = "2026-08-19 06:45 IST",
            hashMatches = 32,
            hashDbStatus = "Known Ransomware Signature (LockBit 3.0 variant)",
            locationHint = "Thiruvananthapuram Medical City",
            department = "Cyber Emergency Response Cell (CERT-KL)",
            assignedTo = "SI Ananya Nair (Badge #KL-CY-512)",
            suspectCount = 1,
            notes = "Patient records encrypted; ransom demanded in Monero (XMR). Decryption sample isolated.",
            events = listOf(
                Event("06:45", "Emergency intake received from hospital IT director", "INCIDENT"),
                Event("07:15", "Network segment isolated; forensic RAM dump captured", "TRIAGE"),
                Event("08:00", "Malware payload SHA-256 registered in Vault", "FORENSIC")
            ),
            entities = listOf("Wallet: 888tNk...xmr", "C2 Server: 185.220.101.5", "Malware: LockBit.variant.kl")
        ),
        Case(
            id = "ACPIA-2026-001",
            firNumber = "FIR No. 04/2026 u/s 67, 67A IT Act",
            title = "Deepfake Impersonation & Financial Swindle",
            status = "Closed",
            priority = CasePriority.MEDIUM,
            stage = CaseStage.COURT_TRIAL,
            platform = "WhatsApp Video / Telegram",
            reportedAt = "2026-07-28 11:00 IST",
            hashMatches = 4,
            hashDbStatus = "Court Admissible Certified",
            locationHint = "Alappuzha",
            department = "Cyber Crime Police Station, Alappuzha",
            assignedTo = "Insp. R. Chandran (Badge #KL-CY-409)",
            suspectCount = 1,
            notes = "Accused used AI voice clone of company director to authorize wire transfers. Chargesheet filed.",
            events = listOf(
                Event("11:00", "Complaint lodged with voice recording evidence", "INTAKE"),
                Event("15:30", "Audio forensic spectral analysis completed", "FORENSIC"),
                Event("17:00", "Final 65B Certificate generated for District Court", "TRIAL")
            ),
            entities = listOf("Suspect: P. V. Mathew", "Bank A/c: 409100293810", "Phone: +91 94471 00000")
        )
    )

    private val initialEvidence = listOf(
        Evidence(
            id = "EV-2026-8801",
            caseId = "ACPIA-2026-014",
            name = "Rogue_Loan_App_v2.4.apk",
            kind = "ANDROID_APK · 14.8 MB",
            count = "18 Perms · 4 C2 IPs",
            status = "Verified",
            type = "DEVICE",
            sha256Hash = "8f4b237c19a930de72958f0012bcfe39058b7654a9d701e3b5e40e21a8b940cd",
            fileSizeBytes = 15518924L,
            seizureLocation = "Suspect Phone Physical Seizure",
            verifiedByOfficer = "Insp. R. Chandran (Badge #KL-CY-409)",
            custodyLogs = listOf(
                CustodyRecord("2026-08-18 09:30 IST", "SI K. Varma", "KL-CY-451", "Seized and bagged in Faraday pouch", "Ernakulam Hub", "SEAL_8F4B237C19A930DE"),
                CustodyRecord("2026-08-18 10:15 IST", "Insp. R. Chandran", "KL-CY-409", "Extracted APK in Cyber Lab sandbox & Hashed", "Cyberdome Lab", "SEAL_90DE72958F0012BC")
            )
        ),
        Evidence(
            id = "EV-2026-8802",
            caseId = "ACPIA-2026-014",
            name = "Victim_Threat_WhatsApp_Export.txt",
            kind = "MESSAGE_LOG · 2.8 MB",
            count = "842 messages · 14 images",
            status = "Verified",
            type = "MESSAGE",
            sha256Hash = "3e23e8160039594a33894f6564e1b1348bbd7a0088d42c4acb73eeaed59c009d",
            fileSizeBytes = 2936012L,
            seizureLocation = "Victim Device Direct Export",
            verifiedByOfficer = "SI Ananya Nair (Badge #KL-CY-512)",
            custodyLogs = listOf(
                CustodyRecord("2026-08-18 09:45 IST", "SI Ananya Nair", "KL-CY-512", "Received via cryptographically signed portal", "Cyber Cell TVM", "SEAL_3E23E8160039594A")
            )
        ),
        Evidence(
            id = "EV-2026-8803",
            caseId = "ACPIA-2026-014",
            name = "C2_Network_Traffic_Capture.pcap",
            kind = "NETWORK_PCAP · 128 MB",
            count = "1.4M packets · 12 DNS queries",
            status = "Verified",
            type = "LOG",
            sha256Hash = "d4157e262f615c63860a4164d07a4501d21e8d60b1e59c1fa4ff78a0f6e311bc",
            fileSizeBytes = 134217728L,
            seizureLocation = "Gateway Sniffer Mirror Port",
            verifiedByOfficer = "Insp. R. Chandran (Badge #KL-CY-409)",
            custodyLogs = listOf(
                CustodyRecord("2026-08-18 11:00 IST", "Insp. R. Chandran", "KL-CY-409", "PCAP capture sealed with SHA-256", "Cyberdome Lab", "SEAL_D4157E262F615C63")
            )
        ),
        Evidence(
            id = "EV-2026-7704",
            caseId = "ACPIA-2026-009",
            name = "SIMBox_64Port_Flash_Dump.bin",
            kind = "DEVICE_ROM · 64 MB",
            count = "64 IMSI Records",
            status = "Under Review",
            type = "DEVICE",
            sha256Hash = "5a105e8b9d40e1329780d62ea2265d8a127ecd85ec56b9bf3141592653589793",
            fileSizeBytes = 67108864L,
            seizureLocation = "Kozhikode Hideout Raid",
            verifiedByOfficer = "DySP M. Suresh (Badge #KL-CY-102)",
            custodyLogs = listOf(
                CustodyRecord("2026-08-15 15:00 IST", "DySP M. Suresh", "KL-CY-102", "Dumped firmware via UART interface", "Forensic Van", "SEAL_5A105E8B9D40E132")
            )
        )
    )

    private val initialAuditLogs = listOf(
        AuditLogEntry(
            id = "AUD-9901",
            timestamp = "2026-08-20 18:40:12 IST",
            officerName = "Insp. R. Chandran",
            badgeNumber = "KL-CY-409",
            actionType = "HASH_VERIFICATION",
            details = "SHA-256 verification passed for Rogue_Loan_App_v2.4.apk against Central Forensic DB",
            tamperProofHash = "9d3004e0e29b11e2890b0123456789abcdef0123456789abcdef0123456789ab"
        ),
        AuditLogEntry(
            id = "AUD-9902",
            timestamp = "2026-08-20 17:15:00 IST",
            officerName = "SI Ananya Nair",
            badgeNumber = "KL-CY-512",
            actionType = "EVIDENCE_ACCESS",
            details = "Extracted 14 victim screenshots from case ACPIA-2026-014 for OCR entity extraction",
            tamperProofHash = "e87291a0c44b91f1827c1234567890abcdef1234567890abcdef1234567890ab"
        ),
        AuditLogEntry(
            id = "AUD-9903",
            timestamp = "2026-08-20 15:30:22 IST",
            officerName = "DySP M. Suresh",
            badgeNumber = "KL-CY-102",
            actionType = "EXPORT_REPORT",
            details = "Generated Section 65B Electronic Evidence Certificate for FIR No. 84/2026",
            tamperProofHash = "fa7630bc12984ef119041234567890abcdef1234567890abcdef1234567890ab"
        )
    )

    private val initialEntityNodes = listOf(
        EntityNode("N1", "Suspect: A. Sharma", "SUSPECT", 92, 0.5f, 0.45f),
        EntityNode("N2", "UPI: quickpay99@ibl", "WALLET", 88, 0.25f, 0.3f),
        EntityNode("N3", "IP: 103.24.18.91", "IP", 75, 0.75f, 0.25f),
        EntityNode("N4", "IMEI: 864920194820194", "DEVICE", 95, 0.35f, 0.75f),
        EntityNode("N5", "SIM: +91 98470 11223", "PHONE", 80, 0.68f, 0.7f),
        EntityNode("N6", "Server: loan-fast.top", "IP", 90, 0.85f, 0.55f)
    )

    private val initialEntityLinks = listOf(
        EntityLink("N1", "N2", "Mule Bank Linked"),
        EntityLink("N1", "N4", "Primary Phone Used"),
        EntityLink("N4", "N5", "SIM Inserted"),
        EntityLink("N1", "N3", "Admin Login IP"),
        EntityLink("N3", "N6", "C2 Control Server")
    )

    private val initialInsights = listOf(
        AiInsight(
            id = "AI-101",
            category = "CROSS_CASE_CORRELATION",
            headline = "Mule UPI ID matches FIR 44/2026 (Kochi)",
            confidence = 0.96f,
            summary = "The UPI ID 'quickpay99@ibl' flagged in this loan case received Rs. 4.2L from a phishing victim in another active investigation.",
            recommendedAction = "Issue freeze order u/s 102 CrPC to Bank Nodal Officer immediately."
        ),
        AiInsight(
            id = "AI-102",
            category = "INFRASTRUCTURE_DETECTION",
            headline = "C2 Domain hosted on Bulletproof Offshore ASN",
            confidence = 0.89f,
            summary = "Domain 'loan-fast.top' resolves to ASN 49870 known for bulletproof hosting. Fast-flux DNS changes detected every 3 hours.",
            recommendedAction = "Submit international emergency takedown request via CERT-In."
        ),
        AiInsight(
            id = "AI-103",
            category = "VICTIM_THREAT_ANALYSIS",
            headline = "High risk of secondary morphing extortion",
            confidence = 0.94f,
            summary = "Extracted WhatsApp chats show automated bot messages threatening photo manipulation using cloud contact sync.",
            recommendedAction = "Alert victim support liaison and preserve contact-list exfiltration logs."
        )
    )

    // -------------------------------------------------------------
    // Reactive StateFlow Containers
    // -------------------------------------------------------------
    private val _casesState = MutableStateFlow(initialCases)
    private val _evidenceState = MutableStateFlow(initialEvidence)
    private val _auditLogsState = MutableStateFlow(initialAuditLogs)
    private val _entityNodesState = MutableStateFlow(initialEntityNodes)
    private val _entityLinksState = MutableStateFlow(initialEntityLinks)
    private val _insightsState = MutableStateFlow(initialInsights)

    // -------------------------------------------------------------
    // ICaseRepository Implementation
    // -------------------------------------------------------------
    override fun getAllCases(): Flow<List<Case>> = _casesState.asStateFlow()

    override fun getCaseById(caseId: String): Flow<Case?> = _casesState.map { list ->
        list.find { it.id == caseId }
    }

    override suspend fun createCase(case: Case) {
        val updated = listOf(case) + _casesState.value
        _casesState.value = updated
        logAction(
            officerName = "Officer (Current)",
            badgeNumber = "KL-CY-PORTAL",
            actionType = "CASE_CREATED",
            details = "Created Case ${case.id} (${case.title}) - Priority: ${case.priority.label}"
        )
    }

    override suspend fun updateCaseStatus(caseId: String, status: String, stage: CaseStage) {
        _casesState.value = _casesState.value.map {
            if (it.id == caseId) it.copy(status = status, stage = stage) else it
        }
        logAction(
            officerName = "Officer (Current)",
            badgeNumber = "KL-CY-PORTAL",
            actionType = "CASE_UPDATE",
            details = "Updated Case $caseId to stage ${stage.label} ($status)"
        )
    }

    override suspend fun addEventToCase(caseId: String, event: Event) {
        _casesState.value = _casesState.value.map {
            if (it.id == caseId) it.copy(events = listOf(event) + it.events) else it
        }
    }

    // -------------------------------------------------------------
    // IEvidenceRepository Implementation
    // -------------------------------------------------------------
    override fun getAllEvidence(): Flow<List<Evidence>> = _evidenceState.asStateFlow()

    override fun getEvidenceForCase(caseId: String): Flow<List<Evidence>> = _evidenceState.map { list ->
        list.filter { it.caseId == caseId }
    }

    override suspend fun addEvidence(evidence: Evidence) {
        _evidenceState.value = listOf(evidence) + _evidenceState.value
        logAction(
            officerName = evidence.verifiedByOfficer.ifBlank { "Officer (Current)" },
            badgeNumber = "KL-CY-PORTAL",
            actionType = "EVIDENCE_INGESTED",
            details = "Ingested ${evidence.name} into Case ${evidence.caseId} [SHA256: ${evidence.sha256Hash.take(12)}...]"
        )
    }

    override suspend fun updateEvidenceStatus(evidenceId: String, status: String) {
        _evidenceState.value = _evidenceState.value.map {
            if (it.id == evidenceId) it.copy(status = status) else it
        }
    }

    override suspend fun appendCustodyLog(evidenceId: String, record: CustodyRecord) {
        _evidenceState.value = _evidenceState.value.map {
            if (it.id == evidenceId) it.copy(custodyLogs = it.custodyLogs + record) else it
        }
        logAction(
            officerName = record.officerName,
            badgeNumber = record.badgeNumber,
            actionType = "CUSTODY_TRANSFER",
            details = "Evidence $evidenceId: ${record.action} at ${record.location}"
        )
    }

    override suspend fun verifyEvidenceHash(evidenceId: String, samplePayload: String): Boolean {
        val item = _evidenceState.value.find { it.id == evidenceId } ?: return false
        val computed = SecurityCryptoManager.computeSha256(samplePayload)
        val matches = computed.equals(item.sha256Hash, ignoreCase = true)
        logAction(
            officerName = "Officer (Current)",
            badgeNumber = "KL-CY-PORTAL",
            actionType = "HASH_VERIFICATION",
            details = "Integrity check for $evidenceId (${item.name}): ${if (matches) "PASSED (Integrity Preserved)" else "FAILED (Tampering Detected)"}"
        )
        return matches
    }

    // -------------------------------------------------------------
    // IAuditRepository Implementation
    // -------------------------------------------------------------
    override fun getAuditLogs(): Flow<List<AuditLogEntry>> = _auditLogsState.asStateFlow()

    override suspend fun logAction(
        officerName: String,
        badgeNumber: String,
        actionType: String,
        details: String
    ) {
        val id = "AUD-${System.currentTimeMillis().toString().takeLast(6)}"
        val timestamp = SecurityCryptoManager.getFormattedTimestamp()
        val prevHash = _auditLogsState.value.firstOrNull()?.tamperProofHash ?: "GENESIS"
        val hash = SecurityCryptoManager.generateAuditHash(id, timestamp, badgeNumber, actionType, prevHash)

        val entry = AuditLogEntry(
            id = id,
            timestamp = timestamp,
            officerName = officerName,
            badgeNumber = badgeNumber,
            actionType = actionType,
            details = details,
            tamperProofHash = hash
        )
        _auditLogsState.value = listOf(entry) + _auditLogsState.value
    }

    // -------------------------------------------------------------
    // IEntityGraphRepository Implementation
    // -------------------------------------------------------------
    override fun getEntityNodes(): Flow<List<EntityNode>> = _entityNodesState.asStateFlow()
    override fun getEntityLinks(): Flow<List<EntityLink>> = _entityLinksState.asStateFlow()

    // -------------------------------------------------------------
    // IAiAssistantRepository Implementation
    // -------------------------------------------------------------
    override fun getActiveInsights(): Flow<List<AiInsight>> = _insightsState.asStateFlow()

    override suspend fun queryCopilot(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            "mule" in lower || "bank" in lower || "upi" in lower -> {
                "AI Analysis: UPI ID 'quickpay99@ibl' is associated with 3 active FIRs across Kochi, Thrissur and Bengaluru. Total inflow in last 48 hours: Rs. 14,80,000. Recommend immediate Section 91 CrPC notice to Axis/ICICI Bank nodal officers."
            }
            "hash" in lower || "apk" in lower || "malware" in lower -> {
                "Forensic Engine: 'Rogue_Loan_App_v2.4.apk' (SHA-256: 8f4b...40cd) matches Intel threat cluster APT-SEA-FIN09. It exfiltrates Contacts, SMS inbox, and IMEI to C2 server 103.24.18.91 port 8080."
            }
            "65b" in lower || "court" in lower || "evidence" in lower -> {
                "Legal Co-Pilot: Section 65B(4) Certificate template pre-populated for FIR No. 84/2026. All 4 digital evidence items have verified SHA-256 hash chains with unbroken custody timestamps."
            }
            "suspect" in lower || "who" in lower || "lead" in lower -> {
                "Suspect Intelligence: Primary suspect handle 'Sharma_KL_Hub' correlated with tower dump at Kochi InfoPark on 2026-08-18 09:30 IST. 2 alternate SIMs identified."
            }
            else -> {
                "Cyberdome AI: Processed inquiry against active intelligence database. 4 cases, 6 evidence packages, and 6 correlated entities reviewed. All systems operating under lawful interception and custody protocols."
            }
        }
    }
}
