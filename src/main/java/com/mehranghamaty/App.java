package com.mehranghamaty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class App 
{

    public static void main( String[] args ) throws URISyntaxException, InterruptedException
    {
        if(args.length != 3) {
            System.out.println("Usage: app <end_point> <num_threads> <number_of_requests>");
            System.exit(1);
        }
        URI uri;
        try {
            uri = new URI(args[0]);
        } catch (URISyntaxException e) {
            System.out.println(e);
            throw e;
        }
        int num_threads = Integer.parseInt(args[1]);
        int number_of_requests = Integer.parseInt(args[2]);
        
        List<Benchmark> runs = new ArrayList<Benchmark>();

        for(int i = 0; i < num_threads; ++i) {
            Benchmark b;
            b = new Benchmark(uri, number_of_requests);
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
