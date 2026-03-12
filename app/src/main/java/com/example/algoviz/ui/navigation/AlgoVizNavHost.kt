package com.example.algoviz.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                }
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
            // In a real app, we'd use hiltViewModel to get the repository, but for this demo:
            val problemRepository = hiltViewModel<com.example.algoviz.ui.screens.arena.ArenaViewModel>().let { 
                // This is a bit hacky for the demo, normally we'd inject the repo into a ProblemDetailViewModel
                // But I'll just use the NavHost to pass it for now.
            }
            // Re-writing to use a proper ViewModel for ProblemDetail is better.
        }
        
        // Let's fix the ProblemDetail and CodeEditor routes properly
        composable(Screen.ProblemDetail.route) { backStackEntry ->
            val problemId = backStackEntry.arguments?.getString("problemId") ?: ""
            // We need a ProblemDetailViewModel. I'll create it in the next step if needed, 
            // but for now I'll just use a placeholder or the repo directly if I can.
            // Actually, I'll just create the ProblemDetailViewModel now.
        }
    }
}