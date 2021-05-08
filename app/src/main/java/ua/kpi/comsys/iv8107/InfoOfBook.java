package ua.kpi.comsys.iv8107;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class InfoOfBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        String id = getIntent().getStringExtra("id");

        ImageView imgView = findViewById(R.id.imageView);
        TextView ttl = findViewById(R.id.booktitle);
        TextView sbttl = findViewById(R.id.subtitle);
        TextView desc = findViewById(R.id.description);
        TextView authors = findViewById(R.id.authors);
        TextView pblshr = findViewById(R.id.publisher);
        TextView isbn13 = findViewById(R.id.isbn13);
        TextView pages = findViewById(R.id.pages);
        TextView year = findViewById(R.id.year);
        TextView rate = findViewById(R.id.rating);
        TextView price = findViewById(R.id.price);

        try {
            InputStream inputStream = getAssets().open(id + ".txt");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            String str = new String(buffer);
            try {
                JSONObject bookJson = new JSONObject(str);
                Book book = new Book(
                        bookJson.getString("ttl"), bookJson.getString("sbttl"),
                        bookJson.getString("authors"),
                        bookJson.getString("pblshr"), bookJson.getString("isbn13"),
                        bookJson.getInt("pages"), bookJson.getInt("year"),
                        bookJson.getInt("rate"), bookJson.getString("desc"),
                        bookJson.getString("price"), bookJson.getString("image"));

                ttl.setText("Title: " + book.getTtl());
                sbttl.setText("Subtitle: " + book.getSbttl());
                authors.setText("Authors: " + book.getAuthors());
                pblshr.setText("Publisher: " + book.getPblshr());
                isbn13.setText("ISBN-13: " + book.getIsbn13());
                pages.setText("Pages: " + book.getPages());
                year.setText("Year: " + book.getYear());
                rate.setText("Rating: " + book.getRate() + "/5");
                desc.setText("Description: " + book.getDesc());
                price.setText("Price: " + book.getPrice());

                inputStream = getAssets().open(book.getImage());
                Drawable drawable = Drawable.createFromStream(inputStream, null);
                imgView.setImageDrawable(drawable);
                //imgView.set

            } catch (JSONException e) {
                //e.printStackTrace();
            }
        } catch (IOException e) {
            //
        }
        //isbn13.setText("ISBN-13: " + id);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(InfoOfBook.this, ThirdActivity.class);
        startActivity(intent);
    }
}