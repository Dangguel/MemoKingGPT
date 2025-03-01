package kr.co.dangguel.memokinggpt.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kr.co.dangguel.memokinggpt.presentation.ui.screens.MainScreen
import kr.co.dangguel.memokinggpt.presentation.ui.screens.NoteEditorScreen
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = "main") {

        // 📌 메인 화면
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                currentFolderId = null,
                onFolderClick = { folderId -> navController.navigate("folder/$folderId") },
                onNoteClick = { noteId -> navController.navigate("note_edit/$noteId") },
                onBackClick = { navController.popBackStack() },
                onAddFolderClick = { /* 폴더 추가 */ },
                onAddNoteClick = { navController.navigate("note_edit") },
                onEditFolderClick = { /* 폴더 편집 */ },
                onEditNoteClick = { noteId -> navController.navigate("note_edit/$noteId") }
            )
        }

        // 📌 폴더 내부 화면
        composable("folder/{folderId}") { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId")?.toLongOrNull()
            if (folderId != null) {
                MainScreen(
                    viewModel = viewModel,
                    currentFolderId = folderId,
                    onFolderClick = { subFolderId -> navController.navigate("folder/$subFolderId") },
                    onNoteClick = { noteId -> navController.navigate("note_edit/$noteId") },
                    onBackClick = { navController.popBackStack() },
                    onAddFolderClick = { /* 폴더 추가 */ },
                    onAddNoteClick = { navController.navigate("note_edit") },
                    onEditFolderClick = { /* 폴더 편집 */ },
                    onEditNoteClick = { noteId -> navController.navigate("note_edit/$noteId") }
                )
            }
        }
    }
}
