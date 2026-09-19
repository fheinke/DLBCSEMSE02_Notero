package com.fheinke.notero.ui.editor

import com.fheinke.notero.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Screen for creating or editing a journal entry.
 *
 * @param entryId The ID of the entry to edit, or null for a new entry.
 * @param viewModel The ViewModel managing the state of the editor.
 * @param onBack Callback to navigate back when the entry is saved or the back button is pressed.
 * @param modifier Modifier to be applied to the screen layout.
 *
 * @see EntryEditorViewModel
 * @see EntryEditorFormState
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryEditorScreen(
    entryId: Long?,
    viewModel: EntryEditorViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()

    // Navigate back when the entry is saved
    LaunchedEffect(isSaved) {
        if (isSaved) onBack()
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(if (entryId == null) stringResource(R.string.new_entry) else stringResource(R.string.edit_entry)) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = formState.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text(stringResource(R.string.title)) },
                isError = formState.titleError,
                supportingText = if (formState.titleError) {{ Text(stringResource(R.string.title_required)) }} else null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            OutlinedTextField(
                value = formState.text,
                onValueChange = viewModel::onTextChange,
                label = { Text(stringResource(R.string.journal_entry)) },
                modifier = Modifier.fillMaxWidth().heightIn(min = 150.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            ScoreSlider(stringResource(R.string.mood), formState.mood, 1..5, viewModel::onMoodChange)
            ScoreSlider(stringResource(R.string.sleep), formState.sleepScore, 1..5, viewModel::onSleepScoreChange)
            ScoreSlider(stringResource(R.string.stress), formState.stressLevel, 1..5, viewModel::onStressLevelChange)

            Spacer(Modifier.height(8.dp))

            Button(onClick = viewModel::saveEntry, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

/**
 * A composable function that displays a labeled slider for selecting a score.
 *
 * @param label The label for the slider.
 * @param value The current value of the slider, or null if no value is selected.
 * @param range The range of values the slider can take.
 * @param onValueChange Callback invoked when the slider value changes.
 */
@Composable
private fun ScoreSlider(
    label: String,
    value: Int?,
    range: IntRange,
    onValueChange: (Int?) -> Unit
) {
    Column {
        Text(
            text = if (value != null) "$label: $value" else "$label: —",
            style = MaterialTheme.typography.bodyMedium
        )
        Slider(
            value = value?.toFloat() ?: 0f,
            onValueChange = { onValueChange(it.toInt().takeIf { v -> v > 0 }) },
            valueRange = 0f..range.last.toFloat(),
            steps = range.last - 1
        )
    }
}