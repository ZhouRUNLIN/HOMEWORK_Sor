package test;

import machine.Client;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.*;

public class test_client_2 {
    private ArrayList<Client> _clients = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    int firstPort = 6061; // 初始端口

    public void autoCreateClient(){
        // 仅创建一个客户端
        if (_clients.isEmpty()) {
            try {
                Client client = new Client(firstPort, "c2");
                _clients.add(client);
            } catch (Exception e){
                System.out.println("Fail to create client");
                e.printStackTrace();
            }
        }
    }

    public void testStart() {
        System.out.println("Creating initial client...");
        autoCreateClient(); // 自动创建初始客户端
        if (!_clients.isEmpty()) {
            testClient(_clients.get(0)); // 对当前唯一的客户端进行测试
        }
    }

    public void testClient(Client client) {
        autoCreateDataForClient(client); // 为当前客户端自动创建数据
        while (true) {
            System.out.println("Controlling Client: " + client.getId());
            System.out.println("1: Create data (int)");
            System.out.println("2: Perform request");
            System.out.println("3: Print all Data");
            System.out.println("4: End Test");
            System.out.println("Enter option number: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1:
                    createData(client);
                    break;
                case 2:
                    performRequest(client);
                    break;
                case 3:
                    printData(client);
                    break;
                case 4:
                    System.out.println("Test ended.");
                    return; // 结束测试
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void autoCreateDataForClient(Client client) {
        String name = "c2";
        int value = 100; // 给定一个初始值
        if (!client.heapHaveData(name)) {
            client.setObject(name, value);
            System.out.println("Data auto-creation successful for client " + client.getId() + ": " + name + " = " + value);
        } else {
            System.out.println("Data with the name \"" + name + "\" already exists in client " + client.getId() + ". Auto-creation aborted.");
        }
    }

    private void createData(Client client) {
        System.out.println("Enter a name for the data: ");
        String name = scanner.nextLine();
        System.out.println("Enter an integer value: ");
        int value = scanner.nextInt();
        scanner.nextLine();
        if (!client.heapHaveData(name)) {
            client.setObject(name, value);
            System.out.println("Data creation successful: " + name + " = " + value);
        } else {
            System.out.println("Data with the name \"" + name + "\" already exists. Creation aborted.");
        }
    }

    public void printData(Client client){
        for (String s: client.getLocalHeap().keySet()){
            System.out.println(s + " = " + client.getLocalHeap().get(s));
        }
    }

    private void performRequest(Client client) {
        while (true) {
            System.out.println("Available data in the client heap:");
            HashMap<String, Object> localHeap = client.getLocalHeap();
            if (localHeap.isEmpty()) {
                System.out.println("No data available.");
                return;
            }

            int index = 1;
            for (String key : localHeap.keySet()) {
                System.out.println(index++ + ": " + key);
            }

            System.out.println("Enter the index of the data name for the operation (index starts from 1): ");
            int dataIndex = scanner.nextInt();
            scanner.nextLine();

            String dataName = (String) localHeap.keySet().toArray()[dataIndex - 1];
            System.out.println("Selected data name for operation: " + dataName);

            System.out.println("Available requests:");
            System.out.println("1. dMalloc");
            System.out.println("2. dAccessWrite");
            System.out.println("3. dAccessRead");
            System.out.println("4. dRelease");
            System.out.println("5. dFree");
            System.out.println("Enter request number: ");
            int requestOption = scanner.nextInt();
            scanner.nextLine();

            final ExecutorService executor = Executors.newSingleThreadExecutor();
            final Future<?> future = executor.submit(() -> {
                try {
                    switch (requestOption) {
                        case 1:
                            client.request("dMalloc", dataName);
                            break;
                        case 2:
                            client.request("dAccessWrite", dataName);
                            break;
                        case 3:
                            Scanner scanner1 = new Scanner(System.in);
                            System.out.println("Please enter the name of the data you want to read");
                            String dataName2 = scanner1.nextLine();
                            client.request("dAccessRead", dataName2);
                            break;
                        case 4:
                            client.request("dRelease", dataName);
                            break;
                        case 5:
                            client.request("dFree", dataName);
                            break;
                        default:
                            System.out.println("Invalid request option.");
                            return;
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            });

            try {
                future.get(8, TimeUnit.SECONDS);  // 等待最多8秒
                switch (requestOption) {
                    case 1:
                        System.out.println("dMalloc request sent for " + dataName);
                        break;
                    case 2:
                        System.out.println("dAccessWrite request sent for " + dataName);
                        break;
                    case 3:
                        System.out.println("dAccessRead request sent for " + dataName);
                        break;
                    case 4:
                        System.out.println("dRelease request sent for " + dataName);
                        break;
                    case 5:
                        System.out.println("dFree request sent for " + dataName);
                        break;
                }
            } catch (TimeoutException te) {
                System.out.println("Request timed out. Please try again.");
                continue;  // 重新开始循环，提示用户重新操作
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("An error occurred during the operation.");
                e.printStackTrace();
            } finally {
                executor.shutdownNow();  // 关闭线程池
            }
            break;  // 正常结束，跳出循环
        }
    }


    public static void main(String[] args) {
        test_client_2 test = new test_client_2();
        test.testStart();
    }
}
