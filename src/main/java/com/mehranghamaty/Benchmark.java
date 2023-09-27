package com.mehranghamaty;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.Instant;


public class Benchmark extends Thread {
    private HttpClient client;
    private HttpRequest request;
    private long time_to_respond;
    private URI uri;
    private Exception exception;

    public Benchmark(URI uri) {
        this.client = HttpClient.newHttpClient();
        this.time_to_respond = -1;
        this.uri = uri;
        
        this.request = HttpRequest
            .newBuilder(this.uri)
            .POST(HttpRequest.BodyPublishers.noBody())
            .build();
    }
    
    @Override
    public void run() {
        Instant start = Instant.now();
        
        try {
            this.client.send(this.request, null);
        } catch(IOException e) {
            exception = e;
            return;
        } catch(InterruptedException e) {
            exception = e;
            return;
        }
        
        Instant end = Instant.now();
        this.time_to_respond = Duration.between(start, end).toMillis();
    }

    public long getTime() {
        return this.time_to_respond;
    }

    public Exception getException() {
        return this.exception;
    }
}
