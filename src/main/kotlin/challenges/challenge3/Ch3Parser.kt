package challenges.challenge3

object Ch3Parser {
    fun parse(source: String): Ch3Data {
        val numbersBundle: MutableList<Ch3Data.NumberBundle> = mutableListOf()
        val symbolsBundle: MutableList<Ch3Data.SymbolBundle> = mutableListOf()
        val nextNumber = StringBuilder()
        source.lines().filter { it.isNotEmpty() }.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, char ->
                if (char.isDigit()) {
                    nextNumber.append(char)
                } else {
                    if (nextNumber.isNotEmpty()) {
                        val numberString = nextNumber.toString()
//                        val pairs = mutableListOf<Pair<Int, Int>>()
//                        for (index in numberString.indices) {
//                            pairs.add(Pair(rowIndex, columnIndex - numberString.length + index))
//                        }
                        numbersBundle.add(
                            Ch3Data.NumberBundle(
                                Pair(rowIndex, columnIndex - numberString.length),
                                numberString.toInt()
                            )
                        )
                        nextNumber.clear()
                    }
                    if (char != '.') {
                        symbolsBundle.add(Ch3Data.SymbolBundle(Pair(rowIndex, columnIndex), char == '*'))
                    }
                }
                if (columnIndex == row.length - 1 && nextNumber.isNotEmpty()) {
                    val numberString = nextNumber.toString()
                    numbersBundle.add(
                        Ch3Data.NumberBundle(
                            Pair(rowIndex, columnIndex - numberString.length + 1),
                            numberString.toInt()
                        )
                    )
                    nextNumber.clear()
                }
            }
        }
        println(numbersBundle.toString())
        println(symbolsBundle.toString())
        return Ch3Data(numbersBundle, symbolsBundle)
    }
}
