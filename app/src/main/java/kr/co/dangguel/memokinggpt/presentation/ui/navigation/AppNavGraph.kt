package kr.co.dangguel.memokinggpt.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kr.co.dangguel.memokinggpt.presentation.navigation.NavigationManager
import kr.co.dangguel.memokinggpt.presentation.ui.screens.FolderDetailScreen
import kr.co.dangguel.memokinggpt.presentation.ui.screens.MainScreen
import kr.co.dangguel.memokinggpt.presentation.ui.screens.NoteEditorScreen
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
    navManager: NavigationManager // ✅ 추가
) {
    LaunchedEffect(Unit) {
        navManager.navigationCommands.collect { route: String -> // ✅ 타입 명시
            navController.navigate(route)
        }
    }

    NavHost(navController = navController, startDestination = "main") {

        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onFolderClick = { folderId -> viewModel.navigateToFolder(folderId) },
                onNoteClick = { noteId -> viewModel.navigateToNote(noteId) },
                onBackClick = { navController.popBackStack() },
                onAddFolderClick = { navController.navigate("folder_edit/null") },
                onAddNoteClick = { navController.navigate("note_edit/null") },
                onEditFolderClick = { folderId -> navController.navigate("folder_edit/$folderId") },
                onEditNoteClick = { noteId -> navController.navigate("note_edit/$noteId") },
                currentFolderId = null
            )
        }

        composable("folderDetail/{folderId}") { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId")?.toLongOrNull() ?: return@composable
            FolderDetailScreen(viewModel = viewModel, folderId = folderId, navController = navController)
        }

        composable("note_edit/{noteId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toLongOrNull()
            val noteEditorViewModel: NoteEditorViewModel = hiltViewModel()

            NoteEditorScreen(
                viewModel = noteEditorViewModel,
                noteId = noteId,
                navController = navController
            )
        }
    }
}
