# 使用官方 Node.js 镜像作为基础镜像
FROM node:14

# 设置工作目录为 /app
WORKDIR /app

# 复制 crud-main 中的 package.json 和 package-lock.json
COPY ./crud/package*.json ./

# 安装应用依赖
RUN npm install

# 复制整个 crud-main 目录的内容到容器中
COPY ./crud-main .

# 暴露应用使用的端口
EXPOSE 3000

# 启动应用
CMD ["npm", "start"]

