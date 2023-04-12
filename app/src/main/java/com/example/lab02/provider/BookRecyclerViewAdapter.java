package com.example.lab02.provider;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab02.R;

import java.util.ArrayList;
import java.util.List;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.BookViewHolder>{

    List<Book> bookList;

    public BookRecyclerViewAdapter() {
        this.bookList = new ArrayList<>();
    }

    public void setData(List<Book> data) {
        this.bookList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        return new BookViewHolder(cardView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.bkCardId.setText("ID: " + bookList.get(position).getId());
        holder.bkCardTitle.setText("Title: " + bookList.get(position).getTitle());
        holder.bkCardAuthor.setText("Author: " + bookList.get(position).getAuthor());
        holder.bkCardIsbn.setText("Isbn: " + bookList.get(position).getIsbn());
        holder.bkCardDesc.setText("Desc: " + bookList.get(position).getDescription());
        holder.bkCardPrice.setText("Price: " + bookList.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        public TextView bkCardId;
        public TextView bkCardTitle;
        public TextView bkCardAuthor;
        public TextView bkCardIsbn;
        public TextView bkCardDesc;
        public TextView bkCardPrice;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bkCardId = itemView.findViewById(R.id.card_book_id);
            bkCardTitle = itemView.findViewById(R.id.card_book_title);
            bkCardAuthor = itemView.findViewById(R.id.card_book_author);
            bkCardIsbn = itemView.findViewById(R.id.card_book_isbn);
            bkCardDesc = itemView.findViewById(R.id.card_book_desc);
            bkCardPrice = itemView.findViewById(R.id.card_book_price);
        }
    }
}

