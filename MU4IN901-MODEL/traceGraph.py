import pandas as pd
import matplotlib.pyplot as plt

# Read data from CSV file
df = pd.read_csv('benchmark_results.csv')

# Plotting
plt.plot(df['Size'], df['NaiveTime'], label='Naive MultPoly')
plt.plot(df['Size'], df['FFTTime'], label='FFT MultPoly')

plt.xlabel('Matrix Size')
plt.ylabel('Time (s)')
plt.title('Benchmark Results')
plt.legend()
plt.show()
