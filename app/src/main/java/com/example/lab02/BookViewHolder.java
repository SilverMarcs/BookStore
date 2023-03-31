package com.example.lab02;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
