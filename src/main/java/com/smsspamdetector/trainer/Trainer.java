package com.smsspamdetector.trainer;

import java.io.*;
import java.util.*;

import com.smsspamdetector.common.DataNormalizer;


/**
 * The main trainer program. It uses Bernoulli naive bayes approach.
 * It reads the given data file, splits it into training and validation data sets,
 * calculates the conditional probabilities for Bernoulli Naive Bayes.
 * Thereafter, it generates a json file that contains these conditional probabilites.
 * It also uses a validator to calculate the accuracy on the validation data set.
 * @author nirav99
 *
 */
public class Trainer
{
	private ArrayList<LabeledData> trainingDataList;
	private ArrayList<LabeledData> validationDataList;
  
	private PriorCountsCalculator priorCalculator;
	
  public Trainer(File dataFile, File outputFile) throws IOException
  {
  	DataReader dataReader = new DataReader(dataFile);
  	DataSeparator dataSeparator = new DataSeparator(dataReader.labeledData());

  	trainingDataList = dataSeparator.trainingData();
  	validationDataList = dataSeparator.validationData();
  	
  	System.out.println("Number of records in training set : " + trainingDataList.size());
  	System.out.println("Number of records in validation set : " + validationDataList.size());
  	trainModel(outputFile);
  }
  
  private void trainModel(File outputFile) throws IOException
  {
  	DataNormalizer normalizer = new DataNormalizer();
  	priorCalculator = new PriorCountsCalculator(trainingDataList);
  	priorCalculator.writeCountsToFile(outputFile);
  	
  	Validator validator = new Validator(priorCalculator, normalizer);
  	System.out.println("Accuracy on validation data set : ");
  	validator.predict(validationDataList);
  	
//  	System.out.println("Accuracy on training data set : ");
//  	validator.predict(trainingDataList);
  }
  
  public static void main(String[] args)
  {
  	try
  	{
//  		File inputFile = new File("/Users/nirav99/Documents/JavaPrograms/SMSSpamDetector/data/SMSSpamCollection.tsv");
//  		File outputFile = new File("/Users/nirav99/Documents/JavaPrograms/SMSSpamDetector/data/priorcounts.json");
  		
  		if(args == null || args.length != 2 || args[0].toLowerCase().contains("help"))
  		{
  			printUsage();
  			return;
  		}
  		File inputFile = new File(args[0]);
  		File outputFile = new File(args[1]);
  		
  		if(!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead())
  		{
  			System.err.println("Error : " + inputFile.getAbsolutePath() + " must be a valid readable file");
  			return;
  		}
  		if(outputFile.exists() && outputFile.isDirectory())
  		{
  			System.out.println("Error : " + outputFile.getAbsolutePath() + " can't be a directory");
  			return;
  		}
  		
  		Trainer trainer = new Trainer(inputFile, outputFile);
  	}
  	catch(Exception e)
  	{
  		e.printStackTrace();
  	}
  }
  
  private static void printUsage()
  {
  	System.err.println("Usage : ");
  	System.err.println("Provide 2 parameters namely ");
  	System.err.println("DataFile - the labeled spam / non-spam data");
  	System.err.println("OutputFile - where the conditional probabilities and the class counts are written to");
  }
}
