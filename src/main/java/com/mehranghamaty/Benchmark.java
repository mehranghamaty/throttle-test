package com.mehranghamaty;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.time.Instant;


public class Benchmark extends Thread {
    private HttpClient client;
    private HttpRequest request;
    private int number_of_responses;
    private int number_of_minutes;
    private URI uri;
    private Exception exception;

    public Benchmark(URI uri, int number_of_minutes) {
        this.client = HttpClient.newHttpClient();
        this.number_of_responses = 0;
        this.number_of_minutes = number_of_minutes;
        this.uri = uri;
        
        this.request = HttpRequest
            .newBuilder(this.uri)
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
    }
    
    @Override
    public void run() {
        Instant start = Instant.now();
        Instant end;
        do {
            try {
                this.client.send(this.request, BodyHandlers.ofString());
            } catch(IOException e) {
                exception = e;
                return;
            } catch(InterruptedException e) {
                exception = e;
                return;
            }
            this.number_of_responses++;
            end = Instant.now();
        } while(Duration.between(start, end).toMinutes() < this.number_of_minutes);
    }

    public long getQPS() {
        return this.number_of_responses/this.number_of_minutes/60;
    }

    public Exception getException() {
        return this.exception;
    }
}
