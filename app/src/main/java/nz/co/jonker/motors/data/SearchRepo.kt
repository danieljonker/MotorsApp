package nz.co.jonker.motors.data

import javax.inject.Inject

class SearchRepoImpl @Inject constructor(private val service: SearchService) : SearchRepo {
    /* In a real system, these would come from an API */
    private val audis = listOf("a1", "a2", "a3", "a4", "a5", "E-Tron")
    private val fords = listOf("Cortina", "escort", "Focus", "Mustand")
    private val kias = listOf("Picanto", "Rio", "Optima")
    private val mazdas = listOf("2", "3", "5", "Bongo", "RX-7")
    private val nissans = listOf("Duke", "Qashqai", "Leaf", "Sunny", "Skyline")
    private val porches = listOf("911", "924", "Boxter", "Taycan", "Cayenne")

    //todo: unit test the Capitalization and ordering
    private val makesAndModels = mapOf(
        Pair("Audi", audis),
        Pair("Ford", fords),
        Pair("Kia", kias),
        Pair("Mazda", mazdas),
        Pair("Nissan", nissans),
        Pair("Porsche", porches)
    )

    override suspend fun search(make: String, model: String, year: String): List<VehicleDto> {
        return service.search(make, model, year).searchResults
    }

    override suspend fun getMakes(): List<String> {
        return makesAndModels.keys.toList()
    }

    override suspend fun getModels(make: String): List<String> {
        return makesAndModels[make] ?: throw IllegalArgumentException("invalid make: $make")
    }
}

interface SearchRepo {
    suspend fun search(make: String, model: String, year: String): List<VehicleDto>

    suspend fun getMakes(): List<String>
    suspend fun getModels(make: String): List<String>
}