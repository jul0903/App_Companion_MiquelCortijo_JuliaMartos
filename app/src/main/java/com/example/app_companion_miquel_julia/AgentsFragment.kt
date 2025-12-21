package com.example.app_companion_miquel_julia

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter:AgentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agents, container, false)
    }

    private fun initRecyclerView(view: View) {

        recyclerView=view.findViewById(R.id.agentsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = AgentsAdapter()
        recyclerView.adapter = adapter
    }

    private fun loadData(){
        val call = ValorantApiInstance.apiService.getContent(Constants.VALORANT_API_KEY)

        call.enqueue(object : Callback<ValorantResponse> {
            override fun onResponse(
                call: Call<ValorantResponse>,
                response: Response<ValorantResponse>
            ) {
                // if (agents != null)
                if (response.isSuccessful) {
                    val agents = response.body()?.characters

                    if(!agents.isNullOrEmpty()){
                        adapter.updateData(agents)
                        // Imprimimos en el Logcat
                        agents.forEach { agente -> Log.d("Valorant", "Agente: ${agente.name}, ${agente.id}") }
                    }
                } else {
                    Log.e("ValorantError", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ValorantResponse>, t: Throwable) {
                Log.e("ValorantError", "Fallo de conexión: ${t.message}")
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializo Recycler
        initRecyclerView(view)

        //Llamo a la api
        loadData()
    }
}