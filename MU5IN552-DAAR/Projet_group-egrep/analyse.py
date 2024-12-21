import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# 读取CSV数据
df = pd.read_csv('result.csv')
df_sorted = df.sort_values(by='nbCharacter')
max_nbCharacter = df['nbCharacter'].max()
print(f"最大nbCharacter值为: {max_nbCharacter}")

# 按照文件大小绘制执行时间的点图
plt.figure(figsize=(12, 6))

for task in df['Task'].unique():
    subset = df[df['Task'] == task]
    # 绘制点图
    plt.scatter(subset['nbCharacter'], subset['Execution Time'], label=task)

    # 线性回归拟合
    x = subset['nbCharacter']
    y = subset['Execution Time']
    
    # 使用numpy进行线性回归
    coef = np.polyfit(x, y, 1)  # 1表示一阶拟合（线性拟合）
    poly1d_fn = np.poly1d(coef)  # 获取回归方程

    # 绘制拟合曲线
    plt.plot(x, poly1d_fn(x), '--', label=f'{task} Fit Line')

plt.title('Execution Time Comparison by File Size with Fit Line')
plt.xlabel('File Size (Number of Characters)')
plt.ylabel('Execution Time (ms)')
plt.grid(True)
plt.legend()
plt.show()

# 按照文件大小绘制内存消耗的点图
plt.figure(figsize=(12, 6))

for task in df['Task'].unique():
    subset = df[df['Task'] == task]
    # 绘制点图
    plt.scatter(subset['nbCharacter'], subset['Memory Usage'], label=task)

    # 线性回归拟合
    x = subset['nbCharacter']
    y = subset['Memory Usage']
    
    # 使用numpy进行线性回归
    coef = np.polyfit(x, y, 1)  # 1表示一阶拟合（线性拟合）
    poly1d_fn = np.poly1d(coef)  # 获取回归方程

    # 绘制拟合曲线
    plt.plot(x, poly1d_fn(x), '--', label=f'{task} Fit Line')

plt.title('Memory Usage Comparison by File Size with Fit Line')
plt.xlabel('File Size (Number of Characters)')
plt.ylabel('Memory Usage (KB)')
plt.grid(True)
plt.legend()
plt.show()
