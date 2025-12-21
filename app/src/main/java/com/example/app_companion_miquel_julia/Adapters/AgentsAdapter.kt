package com.example.app_companion_miquel_julia.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_companion_miquel_julia.ViewHolders.AgentsViewHolder
import com.example.app_companion_miquel_julia.R
import com.example.app_companion_miquel_julia.ApiFiles.ValorantCharacter

class AgentsAdapter(val agents:List<ValorantCharacter> = emptyList()):RecyclerView.Adapter<AgentsViewHolder>()  {

    private var fullAgentList: MutableList<ValorantCharacter> = mutableListOf()
    private var searchAgentList: MutableList<ValorantCharacter> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgentsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AgentsViewHolder(layoutInflater.inflate(R.layout.item_agent, parent, false))
    }

    override fun onBindViewHolder(holder: AgentsViewHolder, position: Int) {
        holder.bind(searchAgentList[position])
    }

    override fun getItemCount(): Int {
        return searchAgentList.size
    }

    fun updateData(newAgents: List<ValorantCharacter>) {
        //Lista "base" (la uqe no se modifica)
        fullAgentList.clear()
        fullAgentList.addAll(newAgents)

        //Lista que cambia segun lo que se busca en el buscador
        searchAgentList.clear()
        searchAgentList.addAll(newAgents)

        notifyDataSetChanged() // Refresh de pantalla
    }

    fun searchAgent(query:String){
        searchAgentList.clear()
        //Si no hay texto
        if(query.isBlank()){
            searchAgentList.addAll(fullAgentList)
        }
        //Si hay texto
        else{
            searchAgentList.addAll(fullAgentList.filter { it.name.contains(query, ignoreCase = true) })
        }
        notifyDataSetChanged()
    }
}