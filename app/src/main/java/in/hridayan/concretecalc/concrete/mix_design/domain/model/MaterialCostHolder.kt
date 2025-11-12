package `in`.hridayan.concretecalc.concrete.mix_design.domain.model

import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MaterialCost
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object MaterialCostHolder {
    private val _costs = MutableStateFlow<MaterialCost?>(null)
    val costs: StateFlow<MaterialCost?> = _costs.asStateFlow()

    fun setResult(costs: MaterialCost) {
        _costs.value = costs
    }

    fun clear() {
        _costs.value = null
    }
}