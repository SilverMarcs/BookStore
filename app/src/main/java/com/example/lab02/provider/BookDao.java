package com.example.lab02.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {
    @Query("select * from books")
    LiveData<List<Book>> getAllBooks();

    @Query("select * from books where bookTitle=:name")
    List<Book> getBook(String name);

    @Query("SELECT COUNT(*) FROM books")
    int getBookCount();

    @Query("DELETE FROM books WHERE bookID = (SELECT max(bookID) FROM books);")
    void deleteLastBook();

    @Query("DELETE FROM books where bookPrice > 50")
    void deleteExpensiveBooks();

    @Insert
    void addBook(Book book);

    @Query("delete from books where bookTitle=:name")
    void deleteBook(String name);

    @Query("delete FROM books")
    void deleteAllBooks();
}
