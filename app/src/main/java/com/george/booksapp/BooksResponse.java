package com.george.booksapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class represents root json response from the the Google books search API
 * It contains list of book items which holds all the information.
 */
public class BooksResponse {

    @SerializedName("items")
    @Expose
    private List<BookInfo> items = null;

    public List<BookInfo> getItems() {
        return items;
    }

    public void setItems(List<BookInfo> items) {
        this.items = items;
    }

}