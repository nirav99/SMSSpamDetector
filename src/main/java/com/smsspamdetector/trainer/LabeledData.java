package com.smsspamdetector.trainer;

/**
 * Represents an instance of labeled data
 * @author nirav99
 *
 */
public class LabeledData
{
  private String label;
  private String text;
  
  public LabeledData(String label, String text)
  {
  	this.label = label;
  	this.text = text;
  }
  
  public String label()
  {
  	return this.label;
  }
  
  public String text()
  {
  	return this.text;
  }
}
