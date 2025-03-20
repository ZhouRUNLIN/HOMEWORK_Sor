package org.rhw.bmr.project.common.algo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> KPM(String factor, String fileURL) throws Exception {
        char[] factorChar = factor.toCharArray();
        int[] carryover = computeCarryover(factorChar);
        List<String> matchingLines = new ArrayList<>();

        while (true) {
            URL url = new URL(fileURL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setInstanceFollowRedirects(false);
            httpConnection.setRequestMethod("GET");

            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                fileURL = httpConnection.getHeaderField("Location");
                continue;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
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
                                matchingLines.add(lineNumber + ": " + line);
                                j = carryover[j];
                            }
                        } else {
                            j = carryover[j];
                        }
                    }
                }
            } finally {
                httpConnection.disconnect();
            }

            break;
        }

        return matchingLines;
    }

    public List<Long> KPMLong(String factor, String fileURL) throws Exception {
        char[] factorChar = factor.toCharArray();
        int[] carryover = computeCarryover(factorChar);
        List<Long> matchingLineNumbers = new ArrayList<>();

        while (true) {
            URL url = new URL(fileURL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setInstanceFollowRedirects(false);
            httpConnection.setRequestMethod("GET");

            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                fileURL = httpConnection.getHeaderField("Location");
                continue;
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
                String line;
                long lineNumber = 0;
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
                                matchingLineNumbers.add(lineNumber);
                                j = carryover[j];
                            }
                        } else {
                            j = carryover[j];
                        }
                    }
                }
            } finally {
                httpConnection.disconnect();
            }

            break;
        }

        return matchingLineNumbers;
    }
}