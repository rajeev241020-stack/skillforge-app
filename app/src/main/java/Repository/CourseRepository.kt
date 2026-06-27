package Repository


import Api.CourseApiService
import Api.Course

class CourseRepository(private val api: CourseApiService) {

    suspend fun getPopularCourses(): List<Course> {
        val response = api.getFetch()
        return if (response.isSuccessful) {
            response.body()?.let {
                it.categories.flatMap { it.courses }
            }
                ?: emptyList()

        } else {
            emptyList()
        }
    }
}