package kr.co.dangguel.memokinggpt.data.repository

import kr.co.dangguel.memokinggpt.data.remote.OpenAiService
import kr.co.dangguel.memokinggpt.data.remote.model.GptRequest
import kr.co.dangguel.memokinggpt.data.remote.model.GptResponse
import javax.inject.Inject

class GptRepository @Inject constructor(
    private val api: OpenAiService
) {
    suspend fun requestSummary(request: GptRequest): GptResponse {
        return api.getSummary(request)
    }
}
