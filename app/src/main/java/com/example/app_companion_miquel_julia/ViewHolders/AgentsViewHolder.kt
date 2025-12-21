package com.example.app_companion_miquel_julia.ViewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_companion_miquel_julia.R
import com.example.app_companion_miquel_julia.ApiFiles.ValorantCharacter

class AgentsViewHolder(view: View): RecyclerView.ViewHolder(view) {

    //var id = view.findViewById<TextView>(R.id.agentId)
    var name  = view.findViewById<TextView>(R.id.agentName)
    var image = view.findViewById<ImageView>(R.id.agentImage)

    fun bind(agent: ValorantCharacter){
        name.text = agent.name
        //id.text = agent.id

        // Lo suyo sería hacer: Glide.with(image.context).load(agent.assetName).into(image) (y añadir la librería Glide al proyecto (está añadida))
        // Pero la api no tiene imágenes, así que voy a usar imágenes desde drawable.
        //Convierto assetName en el nombre de mi drawable agent_(nombre del agente)
        val newName = "agent_" + agent.name.lowercase()

        //Puente para acceder a drawable
        val context = itemView.context
        //Busco el newName en com.example.app_companion_miquel_julia --> resources --> drawable
        val imageId = context.resources.getIdentifier(newName, "drawable", context.packageName)

        // Muestro la imagen del agente y añado un placeholder por si no encuentra la imagen del agente
        if(imageId != 0){
            image.setImageResource(imageId)
        }else{
            image.setImageResource(R.drawable.agent_placeholder)
        }
    }
}