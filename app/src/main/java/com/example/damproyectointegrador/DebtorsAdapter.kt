package com.example.damproyectointegrador

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.damproyectointegrador.entities.EMember

class DebtorsAdapter(private val debtors: List<EMember>) :
    RecyclerView.Adapter<DebtorsAdapter.DebtorViewHolder>() {

    class DebtorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvMemberName)
        val dniTextView: TextView = itemView.findViewById(R.id.tvMemberDni)
        val dueDateTextView: TextView = itemView.findViewById(R.id.tvDueDate)
        val membershipTextView: TextView = itemView.findViewById(R.id.tvMembershipNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebtorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_debtor, parent, false)
        return DebtorViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DebtorViewHolder, position: Int) {
        val debtor = debtors[position]
        holder.nameTextView.text = "${debtor.firstname} ${debtor.lastname}"  //"${debtor.firstname} ${debtor.lastname}"
        holder.dniTextView.text = "DNI: " + debtor.dni
        holder.dueDateTextView.text = "Fecha de vencimiento: " + debtor.dueFeeDate
        holder.membershipTextView.text = "Nº de socio: " + debtor.nMember.toString()
    }

    override fun getItemCount() = debtors.size
}