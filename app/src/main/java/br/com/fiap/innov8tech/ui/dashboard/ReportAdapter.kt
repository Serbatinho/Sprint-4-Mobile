package br.com.fiap.innov8tech.ui.dashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.innov8tech.R

data class Report(
    val reportId: String = "",
    val twitterHandle: String = "",
    val status: String = "",
    val dateCreated: String = "",
    val fileName: String = "",
    val fileUrl: String? = null
)

class ReportAdapter(
    private var reports: List<Report>,
    private val onDetailsClicked: (Report) -> Unit,
    private val onDeleteClicked: (Report) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val handleTextView: TextView = itemView.findViewById(R.id.twitter_handle)
        val statusTextView: TextView = itemView.findViewById(R.id.report_status)
        val dateTextView: TextView = itemView.findViewById(R.id.report_date)
        val detailsButton: Button = itemView.findViewById(R.id.details_button)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.report_item, parent, false)
        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reports[position]
        holder.handleTextView.text = report.twitterHandle
        holder.statusTextView.text = report.status
        holder.dateTextView.text = "Data de criação: ${report.dateCreated}"

        holder.detailsButton.setOnClickListener {
            onDetailsClicked(report)
        }
        holder.deleteButton.setOnClickListener {
            onDeleteClicked(report)
        }
    }

    override fun getItemCount(): Int {
        return reports.size
    }

    fun updateReports(newReports: List<Report>) {
        reports = newReports
        Log.d("ReportAdapter", "Total de relatórios para exibição: ${reports.size}")
        notifyDataSetChanged()
    }
}
