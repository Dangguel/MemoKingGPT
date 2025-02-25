package kr.co.dangguel.memokinggpt.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kr.co.dangguel.memokinggpt.presentation.ui.screens.MainScreen
import kr.co.dangguel.memokinggpt.presentation.ui.screens.NoteEditorScreen
import kr.co.dangguel.memokinggpt.presentation.viewmodel.MainViewModel
import kr.co.dangguel.memokinggpt.presentation.viewmodel.NoteEditorViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = "main") {

        // 📌 메인 화면
        composable("main") {
            val items by viewModel.items.collectAsState() // ✅ Flow를 State로 변환
            println("📌 MainActivity에서 받은 아이템 개수: ${items.size}")

            MainScreen(
                items = items,
                onItemClick = { id -> },
                onAddNoteClick = { navController.navigate("note_edit") },
                viewModel = viewModel
            )
        }

        // 📌 노트 작성 화면
        composable("note_edit") {
            val noteEditorViewModel: NoteEditorViewModel = hiltViewModel() // ✅ NoteEditorViewModel 주입

            NoteEditorScreen(
                viewModel = noteEditorViewModel, // ✅ 올바르게 전달
                onBackClick = { navController.popBackStack() },
                onSaveClick = { text ->
                    navController.popBackStack()
                }
            )
        }
    }
}
