# Throttle Test

Multithreaded load tester for URLs. Purposefully plain and easy to extend. Sometimes simplier things work better.

Especially when you can integrate them into other systems, example `run_experiement.py` provided.

## Things to note

It doesn't make sense to be running the server and the load tester on the same computer these end 2 end style 
performance tests should be performed against systems deployed with a similar stack of software/hardware as 
the production platform. 