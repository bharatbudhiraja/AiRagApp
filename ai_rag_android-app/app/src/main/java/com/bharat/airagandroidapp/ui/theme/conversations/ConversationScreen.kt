package com.bharat.airagandroidapp.ui.theme.conversations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bharat.airagandroidapp.ui.theme.chat.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onConversationClick: (String?) -> Unit
) {

    val state by viewModel.stateConversations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    Column(modifier = Modifier.fillMaxSize(),
    ) {

        TopAppBar(
            title = {
                Text("Conversations")
            }
        )

        if (state.isLoading) {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn {
            items(state.conversations) { convo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            onConversationClick(convo.id)
                        }
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = convo.text ?: "Chat",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.clickable{
                                    onConversationClick(convo.id)
                                }
                            )

                            Text(
                                text = convo.createdAt.take(10), // temp formatting
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Tap to open chat", // later replace with last message
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }

    if (state.conversations.isEmpty() && !state.isLoading) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("No conversations yet")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                // create new conversation (we'll wire next)
            }) {
                Text("Start New Chat")
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            items(state.conversations) { convo ->
                // your card UI
            }
        }

        FloatingActionButton(
            onClick = {
                onConversationClick.invoke(null)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }
    }
}