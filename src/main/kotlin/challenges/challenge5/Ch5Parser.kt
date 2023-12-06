package challenges.challenge5

object Ch5Parser {
    fun parse(source: String, seedPairs: Boolean = false): Ch5Data {
        val seeds: MutableList<Long> = mutableListOf()
        val mapSegments: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        val seedToSoil: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        val soilToFertilizer: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        val fertilizerToWater: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        val waterToLight: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        val lightToTemperature: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        val temperatureToHumidity: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        val humidityToLocation: MutableList<Triple<Long, Long, Long>> = mutableListOf()
        source
            .lines()
            .filter { it.isNotEmpty() }
            .toMutableList()
            .apply { add("end") }
            .forEachIndexed { index, line ->
                if (index == 0) {
                    seeds.addAll(line.split(":")[1].split(" ").mapNotNull { it.toLongOrNull() })
                } else {
                    when {
                        line.find { it.isDigit() } == null -> {
                            if (mapSegments.isNotEmpty()) {
                                mapSegments.sortBy { it.second }
                                if (mapSegments[0].second > 0) {
                                    mapSegments.add(0, Triple(0, 0, mapSegments[0].second))
                                }
                                if (seedToSoil.isEmpty()) {
                                    seedToSoil.addAll(mapSegments)
                                } else if (soilToFertilizer.isEmpty()) {
                                    soilToFertilizer.addAll(mapSegments)
                                } else if (fertilizerToWater.isEmpty()) {
                                    fertilizerToWater.addAll(mapSegments)
                                } else if (waterToLight.isEmpty()) {
                                    waterToLight.addAll(mapSegments)
                                } else if (lightToTemperature.isEmpty()) {
                                    lightToTemperature.addAll(mapSegments)
                                } else if (temperatureToHumidity.isEmpty()) {
                                    temperatureToHumidity.addAll(mapSegments)
                                } else if (humidityToLocation.isEmpty()) {
                                    humidityToLocation.addAll(mapSegments)
                                }
                                mapSegments.clear()
                            }
                        }

                        else -> {
                            val (to, from, length) = line.split(" ").mapNotNull { it.toLongOrNull() }
                            mapSegments.add(Triple(to, from, length))
                        }
                    }
                }
            }
        return Ch5Data(
            seeds,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation,
        )
    }
}
