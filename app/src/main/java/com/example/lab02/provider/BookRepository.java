package com.example.lab02.provider;

import android.app.Application;

import androidx.lifecycle.LiveData;


import java.util.List;

// This repository is used to communicate between app and database
public class BookRepository {
    private BookDao mBookDao;
    private LiveData<List<Book>> mAllBooks;

    BookRepository(Application application) {
        BookDatabase db = BookDatabase.getDatabase(application);
        mBookDao = db.bookDao();
        mAllBooks = mBookDao.getAllBooks();
    }

    LiveData<List<Book>> getAllBooks() {
        return mAllBooks;
    }

    void insert(Book book) {
        BookDatabase.databaseWriteExecutor.execute(() -> mBookDao.addBook(book));
    }

    int getBookCount() {
        return mBookDao.getBookCount();
    }

    void deleteAll(){
        BookDatabase.databaseWriteExecutor.execute(()->{
            mBookDao.deleteAllBooks();
        });
    }
}
