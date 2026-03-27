package com.bharat.airagandroidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.bharat.airagandroidapp.ui.theme.chat.ChatScreen
import com.bharat.airagandroidapp.ui.theme.conversations.ConversationScreen
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.composable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "conversations"
            ) {

                composable("conversations") {
                    ConversationScreen(
                        onConversationClick = { id ->
                            if(id == null){
                                navController.navigate("chat")
                            }else{
                                navController.navigate("chat/$id")
                            }
                        }
                    )
                }

                composable("chat") {
                    ChatScreen(conversationId = null, navController = navController)
                }

                composable("chat/{conversationId}") { backStackEntry ->

                    val conversationId =
                        backStackEntry.arguments?.getString("conversationId")

                    ChatScreen(conversationId = conversationId, navController = navController)
                }
            }
        }
    }
}
