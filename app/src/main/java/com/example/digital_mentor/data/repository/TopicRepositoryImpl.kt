package com.example.digital_mentor.data.repository

import android.util.Log
import com.example.digital_mentor.data.model.TopicDto
import com.example.digital_mentor.data.model.toDomain
import com.example.digital_mentor.domain.model.Topic
import com.example.digital_mentor.domain.repository.TopicRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class TopicRepositoryImpl(
    private val supabaseClient: SupabaseClient
) : TopicRepository {
    override suspend fun getTopicsWithQuestions(): Result<List<Topic>> {
        return try {
            val columnsToSelect = Columns.raw(
                    """
                        id,
                        name,
                        description,
                        topic_questions(
                            id, 
                            topic_id, 
                            question_text, 
                            response_type, 
                            order, 
                            options(
                                id, 
                                question_id, 
                                option_text, 
                                need_description
                            )
                        )
                    """.trimIndent())

            val result =
                supabaseClient.from("topics").select(columnsToSelect)

            val topicsDto = result.decodeList<TopicDto>()

            val topics = topicsDto.map { topicDto -> topicDto.toDomain() }

            return Result.success(topics)
        } catch (e: Exception) {
            Log.d("TopicRepository", "This is the repository error: $e")
            Result.failure(e)
        }
    }
}