package com.smsspamdetector.featurefinder;

import java.util.*;

/**
 * Calculates the mutual information of the words from the given spam and non-spam data sets.
 * It shows the 50 words with the highest mutual information score, which are selected for classification.
 * @author nirav99
 *
 */
public class MutualInformationCalculator
{
  private HashSet<String> wordSet;
  
  private HashMap<String, Integer> spamWordDocs;  // How many spam documents contain the given word
  private HashMap<String, Integer> nonspamWordDocs; // How many non-spam documents contain the given word
  
  private int totalSpamDocs;
  private int totalNonspamDocs;
  
  private ArrayList<MutualInformation> miScoreList;
  
  public MutualInformationCalculator(ArrayList<TextDocument> textDocumentList)
  {
  	wordSet = new HashSet<String>();
  	spamWordDocs = new HashMap<String, Integer>();
  	nonspamWordDocs = new HashMap<String, Integer>();
  	
  	buildMaps(textDocumentList);
  	calculateMI();
  }
  
  private void buildMaps(ArrayList<TextDocument> textDocumentList)
  {
  	for(TextDocument textDoc : textDocumentList)
  	{
  		wordSet.addAll(textDoc.wordCountMap().keySet());
  		
  		if(textDoc.classLabel().equalsIgnoreCase("spam"))
  		{
  			buildMapHelper(textDoc, spamWordDocs);
  			totalSpamDocs++;
  		}
  		else
  		{
  			buildMapHelper(textDoc, nonspamWordDocs);
  			totalNonspamDocs++;
  		}
  	}
  }
  
  private void buildMapHelper(TextDocument textDoc, HashMap<String, Integer> wordDocMap)
  {
  	Set<String> wordSet = textDoc.wordCountMap().keySet();
  	
  	Integer value = null;
  	
  	for(String word : wordSet)
  	{
  		value = wordDocMap.get(word);
  		
  		if(value == null)
  			value = 0;
  		value = value + 1;
  		
  		wordDocMap.put(word, value);
  	}
  }
  
  private void calculateMI()
  {
  	miScoreList = new ArrayList<MutualInformation>();
  	
  	for(String word : wordSet)
  	{
  		miScoreList.add(new MutualInformation(word, getMIScore(word)));
  	}
  	
  	Collections.sort(miScoreList, new MutualInformationComparator());
  }
  
  public void showMutualInformationScoreValues()
  {
  	System.out.println("Total spam docs = " + totalSpamDocs);
  	System.out.println("Total non-spam docs = " + totalNonspamDocs);
  	
  	String word = null;
  	
  	for(int i = 0; i < 50 && i < miScoreList.size(); i++)
  	{
  		word = miScoreList.get(i).word();
  		System.out.print(word + " : ");
  		System.out.format("%.4f\n", miScoreList.get(i).mutualInformationScore());
  		System.out.println("Num spam docs : " + spamWordDocs.get(word));
  		System.out.println("Num non-spam docs : " + nonspamWordDocs.get(word));
  		System.out.println("==========================================");
  	}
  }
  
  private double getMIScore(String word)
  {
  	int N = totalSpamDocs + totalNonspamDocs;
  	Integer temp;
  	
  	// N11 = number of spam documents in which this word appears
  	int N11 = ((temp = spamWordDocs.get(word)) != null) ? temp.intValue() : 0; 
  	// N10 = number of non-spam documents in which this word appears
  	int N10 = ((temp = nonspamWordDocs.get(word)) != null) ? temp.intValue() : 0;
  	// N01 = number of spam documents that don't have this word
  	int N01 = totalSpamDocs - N11;
  	// N00 = number of non-spam documents that don't have this word
  	int N00 = totalNonspamDocs - N10;
  	
  	int N1_ = N11 + N10;
  	int N_1 = N01 + N11;
  	
  	int N0_ = N00 + N01;
  	int N_0 = N10 + N00;
  	
  	double mutualInformation = 0;
  	
  	double term1 = calculateTerm(N11, N1_, N_1, N); //(N1_ > 0 && N_1 > 0 && N11 > 0) ? 1.0 * N11/N * Math.log(1.0 * N * N11 / (N1_ * N_1)) : 0;
  	double term2 = calculateTerm(N01, N0_, N_1, N); //(N0_ > 0 && N_1 > 0 && N01 > 0) ? 1.0 * N01/N * Math.log(1.0 * N * N01 / (N0_ * N_1)) : 0;
  	double term3 = calculateTerm(N10, N1_, N_0, N); // ? 1.0 * N10/N * Math.log(1.0 * N * N10 / (N1_ * N_0)) : 0;
  	double term4 = calculateTerm(N00, N0_, N_0, N); //(N0_ > 0 && N_0 > 0 && N00 > 0) ? 1.0 * N00/N * Math.log(1.0 * N * N00 / (N0_ * N_0)) : 0;
  	
  	mutualInformation = term1 + term2 + term3 + term4; 
  	return mutualInformation;
  }
  
  private double calculateTerm(int numerator, int denom1, int denom2, int N)
  {
  	double result = 0;
  	
  	if(numerator == 0 || denom1 == 0 || denom2 == 0)
  		return 0.0;
  	
 // 	System.out.println(numerator + " " + denom1 + " " + denom2 + " " + N);
  	double temp = 1.0 * N / denom1 * numerator / denom2;
 // 	System.out.println("temp = " + temp + " log temp = " + Math.log(temp));
  	
  	result = 1.0 * numerator / N * Math.log(temp);
 // 	System.out.println("result = " + result);
  	return result;
  }
}
