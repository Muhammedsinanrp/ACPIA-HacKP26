package com.example.acpia.ui.modules

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.acpia.model.*
import com.example.acpia.viewmodel.InvestigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CasesModule(
    viewModel: InvestigationViewModel,
    onNavigateToEvidence: () -> Unit
) {
    val cases by viewModel.cases.collectAsState()
    val selectedCase by viewModel.selectedCase.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val statusFilter by viewModel.statusFilter.collectAsState()

    val configuration = LocalConfiguration.current
    val isMobile = configuration.screenWidthDp < 600
    var showDetailOnMobile by remember { mutableStateOf(false) }

    var showNewCaseDialog by remember { mutableStateOf(false) }

    val filteredCases = cases.filter { case ->
        val matchesFilter = when (statusFilter) {
            "ACTIVE" -> case.status == "Active"
            "UNDER_REVIEW" -> case.status == "Under Review"
            "ESCALATED" -> case.status == "Escalated"
            "CLOSED" -> case.status == "Closed"
            else -> true
        }
        val matchesSearch = searchQuery.isBlank() ||
                case.title.contains(searchQuery, ignoreCase = true) ||
                case.firNumber.contains(searchQuery, ignoreCase = true) ||
                case.id.contains(searchQuery, ignoreCase = true)

        matchesFilter && matchesSearch
    }

    if (isMobile && showDetailOnMobile && selectedCase != null) {
        BackHandler {
            showDetailOnMobile = false
        }
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize()
        ) {
            CaseInspector(
                c = selectedCase!!,
                viewModel = viewModel,
                onNavigateToEvidence = onNavigateToEvidence,
                isMobile = true,
                onBack = { showDetailOnMobile = false }
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Left Column: Case Directory & Filters
            Surface(
                color = Color.White,
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.weight(1.1f).fillMaxHeight()
            ) {
                Column(modifier = Modifier.padding(if (isMobile) 12.dp else 18.dp)) {
                    // Header & Add Case Button
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text("Investigation Files", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                            Text("${filteredCases.size} cases", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            onClick = { showNewCaseDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            contentPadding = PaddingValues(horizontal = if (isMobile) 8.dp else 16.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            if (!isMobile) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Register FIR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "ALL" to "All",
                            "ACTIVE" to "Active",
                            "ESCALATED" to "Escalated",
                            "CLOSED" to "Closed"
                        ).forEach { (key, label) ->
                            val isSelected = statusFilter == key
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setStatusFilter(key) },
                                label = { Text(label, fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Case List
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredCases) { caseItem ->
                            val isSelected = caseItem.id == selectedCase?.id
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
                                        viewModel.selectCase(caseItem.id)
                                        if (isMobile) {
                                            showDetailOnMobile = true
                                        }
                                    }
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            caseItem.id,
                                            fontSize = 11.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Surface(
                                            color = Color(caseItem.priority.colorHex).copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                caseItem.priority.label,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(caseItem.priority.colorHex),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        caseItem.title,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                    Text(
                                        caseItem.firNumber,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Right Column: Active Case Inspector (Desktop only)
            if (!isMobile) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier.weight(1.4f).fillMaxHeight()
                ) {
                    selectedCase?.let { c ->
                        CaseInspector(c, viewModel, onNavigateToEvidence, isMobile = false, onBack = {})
                    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Select a case from the directory to view complete details.")
                    }
                }
            }
        }
    }

    // New Case Registration Modal
    if (showNewCaseDialog) {
        NewCaseDialog(
            onDismiss = { showNewCaseDialog = false },
            onCreate = { title, fir, prio, platform, loc, notes ->
                viewModel.createNewCase(title, fir, prio, platform, loc, notes)
                showNewCaseDialog = false
            }
        )
    }
}

@Composable
fun CaseInspector(
    c: Case,
    viewModel: InvestigationViewModel,
    onNavigateToEvidence: () -> Unit,
    isMobile: Boolean,
    onBack: () -> Unit
) {
    val scroll = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize().padding(if (isMobile) 12.dp else 20.dp).verticalScroll(scroll)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                if (isMobile) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Column {
                    Text(c.firNumber, fontSize = 12.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(c.title, fontSize = if (isMobile) 18.sp else 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(top = 2.dp))
                }
            }
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    "Status: ${c.status}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Investigation Stage Progression Stepper
        Text("INVESTIGATION STAGE", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CaseStage.values().forEachIndexed { index, stage ->
                        val isCurrent = c.stage == stage
                        val isPassed = c.stage.ordinal >= stage.ordinal
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(
                                        if (isPassed) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${index + 1}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                stage.label,
                                fontSize = 9.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                // Stage update actions
                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (c.stage.ordinal < CaseStage.values().size - 1) {
                        val nextStage = CaseStage.values()[c.stage.ordinal + 1]
                        Button(
                            onClick = { viewModel.updateCaseStage(c.id, nextStage, "Active") },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Advance to ${nextStage.label}", fontSize = 10.sp)
                        }
                    }
                    if (c.status != "Closed") {
                        OutlinedButton(
                            onClick = { viewModel.updateCaseStage(c.id, c.stage, "Closed") },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("Close Case", fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Case Details Grid
        Text("METADATA & ASSIGNMENT", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DetailRow("Investigating Officer", c.assignedTo)
            DetailRow("Jurisdiction", c.department)
            DetailRow("Target Platform", c.platform)
            DetailRow("Location Hint", c.locationHint)
            DetailRow("Reported Time", c.reportedAt)
            DetailRow("Intel Matches", "${c.hashMatches} indicators")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("INVESTIGATION NOTES", fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(6.dp))
        Text(c.notes, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, lineHeight = 18.sp)

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onNavigateToEvidence,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Icon(Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Open Linked Evidence", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCaseDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, fir: String, prio: CasePriority, platform: String, loc: String, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var fir by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var loc by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(CasePriority.HIGH) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Register FIR / Intake", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                Text("Kerala Cyber Operations", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Case Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = fir,
                    onValueChange = { fir = it },
                    label = { Text("FIR No. / Sections") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = platform,
                    onValueChange = { platform = it },
                    label = { Text("Platform") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = loc,
                    onValueChange = { loc = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Narrative Summary") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onCreate(title, fir, priority, platform, loc, notes)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Seal & Create")
                    }
                }
            }
        }
    }
}
