package com.example.app_companion_miquel_julia

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AgentsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var id = view.findViewById<TextView>(R.id.agentId)
    var name  = view.findViewById<TextView>(R.id.agentName)

    fun bind(agent: ValorantCharacter){
        name.text = agent.name
        id.text = agent.id
    }
}