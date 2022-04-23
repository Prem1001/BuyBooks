package com.george.booksapp;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This interface contains all web service definitions for fetching books data from Google Books API
 * All service definitions returns OkHttp {@link Call} object which can be enqueued in order to fetch data asynchronously
 **/
public interface BookService {

    @GET("volumes")
    Call<BooksResponse> searchBooks(
            @Query("q") String query,
            @Query("key") String apiKey,
            @Query("maxResults") int maxResults
    );

}
