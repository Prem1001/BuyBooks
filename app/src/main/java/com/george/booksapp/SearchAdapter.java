package com.george.booksapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.george.booksapp.databinding.ItemBookBinding;

import java.util.ArrayList;

/**
 * this class extends the {@link GenericAdapter} and binds the book response search results to book item UI
 * {@link #onBindData} method does all the binding work
 */
public class SearchAdapter extends GenericAdapter<BookInfo, ItemBookBinding> {

    public SearchAdapter(Context context, ArrayList<BookInfo> arrayList) {
        super(context, arrayList);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.item_book;
    }

    @Override
    public void onBindData(BookInfo model, int position, ItemBookBinding dataBinding) {
        //if response contains authors, showing comma-separated authro names, otherwise hiding the text view for the same
        if (model.getVolumeInfo().getAuthors() != null && !model.getVolumeInfo().getAuthors().isEmpty()) {
            dataBinding.txtBookAuthor.setVisibility(View.VISIBLE);
            dataBinding.txtBookAuthor.setText(TextUtils.join(",", model.getVolumeInfo().getAuthors()));
        } else {
            dataBinding.txtBookAuthor.setVisibility(View.GONE);
        }

        //binding name and subtitle to ui
        dataBinding.txtBookName.setText(model.getVolumeInfo().getTitle());
        dataBinding.txtBookSubtitle.setText(model.getVolumeInfo().getSubtitle());

        //setting up click listener on add to wish list button, future implementation will save that book item in a wishlist in the database
        dataBinding.imgAddWishlist.setOnClickListener(v -> {
            Toast.makeText(dataBinding.getRoot().getContext(), R.string.info_wishlist, Toast.LENGTH_SHORT).show();
            onItemClick(model, position);
        });

        //if thumbnail images are available loading them in the image view with glide library
        if (model.getVolumeInfo().getImageLinks() != null) {
            if (model.getVolumeInfo().getImageLinks().getThumbnail() != null) {
                Glide.with(dataBinding.getRoot().getContext()).load(model.getVolumeInfo().getImageLinks().getThumbnail()).into(dataBinding.imgBook);
            } else if (model.getVolumeInfo().getImageLinks().getSmallThumbnail() != null) {
                Glide.with(dataBinding.getRoot().getContext()).load(model.getVolumeInfo().getImageLinks().getSmallThumbnail()).into(dataBinding.imgBook);
            } else {
                dataBinding.imgBook.setImageResource(R.drawable.ic_book_black_24dp);
            }
        } else {
            //else setting the book icon in image view
            dataBinding.imgBook.setImageResource(R.drawable.ic_book_black_24dp);
        }
    }

    @Override
    public void onItemClick(BookInfo model, int position) {
        //no implementation for add to wish list right now.
    }
}