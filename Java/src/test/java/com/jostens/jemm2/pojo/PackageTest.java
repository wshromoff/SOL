package com.jostens.jemm2.pojo;

import org.junit.Test;

/**
 * This pojo is complex in what it's supplied for extract text so this test will make sure that processing is correct.
 */
public class PackageTest
{

//	@Test
	public void testUniqueIdentifierEtchings()
	{
		Package package1 = new Package("ET044875_1060825_mascot_bitmap_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr|Mascot|ET044875|1060825|US|School|Black|Black|||||||||Black|Announcement (Traditional)|Announcement (Traditional)|Complete (Publish-Ready)|Cataloged|||1|||false|false|false|true|");
		Package package2 = new Package("ET044875_1060368_mascot_bitmap_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr|Mascot|ET044875|1060368|US|School|Black|Black|||||||||Black|Announcement (Traditional)|Announcement (Traditional)|Complete (Publish-Ready)|Cataloged|||1|||false|false|false|true|");
		
		System.out.println("Identifier 1=" + package1.getUniqueIdentifier());
		System.out.println("Identifier 2=" + package2.getUniqueIdentifier());
	}

	
	@Test
	public void testUniqueIdentifierBrand()
	{
		Package package1 = new Package("MO048800_2378339_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2378339|US|School|Black|Black|||||||||Black|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||1||Customer Default|false|true|true|true|");
		Package package2 = new Package("MO048800_2367563_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2367563|PG|School|Black|Black|||||||||Black|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||1||Customer Default|false|true|true|true|");
		Package package3 = new Package("MO048800_2322341_crest_vector_flat_2t_dx_0x_gds_grd_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2322341|US|School|Gold|Gold|Dark Green||||||||Gold, Dark Green|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||2||Customer Default|true|true|true|true|");
		Package package4 = new Package("MO048800_2263334_crest_vector_flat_3t_dbks_0x_svs_blx_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2263334|US|School|Silver|Silver|Reflex Blue||||||||Silver, Reflex Blue|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||3|Black|Customer Default|true|true|true|true|");
		
		System.out.println("Identifier 1=" + package1.getUniqueIdentifier());
		System.out.println("Identifier 2=" + package2.getUniqueIdentifier());
		System.out.println("Identifier 3=" + package3.getUniqueIdentifier());
		System.out.println("Identifier 4=" + package4.getUniqueIdentifier());
	}

//	MO048800_2378339_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2378339|US|School|Black|Black|||||||||Black|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||1||Customer Default|false|true|true|true|
//	MO048800_2367563_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2367563|PG|School|Black|Black|||||||||Black|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||1||Customer Default|false|true|true|true|
//	MO048800_2338421_crest_vector_flat_1t_dx_0x_bks_x_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2338421|US|School|Black|Black|||||||||Black|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||1||Customer Default|false|true|true|true|
//	MO048800_2322341_crest_vector_flat_2t_dx_0x_gds_grd_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2322341|US|School|Gold|Gold|Dark Green||||||||Gold, Dark Green|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||2||Customer Default|true|true|true|true|
//	MO048800_2303879_crest_vector_flat_3t_dx_0x_svs_bln_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2303879|US|School|Silver|Silver|Navy Blue||||||||Silver, Navy Blue|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||3||Customer Default|true|true|true|true|
//	MO048800_2263334_crest_vector_flat_3t_dbks_0x_svs_blx_x_x_x_x_x_x_x_x.cdr|Crest|MO048800|2263334|US|School|Silver|Silver|Reflex Blue||||||||Silver, Reflex Blue|Announcement (Digital)|Announcement (Digital)|Complete (Publish-Ready)|Cataloged|||3|Black|Customer Default|true|true|true|true|

}
