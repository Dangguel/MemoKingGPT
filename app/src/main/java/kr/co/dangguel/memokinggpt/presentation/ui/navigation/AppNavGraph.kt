package kr.co.dangguel.memokinggpt.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kr.co.dangguel.memokinggpt.presentation.ui.screens.MainScreen
import kr.co.dangguel.memokinggpt.presentation.ui.screens.NoteEditorScreen
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel
import kr.co.dangguel.memokinggpt.presentation.viewmodel.Item

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(navController = navController, startDestination = "main") {

        // 📌 메인 화면
        composable("main") {
            val items by viewModel.items.collectAsState()
            val folders by viewModel.folders.collectAsState()
            val notes by viewModel.notes.collectAsState()

            MainScreen(
                viewModel = viewModel,
                folders = folders.map { Item.FolderItem(it) },  // ✅ FolderEntity -> Item.FolderItem 변환
                notes = notes.map { Item.NoteItem(it) },  // ✅ NoteEntity -> Item.NoteItem 변환
                onFolderClick = { folderId -> println("📁 폴더 클릭: $folderId") },
                onNoteClick = { noteId -> navController.navigate("note_edit/$noteId") },
                onAddFolderClick = { println("➕ 새 폴더 추가") },
                onEditFolderClick = { folderId -> println("✏️ 폴더 편집: $folderId") },
                onEditNoteClick = { noteId -> navController.navigate("note_edit/$noteId") },
                onAddNoteClick = { navController.navigate("note_edit") }
            )
        }

        // 📌 노트 작성/편집 화면
        composable(
            route = "note_edit/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")?.takeIf { it != -1L }
            val noteEditorViewModel: NoteEditorViewModel = hiltViewModel()

            NoteEditorScreen(
                viewModel = noteEditorViewModel,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() }
            )
        }

        // 📌 새 노트 작성 화면 (noteId 없음)
        composable("note_edit") {
            val noteEditorViewModel: NoteEditorViewModel = hiltViewModel()

            NoteEditorScreen(
                viewModel = noteEditorViewModel,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() }
            )
        }
    }
}
