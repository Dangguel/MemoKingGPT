package kr.co.dangguel.memokinggpt.data.remote

import kr.co.dangguel.memokinggpt.data.remote.model.GptRequest
import kr.co.dangguel.memokinggpt.data.remote.model.GptResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getSummary(@Body request: GptRequest): GptResponse
}
