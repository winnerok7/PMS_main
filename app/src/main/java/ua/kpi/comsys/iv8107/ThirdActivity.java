package ua.kpi.comsys.iv8107;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import org.json.*;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.getTabAt(2).select();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    Intent intent = new Intent(ThirdActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (tab.getPosition() == 1) {
                    Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        ArrayList<Book> books = new ArrayList<Book>();
        Collections.addAll(books, this.loadBookArray());
        TableLayout tbl = findViewById(R.id.table);
        TableRow[] tblRows = new TableRow[books.size()];
        TextView[] infOfBooks = new TextView[books.size()];
        ImageView[] imgOfBooks = new ImageView[books.size()];
        ConstraintLayout.LayoutParams params;

        this.redrawTable(books, tblRows, imgOfBooks, infOfBooks, tbl, books);

        SearchView search = findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Book> booksFiltered = new ArrayList<Book>();
                tbl.removeAllViews();
                int j = 0;
                if (!newText.isEmpty()) {
                    for (int i = 0; i < books.size(); i++) {
                        if (books.get(i).getTtl().contains(newText)) {
                            booksFiltered.add(books.get(i));
                            j++;
                        }
                    }
                    redrawTable(booksFiltered, tblRows, imgOfBooks, infOfBooks, tbl, books);
                } else {
                    redrawTable(books, tblRows, imgOfBooks, infOfBooks, tbl, books);
                }
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, AddBook.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void redrawTable(
            ArrayList<Book> booksArrayFiltered, TableRow[] tblRows, ImageView[] imgOfBooks, TextView[] infOfBooks,
            TableLayout table, ArrayList<Book> booksArrayUnfiltered
    ){
        tblRows = new TableRow[booksArrayFiltered.size()];
        infOfBooks = new TextView[booksArrayFiltered.size()];
        imgOfBooks = new ImageView[booksArrayFiltered.size()];
        List<String> filesOfBook = null;
        try {
            filesOfBook = Arrays.asList(getAssets().list(""));
        } catch (IOException e){
            e.printStackTrace();
        }
        for (int i = 0; i < booksArrayFiltered.size(); i++) {
            tblRows[i] = new TableRow(this);
            tblRows[i].setPadding(10, 10, 10, 10);
            imgOfBooks[i] = this.createBookImage(booksArrayFiltered.get(i));
            tblRows[i].addView(imgOfBooks[i]);
            infOfBooks[i] = new TextView(this);
            infOfBooks[i].setText(booksArrayFiltered.get(i).getTtl() + "\n" + booksArrayFiltered.get(i).getSbttl() + "\n" + booksArrayFiltered.get(i).getIsbn13() + "\n" + booksArrayFiltered.get(i).getPrice()
            );
            infOfBooks[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            infOfBooks[i].setPadding(10, 10, 10, 10);
            tblRows[i].addView(infOfBooks[i]);
            String isbn13 = booksArrayFiltered.get(i).getIsbn13();
            TableRow tblRow = tblRows[i];
            Book books = booksArrayFiltered.get(i);
            if (filesOfBook.contains(isbn13 + ".txt")) {
                tblRows[i].setOnTouchListener(new SwipeDelete(ThirdActivity.this) {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(ThirdActivity.this, InfoOfBook.class);
                        intent.putExtra("id", isbn13);
                        startActivity(intent);
                    }

                    @Override
                    public void onSwipeLeft() {
                        swipeLeftMethod(table, tblRow, booksArrayUnfiltered, booksArrayFiltered, books);
                    }
                });
            } else {
                tblRows[i].setOnTouchListener(new SwipeDelete(ThirdActivity.this) {
                    @Override
                    public void onSwipeLeft() {
                        swipeLeftMethod(table, tblRow, booksArrayUnfiltered, booksArrayFiltered, books);
                    }
                });
            }
        //}
        table.addView(tblRows[i]);
        }
        if (booksArrayFiltered.isEmpty()){
            TableRow tblRowNoBooks = new TableRow(this);
            TextView txtViewNoBooks = new TextView(this);
            txtViewNoBooks.setText("No books");
            tblRowNoBooks.addView(txtViewNoBooks);
            table.addView(tblRowNoBooks);
        }
    }

    private Book[] loadBookArray() {
        String str = "";
        JSONObject booksJson = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(openFileInput("BooksListUser.txt"));
            booksJson = new JSONObject((String) inputStream.readObject());
        }
        catch (IOException e) {
            try {
                InputStream inputStream = getAssets().open("BooksList.txt");
                int size = inputStream.available();
                byte[] buffer = new byte[size];
                inputStream.read(buffer);

                str = new String(buffer);
                booksJson = new JSONObject(str);
                ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("BooksListUser.txt", MODE_PRIVATE));
                stream.writeObject(booksJson.toString());
                stream.close();
            } catch (IOException | JSONException e2) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Book[] books = new Book[0];
        try {
            JSONArray booksJsonArray = booksJson.getJSONArray("books");
            books = new Book[booksJsonArray.length()];
            JSONObject book;
            for (int i = 0; i < booksJsonArray.length(); i++) {
                book = booksJsonArray.getJSONObject(i);
                books[i] = new Book(
                        book.getString("title"),
                        book.getString("subtitle"),
                        book.getString("isbn13"),
                        book.getString("price"),
                        book.getString("image")
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

    private ImageView createBookImage(Book book) {
        ImageView bookImage = new ImageView(this);
        try {
            InputStream inputStream = getAssets().open(book.getImage());
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            bookImage.setImageDrawable(drawable);
        } catch (IOException e) {
        }
        bookImage.setContentDescription(book.getTtl());
        bookImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bookImage.setMinimumWidth(200);
        bookImage.setMinimumHeight(200);
        bookImage.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        return bookImage;
    }

    private void swipeLeftMethod(TableLayout table,
                                 TableRow tableRow,
                                 ArrayList<Book> booksArrayUnfiltered,
                                 ArrayList<Book> booksArrayFiltered,
                                 Book book){
        table.removeView(tableRow);
        booksArrayUnfiltered.remove(book);
        booksArrayFiltered.remove(book);
        JSONArray booksJsonArray = new JSONArray();
        for (int j = 0; j < booksArrayUnfiltered.size(); j++) {
            try {
                booksJsonArray.put(new JSONObject().
                        put("title", booksArrayUnfiltered.get(j).getTtl()).
                        put("subtitle", booksArrayUnfiltered.get(j).getSbttl()).
                        put("isbn13", booksArrayUnfiltered.get(j).getIsbn13()).
                        put("price", booksArrayUnfiltered.get(j).getPrice()).
                        put("image", booksArrayUnfiltered.get(j).getImage()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            JSONObject booksJson = new JSONObject().put("books", booksJsonArray);
            ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("BooksListUser.txt", MODE_PRIVATE));
            stream.writeObject(booksJson.toString());
            stream.close();
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

