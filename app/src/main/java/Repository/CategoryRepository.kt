package Repository

import Api.Category
import Api.CourseApiService
import Api.PopularCourseData

class CategoryRepository(private val api: CourseApiService) {

    private var cachedData: PopularCourseData? = null

    private suspend fun fetchData(): PopularCourseData? {
        if (cachedData != null) return cachedData
        return try {
            val response = api.getFetch()
            if (response.isSuccessful) {
                cachedData = response.body()
                cachedData
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCategories(): List<Category> {
        return fetchData()?.categories ?: emptyList()
    }
}