package com.george.booksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic adapter is a common recyclerview adapter which can be extended in order to fit the list items in rows/columns.
 * It abstracts the binding of item UI to data model object.
 * @param <T> type of data
 * @param <D> type of view binding
 */
public abstract class GenericAdapter<T, D> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<T> mArrayList;

    /**
     *  this method returns the layout resource for item in the list
     */
    public abstract int getLayoutResId();

    /**
     * this method binds the data model to ui
     */
    public abstract void onBindData(T model, int position, D dataBinding);

    /**
     *  this method is listens the click event on item in the list
     */
    public abstract void onItemClick(T model, int position);

    public GenericAdapter(Context context, ArrayList<T> arrayList) {
        this.mContext = context;
        this.mArrayList = arrayList;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        //inflating the view and returning the view holder
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResId(), parent, false);
        RecyclerView.ViewHolder holder = new ItemViewHolder(dataBinding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {
        //binding the data model to ui
        onBindData(mArrayList.get(position), position, ((ItemViewHolder) holder).mDataBinding);

        //setting up click event listener on item view root
        ((ViewDataBinding) ((ItemViewHolder) holder).mDataBinding).getRoot().setOnClickListener(view -> onItemClick(mArrayList.get(position), position));
    }

    /**
     * returns count of items in the list
     */
    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    /**
     * returns data model on specific position
     */
    public T getItem(int position) {
        return mArrayList.get(position);
    }

    /**
     * this inner is a recyclerview view holder which is used by android framework to hold ui elements inside item view
     */
     class ItemViewHolder extends RecyclerView.ViewHolder {
        protected D mDataBinding;

        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mDataBinding = (D) binding;
        }
    }
}