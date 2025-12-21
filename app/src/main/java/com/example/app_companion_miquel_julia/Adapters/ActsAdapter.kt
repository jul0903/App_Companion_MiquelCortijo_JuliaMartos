package com.example.app_companion_miquel_julia.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_companion_miquel_julia.ApiFiles.ValorantAct
import com.example.app_companion_miquel_julia.R
import com.example.app_companion_miquel_julia.ViewHolders.ActsViewHolder

//Llamaré a esta función y le pasaré el act
class ActsAdapter(val acts:List<ValorantAct> = emptyList(), private val onActClick:(ValorantAct) -> Unit): RecyclerView.Adapter<ActsViewHolder>() {

    private var fullActList: MutableList<ValorantAct> = mutableListOf()
    private var searchActList: MutableList<ValorantAct> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ActsViewHolder(layoutInflater.inflate(R.layout.item_act, parent, false))
    }

    override fun getItemCount(): Int {
        return searchActList.size
    }

    override fun onBindViewHolder(holder: ActsViewHolder, position: Int) {
        val act = searchActList[position]
        holder.bind(act)
        //Aviso al fragment
        holder.itemView.setOnClickListener { onActClick(act) }
    }

    fun updateData(newActs: List<ValorantAct>) {
        fullActList.clear()
        fullActList.addAll(newActs)

        searchActList.clear()
        searchActList.addAll(newActs)

        notifyDataSetChanged()
    }

    fun searchAct(query:String){
        searchActList.clear()
        if(query.isBlank()){
            searchActList.addAll(fullActList)
        }
        //Si hay texto
        else{
            searchActList.addAll(fullActList.filter { act ->
                act.name.contains(query, ignoreCase = true) ||
                act.type.contains(query, ignoreCase = true)})
        }
        notifyDataSetChanged()
    }
}