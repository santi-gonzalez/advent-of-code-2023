package challenges.challenge6

data class Ch6Data(val races: List<Ch6Race>) {
    data class Ch6Race(val time: Long, val record: Long) {
        fun getWaysOfWinning(): Int {
            var count = 0
            for (pushTime in (time / 2) + 1 until time) {
                if (beatsRecord(pushTime)) count++
                else break
            }
            for (pushTime in (time / 2) downTo 1) {
                if (beatsRecord(pushTime)) count++
                else break
            }
            return count
        }

        private fun beatsRecord(pushTime: Long) = (time - pushTime) * pushTime > record
    }
}
