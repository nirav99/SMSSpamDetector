package com.smsspamdetector.featurefinder;

import java.util.Comparator;

/**
 * Encapsulates a word and its mutual information score for 2 classes namely spam and nonspam
 * @author nirav99
 *
 */
public class MutualInformation
{
  private String word;
  private double mutualInformationScore;
  
  public MutualInformation(String word, double miScore)
  {
  	this.word = word;
  	this.mutualInformationScore = miScore;
  }
  
  public String word()
  {
  	return word;
  }
  
  public double mutualInformationScore()
  {
  	return this.mutualInformationScore;
  }
  
  @Override
  public String toString()
  {
  	return word + " --> " + String.format("%.4f", this.mutualInformationScore);
  }
}

/**
 * Sorts the list based on decreasing value of mutual information
 * @author nirav99
 *
 */
class MutualInformationComparator implements Comparator<MutualInformation>
{
	@Override
	public int compare(MutualInformation mi1, MutualInformation mi2)
	{
		if(mi1.mutualInformationScore() > mi2.mutualInformationScore())
			return -1;
		else
		if(mi1.mutualInformationScore() < mi2.mutualInformationScore())
		  return 1;
		else
			return 0;
	}
}