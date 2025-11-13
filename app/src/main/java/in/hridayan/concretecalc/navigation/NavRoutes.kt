package `in`.hridayan.concretecalc.navigation

import kotlinx.serialization.Serializable

sealed class NavRoutes {
    @Serializable
    object HomeScreen

    @Serializable
    object MixDesignScreen

    @Serializable
    object ResultsScreen

    @Serializable
    object SavedResultsScreen

}
