package `in`.hridayan.concretecalc.concrete.data.is_codes._456_2000

import `in`.hridayan.concretecalc.concrete.data.model.CementGrades


data class CurvePoint(val wcRatio: Double, val targetStrength: Double)

// Curve data as per IS 456:2000 (approximate)
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
    CurvePoint(0.65, 24.0),
)

/**
 * Returns interpolated water-cement ratio for a given target strength.
 * If targetStrength is outside the curve range, clamps it to the min/max values.
 */
fun getWCratioForTargetStrength(
    gradeOfCement: CementGrades,
    targetStrength: Double
): Double {
    val curve = when (gradeOfCement) {
        CementGrades.OPC_33 -> curve1
        CementGrades.OPC_43 -> curve2
        CementGrades.OPC_53 -> curve3
        else -> curve2
    }

    // Clamp target strength to avoid out-of-range issues
    val clampedStrength = targetStrength.coerceIn(
        curve.minOf { it.targetStrength },
        curve.maxOf { it.targetStrength }
    )

    // Find two points between which this clampedStrength lies
    for (i in 0 until curve.lastIndex) {
        val p1 = curve[i]
        val p2 = curve[i + 1]

        if ((clampedStrength <= p1.targetStrength && clampedStrength >= p2.targetStrength) ||
            (clampedStrength >= p1.targetStrength && clampedStrength <= p2.targetStrength)
        ) {
            // Linear interpolation
            val ratio =
                p1.wcRatio + (clampedStrength - p1.targetStrength) *
                        (p2.wcRatio - p1.wcRatio) /
                        (p2.targetStrength - p1.targetStrength)

            return ratio
        }
    }

    // fallback (shouldn’t happen)
    return curve.last().wcRatio
}
