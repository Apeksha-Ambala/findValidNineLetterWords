package org.example;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

/**
 * This is a test class for Find Valid Nine-Letter Words program
 *
 * @author Apeksha Ambala
 * @since 1.0
 * @version 1.0
 */
@PrepareForTest
public class TestFindValidNineLetterWords {

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  private List<String> loadNineLetterWordList() {
    System.out.println("In loadDictionaryAndNineLetterWordList()");
    List<String> dict = new ArrayList<>();
    dict.add("ABOUNDING");
    return dict;
  }

  @Test
  public void testCheckValidWords() throws Exception {
    List<String> wordsToCheck = new ArrayList<>();
    List<String> expected = new ArrayList<>();
    List<String> notExpected =
        Arrays.asList(
            new String[] {"BOUNDING", "BOUNING", "BONING", "BOING", "BING", "BIG", "BI", "I"});

    wordsToCheck.add("ABOUNDING");

    List<String> wordList =
        Whitebox.invokeMethod(new findValidNineLetterWords(), "checkValidWords", wordsToCheck);

    // Since the dictionary is empty, it should not return any value but empty list
    Assertions.assertNotNull(wordList);
    Assertions.assertEquals(expected, wordList);
    Assertions.assertNotEquals(notExpected, wordList);
  }
}
