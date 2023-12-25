package challenges.challenge24

import com.microsoft.z3.Context
import com.microsoft.z3.Status
import java.math.BigDecimal

class Ch24Data(private val hailstones: List<Ch24Hailstone>) {
    fun calculateIntersections(): Int {
//        val range = BigDecimal("7")..BigDecimal("27")
        val range = BigDecimal("200000000000000")..BigDecimal("400000000000000")
        var count = 0
        for (i in hailstones.indices) {
            for (j in i + 1 until hailstones.size) {
                val h1 = hailstones[i]
                val h2 = hailstones[j]
                // check if they are not parallel
                if (h1.a * h2.b != h1.b * h2.a) {
                    // find collision point using Cramer's Rule
                    val x = (h1.c * h2.b - h2.c * h1.b) / (h1.a * h2.b - h2.a * h1.b)
                    val y = (h2.c * h1.a - h1.c * h2.a) / (h1.a * h2.b - h2.a * h1.b)
                    // count only if criteria is met (the possible collision would be in bounds and in the future)
                    if (isInBounds(range, x, y) && willIntersectInTheFuture(h1, h2, x, y)) {
                        count++
                    }
                }
            }
        }
        return count
    }

    private fun willIntersectInTheFuture(
        hailstone1: Ch24Hailstone,
        hailstone2: Ch24Hailstone,
        x: BigDecimal,
        y: BigDecimal
    ) = setOf(hailstone1, hailstone2).all { willIntersectInTheFuture(it, x, y) }

    private fun willIntersectInTheFuture(
        hailstone: Ch24Hailstone,
        x: BigDecimal,
        y: BigDecimal
    ) = (x - hailstone.x) * hailstone.vx >= BigDecimal.ZERO && (y - hailstone.y) * hailstone.vy >= BigDecimal.ZERO

    private fun isInBounds(
        range: ClosedRange<BigDecimal>,
        x: BigDecimal,
        y: BigDecimal
    ): Boolean = x in range && y in range

    // I got (stole?) this solution directly from a Reddit thread. A million thanks to the author, which I can't remember their
    // name. For me, the challenge was to understand the problem and the strategy behind, and also setting up the Z3 environment
    fun findThrowingPoint(): Long {
        val hail = hailstones
        val ctx = Context()
        val solver = ctx.mkSolver()
        val mx = ctx.mkRealConst("mx")
        val m = ctx.mkRealConst("m")
        val mz = ctx.mkRealConst("mz")
        val mxv = ctx.mkRealConst("mxv")
        val mv = ctx.mkRealConst("mv")
        val mzv = ctx.mkRealConst("mzv")
        repeat(3) {
            val (sx, sy, sz, sxv, syv, szv) = hail[it]
            val t = ctx.mkRealConst("t$it")
            solver.add(
                ctx.mkEq(
                    ctx.mkAdd(mx, ctx.mkMul(mxv, t)),
                    ctx.mkAdd(ctx.mkReal(sx.toString()), ctx.mkMul(ctx.mkReal(sxv.toString()), t))
                )
            )
            solver.add(
                ctx.mkEq(
                    ctx.mkAdd(m, ctx.mkMul(mv, t)),
                    ctx.mkAdd(ctx.mkReal(sy.toString()), ctx.mkMul(ctx.mkReal(syv.toString()), t))
                )
            )
            solver.add(
                ctx.mkEq(
                    ctx.mkAdd(mz, ctx.mkMul(mzv, t)),
                    ctx.mkAdd(ctx.mkReal(sz.toString()), ctx.mkMul(ctx.mkReal(szv.toString()), t))
                )
            )
        }
        if (solver.check() == Status.SATISFIABLE) {
            val model = solver.model
            val solution = listOf(mx, m, mz).sumOf { model.eval(it, false).toString().toDouble() }
            return solution.toLong()
        }
        return 0L
    }

    fun print(): Ch24Data {
        hailstones.forEach { hailstone ->
            println(hailstone)
        }
        return this
    }

    data class Ch24Hailstone(
        val x: BigDecimal,
        val y: BigDecimal,
        val z: BigDecimal,
        val vx: BigDecimal,
        val vy: BigDecimal,
        val vz: BigDecimal
    ) {
        val a = vy
        val b = -vx
        val c = vy * x - vx * y

        override fun toString(): String = "$x, $y, $z @ $vx, $vy, $vz"
    }
}
