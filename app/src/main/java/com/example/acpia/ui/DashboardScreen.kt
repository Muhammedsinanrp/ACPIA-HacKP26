package com.example.acpia.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.acpia.ui.modules.*
import com.example.acpia.viewmodel.InvestigationViewModel

data class NavItem(
    val key: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun DashboardScreen(
    viewModel: InvestigationViewModel,
    onLogout: () -> Unit
) {
    var activeView by remember { mutableStateOf("overview") }
    val selectedCase by viewModel.selectedCase.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val navItems = listOf(
        NavItem("overview", "Overview", Icons.Default.Dashboard),
        NavItem("cases", "Cases & FIRs", Icons.Default.FolderSpecial),
        NavItem("evidence", "Evidence Vault", Icons.Default.Storage),
        NavItem("graph", "Entity Graph", Icons.Default.Hub),
        NavItem("ai", "AI Co-Pilot", Icons.Default.AutoAwesome),
        NavItem("reports", "Court Reports & 65B", Icons.Default.Description),
        NavItem("services", "Cyberdome Services", Icons.Default.Shield),
        NavItem("training", "Legal & SOPs", Icons.Default.MenuBook),
        NavItem("contact", "Directory", Icons.Default.SupportAgent),
        NavItem("settings", "Settings", Icons.Default.Settings)
    )

    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar Navigation
        Column(
            modifier = Modifier
                .width(260.dp)
                .fillMaxHeight()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0D283E), Color(0xFF113854))
                    )
                )
                .padding(20.dp)
        ) {
            // App Logo/Badge
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 20.dp)) {
                Surface(
                    color = Color.White.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("ACPIA PORTAL", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
                    Text("Kerala Cyber Police", fontSize = 11.sp, color = Color.White.copy(alpha = 0.75f), fontFamily = FontFamily.Monospace)
                }
            }

            // Security Status Chip
            Surface(
                color = Color.White.copy(alpha = 0.1f),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                modifier = Modifier.padding(bottom = 18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(6.dp).background(Color(0xFF25B07B), CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("SHA-256 Vault Online", fontSize = 10.sp, color = Color.White, fontFamily = FontFamily.Monospace)
                }
            }

            Text("COMMAND MODULES", fontSize = 10.sp, fontFamily = FontFamily.Monospace, letterSpacing = 1.sp, color = Color.White.copy(alpha = 0.55f))
            Spacer(modifier = Modifier.height(8.dp))

            // Navigation Links
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                navItems.forEach { item ->
                    val isSelected = activeView == item.key
                    TextButton(
                        onClick = { activeView = item.key },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isSelected) Color.White.copy(alpha = 0.16f) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                item.icon,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                item.label,
                                fontSize = 12.sp,
                                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.82f),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Footer Badge
            Surface(
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.tertiary)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Court Admissible Environment", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Text(
                        "Section 65B Evidence Act compliant sealing.",
                        fontSize = 9.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Main Content Area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFF4FAFF), Color(0xFFE9F4FC))
                    )
                )
                .padding(20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "ACTIVE: ${selectedCase?.id ?: "ALL FILES"} · ${selectedCase?.firNumber ?: "Cyber Operations"}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        navItems.find { it.key == activeView }?.label ?: "Investigation Command",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = { Text("Search case, suspect, or hash...", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .width(280.dp)
                            .height(48.dp)
                            .padding(end = 12.dp)
                    )

                    Button(
                        onClick = onLogout,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sign Out", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Module View Router
            Box(modifier = Modifier.fillMaxSize()) {
                when (activeView) {
                    "overview" -> OverviewModule(viewModel, onNavigateTo = { activeView = it })
                    "cases" -> CasesModule(viewModel, onNavigateToEvidence = { activeView = "evidence" })
                    "evidence" -> EvidenceModule(viewModel)
                    "graph" -> EntityGraphModule(viewModel)
                    "ai" -> AiAssistantModule(viewModel)
                    "reports" -> ReportsModule(viewModel)
                    "services" -> ServicesModule()
                    "training" -> TrainingModule()
                    "contact" -> ContactModule()
                    "settings" -> SettingsModule()
                    else -> OverviewModule(viewModel, onNavigateTo = { activeView = it })
                }
            }
        }
    }
}
