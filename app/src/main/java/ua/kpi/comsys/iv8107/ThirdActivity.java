package ua.kpi.comsys.iv8107;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.*;

import java.io.IOException;

import java.io.*;

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
        Book[] books = this.BookArray();
        TableLayout tbl = findViewById(R.id.table);
        TableRow[] rowsTbl = new TableRow[books.length];
        TextView[] infOfBooks = new TextView[books.length];
        ImageView[] imgOfBooks = new ImageView[books.length];
        ConstraintLayout.LayoutParams params;
        for (int i = 0; i < books.length; i++){
            rowsTbl[i] = new TableRow(this);
            rowsTbl[i].setPadding(10, 10, 10, 10);
            imgOfBooks[i] = this.BookImage(books[i]);
            rowsTbl[i].addView(imgOfBooks[i]);
            infOfBooks[i] = new TextView(this);
            infOfBooks[i].setText(
                    books[i].getTitle() + "\n" + books[i].getSubtitle() + "\n" +
                            books[i].getIsbn13() + "\n" + books[i].getPrice()
            );
            infOfBooks[i].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            infOfBooks[i].setPadding(15, 15, 15, 15);
            rowsTbl[i].addView(infOfBooks[i]);
            tbl.addView(rowsTbl[i]);

        }
    }

    private Book[] BookArray() {
        String str = "";
        try {
            InputStream inputStream = getAssets().open("BooksList.txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);

            str = new String(buffer);
            System.out.println(str);
        } catch (IOException e){
            e.printStackTrace();
        }
        Book[] books = new Book[0];
        try {
            JSONObject jsonOfBooks = new JSONObject(str);
            JSONArray booksJsnArr = jsonOfBooks.getJSONArray("books");
            books = new Book[booksJsnArr.length()];
            JSONObject book;
            for (int i = 0; i < booksJsnArr.length(); i++) {
                book = booksJsnArr.getJSONObject(i);
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

    private ImageView BookImage(Book book) {
        ImageView bookImage = new ImageView(this);
        try {
            InputStream inputStream = getAssets().open(book.getImage());
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            bookImage.setImageDrawable(drawable);
        } catch (IOException ignored) {
        }
        bookImage.setContentDescription(book.getTitle());
        bookImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        bookImage.setMinimumWidth(250);
        bookImage.setMinimumHeight(250);
        bookImage.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        return bookImage;
    }
}

