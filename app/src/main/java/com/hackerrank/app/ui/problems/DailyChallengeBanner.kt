package com.hackerrank.app.ui.problems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hackerrank.app.R
import com.hackerrank.app.core.localizedName

@Composable
fun DailyChallengeBanner(
    state: DailyChallengeState,
    onProblemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading || state.isUnavailable) return

    Card(
        onClick = state.problem?.let { { onProblemClick(it.id) } } ?: {},
        modifier = modifier.fillMaxWidth(),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (state.isCompleted) {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.tertiaryContainer
                    },
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (state.isCompleted) Icons.Default.CheckCircle else Icons.Default.Whatshot,
                contentDescription = null,
                tint = if (state.isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.size(32.dp),
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.daily_challenge_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                state.problem?.let { problem ->
                    Text(
                        text = "${problem.title} • ${problem.difficulty.localizedName()}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (!state.isCompleted) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.daily_challenge_bonus, state.bonusXp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            if (state.isCompleted) {
                Text(
                    text = stringResource(R.string.daily_challenge_completed),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50),
                )
            } else {
                Text(
                    text = stringResource(R.string.daily_challenge_solve),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}
