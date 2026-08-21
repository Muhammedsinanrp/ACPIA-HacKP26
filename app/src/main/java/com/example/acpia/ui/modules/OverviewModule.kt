package com.example.acpia.ui.modules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.acpia.model.*
import com.example.acpia.viewmodel.InvestigationViewModel

@Composable
fun OverviewModule(
    viewModel: InvestigationViewModel,
    onNavigateTo: (String) -> Unit
) {
    val cases by viewModel.cases.collectAsState()
    val evidenceList by viewModel.evidenceList.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()
    val aiInsights by viewModel.aiInsights.collectAsState()
    val nodes by viewModel.entityNodes.collectAsState()

    val scrollState = rememberScrollState()
    val configuration = LocalConfiguration.current
    val isMobile = configuration.screenWidthDp < 600

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dynamic KPI Stats Row
        if (isMobile) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        label = "ACTIVE CASES",
                        value = "${cases.count { it.status != "Closed" }}",
                        detail = "In jurisdiction",
                        color = 0xFF1F8ED6,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "SEALED EVIDENCE",
                        value = "${evidenceList.size}",
                        detail = "SHA-256 Verified",
                        color = 0xFF25B07B,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(
                        label = "ENTITIES",
                        value = "${nodes.size}",
                        detail = "Mapped links",
                        color = 0xFF7A6CF1,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = "AI ALERTS",
                        value = "${aiInsights.size}",
                        detail = "Threat matches",
                        color = 0xFFFF6B5F,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    label = "ACTIVE CASES",
                    value = "${cases.count { it.status != "Closed" }}",
                    detail = "${cases.size} total in jurisdiction",
                    color = 0xFF1F8ED6,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "SEALED EVIDENCE",
                    value = "${evidenceList.size}",
                    detail = "100% SHA-256 Verified",
                    color = 0xFF25B07B,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "ENTITIES CORRELATED",
                    value = "${nodes.size}",
                    detail = "Phones, IPs, UPI & IMEIs",
                    color = 0xFF7A6CF1,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label = "AI THREAT ALERTS",
                    value = "${aiInsights.size}",
                    detail = "Cross-case matches",
                    color = 0xFFFF6B5F,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Adaptive content layout
        if (isMobile) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                CaseSpotlight(selectedCase, evidenceList, onNavigateTo, isMobile)
                CyberdomePanel(isMobile)
                EntityCorrelationPreview(nodes, onNavigateTo, isMobile)
                TimelineFeed(selectedCase, isMobile)
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1.3f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CaseSpotlight(selectedCase, evidenceList, onNavigateTo, isMobile)
                    EntityCorrelationPreview(nodes, onNavigateTo, isMobile)
                }
                Column(modifier = Modifier.weight(0.7f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    CyberdomePanel(isMobile)
                    TimelineFeed(selectedCase, isMobile)
                }
            }
        }
    }
}

@Composable
fun CaseSpotlight(
    selectedCase: Case?,
    evidenceList: List<Evidence>,
    onNavigateTo: (String) -> Unit,
    isMobile: Boolean
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(if (isMobile) 16.dp else 20.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(selectedCase?.priority?.colorHex ?: 0xFF1F8ED6).copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "PRIORITY: ${selectedCase?.priority?.label ?: "HIGH"}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(selectedCase?.priority?.colorHex ?: 0xFF1F8ED6),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            selectedCase?.firNumber ?: "FIR Pending",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        selectedCase?.title ?: "Select a Case",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
                if (!isMobile) {
                    Button(
                        onClick = { onNavigateTo("cases") },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Manage Case", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                selectedCase?.notes ?: "No case description provided.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isMobile) {
                Button(
                    onClick = { onNavigateTo("cases") },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Manage Investigation File", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Quick Action Link Row
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                QuickActionPill(
                    icon = Icons.Default.FolderSpecial,
                    label = "Evidence",
                    count = "${evidenceList.count { it.caseId == selectedCase?.id }}",
                    onClick = { onNavigateTo("evidence") },
                    modifier = Modifier.weight(1f)
                )
                QuickActionPill(
                    icon = Icons.Default.Share,
                    label = "Graph",
                    count = "Links",
                    onClick = { onNavigateTo("graph") },
                    modifier = Modifier.weight(1f)
                )
                QuickActionPill(
                    icon = Icons.Default.Description,
                    label = "Cert",
                    count = "65B",
                    onClick = { onNavigateTo("reports") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CyberdomePanel(isMobile: Boolean) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(if (isMobile) 16.dp else 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Shield,
                        contentDescription = null,
                        modifier = Modifier.padding(8.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Kerala Cyberdome", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    Text("Operational Node", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Unified digital intelligence platform ensuring chain-of-custody preservation and fast correlation across interstate cyber syndicates.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color(0xFF25B07B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hash Registry: SYNCED", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun EntityCorrelationPreview(nodes: List<EntityNode>, onNavigateTo: (String) -> Unit, isMobile: Boolean) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(if (isMobile) 16.dp else 18.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Hub, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Correlated Entities", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                }
                TextButton(onClick = { onNavigateTo("graph") }) {
                    Text("Open Graph →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                nodes.take(if (isMobile) 3 else 4).forEach { node ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            if (node.riskScore > 85) Color(0xFFFF4D4F) else Color(0xFFF49E0B),
                                            CircleShape
                                        )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(node.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Text(
                                "Risk: ${node.riskScore}%",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineFeed(selectedCase: Case?, isMobile: Boolean) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(if (isMobile) 16.dp else 18.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Investigation Timeline", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(6.dp).background(Color(0xFF25B07B), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Live", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF25B07B))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val events = selectedCase?.events ?: emptyList()
            if (events.isEmpty()) {
                Text("No timeline events logged yet.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    events.take(if (isMobile) 5 else Int.MAX_VALUE).forEach { entry ->
                        Row(verticalAlignment = Alignment.Top) {
                            Text(
                                entry.t,
                                modifier = Modifier.width(50.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .padding(top = 4.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(entry.label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, detail: String, color: Long, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, color = Color(color), modifier = Modifier.padding(top = 4.dp))
            Text(detail, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
        }
    }
}

@Composable
fun QuickActionPill(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, count: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(15.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(6.dp))
                Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Text(count, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
        }
    }
}
