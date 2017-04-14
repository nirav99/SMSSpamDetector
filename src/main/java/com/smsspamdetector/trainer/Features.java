package com.smsspamdetector.trainer;

import java.util.*;

import com.smsspamdetector.common.DataNormalizer;

/**
 * Encapsulates the features of spam and non-spam messages identified using mutual information.
 * It builds the vocabulary using these features and calculates the conditional probabilities for the given class.
 * @author nirav99
 *
 */
public class Features
{
	/**
	 * The first 3 namely phonenumber, wordswithletternumber and money are not actual words, but word patterns identified
	 * to have high occurrence during mutual information analysis.
	 * Generally spam messages have a much higher occurrences of phone numbers.
	 * Also, words with letters and numbers e.g R3c3ived are found more commonly in spam.
	 * Words representing money e.g. $1300.00 are also found more commonly in spam.
	 */
  private transient final static String[] features = {
    "phonenumber", "wordwithletternumber", "money", "text", "call", "free", "win", "claim", "to", 
    "mobile", "prize", "i", "your", "stop", "now", "ur    gent", "reply", "guaranteed", "service", "cash", 
    "18", "from", "nokia", "contact", "tone", "our", "awarded", "a", "customer", "150ppm", "per", "draw", "latest", 
    "for", "landline", "my", "code", "new", "ringtone", "150p", "line", "box"	
  };
  private transient static HashSet<String> vocabulary;
  
  private transient String classLabel;
  private transient DataNormalizer normalizer;

  private transient HashMap<String, Integer> numDocMap; // Number of documents in the given class where the specified feature word appears
  
  private HashMap<String, Double> conditionalProbabilityMap ; // Log-likelihood map for words
  
  /**
   * Builds a vocabulary set from the list of feature words
   */
  static
  {
  	vocabulary = new HashSet<String>();
  	
  	for(String s : features)
  		vocabulary.add(s);
  }
  
  public Features(String classLabel, DataNormalizer normalizer)
  {
  	this.classLabel = classLabel;
  	numDocMap = new HashMap<String, Integer>();
  	this.normalizer = normalizer;
  	conditionalProbabilityMap = new HashMap<String, Double>();
  }
  
  /**
   * Splits the given text into words and updates the count of how many document a given word occurs in for the given class.
   * @param classLabel
   * @param text
   */
  public void updateFeatureCounts(String classLabel, String text)
  {
  	if(!this.classLabel.equalsIgnoreCase(classLabel))
  	{
  		System.out.println("Not updating counts as class labels don't match for " + text);
  		return;
  	}
  	
  	String[] words = normalizer.normalize(text).toLowerCase().split("\\s+");
  	
  	for(String word : words)
  	{
  		word = word.replaceFirst("^\\W*", "").replaceAll("\\W*$", "");
  		
  		if(vocabulary.contains(word))
  		{
  			Integer value = numDocMap.get(word);
  			if(value == null) value = 0;
  			value = value + 1;
  			numDocMap.put(word, value);
  		}
  	}
  }
  
  /**
   * Calculates the conditional probabilities of the words in the vocabulary in the given class.
   * @param numDocsInClass
   */
  public void calculateConditionalProbabilities(int numDocsInClass)
  {
  	String word;
  	Integer value;
  	double conditionalProbability;
  	
  	for(Map.Entry<String, Integer> entry : numDocMap.entrySet())
  	{
  		word = entry.getKey();
  		value = entry.getValue();
  		
  		if(value == null)
  			value = 0;
  		
  		// Apply smoothing for bernoulli naive bayes
  		conditionalProbability = 1.0 * (value.intValue() + 1) / (numDocsInClass + 2.0);
  		
  		conditionalProbabilityMap.put(word, conditionalProbability);
  	}
  }
  
  public HashSet<String> vocabulary()
  {
  	return vocabulary;
  }
  
  /**
   * Returns the conditional probability for the given word
   * @param word
   * @return
   */
  public double getConditionalProbabilityForWord(String word)
  {	
  	Double result = conditionalProbabilityMap.get(word);
  	
  	return result != null ? result.doubleValue() : 0;
  }
}
