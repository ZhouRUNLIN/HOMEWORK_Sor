package test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;

public class CPTtime {


    public static void main(String[] args) throws IOException, InvocationTargetException, IllegalAccessException, InterruptedException {
        calculsCPT calculsCPT = new calculsCPT();

        calculsCPT.initiaC0();


        FileWriter fileWriter = new FileWriter("test_times.csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);


        for (int i = 1; i <= 50; i++) {

            long executionTime = calculsCPT.test(i);


            printWriter.println( executionTime);  // 将测试编号和执行时间写入CSV
            //System.out.println("Test " + 5 + " completed in " + executionTime + " ms.");
        }

        printWriter.close();  // 关闭文件
        fileWriter.close();   // 关闭文件流
    }
}
