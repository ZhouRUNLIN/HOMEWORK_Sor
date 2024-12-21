import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KPMalgo {

    public int[] computeCarryover(char[] pattern) {
        int len = pattern.length;
        int[] next = new int[len + 1];
        next[0] = -1;
        int i = 0, j = -1;

        // getLTS
        while (i < len) {
            if (j == -1 || pattern[i] == pattern[j]) {
                i++;
                j++;
                next[i] = j;
            } else {
                j = next[j];
            }
        }

        // LTS to carryover
        for (i = 1; i <= len; i++) {
            if (next[i] >= 0 && i < len && pattern[i] == pattern[next[i]]) {
                next[i] = next[next[i]];
            }
        }


        return next;
    }

    public void KPM(String factor, String file) throws MalformedURLException {

        char[] factorChar = factor.toCharArray();

        int[] carryover = computeCarryover(factorChar);

        //read from local file
        //try (BufferedReader br = new BufferedReader(new FileReader(file))) {

        //read from net file
        URL url = new URL(file);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {

            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;

                int i = 0;
                int j = 0;
                int textLen = line.length();
                int factorLen = factorChar.length;

                while (i < textLen) {
                    if (j == -1 || line.charAt(i) == factorChar[j]) {
                        i++;
                        j++;
                        if (j == factorLen) {
                            System.out.print(lineNumber + "：");
                            System.out.println(line);
                            j = carryover[j];
                        }
                    } else {
                        j = carryover[j];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
