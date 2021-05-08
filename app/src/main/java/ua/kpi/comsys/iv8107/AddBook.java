package ua.kpi.comsys.iv8107;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class AddBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        EditText ttlInput = findViewById(R.id.titleInput);
        EditText subttlInput = findViewById(R.id.subtitleInput);
        EditText priceInput = findViewById(R.id.priceInput);

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ttl = ttlInput.getText().toString();
                    if (ttl.matches("")){
                        return;
                    }
                    String price = priceInput.getText().toString();
                    if (price.matches("")){
                        return;
                    }
                    String sbttl = subttlInput.getText().toString();
                    String str = "";
                    JSONObject booksJson = null;
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(openFileInput("BooksListUser.txt"));
                        booksJson = new JSONObject((String) inputStream.readObject());
                        inputStream.close();
                    }
                    catch (IOException e) {
                        try {
                            InputStream inputStream = getAssets().open("BooksList.txt");
                            int size = inputStream.available();
                            byte[] buffer = new byte[size];
                            inputStream.read(buffer);

                            str = new String(buffer);
                            booksJson = new JSONObject(str);
                            inputStream.close();
                        } catch (IOException | JSONException e2) {
                            e.printStackTrace();
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray booksJsonArray = booksJson.getJSONArray("books");
                    booksJsonArray.put(
                            new JSONObject().put("ttl", ttl).
                                    put("sbttl", sbttl).put("isbn13", "/\\noid/\\").
                                    put("price", "$" + price).put("image", ""));
                    booksJson = new JSONObject().put("books", booksJsonArray);

                    ObjectOutputStream stream = new ObjectOutputStream(openFileOutput("BooksListUser.txt", MODE_PRIVATE));
                    stream.writeObject(booksJson.toString());
                    stream.close();

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(AddBook.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }
}