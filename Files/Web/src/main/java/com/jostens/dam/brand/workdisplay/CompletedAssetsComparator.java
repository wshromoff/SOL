package com.jostens.dam.brand.workdisplay;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CompletedAssetsComparator implements Comparator<Map.Entry<String, List<DisplayAsset>>>
{

	@Override
	public int compare(Entry<String, List<DisplayAsset>> o1, Entry<String, List<DisplayAsset>> o2)
	{
		String scoreAsset1 = getScore(o1);
		String scoreAsset2 = getScore(o2);
		return scoreAsset1.compareTo(scoreAsset2);
	}

	public String getScore(Entry<String, List<DisplayAsset>> artistCompletedAssets)
	{

//		StringBuffer sb = new StringBuffer();
		String score = String.format("%02d_%s", (100 - artistCompletedAssets.getValue().size()), artistCompletedAssets.getKey());
//		System.out.println("SCORE=" + score);
		return score;
	}

}
