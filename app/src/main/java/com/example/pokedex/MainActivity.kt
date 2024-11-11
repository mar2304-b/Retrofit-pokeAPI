package com.example.pokedex

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import api.PokemonAPIService
import com.google.gson.GsonBuilder
import models.Pokemon
import models.PokemonFetchResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        var listView = this.findViewById<ListView>(R.id.listView)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*val ListElements = arrayOf(
            "Android",
            "PHP"
        )

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this@MainActivity, android.R.layout.simple_list_item_1,
            ListElements
        )

        listView.setAdapter(adapter)*/

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
        val pokemonApiService = retrofit.create(PokemonAPIService::class.java)
        val call = pokemonApiService.getPokemons()
        Log.d("MainActivity", "Después de crear el call")
        call.enqueue(object: Callback<PokemonFetchResults>{
            override fun onResponse(call: Call<PokemonFetchResults>, response: Response<PokemonFetchResults>) {
                Log.d("MainActivity", response.body().toString())
                if (response.isSuccessful) {
                    var listView = findViewById<ListView>(R.id.listView)
                    var ListElements = response.body()?.getResults()
                    fillList(listView, ListElements)
                    response.body()?.results?.forEach { pokemon ->
                        Log.d("MainActivity", pokemon.getName())
                    }
                    Log.d("MainActivity", response.body().toString())
                } else {
                    println("API Error: ${response.errorBody()}")
                }
            }

             override fun onFailure(call: Call<PokemonFetchResults>, t: Throwable) {
                 Log.d("MainActivity", t.message.toString())
                println("API Error: ${t.message}")
            }
        })

    }

    fun fillList(listView: ListView, ListElements: ArrayList<Pokemon>?) {
        val adapter: ArrayAdapter<Pokemon> = ArrayAdapter<Pokemon>(
            this@MainActivity, android.R.layout.simple_list_item_1,
            ListElements?: ArrayList<Pokemon>()
        )
        listView.setAdapter(adapter)
    }

}