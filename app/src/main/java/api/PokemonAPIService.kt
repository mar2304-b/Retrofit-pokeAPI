package api;

import models.PokemonFetchResults;
import retrofit2.Call;
import retrofit2.http.GET;

interface PokemonAPIService{
    @GET("pokemon?limit=50")
    fun getPokemons(): Call<PokemonFetchResults>
}