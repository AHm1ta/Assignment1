package com.example.assignment1.Service;


import com.example.assignment1.Model.MovieList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {
    //url=http://api.themoviedb.org/3/movie/popular?api_key=3354c6563712f6717437182b5fa0e039
    String BASE_URL="http://api.themoviedb.org/3/";
    String api_key="movie/popular";
    //?api_key=3354c6563712f6717437182b5fa0e039";

    Retrofit retrofit= new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    //     @GET(api_key)
//     Call<List<MovieList>> getItems();
    @GET(api_key)
    Call<MovieList> getItems(@Query("api_key") String str,
                             @Query("page") int page);




}
