/**
  Result.java
 ***********************************************************************************************************************
 Description: 	

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 01-Aug-2017	Gurpreet Singh Saini	Initial Version

 ***********************************************************************************************************************
 */
package com.poster.grabber.model;

import java.util.List;

/**
 *
 */
public class Result {
	
	private List<MovieModel> results;

	public List<MovieModel> getResults() {
		return results;
	}

	public void setResults(List<MovieModel> results) {
		this.results = results;
	}

}
