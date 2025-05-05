package com.example.echatbotproject.abstracts.interfaces;

public interface ModelResponseCallback {

    void onSuccess(String responseMessage);
    void onError(String errorMessage);

}
