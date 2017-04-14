package com.smsspamdetector.trainer;

import java.util.*;
import java.io.*;

import com.google.gson.*;
import com.smsspamdetector.common.DataNormalizer;

/**
 * Calculates the prior probabilities and counts of features in the training data for Bernoulli naive bayes model.
 * 
 * @author nirav99
 *
 */
public class PriorCountsCalculator
{
  private transient ArrayList<String> spamData;
  private transient ArrayList<String> nonspamData;
  
  private int totalRecords;
  private int totalSpamRecords;
  private int totalNonSpamRecords;
  
  private transient DataNormalizer normalizer;
  
  private Features spamFeatures;
  private Features nonspamFeatures;
  
  public PriorCountsCalculator(ArrayList<LabeledData> labeledDataList)
  {
  	normalizer = new DataNormalizer();
    spamData = new ArrayList<String>();
    nonspamData = new ArrayList<String>();
  	
    separateLabeledData(labeledDataList);
    
  	totalRecords = this.spamData.size() + this.nonspamData.size();
  	this.totalSpamRecords = this.spamData.size();
  	this.totalNonSpamRecords = this.nonspamData.size();
  	
  	spamFeatures = new Features("spam", normalizer);
  	nonspamFeatures = new Features("nonspam", normalizer);
  	
  	updateFeatureCounts(spamData, spamFeatures, "spam");
  	updateFeatureCounts(nonspamData, nonspamFeatures , "nonspam");
  	
  	spamFeatures.calculateConditionalProbabilities(totalSpamRecords);
  	nonspamFeatures.calculateConditionalProbabilities(totalNonSpamRecords);
  }
  
  /**
   * Separates the given labeled training data into spam and non-spam
   * @param labeledDataList
   */
  private void separateLabeledData(ArrayList<LabeledData> labeledDataList)
  {
  	for(LabeledData labeledData : labeledDataList)
  	{
  		if(labeledData.label().equalsIgnoreCase("spam"))
  			spamData.add(normalizer.normalize(labeledData.text()));
  		else
  			nonspamData.add(normalizer.normalize(labeledData.text()));
  	}
  }
  
  private void updateFeatureCounts(ArrayList<String> data, Features features, String classLabel)
  {
  	for(String s : data)
  		features.updateFeatureCounts(classLabel, s);
  }
  
  public void writeCountsToFile(File outputFile) throws IOException
  {
  	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
  	Gson gson = new GsonBuilder().setPrettyPrinting().create();
  	
  	String jsonText = gson.toJson(this);
  	
  	writer.write(jsonText);
  	writer.close();
  }
  
  public int totalRecords()
  {
  	return this.totalRecords;
  }
  
  public int totalSpamRecords()
  {
  	return this.totalSpamRecords;
  }
  
  public int totalNonspamRecords()
  {
  	return this.totalNonSpamRecords;
  }
  
  public Features spamFeatures()
  {
  	return this.spamFeatures;
  }
  
  public Features nonspamFeatures()
  {
  	return this.nonspamFeatures;
  }
}
