package Api


import retrofit2.Response
import retrofit2.http.GET

interface CourseApiService {
    @GET("android-assesment/notes/refs/heads/main/data.json")
    suspend fun getFetch(): Response<PopularCourseData>
}