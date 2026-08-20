package com.example.acpia.ui.modules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.acpia.model.Service

@Composable
fun ServicesModule() {
    val siteServices = listOf(
        Service("Cyber Crime Prevention", "Building a resilient ecosystem for safer digital public spaces in Kerala.", Icons.Default.Shield),
        Service("Public-Private Partnership", "Connecting government, academia, industry and ethical experts under one network.", Icons.Default.NetworkCheck),
        Service("Technology Augmentation", "Using AI, digital forensics and modern tools to strengthen investigation capacity.", Icons.Default.Memory),
        Service("Cyber Crime Investigation", "Providing expert technical review, forensic support and evidentiary guidance.", Icons.Default.Storage),
        Service("Training & Awareness", "Delivering workshops, awareness programs and safety education for citizens and officers.", Icons.Default.MenuBook),
        Service("Research & Development", "Driving innovation in cyber security and emerging technology solutions.", Icons.Default.AutoAwesome)
    )

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxSize()
    ) {
        val scroll = rememberScrollState()
        Column(modifier = Modifier.padding(24.dp).verticalScroll(scroll)) {
            Text("Kerala Cyberdome Operational Capabilities", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text("Citizen safety, threat monitoring, and inter-agency services", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(20.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                siteServices.chunked(2).forEach { rowServices ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        rowServices.forEach { service ->
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Icon(service.icon, contentDescription = null, modifier = Modifier.padding(8.dp), tint = MaterialTheme.colorScheme.primary)
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(service.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        service.description,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
