package com.bharat.airagandroidapp.ui.theme.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    conversationId: String?,
    navController: NavController
) {
    val state by viewModel.stateMessages.collectAsState()
    val lazyListState = rememberLazyListState()
    var input by remember { mutableStateOf("") }
    LaunchedEffect(conversationId) {
        viewModel.setConversation(conversationId)
        viewModel.loadMessages()
    }
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            lazyListState.scrollToItem(
                index = state.messages.size - 1,
                scrollOffset = Int.MAX_VALUE
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopAppBar(
            title = {
                Text("Chat")
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Bottom
        ) {
            items(state.messages) { message ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = if (message.isUser)
                        Arrangement.End else Arrangement.Start
                ) {

                    Box(
                        modifier = Modifier
                            .background(
                                color = if (message.isUser)
                                    Color(0xFFDCF8C6)   // light green
                                else
                                    Color(0xFFEAEAEA), // light grey
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                            .widthIn(max = 280.dp)
                    ) {
                        Text(
                            text = message.text,
                            color = Color.Black
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xD2E6E1FD))
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        viewModel.sendMessage(input)
                        input = ""
                    }
                ) {
                    Text("Send")
                }
            }
        }

    }
}