package br.com.fiap.innov8tech.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.innov8tech.ui.dashboard.Report

class NotificationsViewModel : ViewModel() {

    private val _completedReports = MutableLiveData<List<Report>>()
    val completedReports: LiveData<List<Report>> = _completedReports

    init {
        _completedReports.value = emptyList()
    }

    fun addCompletedReport(report: Report) {
        _completedReports.value = _completedReports.value?.plus(report) ?: listOf(report)
    }
}
