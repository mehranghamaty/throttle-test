package com.mehranghamaty;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{

    public static void main( String[] args ) throws URISyntaxException, InterruptedException
    {
        if(args.length != 3) {
            System.out.println("Usage: app <end_point> <num_threads>");
            System.exit(1);
        }
        int num_threads = Integer.parseInt(args[0]);
        URI uri;
        try {
            uri = new URI(args[1]);
        } catch (URISyntaxException e) {
            System.out.println(e);
            throw e;
        }
        List<Benchmark> runs = new ArrayList<Benchmark>();

        for(int i = 0; i < num_threads; ++i) {
            Benchmark b;
            b = new Benchmark(uri);
            b.start();
            runs.add(b);
        }

        for(Benchmark b : runs) {
            try {
                b.join();
            } catch(InterruptedException e) {
                throw e;
            }
        }

        long average_time = 0;
        for(Benchmark b : runs) {
            average_time = average_time + b.getTime();
        }
        average_time = average_time / num_threads;


        System.out.println( "Time to process: " + average_time);
    }
}
