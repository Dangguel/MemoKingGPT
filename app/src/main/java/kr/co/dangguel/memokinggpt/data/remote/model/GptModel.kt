package kr.co.dangguel.memokinggpt.data.remote.model

data class GptRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<GptMessage>,
    val max_tokens: Int = 500,
    val temperature: Double = 0.7
)

data class GptMessage(
    val role: String = "user",
    val content: String
)

data class GptResponse(
    val choices: List<GptChoice>
)

data class GptChoice(
    val message: GptMessageContent
)

data class GptMessageContent(
    val content: String
)