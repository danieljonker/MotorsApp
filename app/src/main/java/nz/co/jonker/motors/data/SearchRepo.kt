package nz.co.jonker.motors.data

import javax.inject.Inject

class SearchRepoImpl @Inject constructor(private val service: SearchService) : SearchRepo {
    override suspend fun search(make: String, model: String, year: String): List<VehicleDto> {
        return service.search(make, model, year).searchResults
    }
}

interface SearchRepo {
    suspend fun search(make: String, model: String, year: String): List<VehicleDto>
}
