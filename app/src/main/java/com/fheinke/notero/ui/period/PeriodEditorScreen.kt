package com.fheinke.notero.ui.period

import com.fheinke.notero.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Screen for creating or editing a period record.
 *
 * @param recordId The ID of the record to edit, or null for a new record.
 * @param viewModel The ViewModel managing the state of the screen.
 * @param onBack Callback to navigate back when the user is done.
 * @param modifier Modifier to be applied to the screen layout.
 *
 * @see PeriodEditorViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodEditorScreen(
    recordId: Long?,
    viewModel: PeriodEditorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(isSaved) {
        if (isSaved) onBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(if (recordId == null) stringResource(R.string.new_entry) else stringResource(R.string.edit_entry)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

            // Start Date
            OutlinedButton(
                onClick = { showStartDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("${stringResource(R.string.start_date)}: ${formState.startDate.format(formatter)}")
            }
            if (formState.startDateError) {
                Text(stringResource(R.string.start_date_invalid), color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            // End Date
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("${stringResource(R.string.end_date)}: ${formState.endDate?.format(formatter) ?: stringResource(R.string.ongoing)}")
                }
                if (formState.endDate != null) {
                    TextButton(onClick = { viewModel.onEndDateChange(null) }) {
                        Text(stringResource(R.string.clear))
                    }
                }
            }
            if (formState.endDateError) {
                Text(stringResource(R.string.end_date_invalid),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall)
            }

            HorizontalDivider()

            // Flow Intensity
            Text("${stringResource(R.string.intensity)}: ${formState.flowIntensity} / 5",
                style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = formState.flowIntensity.toFloat(),
                onValueChange = { viewModel.onFlowIntensityChange(it.toInt()) },
                valueRange = 1f..5f,
                steps = 3
            )

            // Note
            OutlinedTextField(
                value = formState.note,
                onValueChange = viewModel::onNoteChange,
                label = { Text(stringResource(R.string.note_optional)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = viewModel::saveRecord,
                enabled = !formState.endDateError,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }

    // Start Date Picker
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismiss = { showStartDatePicker = false },
            onDateSelected = {
                viewModel.onStartDateChange(it)
                showStartDatePicker = false
            },
            initialDate = formState.startDate
        )
    }

    // End Date Picker
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismiss = { showEndDatePicker = false },
            onDateSelected = {
                viewModel.onEndDateChange(it)
                showEndDatePicker = false
            },
            initialDate = formState.endDate ?: LocalDate.now()
        )
    }
}

/**
 * A dialog that allows the user to pick a date.
 *
 * @param onDismiss Callback when the dialog is dismissed.
 * @param onDateSelected Callback when a date is selected.
 * @param initialDate The initial date to display in the picker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    initialDate: LocalDate
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .toEpochDay() * 86_400_000L
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                state.selectedDateMillis?.let { millis ->
                    onDateSelected(LocalDate.ofEpochDay(millis / 86_400_000L))
                }
                onDismiss()
            }) { Text(stringResource(R.string.save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    ) {
        DatePicker(state = state)
    }
}