import subprocess
from matplotlib import pyplot as plt

num_threads = [str(i) for i in range(1,9)]
cmd =['java', '-jar', 
        'target/throttle-test-1.0-SNAPSHOT.jar',
        'X', 'Y', '1', '-pipe']
    

def get_for(url: str)->[]:
    cmd[3] = url
    print(f"Sending requests to: {url}")
    results = []
    for i in num_threads:
        cmd[4] = i
        print(f"Starting for {i} cores")
        output = subprocess.check_output(cmd)
        print(f"Number of cores {i} has QPS of {output}")
        results.append(float(output))
    return results

results_spring = [543.0, 271.5, 181.0, 135.75, 108.6, 90.166664, 77.28571, 67.5]#get_for('http://localhost:8080/annotate')
results_fastapi = [878.0, 648.0, 455.0, 360.0, 315.0, 262.0, 223.0, 202.0] #get_for('http://localhost:8000/annotate')
results_fastapi_8 = [1035.0, 1011.0, 868.6667, 366.0, 627.4, 497.5, 450.14285, 397.875] #get_for('http://localhost:8000/annotate')
print(results_fastapi_8)


plt.plot(num_threads, results_spring, label="Spring Boot")
plt.plot(num_threads, results_fastapi, label="FastAPI")
plt.plot(num_threads, results_fastapi_8, label="FastAPI (8 Threads responding to requests)")

plt.legend()
plt.title("Comparing FastAPI and SpringBoot's QPS (higher is better)")
plt.xlabel("number of threads making requests")
plt.ylabel("QPS")
plt.show()