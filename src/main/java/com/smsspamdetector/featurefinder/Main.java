package com.smsspamdetector.featurefinder;

import java.io.*;
import java.util.*;

public class Main
{
  public static void main(String[] args)
  {
  	try
  	{
  		File inputFile = new File("/Users/nirav99/Documents/JavaPrograms/SMSSpamDetector/data/SMSSpamCollection.tsv");
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
}
