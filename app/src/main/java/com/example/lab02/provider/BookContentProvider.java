package com.example.lab02.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class BookContentProvider extends ContentProvider {

    BookDatabase db;
    private final String tableName = "books";
    public static final String CONTENT_AUTHORITY = "fit2081.app.Zabir";
    public static final Uri CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public BookContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletionCount;

        deletionCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .delete(tableName, selection, selectionArgs);

        return deletionCount;
    }

    // It is used by applications to retrieve the MIME type of the given content URI. In other words, is used to tell the other application the type of the return of the given URI.
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //    getOpenHelper is an Android Library that helps us access the database
    //
    //    contentValues is a special data structure used to hold the data of one row only. It is used to send data to the database.
    //
    //    The content values data structure uses the key-value pair format.
    //
    //    Difference between Content Values and Bundle is that the keys in the content values are the table's columns names.
    //
    //    A) Content Values can hold the data of one row only, while Cursor might contain data for multiple rows.
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db
                .getOpenHelper()
                .getWritableDatabase()
                .insert(tableName, 0, values);

        return ContentUris.withAppendedId(CONTENT_URI, rowId);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        db = BookDatabase.getDatabase(getContext());
        return true;
    }


    // This Cursor interface provides random read-write access to the result set returned by a database query. In other words, it's a data structure that holds one or more rows retrieved from a database. It contains methods to move the cursor to the next row, previous, first, or the last row.
    //    Uri: maps to the table name
    //    projection: list of columns that should be included in each row
    //    selection: is a string that represents the where clause
    //    Selection Arguments: an array of strings represents values that should be embedded in the selection statement. parameters-ish
    //    sort order: a string that indicates whether to sort the data in ascending or descending order.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(tableName);
        String query = builder.buildQuery(projection, selection, null, null, sortOrder, null);

        final Cursor cursor = db
                .getOpenHelper()
                .getReadableDatabase()
                .query(query);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updateCount;
        updateCount = db
                .getOpenHelper()
                .getWritableDatabase()
                .update(tableName, 0, values, selection, selectionArgs);

        return updateCount;
    }
}



//   This method is used to create a new instance of the UriMatcher class. In this method, we give each URI a unique code that will be used later by the content provider's methods to tell which table to use or retrieve the requested ID.

//    private static final int MULTIPLE_ROWS_TASKS = 1;
//    private static final int SINGLE_ROW_TASKS = 2;
//
//  In the code (line@118), the '#' character represents an integer value that represents an ID. Now, let's assume we have another table called 'users', then the new UriMatacher will be as follows:
//  This approach will allow us to retrieve data from the caller, such as the id of the row that the caller is intended to delete.
//private static UriMatcher createUriMatcher() {
//
//    final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//    final String authority = CONTENT_AUTHORITY;
//
//    //sUriMatcher will return code 1 if uri like authority/tasks
//    uriMatcher.addURI(authority, Task.TABLE_NAME, MULTIPLE_ROWS_TASKS);
//
//    //sUriMatcher will return code 2 if uri like e.g. authority/tasks/7 (where 7 is id of row in tasks table)
//    uriMatcher.addURI(authority, Task.TABLE_NAME + "/#", SINGLE_ROW_TASKS);
//
//    //sUriMatcher will return code 1 if uri like authority/users
//    uriMatcher.addURI(authority, "users", MULTIPLE_ROWS_USERS);
//
//    //sUriMatcher will return code 2 if uri like e.g. authority/users/7 (where 7 is id of row in users table)
//    uriMatcher.addURI(authority, "users" + "/#", SINGLE_ROW_USERS);
//
//    return uriMatcher;
//    }
//}

// example delete method:
//public int delete(Uri uri, String selection, String[] selectionArgs) {
//    int uriType = sUriMatcher.match(uri);
//    int deletionCount = 0;
//
//    switch (uriType) {
//        case MULTIPLE_ROWS_TASKS: //no trailing row id so selection may indicate more than 1 row needs to be deleted if they can be found
//            deletionCount = db
//                    .getOpenHelper()
//                    .getWritableDatabase()
//                    .delete(Task.TABLE_NAME, selection, selectionArgs);
//            break;
//        case SINGLE_ROW_TASKS: //trailing row id, so just one row to be deleted if it can be found
//            String id = uri.getLastPathSegment();
//            String selectionId = Task.COLUMN_ID + " = ?";
//            String [] selectionArgsId={String.valueOf(id)};
//            deletionCount = db
//                    .getOpenHelper()
//                    .getWritableDatabase()
//                    .delete(Task.TABLE_NAME, selectionId, selectionArgsId);
//            break;
//        default:
//            throw new IllegalArgumentException("Unknown URI: " + uri);
//    }
//    return deletionCount;
//}