package com.example.acpia.core.data

import com.example.acpia.model.*
import kotlinx.coroutines.flow.Flow

interface ICaseRepository {
    fun getAllCases(): Flow<List<Case>>
    fun getCaseById(caseId: String): Flow<Case?>
    suspend fun createCase(case: Case)
    suspend fun updateCaseStatus(caseId: String, status: String, stage: CaseStage)
    suspend fun addEventToCase(caseId: String, event: Event)
}

interface IEvidenceRepository {
    fun getAllEvidence(): Flow<List<Evidence>>
    fun getEvidenceForCase(caseId: String): Flow<List<Evidence>>
    suspend fun addEvidence(evidence: Evidence)
    suspend fun updateEvidenceStatus(evidenceId: String, status: String)
    suspend fun appendCustodyLog(evidenceId: String, record: CustodyRecord)
    suspend fun verifyEvidenceHash(evidenceId: String, samplePayload: String): Boolean
}

interface IAuditRepository {
    fun getAuditLogs(): Flow<List<AuditLogEntry>>
    suspend fun logAction(officerName: String, badgeNumber: String, actionType: String, details: String)
}

interface IEntityGraphRepository {
    fun getEntityNodes(): Flow<List<EntityNode>>
    fun getEntityLinks(): Flow<List<EntityLink>>
}

interface IAiAssistantRepository {
    fun getActiveInsights(): Flow<List<AiInsight>>
    suspend fun queryCopilot(prompt: String): String
}
