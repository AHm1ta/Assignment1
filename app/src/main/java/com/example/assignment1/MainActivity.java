package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.assignment1.Adapter.MovieAdapter;
import com.example.assignment1.Model.MovieList;
import com.example.assignment1.Model.PopularMovie;
import com.example.assignment1.Service.Service;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.assignment1.Pagination.PAGE_START;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MovieList movieLists = new MovieList();
   // LinearLayoutManager manager;
    GridLayoutManager manager;
    ProgressBar pb;
    MovieAdapter movieAdapter;
    Service service;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private final int totalPage = 10;
    private boolean isLoading = false;
    //https://www.youtube.com/watch?v=pM1fAmUQn8g


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerview);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        pb= findViewById(R.id.pb);
        //manager = new LinearLayoutManager(this);
        manager= new GridLayoutManager(this,2);
        LoadingPage();

        recyclerView.addOnScrollListener(new Pagination(manager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadSecondPage();
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void LoadingPage() {
        service = Service.retrofit.create(Service.class);
        Call<MovieList> call = service.getItems("3354c6563712f6717437182b5fa0e039", currentPage);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieList movieList = response.body();
                    ArrayList<PopularMovie> popularMovies1 = new ArrayList<>(movieList.getResults());
                    movieAdapter = new MovieAdapter(popularMovies1, MainActivity.this);
                    movieAdapter.addItems(popularMovies1);
                    recyclerView.setAdapter(movieAdapter);
                    recyclerView.setLayoutManager(manager);
                    pb.setVisibility(View.GONE);


                    if (currentPage <= totalPage) movieAdapter.addLoading();
                    else isLastPage = true;

                    for (int i = 0; i < popularMovies1.size()-1; i++) {
                        Log.d("check", popularMovies1.get(i).getPosterPath());
                    }
                }
            }
            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("failed", t.getMessage());
            }
        });

    }

    private void loadSecondPage() {
        service = Service.retrofit.create(Service.class);
        Call<MovieList> call = service.getItems("3354c6563712f6717437182b5fa0e039", currentPage);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                movieAdapter.removeLoading();
                isLoading = false;
                movieLists = response.body();
                MovieList movieList = response.body();
                ArrayList<PopularMovie> popularMovies1 = new ArrayList<>(movieList.getResults());
                movieAdapter = new MovieAdapter(popularMovies1, MainActivity.this);
                recyclerView.setAdapter(movieAdapter);
                recyclerView.setLayoutManager(manager);
                movieAdapter.addItems(popularMovies1);

                if (currentPage != totalPage) movieAdapter.addLoading();
                else isLastPage = true;
                for (int i = 0; i < popularMovies1.size() -1; i++) {
                    Log.d("check", popularMovies1.get(i).getPosterPath());
                }

            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("failed", t.getMessage());
            }
        });
    }
}

