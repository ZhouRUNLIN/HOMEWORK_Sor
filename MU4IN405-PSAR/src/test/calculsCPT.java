package test;

import machine.Client;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class calculsCPT {


    private List<Client> clientsList = new ArrayList<>();

    private List<Runnable> jobsList = new ArrayList<>();

    private int nb = 1;

    public void createClientList(int nbClient) throws IOException {
        int start = nb;
        for (int i = start; i < start + nbClient; i++) {
            String clientName = "Client" + i;
            int clientPort = 16060 + i;
            Client client = new Client(clientPort, clientName);
            clientsList.add(client);
        }
        nb += nbClient;
    }

    public void initiaJobList() {
        for (int i = 0; i < 100; i++) { // 假设有100个任务
            jobsList.add(new Runnable() {
                @Override
                public void run() {
                    Client client = getClientForJob(); // 从某处获取一个Client对象
                    if (client != null) {
                        try {
                            clientRun(client);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            releaseClientAfterJob(client); // 完成任务后释放Client对象
                        }
                    }
                }
            });
        }
    }

    private synchronized Client getClientForJob() {
        // 实现获取客户端的逻辑，例如可以使用队列等待可用的客户端
        return clientsList.isEmpty() ? null : clientsList.remove(0);
    }

    private synchronized void releaseClientAfterJob(Client client) {
        // 实现释放客户端的逻辑，把客户端重新加入到可用的客户端列表
        clientsList.add(client);
    }


    public void clientRun(Client client) throws InvocationTargetException, IllegalAccessException, InterruptedException {


        synchronized (Client.class){
            client.request("dAccessRead","c0");

            int ownCpt = getCpt(client);

            int ccc = ownCpt +1;

            updateCpt(client,ccc);
        }

        synchronized (Client.class){
            client.request("dAccessWrite","c0");

            client.request("dRelease","c0");

            System.out.println("cpt: " + getCpt(client) );
        }


    }

    int getCpt(Client client){

        HashMap<String,Object> hashMap = client.getLocalHeap();
        if (hashMap.containsKey("c0")) {
            return (Integer) hashMap.get("c0");
        } else {
            return -1;
        }

    }

    void updateCpt(Client client, int newCpt) {
        HashMap<String, Object> hashMap = client.getLocalHeap();
        hashMap.put("c0", newCpt);
    }
    public void initiaC0() throws IOException, InvocationTargetException, IllegalAccessException, InterruptedException {
        Client client0 = new Client(6060,"client0");
        String c0 = "c0";
        int numC0 = 0;
        client0.setObject(c0, numC0);
        client0.request("dMalloc",c0);
        client0.request("dAccessWrite",c0);
        client0.request("dRelease",c0);
    }




    public long test(int nb) throws IOException, InterruptedException, InvocationTargetException, IllegalAccessException {
        createClientList(nb);  // 假设这是一个已经定义好的方法
        initiaJobList();          // 假设这是一个已经定义好的方法



        ExecutorService executorService = Executors.newFixedThreadPool(nb);

        long startTime = System.nanoTime();  // 测试开始时间，单位为纳秒

        for (Runnable job : jobsList) {
            executorService.submit(job);
        }


        executorService.shutdown();
        try {
            // 等待直到所有任务完成执行，或者等待时间超过长时间，这里设置的是1天
            if (!executorService.awaitTermination(1, TimeUnit.DAYS)) {
                executorService.shutdownNow(); // 尝试停止所有正在执行的任务
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow(); // 重新尝试停止所有正在执行的任务
            Thread.currentThread().interrupt(); // 保留中断状态
        }
        long endTime = System.nanoTime();  // 测试结束时间，单位为纳秒

        long executionTime = (endTime - startTime)/1000;

        clientsList.clear();
        jobsList.clear();


        return executionTime;
    }


}