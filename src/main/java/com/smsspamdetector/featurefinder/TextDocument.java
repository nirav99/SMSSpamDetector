package com.smsspamdetector.featurefinder;

import java.util.*;

import com.smsspamdetector.common.DataNormalizer;

/**
 * A single text message.
 * @author nirav99
 *
 */
public class TextDocument
{
  private HashMap<String, Integer> wordFreqMap;
  private String classLabel;
  private int totalWords;
  private String text;
  private static DataNormalizer normalizer;
  
  static
  {
  	normalizer = new DataNormalizer();
  }
  
  public TextDocument(String text, String classLabel)
  {
  	wordFreqMap = new HashMap<String, Integer>();
  	this.classLabel = classLabel;
  	this.text = text;
  	buildWordFreqMap();
  }
  
  private void buildWordFreqMap()
  {
  	String normalizedText = normalizer.normalize(text);
  	String[] words = normalizedText.split("\\s+");
  	totalWords = words.length;
  	
  	Integer value = null;
  	String normalizedWord = null;
  	
  	for(String word : words)
  	{
  		if(!word.startsWith("##") && !word.endsWith("##"))
  		  normalizedWord = word.toLowerCase().replaceFirst("^\\W*", "").replaceFirst("\\W*$", "");
  		else
  			normalizedWord = word.toLowerCase();
  		
 // 		if(normalizedWord.trim().matches("\\d+") || normalizedWord.trim().matches("\\d+\\.\\d+"))
 // 			continue;
  		
//  		if(normalizedWord.matches("txt"))
//  			normalizedWord = "text";
  		value = wordFreqMap.get(normalizedWord);
  		
  		if(value == null) value = 0;
  		value = value + 1;
  		
  		wordFreqMap.put(normalizedWord, value);
  	}
  }
  
  public HashMap<String, Integer> wordCountMap()
  {
  	return wordFreqMap;
  }
  
  public String classLabel()
  {
  	return classLabel;
  }
  
  public int totalWords()
  {
  	return totalWords;
  }
  
  public Integer getWordFrequency(String word)
  {
  	Integer frequency = wordFreqMap.get(word.toLowerCase());
  	
  	return (frequency != null) ? frequency : 0;
  }
  
  public boolean doesWordExist(String word)
  {
  	return wordFreqMap.containsKey(word.toLowerCase());
  }
  
  public String text()
  {
  	return this.text;
  }
}
