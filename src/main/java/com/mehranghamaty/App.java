package com.mehranghamaty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class App 
{

    public static void main( String[] args ) throws URISyntaxException, InterruptedException
    {
        if(args.length != 3 && args.length != 4) {
            System.out.println("Usage: app <end_point> <num_threads> <num_minutes> [optional flag to change output for piping]");
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
        int num_minutes = Integer.parseInt(args[2]);
        List<Thread> runs = new ArrayList<>();
        List<Benchmark> results = new ArrayList<>();

        for(int i = 0; i < num_threads; ++i) {
            Benchmark b = new Benchmark(uri, num_minutes);
            Thread thread = new Thread(b, "thread "+i);
            thread.start();
            runs.add(thread);
            results.add(b);
        }

        Runnable tick = new Runnable() {
            public void run() {
                if(args.length == 3) {
                    System.out.println("a minute has passed");
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> promise = executor.scheduleAtFixedRate(tick, 1, 1, TimeUnit.MINUTES);
        
        for(Thread thr : runs) {
            try {
                thr.join();
            } catch(InterruptedException e) {
                System.out.println(e);
                throw e;
            }
        }
        float average_qps = 0;
        for(Benchmark b : results) {
            average_qps = average_qps + b.getQPS();
        }
        average_qps = average_qps / num_threads;

        if(args.length == 3) {
            System.out.println( "Average qps: " + average_qps);
        } else {
            System.out.println(average_qps);
        }
        promise.cancel(true);
        System.exit(0);
    }
}
