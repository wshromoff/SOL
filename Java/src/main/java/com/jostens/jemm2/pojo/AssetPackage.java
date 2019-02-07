package com.jostens.jemm2.pojo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AssetPackage
{
	private int ID = 0;
	private String name;
	private String firstCustomerID;
	private String partName;
	private int partID = 0;
	private String brandAssetType;
	private String baseColorTones;
	private String enhancementColor;
	private String color1;
	private String color2;
	private String color3;
	private String color4;
	private String color5;
	private String color6;
	private String color7;
	private String color8;
	private String color9;
	private String color10;
	private String baseColor;
	private String colorScheme;
	private String identifier;
	
	String found1B;
	String found1G;
	String found1S;
	String foundBA;
	
	private List<Asset> assets = new ArrayList<Asset>();
	private CustomerPackage customerPackage = new CustomerPackage();

	private String startAssetPath = "";
	
	public AssetPackage()
	{
		
	}
	
	// BR007342_1075323_mascot_vector_flat_1t_dx_0x_grd_rds_wts_x_x_x_x_x_x_x.cdr|Mascot|BR007342|1075323|US|School|Dark Green|Dark Green|Red|White|||||||Dark Green, Red, White|Yearbook (Web Experience),Announcement (Digital)|Yearbook (Web Experience),Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||1|||true|true|true|true|
//	MetadataGUIDS.BRAND_ASSET_TYPE,				1 
//	MetadataGUIDS.PART_ID,						2
//	MetadataGUIDS.CUSTOMER_ID_ORACLE,			3
//	MetadataGUIDS.CUSTOMER_COUNTRY,				4
//	MetadataGUIDS.AFFILIATION_BY_USE,			5
//	MetadataGUIDS.BASE_COLOR,					6
//	MetadataGUIDS.COLOR_1,						7
//	MetadataGUIDS.COLOR_2,						8
//	MetadataGUIDS.COLOR_3,						9
//	MetadataGUIDS.COLOR_4,						10
//	MetadataGUIDS.COLOR_5,						11
//	MetadataGUIDS.COLOR_6,						12
//	MetadataGUIDS.COLOR_7,						13
//	MetadataGUIDS.COLOR_8,						14
//	MetadataGUIDS.COLOR_9,						15
//	MetadataGUIDS.COLOR_SCHEME,					16
//	MetadataGUIDS.CUSTOMER_HISTORIC_USE_COLOR,	17
//	MetadataGUIDS.CUSTOMER_HISTORIC_USE_DESIGN,	18
//	MetadataGUIDS.STATUS_LIFE_CYCLE,			19
//	MetadataGUIDS.STATUS_CATALOGING,			20
//	MetadataGUIDS.STATUS_AUTOMATION,			21
//	MetadataGUIDS.STATUS_AVAILABILITY,			22
//	MetadataGUIDS.BASE_COLOR_TONES_RENDERED,	23
//	MetadataGUIDS.DETAIL_ENHANCEMENT_COLOR,		24
//	MetadataGUIDS.BUSINESS_DEFAULT_USE			25
//	1B											26
//	1G											27
//	1S											28
//	BA											29

	public AssetPackage(String exportString)
	{
		String splitString = exportString.replaceAll("\\|\\|", "| |");
		splitString = splitString.replaceAll("\\|\\|", "| |");
		String[] stringArr = splitString.split("\\|");
		if (stringArr.length != 30)
		{
			System.out.println("-->(" + stringArr.length + ") " + exportString);
			for (int i = 0; i < stringArr.length; i++)
			{
				System.out.println(" " + i + "   " + stringArr[i]);
			}
			return;
		}
		setName(stringArr[0]);
		setBrandAssetType(emptyToNull(stringArr[1]));
		setPartName(emptyToNull(stringArr[2]));
		setFirstCustomerID(emptyToNull(stringArr[3]));
		
		// Build the start asset path based on customer ID so the asset is found in customer profile
		if (firstCustomerID == null || firstCustomerID.length() != 7)
		{	// This is the case for etching with no customer defined
			firstCustomerID = "0000000";
		}
		String level1 = firstCustomerID.substring(6);
		String level2 = firstCustomerID.substring(4, 6);
		startAssetPath = "DAM/" + level1 + "/" + level2 + "/";		
		
		// Remove the first customer ID from the asset name since packages are not customer specific
		String newName = "";
		System.out.println(" * 0 *" + getFirstCustomerID());

		if ("0000000".equals(getFirstCustomerID()))
		{
			System.out.println(" * 1 *");
			newName = getName().replaceFirst("_x_", "_");
		}
		else
		{
			System.out.println(" * 2 *");
			newName = getName().replace(getFirstCustomerID() + "_", "");
		}
		System.out.println("-->" + getName() + ":" + newName);
		setName(newName);
//		setColor1(emptyToNull(stringArr[4]));		//		Skip - This is customer country
		customerPackage.setAffiliationByUse(emptyToNull(stringArr[5]));		// Affiliation by use		<Customer>
		setBaseColor(emptyToNull(stringArr[6]));
		setColor1(emptyToNull(stringArr[7]));
		setColor2(emptyToNull(stringArr[8]));
		setColor3(emptyToNull(stringArr[9]));
		setColor4(emptyToNull(stringArr[10]));
		setColor5(emptyToNull(stringArr[11]));
		setColor6(emptyToNull(stringArr[12]));
		setColor7(emptyToNull(stringArr[13]));
		setColor8(emptyToNull(stringArr[14]));
		setColor9(emptyToNull(stringArr[15]));
//		setColor10(emptyToNull(stringArr[16]));		// Export missed color10
		setColorScheme(emptyToNull(stringArr[16]));
		customerPackage.setHistoricUseColor(emptyToNull(stringArr[17]));		// Historic use color		<Customer>
		customerPackage.setHistoricUseDesign(emptyToNull(stringArr[18]));		// Historic use design		<Customer>
		customerPackage.setStatusLifeCycle(emptyToNull(stringArr[19]));		// Status life cycle		<Customer>
		customerPackage.setStatusCataloging(emptyToNull(stringArr[20]));		// Status Cataloging		<Customer>
		customerPackage.setStatusAutomation(emptyToNull(stringArr[21]));		// Status Automation		<Customer>
		customerPackage.setStatusAvailability(emptyToNull(stringArr[22]));		// Status Availability		<Customer>
		setBaseColorTones(emptyToNull(stringArr[23]));
		setEnhancementColor(emptyToNull(stringArr[24]));
		customerPackage.setBusinessDefaultUse(emptyToNull(stringArr[25]));		// Business Default Use		<Customer>
		found1B = emptyToNull(stringArr[26]);	// Black asset found
		found1G = emptyToNull(stringArr[27]);	// Gold asset found
		found1S = emptyToNull(stringArr[28]);	// Silver asset found
		foundBA = emptyToNull(stringArr[29]);	// Best Available asset found

		addAssetIfNeeded(found1B, true, false, false, false);
		addAssetIfNeeded(found1G, false, true, false, false);
		addAssetIfNeeded(found1S, false, false, true, false);
		addAssetIfNeeded(foundBA, false, false, false, true);
	}

	private String emptyToNull(String strValue)
	{
		if (strValue == null || strValue.trim().length() == 0)
		{
			return null;
		}
		return strValue;
	}
	
	private void addAssetIfNeeded(String wasFound, boolean black, boolean gold, boolean silver, boolean bestAvailable)
	{
		if ("false".equals(wasFound))
		{
			return;
		}
		Asset asset = new Asset();
		if (black)
		{
			asset.setIsBlack(1);
			asset.setName(getName().replace(".cdr", "_1b_2550.png"));
		}
		if (gold)
		{
			asset.setIsGold(1);
			asset.setName(getName().replace(".cdr", "_1g_2550.png"));
		}
		if (silver)
		{
			asset.setIsSilver(1);
			asset.setName(getName().replace(".cdr", "_1s_2550.png"));
		}
		if (bestAvailable)
		{
			asset.setIsBestAvailable(1);
			asset.setName(getName().replace(".cdr", "_ba_2550.png"));
		}
		// Build the folder path
		int i = asset.getName().indexOf("_");
		String nameWCustomerID = "";
		if ("0000000".equals(getFirstCustomerID()))
		{
			nameWCustomerID = asset.getName().substring(0, i) + "_x" + asset.getName().substring(i);
		}
		else
		{
			nameWCustomerID = asset.getName().substring(0, i) + "_" + firstCustomerID + asset.getName().substring(i);
		}

		asset.setFolderPath(startAssetPath + nameWCustomerID);
		assets.add(asset);
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
	public String getFirstCustomerID()
	{
		return firstCustomerID;
	}
	public void setFirstCustomerID(String firstCustomerID)
	{
		this.firstCustomerID = firstCustomerID;
	}
	public int getPartID()
	{
		return partID;
	}
	public void setPartID(int partID)
	{
		this.partID = partID;
	}
	public String getBrandAssetType()
	{
		return brandAssetType;
	}
	public void setBrandAssetType(String brandAssetType)
	{
		this.brandAssetType = brandAssetType;
	}
	public String getBaseColorTones()
	{
		return baseColorTones;
	}
	public void setBaseColorTones(String baseColorTones)
	{
		this.baseColorTones = baseColorTones;
	}
	public String getEnhancementColor()
	{
		return enhancementColor;
	}
	public void setEnhancementColor(String enhancementColor)
	{
		this.enhancementColor = enhancementColor;
	}
	public String getColor1()
	{
		return color1;
	}
	public void setColor1(String color1)
	{
		this.color1 = color1;
	}
	public String getColor2()
	{
		return color2;
	}
	public void setColor2(String color2)
	{
		this.color2 = color2;
	}
	public String getColor3()
	{
		return color3;
	}
	public void setColor3(String color3)
	{
		this.color3 = color3;
	}
	public String getColor4()
	{
		return color4;
	}
	public void setColor4(String color4)
	{
		this.color4 = color4;
	}
	public String getColor5()
	{
		return color5;
	}
	public void setColor5(String color5)
	{
		this.color5 = color5;
	}
	public String getColor6()
	{
		return color6;
	}
	public void setColor6(String color6)
	{
		this.color6 = color6;
	}
	public String getColor7()
	{
		return color7;
	}
	public void setColor7(String color7)
	{
		this.color7 = color7;
	}
	public String getColor8()
	{
		return color8;
	}
	public void setColor8(String color8)
	{
		this.color8 = color8;
	}
	public String getColor9()
	{
		return color9;
	}
	public void setColor9(String color9)
	{
		this.color9 = color9;
	}
	public String getColor10()
	{
		return color10;
	}
	public void setColor10(String color10)
	{
		this.color10 = color10;
	}
	public String getPartName()
	{
		int i = getName().indexOf("_");
		if (i == -1)
		{
			return "";
		}
		return getName().substring(0, i);
	}
	public void setPartName(String partName)
	{
		this.partName = partName;
	}

	public String getBaseColor()
	{
		return baseColor;
	}

	public void setBaseColor(String baseColor)
	{
		this.baseColor = baseColor;
	}
	public String getColorScheme()
	{
		return colorScheme;
	}
	public void setColorScheme(String colorScheme)
	{
		this.colorScheme = colorScheme;
	}

	/**
	 * Return a combination of fields which mark this package with a unique name
	 */
	public String getIdentifier()
	{
		StringBuffer sb = new StringBuffer(getPartID() + "|");
		sb.append(getBrandAssetType() + "|");
		sb.append(getBaseColorTones() + "|");
		sb.append(getEnhancementColor() + "|");
		sb.append(getColorScheme() + "|");
		
		return sb.toString();
		
	}
	public void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	public boolean equals(Object obj)
	{
		AssetPackage aPackage = (AssetPackage)obj;

		if (getID() != aPackage.getID())
		{
			return false;
		}
		if (getPartID() != aPackage.getPartID())
		{
			return false;
		}
		
//		ID, NAME, IDENTIFIER, PARTID, FIRSTCUSTOMERID, BRAND_ASSET_TYPE, BASE_COLOR_TONES, ENHANCEMENT_COLOR, COLOR1, COLOR2, COLOR3, COLOR4, COLOR5, COLOR6, COLOR7, COLOR8, COLOR9, COLOR10, BASE_COLOR, COLOR_SCHEME
		if (!Objects.equals(getName(), aPackage.getName())) { return false; }
		if (!Objects.equals(getIdentifier(), aPackage.getIdentifier())) { return false; }
		if (!Objects.equals(getFirstCustomerID(), aPackage.getFirstCustomerID())) { return false; }
		if (!Objects.equals(getBrandAssetType(), aPackage.getBrandAssetType())) { return false; }
		if (!Objects.equals(getBaseColorTones(), aPackage.getBaseColorTones())) { return false; }
		if (!Objects.equals(getEnhancementColor(), aPackage.getEnhancementColor())) { return false; }
		if (!Objects.equals(getColor1(), aPackage.getColor1())) { return false; }
		if (!Objects.equals(getColor2(), aPackage.getColor2())) { return false; }
		if (!Objects.equals(getColor3(), aPackage.getColor3())) { return false; }
		if (!Objects.equals(getColor4(), aPackage.getColor4())) { return false; }
		if (!Objects.equals(getColor5(), aPackage.getColor5())) { return false; }
		if (!Objects.equals(getColor6(), aPackage.getColor6())) { return false; }
		if (!Objects.equals(getColor7(), aPackage.getColor7())) { return false; }
		if (!Objects.equals(getColor8(), aPackage.getColor8())) { return false; }
		if (!Objects.equals(getColor9(), aPackage.getColor9())) { return false; }
		if (!Objects.equals(getColor10(), aPackage.getColor10())) { return false; }
		if (!Objects.equals(getBaseColor(), aPackage.getBaseColor())) { return false; }
		if (!Objects.equals(getColorScheme(), aPackage.getColorScheme())) { return false; }

		if (getAssets().size() != aPackage.getAssets().size()) {return false;}
		
		return true;

	}

	public List<Asset> getAssets()
	{
		return assets;
	}
	public void setAssets(List<Asset> assets)
	{
		this.assets = assets;
	}
	public void addAsset(Asset asset)
	{
		assets.add(asset);
	}

	public CustomerPackage getCustomerPackage()
	{
		return customerPackage;
	}
	
}
