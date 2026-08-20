package com.example.acpia.ui.modules

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.acpia.model.CyberLawReference

@Composable
fun TrainingModule() {
    val legalReferences = listOf(
        CyberLawReference("Section 66C IT Act", "Identity Theft & Impersonation", "Fraudulent use of password, electronic signature, or unique identification feature of any person.", "Imprisonment up to 3 years and fine up to Rs. 1 Lakh"),
        CyberLawReference("Section 66D IT Act", "Cheating by Personation using Computer", "Cheating by personation by using any communication device or computer resource.", "Imprisonment up to 3 years and fine up to Rs. 1 Lakh"),
        CyberLawReference("Section 66E IT Act", "Violation of Privacy", "Intentionally capturing, publishing, or transmitting images of private areas without consent.", "Imprisonment up to 3 years or fine up to Rs. 2 Lakhs"),
        CyberLawReference("Section 67 / 67A IT Act", "Transmitting Obscene Material / CSAM", "Publishing or transmitting obscene or sexually explicit material in electronic form.", "Imprisonment up to 5-7 years and fine up to Rs. 10 Lakhs"),
        CyberLawReference("Section 43 IT Act", "Penalty for Damage to Computer System", "Unauthorized downloading, extracting, introducing virus, or disrupting computer networks.", "Civil compensation up to Rs. 5 Crores payable to affected party")
    )

    val sops = listOf(
        "Digital Evidence Seizure" to "Always place mobile phones in RF-shielded Faraday bags immediately. Avoid powering off if encrypted volatile RAM dump is required.",
        "Bit-Stream Disk Imaging" to "Use hardware write-blockers before connecting target hard drives. Calculate MD5 & SHA-256 hashes before and after imaging.",
        "Social Media Takedown" to "Submit urgent law-enforcement preservation requests under Section 91 CrPC followed by formal MLAT / Emergency Disclosure Request."
    )

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Left Column: Legal References (IT Act)
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.weight(1.2f).fillMaxHeight()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Cyber Law Quick Reference", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Information Technology Act 2000 (Amended 2008)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(legalReferences) { law ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(law.section, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                    Text(law.title, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(law.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 15.sp)
                                Spacer(modifier = Modifier.height(6.dp))
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        "Penal: ${law.punishment}",
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        color = Color(0xFFFF6B5F),
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Right Column: Forensics Standard Operating Procedures (SOPs)
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.weight(1.0f).fillMaxHeight()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Forensic Investigation SOPs", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                        Text("Standards for court-admissible evidence", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    sops.forEach { (title, desc) ->
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF25B07B), modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
