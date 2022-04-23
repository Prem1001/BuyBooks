package com.george.booksapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.george.booksapp.databinding.FragmentSearchBinding;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this fragment represents the screen for search UI
 */
public class SearchFragment extends Fragment {

    private final BookService bookService = ServiceGenerator.createService(BookService.class);
    private FragmentSearchBinding binding;
    private ArrayList<BookInfo> books;
    private SearchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating the view in fragment
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //when view gets created, listening all user searches on search bar
        binding.searchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
            @Override
            public void onSearchConfirmed(CharSequence text) {
                super.onSearchConfirmed(text);
                //when search is confirmed by user, calling the method which fetches data from books API
                if (!TextUtils.isEmpty(text)) {
                    searchBooks(text.toString());
                }
            }
        });
        //keeping search bar open by default
        binding.searchBar.openSearch();

        //taking a blank list of books and setting up adapter with vertical list configuration on recyclerview
        books = new ArrayList<>();
        binding.listBooks.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new SearchAdapter(getContext(), books);
        binding.listBooks.setAdapter(adapter);
    }

    /**
     * this method calls google books API and updates the search results in the view
     * @param query search query - can be anything including author, name or isbn
     */
    private void searchBooks(String query) {
        //showing a loader dialog till it fetches the data
        SimpleArcDialog mDialog = new SimpleArcDialog(getContext());
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();

        //calling web service with given query and max 40 search results(google books api returns only 40 max results for any query)
        Call<BooksResponse> bookInfoCall = bookService.searchBooks(query, getString(R.string.books_api_key),40);

        //enqueuing the  api call asynchronously
        bookInfoCall.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                //when response arrives, checking if user has not left the screen
                if (isDetached()) return;

                //dismissing loader dialog
                mDialog.dismiss();

                //checking if response is successful
                if (response.isSuccessful()) {
                    //checking if results are found within response body for the given query or not
                    BooksResponse booksResponse = response.body();
                    if (booksResponse == null || booksResponse.getItems() == null || booksResponse.getItems().isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_books_found, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //if results are found, clearing the original list and refilling it
                    List<BookInfo> booksResponseItems = booksResponse.getItems();
                    books.clear();
                    books.addAll(booksResponseItems);

                    //also notifying the ui that list has been updated
                    adapter.notifyDataSetChanged();

                    //closing the search bar
                    binding.searchBar.closeSearch();
                } else {
                    //in case of bad response, showing the error message on UI
                    Toast.makeText(getContext(), R.string.err_search, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                //in case of failure, dismissing loader, printing the err to console, closing search bar and showing an error message on UI
                if (isDetached()) return;
                mDialog.dismiss();
                t.printStackTrace();
                binding.searchBar.closeSearch();
                Toast.makeText(getContext(), R.string.err_search, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
