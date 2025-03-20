package org.rhw.bmr.project.service.impl;

import org.rhw.bmr.project.common.algo.KPMalgo;
import org.rhw.bmr.project.common.algo.egreplike.*;
import org.rhw.bmr.project.dto.req.TextInternalSearchByEgreplikeReqDTO;
import org.rhw.bmr.project.dto.req.TextInternalSearchByKMPReqDTO;
import org.rhw.bmr.project.service.TextInternalSearchService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TextInternalSearchImpl implements TextInternalSearchService {

    @Override
    public String[] TextInternalSearchByKMP(TextInternalSearchByKMPReqDTO requestParam) {
        String url = requestParam.getURL();
        String factor = requestParam.getWord();

        KPMalgo kpMalgo = new KPMalgo();
        List<String> matchingLines = new ArrayList<>();

        try {

            List<String> results = kpMalgo.KPM(factor, url);
            matchingLines.addAll(results);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return matchingLines.toArray(new String[0]);
    }

    @Override
    public String[] TextInternalSearchByEgreplike(TextInternalSearchByEgreplikeReqDTO requestParam) {
        String url = requestParam.getURL();
        String word = requestParam.getRegular();

        // 分析正则表达式
        RegEx regEx = new RegEx();
        RegExTree tree = regEx.toTree(word);

        Construction construction = new Construction();
        NonDetFiniAuto ndfa = construction.convert(tree);
        DetFiniAuto dfa = construction.convertToDFA(ndfa);
        DetFiniAuto minimizeDFA = construction.minimizeDFA(dfa);

        List<String> matchingLines = new ArrayList<>();


        while (true) {
            try {
                URL fileURL = new URL(url);
                HttpURLConnection httpConnection = (HttpURLConnection) fileURL.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setInstanceFollowRedirects(false);

                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                    url = httpConnection.getHeaderField("Location");
                    continue;
                }

                try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
                    String line;
                    int lineNumber = 0;
                    while ((line = br.readLine()) != null) {
                        lineNumber++;

                        if (matchesDFA(minimizeDFA, line)) {
                            matchingLines.add(lineNumber + ": " + line);
                        }
                    }
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return matchingLines.toArray(new String[0]);
    }

    @Override
    public long[] TextInternalSearchByKMPLong(TextInternalSearchByKMPReqDTO requestParam) {
        String url = requestParam.getURL();
        String factor = requestParam.getWord();

        KPMalgo kpMalgo = new KPMalgo();
        List<Long> matchingLineNumbers;

        try {

            matchingLineNumbers = kpMalgo.KPMLong(factor, url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return matchingLineNumbers.stream().mapToLong(Long::longValue).toArray();
    }

    @Override
    public long[] TextInternalSearchByEgreplikeLong(TextInternalSearchByEgreplikeReqDTO requestParam) {
        String url = requestParam.getURL();
        String regEx = requestParam.getRegular();

        RegEx regEx1 = new RegEx();
        RegExTree tree = regEx1.toTree(regEx);

        Construction construction = new Construction();
        NonDetFiniAuto ndfa = construction.convert(tree);
        DetFiniAuto dfa = construction.convertToDFA(ndfa);
        DetFiniAuto minimizeDFA = construction.minimizeDFA(dfa);

        List<Long> matchingLineNumbers = new ArrayList<>();


        while (true) {
            try {
                URL fileURL = new URL(url);
                HttpURLConnection httpConnection = (HttpURLConnection) fileURL.openConnection();
                httpConnection.setRequestMethod("GET");
                httpConnection.setInstanceFollowRedirects(false);

                int responseCode = httpConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
                    url = httpConnection.getHeaderField("Location");
                    continue;
                }

                try (BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))) {
                    String line;
                    long lineNumber = 0;
                    while ((line = br.readLine()) != null) {
                        lineNumber++;
                        if (matchesDFA(minimizeDFA, line)) {
                            matchingLineNumbers.add(lineNumber);
                        }
                    }
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return matchingLineNumbers.stream().mapToLong(Long::longValue).toArray();
    }

    private static boolean matchesDFA(DetFiniAuto dfa, String line) {
        for (int start = 0; start < line.length(); start++) {
            int currentState = dfa.startState;

            for (int i = start; i < line.length(); i++) {
                char c = line.charAt(i);

                Map<Character, Integer> transitions = dfa.transitions.get(currentState);
                if (transitions == null || !transitions.containsKey(c)) {
                    break;
                }

                currentState = transitions.get(c);

                if (dfa.acceptStates.contains(currentState)) {
                    return true;
                }
            }
        }
        return false;
    }
}