package com.example.algoviz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.algoviz.ui.screens.ai.AIChatScreen
import com.example.algoviz.ui.screens.arena.ArenaScreen
import com.example.algoviz.ui.screens.arena.ProblemDetailScreen
import com.example.algoviz.ui.screens.arena.CodeEditorScreen
import com.example.algoviz.ui.screens.auth.LoginScreen
import com.example.algoviz.ui.screens.auth.SignupScreen
import com.example.algoviz.ui.screens.home.HomeScreen
import com.example.algoviz.ui.screens.learn.LearnScreen
import com.example.algoviz.ui.screens.learn.LearnDetailScreen
import com.example.algoviz.ui.screens.profile.ProfileScreen
import com.example.algoviz.ui.screens.visualize.VisualizeScreen
import com.example.algoviz.ui.screens.visualize.VisualizationPlayerScreen
import com.example.algoviz.domain.repository.ProblemRepository
import androidx.hilt.navigation.compose.hiltViewModel
import javax.inject.Inject

@Composable
fun AlgoVizNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Login.route,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // Auth
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToSignup = {
                    navController.navigate(Screen.Signup.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Main tabs
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToTopic = { topicId ->
                    navController.navigate(Screen.TopicDetail.createRoute(topicId))
                }
            )
        }
        composable(Screen.Learn.route) {
            LearnScreen(
                onNavigateToTopic = { topicId ->
                    navController.navigate(Screen.TopicDetail.createRoute(topicId))
                }
            )
        }
        composable(Screen.TopicDetail.route) { backStackEntry ->
            val topicId = backStackEntry.arguments?.getString("topicId") ?: ""
            LearnDetailScreen(
                topicId = topicId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToVisualize = { vizId ->
                    navController.navigate(Screen.VisualizationPlayer.createRoute(vizId))
                },
                onNavigateToQuiz = { id ->
                    // Navigate to Quiz
                },
                progressRepository = hiltViewModel(), // Placeholder
                authRepository = hiltViewModel() // Placeholder
            )
        }
        composable(Screen.Visualize.route) {
            VisualizeScreen(
                onNavigateToVisualization = { vizId ->
                    navController.navigate(Screen.VisualizationPlayer.createRoute(vizId))
                }
            )
        }
        composable(Screen.VisualizationPlayer.route) { backStackEntry ->
            val vizId = backStackEntry.arguments?.getString("visualizationId") ?: ""
            VisualizationPlayerScreen(
                vizId = vizId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Arena.route) {
            ArenaScreen(
                onNavigateToProblem = { problemId ->
                    navController.navigate(Screen.ProblemDetail.createRoute(problemId))
                }
            )
        }
        composable(Screen.ProblemDetail.route) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getString("problemId") ?: ""
            // ProblemDetailScreen implementation
        }
        composable(Screen.AIChat.route) {
            AIChatScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}