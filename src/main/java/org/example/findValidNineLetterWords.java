package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to parse all words to find 9-letter valid words
 *
 * @author Apeksha Ambala
 * @since 1.0
 * @version 1.0
 */
public class findValidNineLetterWords {
  private static final Set<String> dictionary = new HashSet<>();

  /**
   * This Method is used to load all words and find 9-letter valid words which satisfy some
   * conditions.
   */
  public static void main(String[] args) {
    try {
      Instant start = Instant.now();
      List<String> wordsToCheck = loadAllWords();
      Instant end = Instant.now();
      Duration timeElapsed = Duration.between(start, end);

      Instant start1 = Instant.now();
      List<String> wordList = checkValidWords(wordsToCheck);
      Instant end1 = Instant.now();
      Duration timeElapsed1 = Duration.between(start1, end1);

      // Display results:
      System.out.println("WORD LIST:");
      System.out.println("----------");
      System.out.println("List of 9-letter words satisfying the conditions are: \n" + wordList);
      System.out.println("\n_________________________________________________________");
      System.out.println("WORD COUNT:");
      System.out.println("-----------");
      System.out.println("Count of 9-letter words satisfying the conditions: " + wordList.size());
      System.out.println("_________________________________________________________\n");
      System.out.println("TIME:");
      System.out.println("-----");
      System.out.println("The time taken to run this program,");
      System.out.println(
          "1. Time taken to load the data: " + timeElapsed.toMillis() + " milliseconds");
      System.out.println(
          "2. Time taken to process the data: " + timeElapsed1.toMillis() + " milliseconds");
      System.out.println("======================================================");
      System.out.println(
          "Total Time taken:" + Duration.between(start, end1).toMillis() + " milliseconds");

    } catch (IOException ioException) {
      System.out.println("IOException occurred");
      throw new RuntimeException(ioException);
    } catch (Exception exception) {
      System.out.println("Exception occurred.");
      throw new RuntimeException(exception);
    }
  }

  /**
   * This Method is used to find all 9-letter valid words which satisfy below conditions. It prints
   * the list of words with all the words which it formed along with the total number of words which
   * satisfies the conditions.
   *
   * <ul>
   *   <li>It is possible to remove one letter from the word and get a valid 8-letter word;
   *   <li>One letter can be removed from the 8-letter word and a valid 7-letter word can be
   *       obtained;
   *   <li>From the 7-letter word one letter can be removed and a valid 6-letter word is obtained,
   *       etc., until a one-letter valid word is obtained;
   *   <li>Valid single letter words are "I" and "A"
   * </ul>
   *
   * @param wordsToCheck - List of String containing words to check against the conditions
   * @return List of String containing valid word with the list of words it formed.
   */
  public static List<String> checkValidWords(List<String> wordsToCheck) {
    List<String> wordList = new ArrayList<>();
    for (String word : wordsToCheck) {
      // 'A' and 'I' is the only valid single word, so if it is not found, skip the whole check.
      if (word.indexOf('A') == -1 && (word.indexOf('I') == -1)) {
        continue;
      }

      List<String> resString = CheckIfWordFollowRules(word);

      if (!resString.isEmpty()) {
        wordList.add("\n" + word + "::" + resString);
      }
    }

    return wordList;
  }

  /**
   * This Method is used to check if word satisfy the conditions.
   *
   * @param word - word to check against the conditions
   * @return List of String containing words it formed for the given word.
   */
  private static List<String> CheckIfWordFollowRules(String word) {
    List<String> returnValue = new ArrayList<>();
    // valid single letter words are "I" and "A"
    if (word.length() == 1 && (word.equalsIgnoreCase("I") || word.equalsIgnoreCase("A"))) {
      return returnValue;
    } else if (word.length() > 1) {
      List<List<String>> allWords = findAllWordsForEachLength(word, 0);

      if (allWords != null && !allWords.isEmpty()) {
        returnValue = allWords.get(0);
        returnValue =
            returnValue.stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .distinct()
                .collect(Collectors.toList());
        return returnValue;
      }
    }
    return returnValue;
  }

  /**
   * This Method is used to check each word and all it's sub-words. This will only run for 1
   * complete 8-letter word sequence.
   *
   * @param word - word to check against the conditions
   * @param counter - a counter to check only for 1 complete word sequence
   * @return a List of (List of String) containing word and it's sub words
   */
  private static List<List<String>> findAllWordsForEachLength(String word, int counter) {
    if (counter != 0) {
      return null;
    }
    List<List<String>> wordList = new ArrayList<>();
    if (word.length() == 1 && (word.equalsIgnoreCase("I") || word.equalsIgnoreCase("A"))) {
      wordList.add(Collections.singletonList(word));
      return wordList;
    }

    Set<String> validWords = getValidWordsAll(word);
    for (String eachWord : validWords) {
      List<List<String>> list = findAllWordsForEachLength(eachWord, counter);

      if (list != null && !list.isEmpty()) {
        if (eachWord.length() == 1 && (word.equalsIgnoreCase("I") || word.equalsIgnoreCase("A"))) {
          wordList.add(list.get(0));
        } else {
          List<String> reversedChain = new ArrayList<>(list.get(0));
          reversedChain.add(eachWord);
          wordList.add(reversedChain);

          if (eachWord.length() == 8) {
            counter++;
          }
        }
      }
    }

    if (wordList.isEmpty()) {
      return null;
    }

    return wordList;
  }

  /**
   * This Method is used to check all possible sub words which are part of the dictionary.
   *
   * @param word - word to check against the conditions
   * @return a set of String containing potentials sub-words
   */
  private static Set<String> getValidWordsAll(String word) {
    Set<String> validWords = new HashSet<>();
    if (word.length() > 1) {
      for (int i = 0; i < word.length(); i++) {
        StringBuilder stringBuilder = new StringBuilder(word);
        stringBuilder.deleteCharAt(i);
        String modifiedWord = stringBuilder.toString();
        if (dictionary.contains(modifiedWord)) {
          validWords.add(modifiedWord);
        } else if (modifiedWord.length() == 1
            && (modifiedWord.equalsIgnoreCase("I") || modifiedWord.equalsIgnoreCase("A"))) {
          validWords.add(modifiedWord);
        }
      }
    }
    return validWords;
  }

  /**
   * This Method is used to load the words in dictionary (variable); find and store 9-letter words
   * for processing
   *
   * @return a list of String containing list of words to check the conditions on
   */
  private static List<String> loadAllWords() throws IOException {
    List<String> nineLetterWords = new ArrayList<>();

    URL url =
        new URL(
            "https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

    // Read input from URL
    try (BufferedReader bufferedReader =
        new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
      bufferedReader
          .lines()
          .skip(2) // skip first 2 lines since they do not contain words
          .forEach(
              word -> {
                if (word.length() == 9) {
                  // All 9-letter words
                  nineLetterWords.add(word);
                } else if (word.length() < 9 && word.length() > 1) {
                  // All words less than 9 length and greater than 1 length as it doesn't make sense
                  // to have a large dictionary/data set with words
                  // 1. greater than 8 as we will only perform check for words of length 8 and less
                  // 2. equal to 1 as there are only 2 valid 1-letter word, i.e., 'I' and 'A'
                  dictionary.add(word);
                }
              });
    }

    return nineLetterWords;
  }
}
