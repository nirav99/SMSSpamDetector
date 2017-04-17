package com.smsspamdetector.trainer;

import java.util.*;

import com.smsspamdetector.common.DataNormalizer;
import com.smsspamdetector.common.Features;
import com.smsspamdetector.common.PriorCountsCalculator;

/**
 * Classifies the labeled data in the validation set to calculate the F-score.
 * It uses Bernoulli Naive Bayes model.
 * @author nirav99
 *
 */
public class Validator
{
  private PriorCountsCalculator priorCountsCalculator;
  
  private double probSpam;
  private double probNonspam;

  private Features spamFeatures;
  private Features nonSpamFeatures;
  private DataNormalizer normalizer;
  
  private int TP; // true positive count
  private int FP; // false positive count
  private int FN; // false negative count
  
  public Validator(PriorCountsCalculator priorCounts, DataNormalizer normalizer)
  {
  	this.priorCountsCalculator = priorCounts;
  	
  	probSpam = 1.0 * priorCountsCalculator.totalSpamRecords() / priorCountsCalculator.totalRecords();
  	probNonspam = 1.0 * priorCountsCalculator.totalNonspamRecords() / priorCountsCalculator.totalRecords();
  	
  	this.spamFeatures = priorCountsCalculator.spamFeatures();
  	this.nonSpamFeatures = priorCountsCalculator.nonspamFeatures();
  	
  	this.normalizer = normalizer;
  	
  	System.out.println("Probability of class spam : " + probSpam);
  	System.out.println("Probability of class nonspam : " + probNonspam);
  }
  
  public void predict(ArrayList<LabeledData> dataSet)
  {
  	TP = 0;
  	FP = 0;
  	FN = 0;
  	
  	for(LabeledData labeledData : dataSet)
  		processData(labeledData);
  	
  	System.out.println("Overall accuracy : ");
  	
  	System.out.println("Total records : " + dataSet.size());
  	System.out.println("Total true positives : " + TP);
  	System.out.println("Total false positives : " + FP);
  	System.out.println("Total false negatives : " + FN);
  	
  	double precision = (TP > 0) ? 1.0 * TP / (TP + FP) : 0;
  	double recall = (TP > 0) ? 1.0 * TP / (TP + FN) : 0;
  	double fscore = (precision > 0 || recall > 0) ? 2.0 * precision * recall / (precision + recall) : 0;
  	
  	System.out.format("Precision : %.4f\n", precision);
  	System.out.format("Recall : %.4f\n", recall);
  	System.out.format("fscore : %.4f\n", fscore);
  }
  
  private void processData(LabeledData labeledData)
  {
  	String actualClassLabel = labeledData.label();
    HashSet<String> wordSetFromDocument = getWordsSetFromDocument(labeledData.text());
  	
  	
  	double likelihoodSpam = getLikelihood(wordSetFromDocument, spamFeatures, probSpam);
  	double likelihoodNonspam = getLikelihood(wordSetFromDocument, nonSpamFeatures, probNonspam);
  	
  	String predictedClassLabel = (likelihoodSpam > likelihoodNonspam) ? "spam" : "nonspam";
  	
  	if(predictedClassLabel.equals(actualClassLabel))
  	{
  	  System.out.println("ACTUAL LABEL = " + actualClassLabel + " PREDICTED LABEL = " + predictedClassLabel);
  	
  	  System.out.println("TEXT = " + labeledData.text());
//  	  System.out.println("Actual : " + actualClassLabel + " Predicted : " + predictedClassLabel);
//  	  System.out.println("Spam class score : " + likelihoodSpam + " Non spam class score : " + likelihoodNonspam);
  	  System.out.println("====================================");
  	}
  	if(actualClassLabel.equalsIgnoreCase(predictedClassLabel) && actualClassLabel.equalsIgnoreCase("spam"))
      TP++;
  	else
    if(actualClassLabel.equalsIgnoreCase("spam") && predictedClassLabel.equalsIgnoreCase("nonspam"))
    	FN++;
    else
    if(actualClassLabel.equalsIgnoreCase("nonspam") && predictedClassLabel.equalsIgnoreCase("spam"))
      FP++;
  }
  
  private HashSet<String> getWordsSetFromDocument(String text)
  {
  	String normalizedText = normalizer.normalize(text);
  	HashSet<String> wordSet = new HashSet<String>();
  	
  	String[] words = normalizedText.toLowerCase().split("\\s+");
  	
  	for(String word : words)
  	{
  		wordSet.add(word.replaceFirst("^\\W*", "").replaceFirst("\\W*$", ""));
  	}
  	
  	return wordSet;
  }
  
  /**
   * Calculates the likelihood using Bernoulli Naive Bayes.
   * @param wordSetFromDoc
   * @param features
   * @param classProbability
   * @return
   */
  private double getLikelihood(HashSet<String> wordSetFromDoc, Features features, double classProbability)
  {
  	double score = classProbability;
  	
  	HashSet<String> vocabulary = features.vocabulary();
  	
  	for(String word : vocabulary)
  	{
  		if(wordSetFromDoc.contains(word))
  		  score += Math.log(features.getConditionalProbabilityForWord(word));
  		else
  			score += Math.log(1 - features.getConditionalProbabilityForWord(word));
  	}
  	return score;
  }
}
