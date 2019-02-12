package com.jostens.jemm2.brand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Defines a folder in the MB folder tree.  This is a subset of the AutomationFolder object and provides the needed information
 * about the folder structure to be used by automations which call the folder structure automation.
 */
public class Folder
{
	private String path = null;				// full MB path to this folder - including 'name'
	private String name;					// Name of the folder that was stripped off the path
	private String GUID;					// GUID of folder
	private List<Folder> childFolders = null;	// Child folders of this folder
	private String childrenAsString = null;		// Comma separated child folder guids as String
	
	
	// Obsolete class attributes
	private List<BaseAsset> assets = null;	// Assets contained in the folder.
				// These assets are BrandAssets with only the valid information name, GUID and metadata Map
	private boolean valid = true;			// If = false, folder failed a filter

//	public Folder()
//	{
//		
//	}
//	
//	/**
//	 * Constructor that initializes this object based on values specified by a source folder and source assets.
//	 * 
//	 * If the source assets have an asset class defined for them, an implementation class is created in place of
//	 * the common BaseAsset class. 
//	 */
//	public Folder(Folder sourceFolder)
//	{
//		path = sourceFolder.getPath();
//		name = sourceFolder.getName();
//		GUID = sourceFolder.getGUID();
//		// Make new copies of the assets - Only name, GUID and Metadata should be needed
//		assets = new ArrayList<BaseAsset>();
//		for (BaseAsset sourceAsset : sourceFolder.getAssets())
//		{
//			BaseAsset returnAsset = null;
//			// Find asset class for further actions
//			String assetClass = sourceAsset.getMetaDataValue(MetadataGUIDS.ASSET_CLASS);
//			if (assetClass == null || assetClass.length() == 0)
//			{	// Asset class is not found, so ignore this asset
//				continue;
//			}
//			else
//			{
//				// Return an implementation asset for sourceAsset - Much other code can depend on this being done.
//				returnAsset = AssetRegistry.getAssetForAssetClass(sourceAsset);
//			}
//			returnAsset.setAssetMetadata(sourceAsset.getAssetMetadata());
//			returnAsset.setFolderName(sourceAsset.getFolderName());
//			returnAsset.setStepValidAsset(true);
//			assets.add(returnAsset);
//		}
//	}
	public void addFolderAssets(Collection<BaseAsset> assets)
	{
		getAssets().addAll(assets);
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getGUID()
	{
		return GUID;
	}
	public void setGUID(String gUID)
	{
		GUID = gUID;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public List<BaseAsset> getAssets()
	{
		if (assets == null)
		{
			assets = new ArrayList<BaseAsset>();
		}
		return assets;
	}
	public void setAssets(List<BaseAsset> assets)
	{
		this.assets = assets;
	}
	public boolean areAnyAssetsValid()
	{
		for (BaseAsset asset : getAssets())
		{
			if (asset.isValid())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isValid()
	{
		return valid;
	}
	public void setValid(boolean valid)
	{
		this.valid = valid;
	}
	public void resetValidState()
	{
		valid = true;
		for (BaseAsset asset : getAssets())
		{
			asset.setValid(true);
		}
	}
	public boolean isReplacementFolder()
	{
		if (path == null)
		{
			return false;
		}
		return path.contains("Replacement (Delete");
	}
	public boolean isRevisionFolder()
	{
		if (path == null)
		{
			return false;
		}
		return path.contains("Revision (Retire");
	}
	public boolean isNewFolder()
	{
		if (path == null)
		{
			return false;
		}
		return path.contains("New (Supplement");
	}
	
	public String getFolderType()
	{
		if (isNewFolder())
		{
			return "new";
		}
		if (isReplacementFolder())
		{
			return "replace";
		}
		if (isRevisionFolder())
		{
			return "revise";
		}
		return "";
	}
	
	/**
	 * Return a folder identifier which is a concatenation of 'folderType:folderName'
	 */
	public String getFolderIdentifier()
	{
		return getFolderType() + ":" + getName();
	}
	/**
	 * Methods to gather information about a folder from the specified identifier.  If the format of the folder identifier changes,
	 * these methods will need to be updated.
	 */
	public boolean isReplacementFolder(String folderIdentifier)
	{
		if (folderIdentifier == null)
		{
			return false;
		}
		return folderIdentifier.startsWith("replace");
	}
	public boolean isRevisionFolder(String folderIdentifier)
	{
		if (folderIdentifier == null)
		{
			return false;
		}
		return folderIdentifier.startsWith("revise");
	}
	public boolean isNewFolder(String folderIdentifier)
	{
		if (folderIdentifier == null)
		{
			return false;
		}
		return folderIdentifier.startsWith("new");
	}
	public String getFolderName(String folderIdentifier)
	{
		int i = folderIdentifier.indexOf(":");
		if (i == -1)
		{
			return "";
		}
		return folderIdentifier.substring(i+1);
	}

	public void addChildFolder(Folder childFolder)
	{
		getChildFolders().add(childFolder);
	}
	public List<Folder> getChildFolders()
	{
		if (childFolders == null)
		{
			childFolders = new ArrayList<Folder>();
		}
		return childFolders;
	}
	public void setChildFolders(List<Folder> childFolders)
	{
		this.childFolders = childFolders;
	}

	public String getChildrenAsString()
	{
		return childrenAsString;
	}
	public void setChildrenAsString(String childrenAsString)
	{
		this.childrenAsString = childrenAsString;
	}

}
