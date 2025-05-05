package com.example.echatbotproject.concretes.genai;

import android.util.Log;

import com.example.echatbotproject.abstracts.interfaces.ModelResponseCallback;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class GeminiHelper {

    // Properties
    private final String TAG = "GEMINIHELPER";
    private final String API_KEY;
    private String modelName;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private GenerativeModel generativeModel;
    private final ModelResponseCallback callback;

    // Cosntructor
    public GeminiHelper(String API_KEY, String modelName, ModelResponseCallback callback){
        this.API_KEY = API_KEY;
        this.modelName = modelName;
        generativeModel = new GenerativeModel(modelName, API_KEY);
        this.callback = callback;
    }



    // Public Methods
    public void generateResponse(String promptText){
        GenerativeModelFutures modelFutures = GenerativeModelFutures.from(generativeModel);

        // Building Content to pass to the model
        Content content = new Content.Builder()
                .addText(promptText)
                .build();

        // Invoke model for response
        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String textResult = result.getText();
                if(callback != null && textResult != null){
                    callback.onSuccess(textResult);
                }else if(callback != null){
                    callback.onError("No reponse from Gemini API");
                }else{
                    assert textResult != null;
                    Log.i(TAG, textResult);
                }
            }
            @Override
            public void onFailure(Throwable t) {
                String errorMessage = "Error occurred: " + t.getMessage();
                if (callback != null) {
                    callback.onError(errorMessage);
                }
                Log.e(TAG, errorMessage, t);}

        }, executorService);

    }

    public void shutdownExecutor(){
        executorService.shutdown();
    }


}
