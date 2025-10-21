package `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000

data class CurvePoint(val wcRatio: Double, val targetStrength: Double)

val curve1 = listOf(
    CurvePoint(0.25, 60.0),
    CurvePoint(0.30, 51.0),
    CurvePoint(0.35, 43.0),
    CurvePoint(0.40, 36.0),
    CurvePoint(0.45, 30.0),
    CurvePoint(0.50, 25.0),
    CurvePoint(0.55, 21.0),
    CurvePoint(0.60, 17.5),
    CurvePoint(0.65, 14.5),
)

val curve2 = listOf(
    CurvePoint(0.25, 65.0),
    CurvePoint(0.30, 57.0),
    CurvePoint(0.35, 48.5),
    CurvePoint(0.40, 41.5),
    CurvePoint(0.45, 35.5),
    CurvePoint(0.50, 30.0),
    CurvePoint(0.55, 25.5),
    CurvePoint(0.60, 21.5),
    CurvePoint(0.65, 18.0),
)

val curve3 = listOf(
    CurvePoint(0.25, 74.0),
    CurvePoint(0.30, 65.0),
    CurvePoint(0.35, 56.0),
    CurvePoint(0.40, 48.5),
    CurvePoint(0.45, 42.0),
    CurvePoint(0.50, 37.0),
    CurvePoint(0.55, 32.5),
    CurvePoint(0.60, 28.0),
    CurvePoint(0.65, 24.0)
)

fun getWCratioForTargetStrength(curve: List<CurvePoint>, targetStrength: Double): Double? {
    for (i in 0 until curve.size - 1) {
        val p1 = curve[i]
        val p2 = curve[i + 1]
        if (targetStrength in p2.targetStrength..p1.targetStrength || targetStrength in p1.targetStrength..p2.targetStrength) {
                val x = p1.wcRatio + (targetStrength - p1.targetStrength) * (p2.wcRatio - p1.wcRatio) / (p2.targetStrength - p1.targetStrength)
            return x
        }
    }
    return null
}

