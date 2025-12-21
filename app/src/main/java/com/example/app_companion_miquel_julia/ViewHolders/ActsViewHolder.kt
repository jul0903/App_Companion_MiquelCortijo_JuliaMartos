package com.example.app_companion_miquel_julia.ViewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_companion_miquel_julia.ApiFiles.ValorantAct
import com.example.app_companion_miquel_julia.R

class ActsViewHolder (view: View): RecyclerView.ViewHolder(view) {

    var type = view.findViewById<TextView>(R.id.actType)
    var name  = view.findViewById<TextView>(R.id.actName)
    var isActive = view.findViewById<TextView>(R.id.actIsActive)

    fun bind(act: ValorantAct) {
        type.text = act.type
        name.text = act.name
        isActive.text = act.isActive.toString()
    }
}