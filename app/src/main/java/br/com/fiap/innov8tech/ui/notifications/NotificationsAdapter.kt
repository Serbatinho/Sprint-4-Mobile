package br.com.fiap.innov8tech.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.fiap.innov8tech.R
import br.com.fiap.innov8tech.ui.dashboard.Report

class NotificationsAdapter(private var completedReports: List<Report>) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    class NotificationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reportTextView: TextView = itemView.findViewById(R.id.report_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val report = completedReports[position]
        holder.reportTextView.text = "Relatório para @${report.twitterHandle} foi concluído em ${report.dateCreated}"
    }

    override fun getItemCount(): Int {
        return completedReports.size
    }

    fun updateReports(newReports: List<Report>) {
        completedReports = newReports
        notifyDataSetChanged()
    }
}
