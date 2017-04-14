package com.smsspamdetector.trainer;

import java.util.*;

/**
 * Separates the input data in training and validation sets.
 * Approximately 20% data is saved as validation set and the rest of the data is used for training.
 * @author nirav99
 *
 */
public class DataSeparator
{
  private ArrayList<LabeledData> trainingData;
  private ArrayList<LabeledData> validationData;
  private Random random;
  
  public DataSeparator(ArrayList<LabeledData> labeledData)
  {
  	trainingData = new ArrayList<LabeledData>();
  	validationData = new ArrayList<LabeledData>();
  	
    random = new Random();
    
    splitData(labeledData);
  }
  
  public ArrayList<LabeledData> trainingData()
  {
  	return this.trainingData;
  }
  
  public ArrayList<LabeledData> validationData()
  {
  	return this.validationData;
  }
  
  private void splitData(ArrayList<LabeledData> inputData)
  {
  	double randomValue;
  	
  	for(LabeledData input : inputData)
  	{
  		randomValue = random.nextDouble();
  		
  		if(randomValue < 0.19)
  			validationData.add(input);
  		else
  			trainingData.add(input);
  	}
  }
}
