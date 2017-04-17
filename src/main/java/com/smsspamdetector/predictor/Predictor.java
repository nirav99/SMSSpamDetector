package com.smsspamdetector.predictor;

import java.io.*;
import java.util.HashSet;

import com.google.gson.Gson;
import com.smsspamdetector.common.DataNormalizer;
import com.smsspamdetector.common.Features;
import com.smsspamdetector.common.PriorCountsCalculator;

/**
 * Given a set of messages, it classifies them as spam or nonspam
 * @author nirav99
 *
 */
public class Predictor
{
  private DataNormalizer normalizer;
  
  private double probSpam;
  private double probNonspam;
  private Features spamFeatures;
  private Features nonSpamFeatures;
  
  public Predictor(File modelFile) throws IOException
  {
  	this.normalizer = new DataNormalizer();
  	buildTrainedModel(modelFile);
  }
  
  private void buildTrainedModel(File modelFile)  throws IOException
  {
  	StringBuilder content = new StringBuilder();
  	String line = null;
  	
  	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(modelFile), "UTF-8"));
  	
  	while((line = reader.readLine()) != null)
      content.append(line).append("\n");
  	reader.close();
  	
  	Gson gson = new Gson();
  	
  	PriorCountsCalculator priorCounts = gson.fromJson(content.toString(), PriorCountsCalculator.class);
  	
  	probSpam = 1.0 * priorCounts.totalSpamRecords() / priorCounts.totalRecords();
  	probNonspam = 1.0 * priorCounts.totalNonspamRecords() / priorCounts.totalRecords();
  	
  	this.spamFeatures = priorCounts.spamFeatures();
  	this.nonSpamFeatures = priorCounts.nonspamFeatures();
  }
  
  public void classifyMessages(File dataFile) throws IOException
  {
  	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF-8"));
  	String line = null;
  	
  	while((line = reader.readLine()) != null)
  		classifyMessage(line);
  	reader.close();
  }
  
  private void classifyMessage(String line)
  {
    HashSet<String> wordSetFromDocument = getWordsSetFromDocument(line);
  	
  	
  	double likelihoodSpam = getLikelihood(wordSetFromDocument, spamFeatures, probSpam);
  	double likelihoodNonspam = getLikelihood(wordSetFromDocument, nonSpamFeatures, probNonspam);
  	
  	String predictedClassLabel = (likelihoodSpam > likelihoodNonspam) ? "spam" : "nonspam";
  	
  	System.out.println("Message :" + line);
  	System.out.println("Class label : " + predictedClassLabel);
  	System.out.println("========================");
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
  
  public static void main(String[] args)
  {
  	try
  	{
  		if(args == null || args.length != 2 || args[0].toLowerCase().contains("help"))
  		{
  			printUsage();
  			return;
  		}
  		File modelFile = new File(args[0]);
  		File dataFile = new File(args[1]);
  		
  		Predictor predictor = new Predictor(modelFile);
  		predictor.classifyMessages(dataFile);
  	}
  	catch(Exception e)
  	{
  		System.err.println(e.getMessage());
  		e.printStackTrace();
  	}
  }
  
  private static void printUsage()
  {
  	System.err.println("Usage : ");
  	System.err.println("Provide 2 parameters namely ");
  	System.err.println("ModelFile - json file created during the training phase");
  	System.err.println("DataFile - file having messages to check for spam/ nonspam");
  }
}
