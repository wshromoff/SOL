package com.jostens.jemm2.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

public class Design
{
	private int ID = 0;
	private String name;
	private String affiliationByDepiction;
	private String designID;
	private String jostensID;
	private String brandAssetType;
	private String creativeIntentDesign;
	private String displayedInitials;
	private String displayedName;
	private String displayedMascot;
	private String displayedMotto;
	private String displayedInscription;
	private String displayedYearDate;
	private String extentOfUsability;
	private String functionalIntent;
	private List<String> keywords = new ArrayList<String>();
	private String mainSubject;
	private String multipleMainSubject;
	private String portionMainSubject;
	private String viewMainSubject;

	// 20180817-16357a3a71ce|	Name
	//School|	Affliation By Depiction
	//20180817-16357a3a71ce|	Design_id
	//20180817-16357a3a71ce|	jostens_id
	//Mascot|	Brand Asset Type
	//Custom (Not Stock)|	Creative Intent Design
	// |	Displayed Initials
	// |	Displayed Name
	// |	Displayed Mascot
	// |	Displayed Moto
	// |	Displayed Inscription
	// |	Displayed year date
	// Common (For Many)|	Extent of Usability
	// Manufacturing|	Functional intent
	// Titan,Mythology,Spartan,Trojan|	Keywords
	// Titan|	Main Subject
	// false|	Multiple main subject
	// Head|	Portion main subject
	// Side View|	view main subject
	public Design()
	{
		
	}
	public Design(String exportString)
	{
		String splitString = exportString.replaceAll("\\|\\|", "| |");
		splitString = splitString.replaceAll("\\|\\|", "| |");
		String[] stringArr = splitString.split("\\|");
//		System.out.println("-->(" + stringArr.length + ") " + splitString);
//		if (1 == 1) return;
		if (stringArr.length != 19)
		{
			System.out.println("-->(" + stringArr.length + ") " + exportString);
			for (int i = 0; i < stringArr.length; i++)
			{
				System.out.println(" " + i + "   " + stringArr[i]);
			}
			return;
		}
//		System.out.println("STRING ARRAY SIZE = " + stringArr.length);
		setName(stringArr[0]);
		setAffiliationByDepiction(emptyToNull(stringArr[1]));
		setDesignID(emptyToNull(stringArr[2]));
		setJostensID(emptyToNull(stringArr[3]));
		setBrandAssetType(emptyToNull(stringArr[4]));
		setCreativeIntentDesign(emptyToNull(stringArr[5]));
		setDisplayedInitials(emptyToNull(stringArr[6]));
		setDisplayedName(emptyToNull(stringArr[7]));
		setDisplayedMascot(emptyToNull(stringArr[8]));
		setDisplayedMotto(emptyToNull(stringArr[9]));
		setDisplayedInscription(emptyToNull(stringArr[10]));
		setDisplayedYearDate(emptyToNull(stringArr[11]));
		setExtentOfUsability(emptyToNull(stringArr[12]));
		setFunctionalIntent(emptyToNull(stringArr[13]));
		setKeywords(emptyToNull(stringArr[14]));
		setMainSubject(emptyToNull(stringArr[15]));
		setMultipleMainSubject(emptyToNull(stringArr[16]));
		setPortionMainSubject(emptyToNull(stringArr[17]));
		setViewMainSubject(emptyToNull(stringArr[18]));
		
//		System.out.println("-->" + getName());

//		StringTokenizer st = new StringTokenizer(exportString, "|");
//		setName(st.nextToken());
//		System.out.println("-->" + getName());
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		st.nextToken();
//		setKeywords(st.nextToken());
	}
	
	private String emptyToNull(String strValue)
	{
		if (strValue == null || strValue.trim().length() == 0)
		{
			return null;
		}
		return strValue;
	}

