/**
  MovieModel.java
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

import java.util.Date;

/**
 *
 */
public class MovieModel {
	
	private String original_title;
	
	private String poster_path;
	
	private Date release_date;

	public String getOriginal_title() {
		return original_title;
	}

	public void setOriginal_title(String original_title) {
		this.original_title = original_title;
	}

	public String getPoster_path() {
		return poster_path;
	}

	public void setPoster_path(String poster_path) {
		this.poster_path = poster_path;
	}
	
	public Date getRelease_date() {
		return release_date;
	}

	public void setRelease_date(Date release_date) {
		this.release_date = release_date;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String res = "original_title: "+this.original_title+" ,poster_path: "+this.poster_path+ " ,relase_date: "+this.release_date;
		return res;
	}

}
