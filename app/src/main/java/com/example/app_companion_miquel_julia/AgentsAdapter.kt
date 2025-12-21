package com.example.app_companion_miquel_julia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AgentsAdapter(val agents:List<ValorantCharacter> = emptyList()):RecyclerView.Adapter<AgentsViewHolder>()  {

    private var agentList: List<ValorantCharacter> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AgentsViewHolder(layoutInflater.inflate(R.layout.item_agent, parent, false))
    }

    override fun onBindViewHolder(holder: AgentsViewHolder, position: Int) {
        holder.bind(agentList[position])
    }

    override fun getItemCount(): Int {
        return agentList.size
    }

    fun updateData(newAgents: List<ValorantCharacter>) {
        this.agentList = newAgents
        notifyDataSetChanged() // Esto es lo que hace que la lista se refresque en pantalla
    }
}