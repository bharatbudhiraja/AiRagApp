package com.bharat.airagandroidapp.ui.theme.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview(showBackground = true)
@Composable
fun ChatScreen(viewModel: ChatViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()
    var input by remember { mutableStateOf("") }
    // ✅ Load messages ONCE
    LaunchedEffect(Unit) {
        viewModel.loadMessages()
    }
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            lazyListState.scrollToItem(state.messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f)
        ) {
            items(state.messages) { message ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = message.text,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .align(if (message.isUser) Alignment.CenterEnd else Alignment.CenterStart)
                            .padding(8.dp),
                        textAlign = if (message.isUser) TextAlign.End else TextAlign.Start
                    )
                }
            }
        }
        Row {
            TextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f)
            )

            Button(onClick = {
                viewModel.sendMessage(input)
                input = ""
            }) {
                Text("Send")
            }
        }
    }
}