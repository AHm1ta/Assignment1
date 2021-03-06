package com.example.assignment1.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.assignment1.model.PopularMovie;
import com.example.assignment1.R;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private ArrayList<PopularMovie> popularMovies;
    private ArrayList<PopularMovie> searchMovie;
    private Activity activity;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w500";
    private boolean isLoaderVisible = false;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;


    public MovieAdapter(ArrayList<PopularMovie> popularMovies, Activity activity) {
        this.popularMovies = popularMovies;
        searchMovie = new ArrayList<>();
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.loader, parent, false));
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (!(holder instanceof ViewHolder)) {
            return;
        }
        PopularMovie popularMovie = popularMovies.get(position);
        Glide.with(activity)
                .load(BASE_URL_IMG + popularMovie.getPosterPath()).
                diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((ViewHolder) holder).poster);
        ((ViewHolder) holder).title.setText(popularMovie.getTitle());
    }

    @Override
    public int getItemCount() {
        return popularMovies.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == popularMovies.size() - 1 ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public void addItems(List<PopularMovie> movies) {
        popularMovies.addAll(movies);
        searchMovie.addAll(movies);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        popularMovies.add(new PopularMovie());
        notifyItemInserted(popularMovies.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = popularMovies.size() - 1;
        PopularMovie movies = getItem(position);
        if (movies != null) {
            popularMovies.remove(position);
            notifyItemRemoved(position);
        }
    }

    PopularMovie getItem(int position) {
        return popularMovies.get(position);
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }
    private final Filter searchFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PopularMovie> filterList= new ArrayList<>();
            if(constraint== null || constraint.length()==0){
                filterList.addAll(searchMovie);
            }else {
                String filterPattern= constraint.toString().toLowerCase().trim();
                for (PopularMovie movie : searchMovie){
                    if (movie.getTitle().toLowerCase().contains(filterPattern)){
                        filterList.add(movie);
                    }
                }
            }
            FilterResults results= new FilterResults();
            results.values= filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            popularMovies.clear();
            popularMovies.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView poster;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            poster = itemView.findViewById(R.id.poster);

        }
    }
    public static class ProgressHolder extends RecyclerView.ViewHolder {
        ProgressHolder(View itemView) {
            super(itemView);

        }
    }

}