	public int getID()
	{
		return ID;
	}
	public void setID(int iD)
	{
		ID = iD;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public List<String> getKeywords()
	{
		return keywords;
	}
	public void setKeywords(List<String> keywords)
	{
		this.keywords = keywords;
	}
	public void setKeywords(String keywordsAsString)
	{
		// Keywords passed as comma separate String - Break into String array
		keywords.clear();
		StringTokenizer st = new StringTokenizer(keywordsAsString, ",");
		while (st.hasMoreTokens())
		{
			String keyword = st.nextToken();
			keywords.add(keyword);
		}
	}
	
	public void addKeyword(String keyword)
	{
		keywords.add(keyword);
	}
	
	public boolean equals(Object obj)
	{
		Design design = (Design)obj;
//		System.out.println("HELLO " + getDisplayedInitials());
//		System.out.println("HELLO " + design.getDisplayedInitials());

		if (getID() != design.getID())
		{
			return false;
		}
		if (!Objects.equals(getName(), design.getName())) { return false; }
		if (!Objects.equals(getAffiliationByDepiction(), design.getAffiliationByDepiction())) { return false; }
		if (!Objects.equals(getDesignID(), design.getDesignID())) { return false; }
		if (!Objects.equals(getJostensID(), design.getJostensID())) { return false; }
		if (!Objects.equals(getBrandAssetType(), design.getBrandAssetType())) { return false; }
		if (!Objects.equals(getCreativeIntentDesign(), design.getCreativeIntentDesign())) { return false; }
		if (!Objects.equals(getDisplayedInitials(), design.getDisplayedInitials())) { return false; }
		if (!Objects.equals(getDisplayedName(), design.getDisplayedName())) { return false; }
		if (!Objects.equals(getDisplayedMascot(), design.getDisplayedMascot())) { return false; }
		if (!Objects.equals(getDisplayedMotto(), design.getDisplayedMotto())) { return false; }
		if (!Objects.equals(getDisplayedInscription(), design.getDisplayedInscription())) { return false; }
		if (!Objects.equals(getDisplayedYearDate(), design.getDisplayedYearDate())) { return false; }
		if (!Objects.equals(getExtentOfUsability(), design.getExtentOfUsability())) { return false; }
		if (!Objects.equals(getFunctionalIntent(), design.getFunctionalIntent())) { return false; }
		if (!Objects.equals(getMainSubject(), design.getMainSubject())) { return false; }
		if (!Objects.equals(getMultipleMainSubject(), design.getMultipleMainSubject())) { return false; }
		if (!Objects.equals(getPortionMainSubject(), design.getPortionMainSubject())) { return false; }
		if (!Objects.equals(getViewMainSubject(), design.getViewMainSubject())) { return false; }
		
		
		if (!Objects.equals(getKeywords(), design.getKeywords())) { return false; }
		return true;

	}
	public String getAffiliationByDepiction()
	{
		return affiliationByDepiction;
	}
	public void setAffiliationByDepiction(String affiliationByDepiction)
	{
		this.affiliationByDepiction = affiliationByDepiction;
	}
	public String getDesignID()
	{
		return designID;
	}
	public void setDesignID(String designID)
	{
		this.designID = designID;
	}
	public String getJostensID()
	{
		return jostensID;
	}
	public void setJostensID(String jostensID)
	{
		this.jostensID = jostensID;
	}
	public String getBrandAssetType()
	{
		return brandAssetType;
	}
	public void setBrandAssetType(String brandAssetType)
	{
		this.brandAssetType = brandAssetType;
	}
	public String getCreativeIntentDesign()
	{
		return creativeIntentDesign;
	}
	public void setCreativeIntentDesign(String creativeIntentDesign)
	{
		this.creativeIntentDesign = creativeIntentDesign;
	}
	public String getDisplayedInitials()
	{
		return displayedInitials;
	}
	public void setDisplayedInitials(String displayedInitials)
	{
		this.displayedInitials = displayedInitials;
	}
	public String getDisplayedName()
	{
		return displayedName;
	}
	public void setDisplayedName(String displayedName)
	{
		this.displayedName = displayedName;
	}
	public String getDisplayedMascot()
	{
		return displayedMascot;
	}
	public void setDisplayedMascot(String displayedMascot)
	{
		this.displayedMascot = displayedMascot;
	}
	public String getDisplayedMotto()
	{
		return displayedMotto;
	}
	public void setDisplayedMotto(String displayedMotto)
	{
		this.displayedMotto = displayedMotto;
	}
	public String getDisplayedInscription()
	{
		return displayedInscription;
	}
	public void setDisplayedInscription(String displayedInscription)
	{
		this.displayedInscription = displayedInscription;
	}
	public String getDisplayedYearDate()
	{
		return displayedYearDate;
	}
	public void setDisplayedYearDate(String displayedYearDate)
	{
		this.displayedYearDate = displayedYearDate;
	}
	public String getExtentOfUsability()
	{
		return extentOfUsability;
	}
	public void setExtentOfUsability(String extentOfUsability)
	{
		this.extentOfUsability = extentOfUsability;
	}
	public String getFunctionalIntent()
	{
		return functionalIntent;
	}
	public void setFunctionalIntent(String functionalIntent)
	{
		this.functionalIntent = functionalIntent;
	}
	public String getMainSubject()
	{
		return mainSubject;
	}
	public void setMainSubject(String mainSubject)
	{
		this.mainSubject = mainSubject;
	}
	public String getMultipleMainSubject()
	{
		return multipleMainSubject;
	}
	public void setMultipleMainSubject(String multipleMainSubject)
	{
		this.multipleMainSubject = multipleMainSubject;
	}
	public String getPortionMainSubject()
	{
		return portionMainSubject;
	}
	public void setPortionMainSubject(String portionMainSubject)
	{
		this.portionMainSubject = portionMainSubject;
	}
	public String getViewMainSubject()
	{
		return viewMainSubject;
	}
	public void setViewMainSubject(String viewMainSubject)
	{
		this.viewMainSubject = viewMainSubject;
	}
	
}
