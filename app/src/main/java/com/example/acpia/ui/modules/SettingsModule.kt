package com.example.acpia.ui.modules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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

@Composable
fun SettingsModule() {
    var biometricEnabled by remember { mutableStateOf(true) }
    var autoHashSync by remember { mutableStateOf(true) }
    var highSecurityMode by remember { mutableStateOf(true) }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxSize()
    ) {
        val scroll = rememberScrollState()
        Column(modifier = Modifier.padding(24.dp).verticalScroll(scroll)) {
            Text("System & Cryptographic Configuration", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text("Kerala Police Cyberdome Security Policies", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(20.dp))

            // Officer Profile Card
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.Badge, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(12.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text("Insp. R. Chandran", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Badge ID: KL-CY-409 · Cyber Operations Lead", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Key Fingerprint: 9F8A...31B2 (Ed25519 Valid)", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("SECURITY PREFERENCES", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SettingToggle(
                    title = "Biometric / Hardware Token Authentication",
                    description = "Require biometric verification prior to viewing raw evidence payloads and exporting Section 65B certificates.",
                    checked = biometricEnabled,
                    onCheckedChange = { biometricEnabled = it }
                )
                SettingToggle(
                    title = "Automatic State Hash Registry Sync",
                    description = "Immediately cross-check all ingested SHA-256 evidence hashes against the National Cyber Forensics DB.",
                    checked = autoHashSync,
                    onCheckedChange = { autoHashSync = it }
                )
                SettingToggle(
                    title = "High-Security Faraday & Memory Protection",
                    description = "Disable cleartext memory caching of seized credential dumps.",
                    checked = highSecurityMode,
                    onCheckedChange = { highSecurityMode = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Node Endpoints
            Text("NETWORK & NODE STATUS", fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Cyberdome Central Node", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("10.140.20.10 (Secured TLS 1.3)", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = Color(0xFF25B07B))
                    }
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("State Evidence Vault Storage", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("Encrypted / Distributed S3", fontFamily = FontFamily.Monospace, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingToggle(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp, modifier = Modifier.padding(top = 4.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
