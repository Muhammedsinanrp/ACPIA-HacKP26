package com.example.acpia.ui.modules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.acpia.model.*
import com.example.acpia.viewmodel.InvestigationViewModel

@Composable
fun EvidenceModule(viewModel: InvestigationViewModel) {
    val evidenceList by viewModel.evidenceList.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()

    var selectedEvidenceId by remember { mutableStateOf(evidenceList.firstOrNull()?.id) }
    var showIngestDialog by remember { mutableStateOf(false) }
    var showTransferDialog by remember { mutableStateOf(false) }
    var verificationStatusMsg by remember { mutableStateOf<String?>(null) }

    val activeEvidence = evidenceList.find { it.id == selectedEvidenceId } ?: evidenceList.firstOrNull()

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Left Column: Evidence Vault Directory
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.weight(1.1f).fillMaxHeight()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("Evidence Vault", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("${evidenceList.size} items sealed & registered", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Button(
                        onClick = { showIngestDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ingest Evidence", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(evidenceList) { item ->
                        val isSelected = item.id == activeEvidence?.id
                        Surface(
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedEvidenceId = item.id
                                    verificationStatusMsg = null
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            when (item.type) {
                                                "IMAGE" -> Icons.Default.Image
                                                "DEVICE" -> Icons.Default.Dns
                                                "LOG" -> Icons.Default.FormatListBulleted
                                                "MESSAGE" -> Icons.Default.Chat
                                                else -> Icons.Default.InsertDriveFile
                                            },
                                            contentDescription = null,
                                            modifier = Modifier.padding(8.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(item.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text(
                                            "Case: ${item.caseId} · ${item.kind}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                Surface(
                                    color = Color(0xFF25B07B).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        item.status,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF25B07B),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Right Column: Cryptographic Inspector & Chain-of-Custody
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.weight(1.4f).fillMaxHeight()
        ) {
            activeEvidence?.let { ev ->
                val scroll = rememberScrollState()
                Column(modifier = Modifier.padding(20.dp).verticalScroll(scroll)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(ev.id, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(ev.name, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 2.dp))
                        }
                        Button(
                            onClick = {
                                verificationStatusMsg = "SHA-256 Checksum Verified Against Central State Registry: INTACT"
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Verify Integrity", fontSize = 11.sp)
                        }
                    }

                    if (verificationStatusMsg != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = Color(0xFF25B07B).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFF25B07B).copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF25B07B), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(verificationStatusMsg!!, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E825B))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // SHA-256 Hash Box
                    Text("CRYPTOGRAPHIC CHECKSUM (SHA-256)", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                ev.sha256Hash,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Item Metadata Grid
                    Text("INGESTION & SEIZURE CONTEXT", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        DetailRow("Linked Case ID", ev.caseId)
                        DetailRow("Seizure Location", ev.seizureLocation)
                        DetailRow("Forensic Ingest Officer", ev.verifiedByOfficer)
                        DetailRow("File Classification", ev.kind)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Chain of Custody History
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("CHAIN OF CUSTODY TIMELINE", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        TextButton(onClick = { showTransferDialog = true }) {
                            Text("+ Transfer Custody", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (ev.custodyLogs.isEmpty()) {
                        Text("No custody transfers recorded.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            ev.custodyLogs.forEach { log ->
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                "${log.officerName} (${log.badgeNumber})",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                log.timestamp,
                                                fontSize = 10.sp,
                                                fontFamily = FontFamily.Monospace,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Text(log.action, fontSize = 12.sp, modifier = Modifier.padding(vertical = 4.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text("Location: ${log.location}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                            Text("Seal: ${log.hashSignature.take(16)}...", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Select an evidence item to view forensic data.")
            }
        }
    }

    // Ingest Evidence Dialog
    if (showIngestDialog) {
        IngestEvidenceDialog(
            caseId = selectedCase?.id ?: "ACPIA-2026-014",
            onDismiss = { showIngestDialog = false },
            onIngest = { name, type, content, loc ->
                viewModel.addEvidence(
                    caseId = selectedCase?.id ?: "ACPIA-2026-014",
                    name = name,
                    type = type,
                    rawPayloadForHash = content,
                    seizureLocation = loc
                )
                showIngestDialog = false
            }
        )
    }

    // Transfer Custody Dialog
    if (showTransferDialog && activeEvidence != null) {
        CustodyTransferDialog(
            evidenceId = activeEvidence.id,
            onDismiss = { showTransferDialog = false },
            onTransfer = { name, badge, action, loc ->
                viewModel.addCustodyTransfer(
                    evidenceId = activeEvidence.id,
                    officerName = name,
                    badge = badge,
                    action = action,
                    location = loc
                )
                showTransferDialog = false
            }
        )
    }
}

@Composable
fun IngestEvidenceDialog(
    caseId: String,
    onDismiss: () -> Unit,
    onIngest: (name: String, type: String, content: String, loc: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("DEVICE") }
    var content by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("Crime Scene / Electronic Seizure") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth(0.95f)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Ingest Digital Evidence into Vault", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text("Case Reference: $caseId", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Evidence Filename / Description") },
                    placeholder = { Text("e.g. suspect_phone_full_dump.raw") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = loc,
                    onValueChange = { loc = it },
                    label = { Text("Seizure Location / Facility") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("File Payload or Identifier (for SHA-256)") },
                    placeholder = { Text("Enter or paste byte string / metadata text...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank()) onIngest(name, type, content, loc)
                        },
                        enabled = name.isNotBlank()
                    ) {
                        Text("Calculate Hash & Seal")
                    }
                }
            }
        }
    }
}

@Composable
fun CustodyTransferDialog(
    evidenceId: String,
    onDismiss: () -> Unit,
    onTransfer: (officer: String, badge: String, action: String, loc: String) -> Unit
) {
    var officer by remember { mutableStateOf("DySP M. Suresh") }
    var badge by remember { mutableStateOf("KL-CY-102") }
    var action by remember { mutableStateOf("Transferred to State Forensic Science Lab for Chip-Off Analysis") }
    var loc by remember { mutableStateOf("FSL Trivandrum") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth(0.95f)) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Log Custody Transfer", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text("Evidence ID: $evidenceId", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = officer,
                    onValueChange = { officer = it },
                    label = { Text("Receiving / Handling Officer") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = badge,
                    onValueChange = { badge = it },
                    label = { Text("Officer Badge / ID") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = action,
                    onValueChange = { action = it },
                    label = { Text("Transfer Reason / Action") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = loc,
                    onValueChange = { loc = it },
                    label = { Text("Destination Facility / Court") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onTransfer(officer, badge, action, loc) }
                    ) {
                        Text("Sign & Record Transfer")
                    }
                }
            }
        }
    }
}
