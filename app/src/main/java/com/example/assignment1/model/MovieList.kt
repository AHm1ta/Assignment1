package com.example.assignment1.model

import com.example.assignment1.model.PopularMovie
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class MovieList {
    @SerializedName("page")
    @Expose
    private val page: Int? = null

    @SerializedName("results")
    @Expose
    val results: List<PopularMovie> = ArrayList()

    @SerializedName("total_results")
    @Expose
    private val totalResults: Int? = null

    @SerializedName("total_pages")
    @Expose
    private val totalPages: Int? = null
}