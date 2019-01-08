package com.jostens.jemm2.solr;

public interface SOLRNeeded
{
	// Define the full content type - Used to limit documents of a particular type
	public String getContentTypeFull();
	// A 2 character abbreviation for document type used as part of the ID
	public String getContentTypeAbbrev();
}
