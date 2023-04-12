package com.example.lab02;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.BookViewHolder>{

    ArrayList<Book> bookList = new ArrayList<Book>();

    public void setData(ArrayList<Book> data) {
        this.bookList = data;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false); //CardView inflated as RecyclerView list item
        BookViewHolder bookViewHolder = new BookViewHolder(cardView);
        return bookViewHolder;
    }

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

