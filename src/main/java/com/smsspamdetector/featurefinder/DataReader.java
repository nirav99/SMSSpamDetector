package com.smsspamdetector.featurefinder;

import java.io.*;
import java.util.*;

import com.smsspamdetector.common.DataNormalizer;

public class DataReader
{
  private ArrayList<String> nonSpamText;
  private ArrayList<String> spamText;
  private ArrayList<TextDocument> textDocumentList;
  
  /**
   * Reads the data file and builds a list of TextDocuments
   * @param inputFile
   * @throws IOException
   */
  public DataReader(File inputFile) throws IOException
  {
  	nonSpamText = new ArrayList<String>();
  	spamText = new ArrayList<String>();
  	textDocumentList = new ArrayList<TextDocument>();
  	readFileAndCollectData(inputFile);
  }
  
  public ArrayList<TextDocument> textDocumentList()
  {
  	return this.textDocumentList;
  }
  
  private void readFileAndCollectData(File inputFile) throws IOException
  {
  	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
  	String line = null;
  	
  	String[] tokens = null;
  	
  	while((line = reader.readLine()) != null)
  	{
  		tokens = line.split("\t");
  		
  		if(tokens[0].equalsIgnoreCase("spam"))
  		{
  		  addData(spamText, tokens);
  		  textDocumentList.add(new TextDocument(tokens[1], "spam"));
  		}
  		else
  		{
  			addData(nonSpamText, tokens);
  			textDocumentList.add(new TextDocument(tokens[1], "nonspam"));
  		}
  	}
  	reader.close();
  }
  
  private void addData(ArrayList<String> list, String[] tokens)
  {
    if(tokens.length == 2)
    	list.add(tokens[1]);
    else
    if(tokens.length > 2)
    {
    	StringBuilder content = new StringBuilder();
    	
    	for(int i = 1; i < tokens.length; i++)
    		content.append(tokens[i]).append(" ");
    	
    	list.add(content.toString().trim());
    }
  }
}
