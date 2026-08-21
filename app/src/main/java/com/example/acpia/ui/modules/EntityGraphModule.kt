package com.example.acpia.ui.modules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.acpia.model.EntityNode
import com.example.acpia.viewmodel.InvestigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntityGraphModule(viewModel: InvestigationViewModel) {
    val nodes by viewModel.entityNodes.collectAsState()
    val links by viewModel.entityLinks.collectAsState()

    var selectedNodeId by remember { mutableStateOf(nodes.firstOrNull()?.id) }
    var entityTypeFilter by remember { mutableStateOf("ALL") }

    val activeNode = nodes.find { it.id == selectedNodeId } ?: nodes.firstOrNull()

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Left Area: Interactive Visual Node Graph Canvas
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.weight(1.5f).fillMaxHeight()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Header with filters
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("Relationship Intelligence Graph", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Multi-hop link analysis (Suspects · Phones · IPs · Wallets)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("ALL" to "All", "SUSPECT" to "Suspects", "WALLET" to "UPI/Crypto", "IP" to "Servers").forEach { (k, label) ->
                            FilterChip(
                                selected = entityTypeFilter == k,
                                onClick = { entityTypeFilter = k },
                                label = { Text(label, fontSize = 10.sp) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Canvas Container
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height

                        // Draw Connections
                        links.forEach { link ->
                            val source = nodes.find { it.id == link.fromId }
                            val target = nodes.find { it.id == link.toId }
                            if (source != null && target != null) {
                                val p1 = Offset(source.xRatio * w, source.yRatio * h)
                                val p2 = Offset(target.xRatio * w, target.yRatio * h)

                                drawLine(
                                    color = Color(0xFFBDD7E8),
                                    start = p1,
                                    end = p2,
                                    strokeWidth = 3f
                                )
                            }
                        }

                        // Draw Nodes
                        nodes.forEach { node ->
                            val pos = Offset(node.xRatio * w, node.yRatio * h)
                            val isSelected = node.id == activeNode?.id

                            val nodeColor = when (node.type) {
                                "SUSPECT" -> Color(0xFFFF4D4F)
                                "WALLET" -> Color(0xFFF49E0B)
                                "IP" -> Color(0xFF1F8ED6)
                                "DEVICE" -> Color(0xFF7A6CF1)
                                else -> Color(0xFF25B07B)
                            }

                            // Halo if selected
                            if (isSelected) {
                                drawCircle(
                                    color = nodeColor.copy(alpha = 0.25f),
                                    radius = 32f,
                                    center = pos
                                )
                            }

                            // Main circle
                            drawCircle(
                                color = Color.White,
                                radius = 20f,
                                center = pos
                            )
                            drawCircle(
                                color = nodeColor,
                                radius = 18f,
                                center = pos
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 6f,
                                center = pos
                            )
                        }
                    }

                    // Node Labels Overlay
                    nodes.forEach { node ->
                        val isSelected = node.id == activeNode?.id
                        Box(
                            modifier = Modifier
                                .offset(
                                    x = ((node.xRatio * 450) - 40).dp,
                                    y = ((node.yRatio * 320) + 16).dp
                                )
                                .clickable { selectedNodeId = node.id }
                        ) {
                            Surface(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
                                shape = RoundedCornerShape(6.dp),
                                shadowElevation = 3.dp,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Text(
                                    node.label,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Right Area: Selected Entity Inspector
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.weight(1.0f).fillMaxHeight()
        ) {
            activeNode?.let { node ->
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(node.type, fontSize = 11.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Surface(
                            color = if (node.riskScore > 80) Color(0xFFFF4D4F).copy(alpha = 0.15f) else Color(0xFFF49E0B).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "Risk Score: ${node.riskScore}/100",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (node.riskScore > 80) Color(0xFFFF4D4F) else Color(0xFFF49E0B),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(node.label, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 4.dp))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("CONNECTED RELATIONSHIPS", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    val connectedLinks = links.filter { it.fromId == node.id || it.toId == node.id }
                    if (connectedLinks.isEmpty()) {
                        Text("No immediate links mapped.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            connectedLinks.forEach { link ->
                                val target = if (link.fromId == node.id) nodes.find { it.id == link.toId } else nodes.find { it.id == link.fromId }
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(link.relationship, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                        Text(target?.label ?: "Unknown Entity", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Text("RECOMMENDED POLICE ACTION", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                when (node.type) {
                                    "WALLET" -> "Send Section 91 CrPC notice to Payment Aggregator to freeze funds and retrieve KYC logs."
                                    "SUSPECT" -> "Execute coordinated raid warrant and seize primary communication devices under Faraday protection."
                                    "IP" -> "Serve preservation request to ISP nodal team for NAT translation and subscriber IPDR."
                                    else -> "Cross-reference IMEI with CEIR portal for active SIM card replacements."
                                },
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Select an entity node to inspect links.")
            }
        }
    }
}
