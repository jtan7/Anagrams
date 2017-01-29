/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<>();
    private int wordLength;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;

        wordLength = DEFAULT_WORD_LENGTH;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            } else {
                lettersToWord.put(sortedWord, new ArrayList<String>(Arrays.asList(word)));
            }

            if (sizeToWords.containsKey(word.length())) {
                sizeToWords.get(word.length()).add(word);
            } else {
                sizeToWords.put(word.length(), new ArrayList<String>(Arrays.asList(word)));
            }

        }
    }

    public boolean isGoodWord(String word, String base) {
        if (wordSet.contains(word)) {
            return !word.contains(base);
        }
        return false;
    }

    public String sortLetters(String word) {
        char chars[] = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        int wordListSize = wordList.size();
        int targetWordSize = targetWord.length();
        for (int i = 0; i < wordListSize; i++) {
            if (wordList.get(i).length() == targetWordSize) {
                String sortedTargetWord = sortLetters(targetWord);
                String sortedWord = sortLetters(wordList.get(i));
                if (sortedTargetWord.equals(sortedWord)) {
                    result.add(wordList.get(i));
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char alphabetChar = 'a'; alphabetChar <= 'z'; alphabetChar ++) {
            List<String> anagrams = getAnagrams(word + alphabetChar);
            if (anagrams.size() > 0) {
                result.addAll(anagrams);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        Random rand = new Random();
        for (int i = wordLength; i < MAX_WORD_LENGTH; i++) {
            int startingIndex = rand.nextInt(sizeToWords.get(i).size());
            for (int j = startingIndex; j < sizeToWords.get(i).size(); j++)  {
                if (getAnagrams(sizeToWords.get(i).get(j)).size() >= MIN_NUM_ANAGRAMS) {
                    wordLength++;
                    return sizeToWords.get(i).get(j);
                }
            }
            for (int j = 0; j < startingIndex; j++)  {
                if (getAnagrams(sizeToWords.get(i).get(j)).size() >= MIN_NUM_ANAGRAMS) {
                    wordLength++;
                    return sizeToWords.get(i).get(j);
                }
            }
        }
        return "";
    }
}
