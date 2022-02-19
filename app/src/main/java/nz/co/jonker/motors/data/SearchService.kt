package nz.co.jonker.motors.data

import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("search")
    suspend fun search(
        @Query("make") make: String,
        @Query("model") model: String,
        @Query("year") year: String
    ): SearchResultsDto
}

@JsonClass(generateAdapter = true)
data class SearchResultsDto(val searchResults: List<VehicleDto>)

@JsonClass(generateAdapter = true)
data class VehicleDto(
    val id: String,
    val name: String,
    val title: String,
    val make: String,
    val model: String,
    val year: String,
    val price: String
)