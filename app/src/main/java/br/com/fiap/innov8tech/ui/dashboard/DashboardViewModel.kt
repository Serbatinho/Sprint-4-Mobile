package br.com.fiap.innov8tech.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.fiap.innov8tech.ui.notifications.NotificationsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel : ViewModel() {

    private val _reports = MutableLiveData<List<Report>>() // Lista de relatórios
    val reports: LiveData<List<Report>> get() = _reports
    private var notificationsViewModel: NotificationsViewModel? = null

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    init {
        loadExistingReports()
    }

    // Associa o NotificationsViewModel para atualizações de notificação
    fun setNotificationsViewModel(notificationsViewModel: NotificationsViewModel) {
        this.notificationsViewModel = notificationsViewModel
    }

    fun loadExistingReports() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val storageRef = firebaseStorage.reference.child("user_reports/$userId/")

            storageRef.listAll().addOnSuccessListener { listResult ->
                val reportsList = mutableListOf<Report>()

                listResult.items.forEach { fileRef ->
                    fileRef.metadata.addOnSuccessListener { metadata ->
                        val fileName = fileRef.name
                        val twitterHandle = fileName.substringBefore("-") // Extrai o @conta do nome do arquivo
                        val dateCreated = metadata.creationTimeMillis // Obtém a data de criação do arquivo no Firebase
                        val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(dateCreated))

                        val report = Report(twitterHandle, "Concluído", fileName, formattedDate)
                        reportsList.add(report)

                        _reports.value = reportsList
                    }
                }
            }.addOnFailureListener { exception ->
                Log.e("DashboardViewModel", "Erro ao carregar relatórios: ${exception.message}")
            }
        }
    }

    fun requestNewReport(twitterHandle: String) {
        val reportId = UUID.randomUUID().toString()
        val fileName = "$twitterHandle-$reportId.txt"

        val reportContent = "Relatório para o perfil @$twitterHandle."

        val dateCreated = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

        val newReport = Report(twitterHandle, "Em processamento", fileName, dateCreated)
        _reports.value = _reports.value?.plus(newReport) ?: listOf(newReport)

        saveReportToFirebaseStorage(fileName, twitterHandle, reportContent, dateCreated)
    }

    private fun saveReportToFirebaseStorage(fileName: String, twitterHandle: String, reportContent: String, dateCreated: String) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val storageRef = firebaseStorage.reference.child("user_reports/$userId/$fileName")

            val file = File.createTempFile("report_$fileName", ".txt")
            file.writeText(reportContent)

            val uploadTask = storageRef.putFile(android.net.Uri.fromFile(file))

            uploadTask.addOnSuccessListener {
                Log.d("DashboardViewModel", "Relatório salvo com sucesso no Firebase Storage")

                storageRef.downloadUrl.addOnSuccessListener {
                    val updatedReport = Report(twitterHandle, "Concluído", fileName, dateCreated)
                    _reports.value = _reports.value?.map { report ->
                        if (report.twitterHandle == twitterHandle) updatedReport else report
                    }
                    Log.d("DashboardViewModel", "Nome do arquivo salvo: $fileName")

                    // Atualizar o NotificationsViewModel
                    notificationsViewModel?.addCompletedReport(updatedReport)
                }
            }.addOnFailureListener { exception ->
                Log.e("DashboardViewModel", "Erro ao salvar relatório: ${exception.message}")
            }
        }
    }

    fun getCurrentUser() = firebaseAuth.currentUser

    fun clearReports() {
        _reports.value = emptyList()
    }
}
