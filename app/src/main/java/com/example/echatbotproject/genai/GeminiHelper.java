package com.example.echatbotproject.genai;

import android.util.Log;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeminiHelper {

    private String API_KEY = " ";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public void modelCall(){


        GenerativeModel generativeModel = new GenerativeModel("gemini-1.5-flash", API_KEY);
        GenerativeModelFutures modelFutures = GenerativeModelFutures.from(generativeModel);



        Content content = new Content.Builder()
                .addText("Write an essay about backpacks.")
                .build();

        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String textResult = result.getText();
                System.out.println(textResult);
                Log.i("GEMINIHELPER", textResult);
            }
            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                Log.i("GEMINIHELPER", "ERROR OCCURED" + t.getMessage());
            }

        }, executorService);
    }

}
