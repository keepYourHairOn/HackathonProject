package com.directory.Model.controller;


import com.directory.EDAPlacementApp;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/result")
    public ResponseEntity<String> getResult(){
        Gson gson = new Gson();
        String result = gson.toJson(EDAPlacementApp.circuit());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        return ResponseEntity.ok().headers(responseHeaders).body(result);
    }
}
