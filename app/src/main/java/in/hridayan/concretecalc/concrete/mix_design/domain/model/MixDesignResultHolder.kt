package `in`.hridayan.concretecalc.concrete.mix_design.domain.model

import `in`.hridayan.concretecalc.concrete.mix_design.data.model.MixDesignResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


object MixDesignResultHolder {
    private val _result = MutableStateFlow<MixDesignResult?>(null)
    val result: StateFlow<MixDesignResult?> = _result.asStateFlow()

    fun setResult(newResult: MixDesignResult) {
        _result.value = newResult
    }

    fun clear() {
        _result.value = null
    }
}