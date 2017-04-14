package com.smsspamdetector.common;

import java.io.*;
import java.util.regex.*;

/**
 * Takes the data set and normalizes it to make it easier for feature collection
 * @author nirav99
 *
 */
public class DataNormalizer
{
  private final String moneyPattern = "(\\$|£)\\s*\\d+(,\\d+)?(\\.d+)?";
  private final String lettersAndNumbersPattern = "([A-Za-z_-]+\\d+[A-Za-z_-]+|[A-Za-z_-]+\\d+)";
  private final String phoneNumberPattern = "(?<=(^|\\b))\\d{5,}(?=($|\\b))"; // Sequences of digits and spaces that are complete words
  private final String textPattern = "(?<=(^|\\b))(text|txt|sms)(?=($|\\b))";
  private final String winPattern = "(?<=(^|\\b))(win|won|victory|winner)(?=($|\\b))";
  private final String punctuationPattern = "[\"`,?!;:\\(\\)]+";
  private final String htmlRemovalPattern = "(#?&(l|g)t;|&amp;)";
  private final String yourPattern = "(?<=(^|\\b))(ur)(?=($|\\b))";
  
  private Pattern moneyRegex;
  private Pattern lettersAndNumbersRegex;
  private Pattern phoneNumberRegex;
  private Pattern textPatternRegex;
  private Pattern winPatternRegex;
  private Pattern punctuationPatternRegex;
  private Pattern yourPatternRegex;
  
  public DataNormalizer()
  {
  	moneyRegex = Pattern.compile(moneyPattern);
  	lettersAndNumbersRegex = Pattern.compile(lettersAndNumbersPattern);
  	phoneNumberRegex = Pattern.compile(phoneNumberPattern);
  	textPatternRegex = Pattern.compile(textPattern, Pattern.CASE_INSENSITIVE);
  	winPatternRegex = Pattern.compile(winPattern, Pattern.CASE_INSENSITIVE);
  	yourPatternRegex = Pattern.compile(yourPattern, Pattern.CASE_INSENSITIVE);
  	
  	punctuationPatternRegex = Pattern.compile(punctuationPattern);
  }
  
  public String normalize(String text)
  {
  	String normalizedText = normalizeTextHelper(text, moneyRegex, " ##money## ");
  	normalizedText = normalizeTextHelper(normalizedText, lettersAndNumbersRegex, " ##wordwithletternumber## ");
  	normalizedText = normalizeTextHelper(normalizedText, phoneNumberRegex, " ##phonenumber## ");
  	normalizedText = normalizeTextHelper(normalizedText, textPatternRegex, " text ");
  	normalizedText = normalizeTextHelper(normalizedText, winPatternRegex, " win ");
  	normalizedText = normalizeTextHelper(normalizedText, Pattern.compile(htmlRemovalPattern), " ");
  	normalizedText = normalizeTextHelper(normalizedText, yourPatternRegex, " your ");
  	normalizedText = normalizeTextHelper(normalizedText, punctuationPatternRegex, " ");
  	normalizedText = normalizedText.replaceAll("\\s+", " ");
  	return normalizedText;
  }
  
  private String normalizeTextHelper(String text, Pattern pattern, String replacement)
  {
  	Matcher matcher = pattern.matcher(text);
  	
  	return matcher.replaceAll(replacement);
  }
  
  public static void main(String[] args)
  {
  	try
  	{
  		File inputFile = new File("./data/SMSSpamCollection.tsv");
  		File outputFile = new File("./data/SMSSpamCollection_normalized.txt");
  		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
  		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
  		String line;
  		
  		DataNormalizer normalizer = new DataNormalizer();
  		
  		while((line = reader.readLine()) != null)
  		{
  			processLine(line, reader, writer, normalizer);
  		}
  		reader.close();
  		writer.close();
  	}
  	catch(Exception e)
  	{
  		e.printStackTrace();
  	}
  }
  
  private static void processLine(String line, BufferedReader reader, BufferedWriter writer, DataNormalizer normalizer) throws IOException
  {
  	String[] tokens = line.split("\t");
  	
  	StringBuilder content = new StringBuilder();
  	
  	for(int i = 1; i < tokens.length; i++)
  		content.append(tokens[1]).append(" ");
  	
  	writer.write("Original : " + content.toString().trim());
  	writer.newLine();
  	writer.write("Normalized : " + normalizer.normalize(content.toString().trim()));
  	writer.newLine();
  	writer.write("======================");
  	writer.newLine();
  }
}
