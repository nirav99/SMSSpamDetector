package com.smsspamdetector.featurefinder;

import java.io.*;
import java.util.*;

public class Main
{
  public static void main(String[] args)
  {
  	try
  	{
  	//	File inputFile = new File("/Users/nirav99/Documents/JavaPrograms/SMSSpamDetector/data/SMSSpamCollection.tsv");
  		
  		if(args == null || args.length != 1 || args[0].toLowerCase().contains("help"))
  		{
  			printUsage();
  			return;
  		}
  		File inputFile = new File(args[0]);
  		
  		if(!inputFile.exists() || !inputFile.isFile() || !inputFile.canRead())
  		{
  			System.err.println("Error : " + inputFile.getAbsolutePath() + " must be a valid readable file");
  			return;
  		}
  		
  		DataReader reader = new DataReader(inputFile);
  		
  		ArrayList<TextDocument> textDocumentList = reader.textDocumentList();
  		
  		MutualInformationCalculator mic = new MutualInformationCalculator(textDocumentList);
  		mic.showMutualInformationScoreValues();
  		
  	}
  	catch(Exception e)
  	{
  		e.printStackTrace();
  	}
  }
  
  private static void printUsage()
  {
  	System.err.println("Usage : ");
  	System.err.println("Provide 1 parameter");
  	System.err.println("DataFile - the labeled spam / non-spam data");
  }
}
