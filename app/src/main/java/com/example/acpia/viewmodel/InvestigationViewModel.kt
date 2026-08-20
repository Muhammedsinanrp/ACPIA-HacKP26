package com.example.acpia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acpia.core.data.InMemoryDataStore
import com.example.acpia.core.security.SecurityCryptoManager
import com.example.acpia.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String, // "OFFICER" or "AI"
    val text: String,
    val timestamp: String = SecurityCryptoManager.getShortTime()
)

class InvestigationViewModel(
    private val dataStore: InMemoryDataStore = InMemoryDataStore()
) : ViewModel() {

    // -------------------------------------------------------------
    // UI State
    // -------------------------------------------------------------
    val cases: StateFlow<List<Case>> = dataStore.getAllCases()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val evidenceList: StateFlow<List<Evidence>> = dataStore.getAllEvidence()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val auditLogs: StateFlow<List<AuditLogEntry>> = dataStore.getAuditLogs()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val entityNodes: StateFlow<List<EntityNode>> = dataStore.getEntityNodes()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val entityLinks: StateFlow<List<EntityLink>> = dataStore.getEntityLinks()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val aiInsights: StateFlow<List<AiInsight>> = dataStore.getActiveInsights()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectedCaseId = MutableStateFlow("ACPIA-2026-014")
    val selectedCaseId: StateFlow<String> = _selectedCaseId.asStateFlow()

    val selectedCase: StateFlow<Case?> = combine(cases, _selectedCaseId) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow("ALL")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage("AI", "Greetings Officer. Cyber Intelligence Co-Pilot is active. Ask about cross-case entity matches, malware hashes, or Section 65B drafting.")
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    // -------------------------------------------------------------
    // Actions
    // -------------------------------------------------------------
    fun selectCase(caseId: String) {
        _selectedCaseId.value = caseId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(filter: String) {
        _statusFilter.value = filter
    }

    fun createNewCase(
        title: String,
        firNumber: String,
        priority: CasePriority,
        platform: String,
        location: String,
        notes: String
    ) {
        viewModelScope.launch {
            val newId = "ACPIA-2026-0" + (cases.value.size + 15)
            val newCase = Case(
                id = newId,
                firNumber = firNumber.ifBlank { "FIR Pending Registration" },
                title = title,
                status = "Active",
                priority = priority,
                stage = CaseStage.INTAKE,
                platform = platform.ifBlank { "General Cyber Infrastructure" },
                reportedAt = SecurityCryptoManager.getFormattedTimestamp(),
                locationHint = location.ifBlank { "Kerala State" },
                notes = notes,
                assignedTo = "Insp. R. Chandran (Badge #KL-CY-409)",
                events = listOf(
                    Event(SecurityCryptoManager.getShortTime(), "Case registered and seal created", "INTAKE")
                )
            )
            dataStore.createCase(newCase)
            _selectedCaseId.value = newId
        }
    }

    fun updateCaseStage(caseId: String, newStage: CaseStage, status: String) {
        viewModelScope.launch {
            dataStore.updateCaseStatus(caseId, status, newStage)
            dataStore.addEventToCase(
                caseId,
                Event(
                    SecurityCryptoManager.getShortTime(),
                    "Stage progressed to ${newStage.label} ($status)",
                    "PROGRESSION"
                )
            )
        }
    }

    fun addEvidence(
        caseId: String,
        name: String,
        type: String,
        rawPayloadForHash: String,
        seizureLocation: String
    ) {
        viewModelScope.launch {
            val count = evidenceList.value.size + 1
            val evidId = "EV-2026-" + (8800 + count)
            val computedHash = SecurityCryptoManager.computeSha256(rawPayloadForHash.ifBlank { name + System.currentTimeMillis() })
            val timestamp = SecurityCryptoManager.getFormattedTimestamp()
            val seal = SecurityCryptoManager.generateChainOfCustodySeal(evidId, "KL-CY-409", timestamp, "INTAKE_SEAL")

            val newEvidence = Evidence(
                id = evidId,
                caseId = caseId,
                name = name,
                kind = "$type · Sealed",
                count = "Verified Initial Hash",
                status = "Verified",
                type = type,
                sha256Hash = computedHash,
                fileSizeBytes = (rawPayloadForHash.toByteArray().size * 1024L).coerceAtLeast(1024L * 512L),
                seizureLocation = seizureLocation.ifBlank { "Cyber Police Station Seizure" },
                verifiedByOfficer = "Insp. R. Chandran (Badge #KL-CY-409)",
                custodyLogs = listOf(
                    CustodyRecord(
                        timestamp = timestamp,
                        officerName = "Insp. R. Chandran",
                        badgeNumber = "KL-CY-409",
                        action = "Initial forensic intake and SHA-256 generation",
                        location = seizureLocation.ifBlank { "Cyber Police Station" },
                        hashSignature = seal
                    )
                )
            )
            dataStore.addEvidence(newEvidence)
        }
    }

    fun addCustodyTransfer(
        evidenceId: String,
        officerName: String,
        badge: String,
        action: String,
        location: String
    ) {
        viewModelScope.launch {
            val timestamp = SecurityCryptoManager.getFormattedTimestamp()
            val seal = SecurityCryptoManager.generateChainOfCustodySeal(evidenceId, badge, timestamp, action)
            val record = CustodyRecord(
                timestamp = timestamp,
                officerName = officerName,
                badgeNumber = badge,
                action = action,
                location = location,
                hashSignature = seal
            )
            dataStore.appendCustodyLog(evidenceId, record)
        }
    }

    fun sendAiPrompt(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = ChatMessage("OFFICER", prompt)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isAiThinking.value = true
            val responseText = dataStore.queryCopilot(prompt)
            val aiMsg = ChatMessage("AI", responseText)
            _chatMessages.value = _chatMessages.value + aiMsg
            _isAiThinking.value = false
        }
    }

    fun generateSection65BCertificate(case: Case, relatedEvidence: List<Evidence>): String {
        val dateStr = SecurityCryptoManager.getFormattedTimestamp()
        val evidenceEntries = relatedEvidence.joinToString("\n\n") { ev ->
            """
            ITEM: ${ev.name} [ID: ${ev.id}]
            TYPE: ${ev.type} | SEIZED AT: ${ev.seizureLocation}
            SHA-256 HASH: ${ev.sha256Hash}
            CHAIN OF CUSTODY ENTRIES: ${ev.custodyLogs.size}
            LATEST SEAL: ${ev.custodyLogs.lastOrNull()?.hashSignature ?: "SEALED"}
            """.trimIndent()
        }

        return """
        ========================================================================
        CERTIFICATE UNDER SECTION 65B(4) OF THE INDIAN EVIDENCE ACT, 1872
        ========================================================================
        KERALA STATE POLICE · CYBER OPERATIONS WING
        
        CASE ID     : ${case.id}
        FIR NUMBER  : ${case.firNumber}
        TITLE       : ${case.title}
        INVESTIGATOR: ${case.assignedTo}
        ISSUED ON   : $dateStr
        
        I, the undersigned Investigating / Forensic Officer, hereby certify that:
        1. The electronic records referenced herein were produced by electronic devices 
           operating properly during the lawful course of investigation.
        2. The cryptographic hash values (SHA-256) were generated immediately upon acquisition
           and verified against the state central tamper-evident registry.
        3. The integrity and chain-of-custody have remained intact without modification.
        
        --- SCHEDULE OF ELECTRONIC EVIDENCE ---
        $evidenceEntries
        
        ------------------------------------------------------------------------
        VERIFICATION SEAL: ${SecurityCryptoManager.computeSha256(case.id + dateStr).take(32).uppercase()}
        OFFICER SIGNATURE: [Digitally Sealed & Certified]
        ========================================================================
        """.trimIndent()
    }
}
