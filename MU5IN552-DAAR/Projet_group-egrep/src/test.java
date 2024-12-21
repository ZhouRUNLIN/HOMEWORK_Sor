import KMP.main.KMPlike;
import egrep.Egrep;
import regEx.main.EgrepLike;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class test {

    private static boolean isFirstWrite = true;

    public static void main(String[] args) {
        // Vider les données précédent
        File file = new File("result.csv");
        if (file.exists()) {
            file.delete();
        } else {
            System.out.println("Err : File not exist");
        }

        String[] books = {"Pony_Tracks.txt", "Frankenstein.txt", "56667-0.txt", "Moby_Dick.txt", "The_Project_Gutenberg_eBook_of_Ulysses.txt"};
        String[] nbCharacters = {"299274", "438119", "748684", "1237282", "1531826"};

        // Calculate the time taken and the memory consumed by the same method for different books
        // (different sizes of text, number of words of the book in ascending order)
        for (int i = 0; i < books.length; i++){
            String[] egrepLikeArg = {"S(a|g|r)+on", books[i]};
            String[] KMPLikeArg = {"Chihuahua", books[i]};
            String[] egrepArg = {"S(a|g|r)+on", books[i]};

            // Starting calculating the average of execution time (10 times)
            // by using threads
            // EgrepLike
            long avgEgrepLikeExecutionTime = measureAverageExecutionTime(() -> EgrepLike.main(egrepLikeArg), 10);
            long avgEgrepLikeMemoryUsage = measureAverageMemoryUsage(() -> EgrepLike.main(egrepLikeArg), 10);
            // KMP algo
            long avgKmpExecutionTime = measureAverageExecutionTime(() -> KMPlike.main(KMPLikeArg), 10);
            long avgKmpMemoryUsage = measureAverageMemoryUsage(() -> KMPlike.main(KMPLikeArg), 10);
            // Egrep (the command of linux)
            long avgEgrepExecutionTime = measureAverageExecutionTime(() -> Egrep.main(egrepArg), 10);
            long avgEgrepMemoryUsage = measureAverageMemoryUsage(() -> Egrep.main(egrepArg), 10);

            writeResultsToCSV(avgEgrepLikeExecutionTime, avgEgrepLikeMemoryUsage, avgKmpExecutionTime, avgKmpMemoryUsage, avgEgrepExecutionTime, avgEgrepMemoryUsage, books[i], nbCharacters[i]);
        }
    }

    public static long measureAverageExecutionTime(Runnable task, int runs) {
        long totalTime = 0;
        for (int i = 0; i < runs; i++) {
            totalTime += measureExecutionTime(task);
        }
        return totalTime / runs;
    }

    public static long measureAverageMemoryUsage(Runnable task, int runs) {
        long totalMemory = 0;
        for (int i = 0; i < runs; i++) {
            totalMemory += measureMemoryUsage(task);
        }
        return totalMemory / runs;
    }

    public static long measureExecutionTime(Runnable task) {
        long startTime = System.nanoTime();
        task.run();
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000; // return the used time, on ms
    }


    public static long measureMemoryUsage(Runnable task) {
        Runtime runtime = Runtime.getRuntime();
        System.gc();

        long startMemory = runtime.totalMemory() - runtime.freeMemory();
        task.run();
        long endMemory = runtime.totalMemory() - runtime.freeMemory();

        return (endMemory - startMemory) / 1024; // return the memory usage on KB
    }


    public static void writeResultsToCSV(long egrepLikeExecutionTime, long egrepLikeMemoryUsage, long kmpExecutionTime, long kmpMemoryUsage, long egrepExecutionTime, long egrepMemoryUsage, String book, String nbCharacter) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("result.csv", true))) {  // 使用 append 模式
            if (isFirstWrite) {
                writer.println("Task,Execution Time (ms),Memory Usage (KB),Book,nbCharacter");
                isFirstWrite = false;
            }
            writer.println("EgrepLike," + egrepLikeExecutionTime + "," + egrepLikeMemoryUsage + "," + book + "," + nbCharacter);
            writer.println("KMP," + kmpExecutionTime + "," + kmpMemoryUsage + "," + book + "," + nbCharacter);
            writer.println("Egrep," + egrepExecutionTime + "," + egrepMemoryUsage + "," + book + "," + nbCharacter);

        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }
    }
}
