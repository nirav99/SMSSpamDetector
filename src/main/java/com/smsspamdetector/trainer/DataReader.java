package com.smsspamdetector.trainer;

import java.io.*;
import java.util.*;

/**
 * Reads the input data file and builds the list of data records
 * @author nirav99
 *
 */
public class DataReader
{
  private ArrayList<LabeledData> labeledDataList;
  
  public DataReader(File inputData) throws IOException
  {
  	labeledDataList = new ArrayList<LabeledData>();
  	readFile(inputData);
  }
  
  public ArrayList<LabeledData> labeledData()
  {
  	return this.labeledDataList;
  }
  
  private void readFile(File inputData) throws IOException
  {
  	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputData), "UTF-8"));
  	String line = null;
  	
  	while((line = reader.readLine()) != null)
  		processLine(line);
  	
  	reader.close();
  }
  
  private void processLine(String line)
  {
  	String[] tokens = line.split("\\t");
  	String classLabel = (tokens[0].equalsIgnoreCase("spam")) ? "spam" : "nonspam";
  	
  	if(tokens.length == 2)
  	{
  		labeledDataList.add(new LabeledData(classLabel, tokens[1]));
  	}
  	else
    if(tokens.length > 2)
    {
    	StringBuilder content = new StringBuilder();
    	
    	for(int i = 1; i < tokens.length; i++)
    		content.append(tokens[i]).append(" ");
    	
    	labeledDataList.add(new LabeledData(classLabel, content.toString().trim()));
    }
  }
}
