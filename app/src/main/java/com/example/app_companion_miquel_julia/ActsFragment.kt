package com.example.app_companion_miquel_julia

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_companion_miquel_julia.Adapters.ActsAdapter
import com.example.app_companion_miquel_julia.ApiFiles.ValorantApiInstance
import com.example.app_companion_miquel_julia.ApiFiles.ValorantResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ActsAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acts, container, false)
    }

    private fun initRecyclerView(view: View) {

        recyclerView=view.findViewById(R.id.actsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        adapter = ActsAdapter{act ->
            val fragment = ActsFragment2().apply {
                //Guardo las cosas que estan en ActsFragment2
                arguments = Bundle().apply {
                    putString("name", act.name)
                    putString("type", act.type)
                    putBoolean("isActive", act.isActive)
                }
            }
            //Cambio el fragment y guardo este para el back
            parentFragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit()}
        recyclerView.adapter = adapter
    }

    private fun loadData(){
        val call = ValorantApiInstance.apiService.getContent(Constants.VALORANT_API_KEY)

        call.enqueue(object : Callback<ValorantResponse> {
            override fun onResponse(
                call: Call<ValorantResponse>,
                response: Response<ValorantResponse>
            ) {
                // if (acts != null)
                if (response.isSuccessful) {
                    val acts = response.body()?.acts

                    if(!acts.isNullOrEmpty()){
                        adapter.updateData(acts)
                        // Imprimimos en el Logcat
                        acts.forEach { act -> Log.d("Valorant", "Acto: ${act.name}, ${act.type}, ${act.isActive}") }
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

    private fun searchActs(view: View){
        searchView = view.findViewById(R.id.actsSearchView)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null){
                    adapter.searchAct(query)
                } else {
                    adapter.searchAct("")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    adapter.searchAct(newText)
                } else {
                    adapter.searchAct("")
                }
                return true
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Inicializo Recycler
        initRecyclerView(view)

        //Llamo a la api
        loadData()

        //Barra de buscar
        searchActs(view)
    }
}