package br.com.fiap.innov8tech.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel : ViewModel() {

    private val _reports = MutableLiveData<List<Report>>()
    val reports: LiveData<List<Report>> get() = _reports

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    init {
        loadExistingReports()
    }

    private fun getUserReportsReference() = firebaseAuth.currentUser?.let {
        database.child("user_reports").child(it.uid)
    }

    fun loadExistingReports() {
        // Limpa a lista antes de carregar novos dados
        _reports.value = emptyList()
        forceNotifyLiveData() // Força notificação da limpeza

        val userReportsRef = getUserReportsReference() ?: return
        Log.d("DashboardViewModel", "Iniciando carregamento de relatórios.")

        userReportsRef.get().addOnSuccessListener { snapshot ->
            val reportsList = mutableListOf<Report>()
            snapshot.children.forEach { reportSnapshot ->
                val report = reportSnapshot.getValue(Report::class.java)
                report?.let {
                    Log.d("DashboardViewModel", "Processando relatório: ${it.fileName}")
                    reportsList.add(it)
                }
            }
            // Atualiza _reports com uma nova lista
            _reports.value = reportsList.toList()
            Log.d("DashboardViewModel", "Total de relatórios carregados: ${reportsList.size}")
            forceNotifyLiveData() // Força notificação após a atualização
        }.addOnFailureListener { exception ->
            Log.e("DashboardViewModel", "Erro ao carregar relatórios: ${exception.message}")
        }
    }





    fun requestNewReport(twitterHandle: String) {
        val reportId = UUID.randomUUID().toString()
        val fileName = "$twitterHandle-$reportId.txt"
        val dateCreated = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val newReport = Report(reportId, twitterHandle, "Em processamento", dateCreated, fileName)
        _reports.value = _reports.value?.plus(newReport) ?: listOf(newReport)

        loadExistingReports()
        saveReportToRealtimeDatabase(reportId, newReport)
    }


    private fun saveReportToRealtimeDatabase(reportId: String, report: Report) {
        val userReportsRef = getUserReportsReference()?.child(reportId) ?: return

        userReportsRef.setValue(report)
            .addOnSuccessListener {
                Log.d("DashboardViewModel", "Relatório salvo com sucesso")
                val updatedReport = report.copy(status = "Concluído")
                _reports.value = _reports.value?.map {
                    if (it.fileName == report.fileName) updatedReport else it
                }
                forceNotifyLiveData()
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardViewModel", "Erro ao salvar relatório: ${exception.message}")
            }
    }

    fun clearReports() {
        _reports.value = emptyList()
        Log.d("DashboardViewModel", "Reports cleared after logout")
        forceNotifyLiveData()
    }

    fun reloadReports() {
        clearReports()
        loadExistingReports()
    }

    private fun forceNotifyLiveData() {
        _reports.value = _reports.value
    }

    fun deleteReport(report: Report) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val databaseRef = FirebaseDatabase.getInstance().reference.child("user_reports").child(userId).child(report.reportId)

            databaseRef.removeValue().addOnSuccessListener {
                _reports.value = _reports.value?.filterNot { it.reportId == report.reportId }
                Log.d("DashboardViewModel", "Relatório deletado com sucesso do Firebase Realtime Database.")
            }.addOnFailureListener { exception ->
                Log.e("DashboardViewModel", "Erro ao deletar relatório: ${exception.message}")
            }
        }
    }


    fun getCurrentUser() = firebaseAuth.currentUser
}
