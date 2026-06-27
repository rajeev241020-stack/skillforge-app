package Api

import java.io.Serializable

data class Instructor(
    val avatarUrl: String,
    val bio: String,
    val id: String,
    val name: String,
    val title: String
): Serializable