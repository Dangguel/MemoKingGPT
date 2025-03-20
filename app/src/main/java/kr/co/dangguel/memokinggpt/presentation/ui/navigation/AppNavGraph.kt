package kr.co.dangguel.memokinggpt.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kr.co.dangguel.memokinggpt.presentation.ui.screens.FolderDetailScreen
import kr.co.dangguel.memokinggpt.presentation.ui.screens.MainScreen
import kr.co.dangguel.memokinggpt.presentation.ui.screens.NoteEditorScreen
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    NavHost(navController = navController, startDestination = "main") {

        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onFolderClick = { folderId ->
                    navController.navigate("folderDetail/$folderId") // ✅ 직접 네비게이션 호출
                },
                onNoteClick = { noteId ->
                    navController.navigate("note_edit?noteId=$noteId&folderId=-1")
                },
                onBackClick = { navController.popBackStack() },
                onAddNoteClick = { navController.navigate("note_edit?noteId=null&folderId=-1") },
                currentFolderId = null
            )
        }

        composable("folderDetail/{folderId}") { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId")?.toLongOrNull() ?: return@composable

            FolderDetailScreen(
                viewModel = viewModel,
                folderId = folderId,
                navController = navController,
                onNoteClick = { noteId ->
                    navController.navigate("note_edit?noteId=$noteId&folderId=${folderId}") // ✅ 직접 네비게이션 호출
                },
                onDeleteNoteClick = { noteId -> viewModel.onDeleteNote(noteId) }
            )
        }

        composable("note_edit?noteId={noteId}&folderId={folderId}") { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId")?.toLongOrNull()
            val folderId = backStackEntry.arguments?.getString("folderId")?.toLongOrNull()
            val noteEditorViewModel: NoteEditorViewModel = hiltViewModel()

            NoteEditorScreen(
                viewModel = noteEditorViewModel,
                noteId = noteId,
                folderId = folderId, // ✅ folderId도 전달
                navController = navController
            )
        }
    }
}
