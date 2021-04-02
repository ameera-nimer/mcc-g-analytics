package com.example.googleanalytics1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private ArrayList<Category> mArrayList;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cat_name1;


        public ViewHolder(View view) {
            super(view);
            cat_name1 = (TextView) view.findViewById(R.id.cat_name1);

        }


    }


    public CategoryAdapter(ArrayList<Category> mArrayList, OnItemClickListener listener) {
        this.mArrayList = mArrayList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cat_name1.setText("" + mArrayList.get(position).getCategoeyName());
        holder.itemView.setOnClickListener(v -> {
            listener.onclick(v, position, "Delete");
        });

    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    interface OnItemClickListener {
        void onclick(View v, int postion, String tag);
    }


}
