package com.example.acpia.ui.modules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ContactUnit(
    val name: String,
    val jurisdiction: String,
    val phone: String,
    val email: String,
    val status: String
)

@Composable
fun ContactModule() {
    val contacts = listOf(
        ContactUnit("Kerala Cyberdome HQ", "Technopark, Thiruvananthapuram", "0471-2415000", "cyberdome.pol@kerala.gov.in", "24/7 Active"),
        ContactUnit("Cyber Crime Police Station (Central)", "Ernakulam / Kochi", "0484-2390100", "cyberps-ekm.pol@kerala.gov.in", "Operational"),
        ContactUnit("Cyber Crime Police Station (North)", "Kozhikode City", "0495-2720100", "cyberps-kkd.pol@kerala.gov.in", "Operational"),
        ContactUnit("National Cyber Crime Helpline", "Ministry of Home Affairs", "1930", "helpdesk@cybercrime.gov.in", "Toll Free (Emergency)")
    )

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.SupportAgent, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text("Emergency Cyber Cell Directory & Escalation", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Direct contact channels for inter-agency coordination", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(contacts) { unit ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(unit.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(unit.jurisdiction, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                    Text("Phone: ${unit.phone}", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                                    Text("Email: ${unit.email}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                            Surface(
                                color = Color(0xFF25B07B).copy(alpha = 0.12f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    unit.status,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF25B07B),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
