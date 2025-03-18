package kr.co.dangguel.memokinggpt.domain.usecase

import kr.co.dangguel.memokinggpt.data.remote.model.GptRequest
import kr.co.dangguel.memokinggpt.data.repository.GptRepository
import javax.inject.Inject

class GptUseCase @Inject constructor(
    private val repository: GptRepository
) {
    suspend fun getSummary(request: GptRequest) = repository.requestSummary(request)
}
