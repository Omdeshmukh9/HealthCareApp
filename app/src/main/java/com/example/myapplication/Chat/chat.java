package com.example.myapplication.Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;

public class chat extends AppCompatActivity {

    private EditText editTextMessage;
    private Button buttonSend;
    private TextView textViewResult;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        recyclerView = findViewById(R.id.recycler_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Set the title
        toolbar.setTitle("ChatBot");

        // Set the toolbar as the support action bar
        setSupportActionBar(toolbar);
        messages = new ArrayList<>();
        adapter = new ChatAdapter(this, messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString();
                messages.add(new ChatMessage(message, true)); // Add user message to the list
                adapter.notifyDataSetChanged(); // Update RecyclerView
                callGeminiAPI(message); // Call Gemini API
                editTextMessage.setText(""); // Clear input field
            }
        });
    }

    // Method to call Gemini API and generate response
    private void callGeminiAPI(String prompt) {
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-pro",
                /* apiKey */ "AIzaSyDnhpi4mkNHjG_w0q2n0WMbFKN3uImnmi0");
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder()
                .addText(prompt)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                messages.add(new ChatMessage(resultText, false)); // Add API response to the list
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged(); // Update RecyclerView
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                String errorText = "Error: " + t.getMessage();
                messages.add(new ChatMessage(errorText, false)); // Add error message to the list
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged(); // Update RecyclerView
                    }
                });
            }
        }, getMainExecutor());
    }
}
