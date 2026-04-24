package edu.dyds.movies.presentation.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dydsproject.composeapp.generated.resources.Res
import dydsproject.composeapp.generated.resources.retry
import edu.dyds.movies.domain.error.AppError
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorScreen(error: AppError, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null
            )
        Spacer(modifier = Modifier.height(16.dp))
        Text(error.toUserMessage(), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height((16.dp)))
        Button(onClick = onRetry) {Text(stringResource(Res.string.retry))}
    }
}
