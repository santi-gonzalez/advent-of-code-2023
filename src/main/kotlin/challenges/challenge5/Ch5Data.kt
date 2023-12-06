package challenges.challenge5

data class Ch5Data(
    val seeds: List<Long>,
    val seedToSoil: List<Triple<Long, Long, Long>>,
    val soilToFertilizer: List<Triple<Long, Long, Long>>,
    val fertilizerToWater: List<Triple<Long, Long, Long>>,
    val waterToLight: List<Triple<Long, Long, Long>>,
    val lightToTemperature: List<Triple<Long, Long, Long>>,
    val temperatureToHumidity: List<Triple<Long, Long, Long>>,
    val humidityToLocation: List<Triple<Long, Long, Long>>,
) {
    fun mapSeedToLocation(seed: Long): Long =
        mapHumidityToLocation(
            mapTemperatureToHumidity(
                mapLightToTemperature(
                    mapWaterToLight(
                        mapFertilizerToWater(
                            mapSoilToFertilizer(
                                mapSeedToSoil(seed)
                            )
                        )
                    )
                )
            )
        )

    fun mapLocationToSeed(location: Long): Long =
        mapSoilToSeed(
            mapFertilizerToSoil(
                mapWaterToFertilizer(
                    mapLightToWater(
                        mapTemperatureToLight(
                            mapHumidityToTemperature(
                                mapLocationToHumidity(location)
                            )
                        )
                    )
                )
            )
        )

    private fun mapSeedToSoil(seed: Long): Long = map(seed, seedToSoil)
    private fun mapSoilToFertilizer(soil: Long): Long = map(soil, soilToFertilizer)
    private fun mapFertilizerToWater(fertilizer: Long): Long = map(fertilizer, fertilizerToWater)
    private fun mapWaterToLight(water: Long): Long = map(water, waterToLight)
    private fun mapLightToTemperature(light: Long): Long = map(light, lightToTemperature)
    private fun mapTemperatureToHumidity(temperature: Long): Long = map(temperature, temperatureToHumidity)
    private fun mapHumidityToLocation(humidity: Long): Long = map(humidity, humidityToLocation)

    private fun mapSoilToSeed(soil: Long): Long = reverseMap(soil, seedToSoil)
    private fun mapFertilizerToSoil(fetilizer: Long): Long = reverseMap(fetilizer, soilToFertilizer)
    private fun mapWaterToFertilizer(water: Long): Long = reverseMap(water, fertilizerToWater)
    private fun mapLightToWater(light: Long): Long = reverseMap(light, waterToLight)
    private fun mapTemperatureToLight(temperature: Long): Long = reverseMap(temperature, lightToTemperature)
    private fun mapHumidityToTemperature(humidity: Long): Long = reverseMap(humidity, temperatureToHumidity)
    private fun mapLocationToHumidity(location: Long): Long = reverseMap(location, humidityToLocation)

    private fun map(source: Long, mapper: List<Triple<Long, Long, Long>>): Long {
        mapper.forEach { (to, from, range) ->
            val mappedValue = source + to - from
            if (from + range > source) {
                return mappedValue
            }
        }
        return source
    }

    private fun reverseMap(source: Long, mapper: List<Triple<Long, Long, Long>>): Long {
        mapper.forEach { (to, from, range) ->
            val mappedValue = source - to + from
            if (mappedValue >= from && mappedValue < from + range) {
                return mappedValue
            }
        }
        return source
    }

    fun getRanges(): List<LongRange> = seeds
        .windowed(size = 2, step = 2, partialWindows = false)
        .sortedBy { it[0] }
        .map { (seedStart: Long, range: Long) ->
            LongRange(seedStart, seedStart + range - 1)
        }

}
