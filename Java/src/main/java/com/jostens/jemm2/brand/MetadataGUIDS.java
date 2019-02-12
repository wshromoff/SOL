package com.jostens.jemm2.brand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The full Metadata GUID list defined within mediabin.  These can be used across the application to reference
 * the constant GUID values.
 * 
 * PLEASE KEEP IN ALPHABETICAL ORDER
 */
public class MetadataGUIDS
{

	// These GUIDS are for metadata that Jostens is attaching to assets
	public final static String AFFILIATION_BY_DEPICTION			 			= "{73FA0CB0-7BBD-4991-AB6F-4A068F4FF52F}";
	public final static String AFFILIATION_BY_USE					 		= "{1C9B7B5D-B446-470B-8927-7F136A24B453}";
	public final static String ARTFORM 										= "{DA67D001-5C72-41C4-B97C-EB31AD89810C}";
	public final static String ARTISTIC_STYLE 								= "{80C24FF0-A61B-43A2-B9C6-BAE26E4F9673}";		// Future
	public final static String ASSET_CLASS  								= "{5328ABE9-7536-4F38-AAD1-AFEDA18B233F}";
	public final static String ASSET_CREATOR								= "{C65DD18A-1FAF-42FF-B123-C7C485B309D5}";
//	public final static String ASSET_SUBCLASS  								= "{B343AF96-21AA-4C3F-B2AF-AA95E737C94B}";		// 5/25/2016 - Not supported at this time
	public final static String ASSET_ID_COLOR_SCHEME  						= "{29678ACF-39EE-4CFA-9A62-4A778DC589DD}";
	public final static String ASSET_ID_DESIGN_CONCEPT  					= "{735C0634-EB66-4E94-BA09-18825813B318}";
	public final static String ASSET_ID_JOSTENS  							= "{C4033C37-1E7A-4802-A5E5-CD913D95F0FB}";
	public final static String ASSET_ID_PARENT_MASTER  						= "{FCCA112A-8B78-4B1B-BB29-BB80D91878BA}";
	public final static String ASSET_ID_PARENT_RENDITION  					= "{86A87EBF-3E23-4015-BA66-3BEBF4C47975}";
	public final static String BASE_COLOR 									= "{09DA6FE4-4910-4DBD-996A-FED52E4D8E5C}";
	public final static String BASE_COLOR_TONES_RENDERED 					= "{B3298B3E-2311-4BAD-B2A9-0FFBE8E48EFB}";
	public final static String BRAND_ASSET_SUBCLASS							= "{6B038C1A-3472-4DAC-A413-0B64DA64CF4E}";
	public final static String BRAND_ASSET_TYPE 							= "{16CCBC03-783A-4D8C-A93C-D7BCDFD8A3D3}";
	public final static String BRAND_VALIDATION 							= "{E81F190A-757B-4318-A8F8-F7A6BB721088}";
	public final static String BRAND_VALIDATION_DATE 						= "{C9877F12-C373-4AF4-B7B8-9EF0890D0C80}";		// Future
	public final static String BRAND_VALIDATION_SOURCE 						= "{C383F062-7FFA-4A9E-BAE1-EB4C252DD03D}";		// Future
	public final static String BUSINESS_DEFAULT_USE 						= "{118CE1D6-AE19-44D1-971C-63C6F50C5CB3}";
	public final static String BUSINESS_DEFAULT_USE_VALIDATION_DATE			= "{69EC8458-2F38-41DD-BB8C-EAFB464E64A0}";
	public final static String BUSINESS_DEFAULT_USE_VALIDATION_SOURCE		= "{4AADF04F-61C0-451D-8D5C-9E1F99BBE0E6}";
	public final static String COLOR_0 										= "{A058C411-F643-47F9-B862-522E79BCDE94}";
	public final static String COLOR_1 										= "{CB46F94E-1CE0-4D86-9CE1-47C25FD29667}";
	public final static String COLOR_2 										= "{99E5060F-0602-4EBF-9C6B-C5C70554494F}";
	public final static String COLOR_3 										= "{E9DA9654-D754-4691-869D-2E23E551831A}";
	public final static String COLOR_4 										= "{3317BDCA-5E1E-4C5B-BF98-C48999D9F499}";
	public final static String COLOR_5 										= "{B1789787-C7CA-44E9-A076-2E3450A1E518}";
	public final static String COLOR_6 										= "{F31A3952-977D-4DF2-8E3D-2756EB0541A5}";
	public final static String COLOR_7 										= "{1BD370C7-F48A-426A-8446-3A586EE446C4}";
	public final static String COLOR_8 										= "{98781C92-F6E8-4651-B4D6-8382FF3822C7}";
	public final static String COLOR_9 										= "{3F8CDA50-FD62-45B5-AA28-CDCBB6EF6A37}";
	public final static String COLOR_10 									= "{C4B38AB1-6CB5-4A1E-B552-B8005C0F4651}";
	public final static String COLOR_PALETTE 								= "{472FD39C-295D-4305-BA62-CAF5A22673D0}";
	public final static String COLOR_QUANTITY  								= "{442A9CAF-0CDA-4A79-ABD3-9EA6551749A7}";
	public final static String COLOR_SCHEME 								= "{FBB06AE6-15E7-48F9-BA2B-13F650F410FE}";
	public final static String COLOR_SCHEME_NATURE 							= "{FB11CADD-8656-4DD1-B22D-E9EB14AAA46E}";		// Future
	public final static String COLOR_VALUES_CMYK							= "{6201ED13-D58C-4DB7-8918-2501769212AA}";
	public final static String COLOR_VALUES_LAB								= "{52CAFA69-9BF6-4808-9D56-4FB08D54948D}";
	public final static String COLOR_VALUES_PMS								= "{E43DE553-D961-4513-91DA-CCA821C96B44}";
	public final static String COLOR_VALUES_RGB								= "{8FBA319D-4AD0-49D7-BB22-C1C6EE284A0A}";
	public final static String COLOR_VALUES_WEB								= "{2AFA80A8-3175-4FE2-9EE7-BF120EB993D6}";
	public final static String COLORIZATION_LEVEL 							= "{ECEB99F5-D5E2-441A-8F6C-71D608532A30}";
	public final static String COLORIZATION_MODE 							= "{0A0AA40A-FD32-48DD-AD27-98CF80786548}";
	public final static String COLORIZATION_TECHNIQUE 						= "{C975F213-6607-4E4A-95D2-0473BE90EF2D}";
	public final static String COPYRIGHT_OWNER 								= "{EBFBAB76-82E4-49AD-B102-52F63FE66D9E}";		// Future
	public final static String COPYRIGHT_STATUS 							= "{49F2EE4A-C788-463A-AC7D-05AACD4A1B8D}";		// Future
	public final static String CREATIVE_INTENT_COLOR 						= "{2A3DE26A-8AAE-4DDA-B4CE-851F49F53729}";
	public final static String CREATIVE_INTENT_DESIGN 						= "{C910303D-E4AB-447E-A748-8D79E3031479}";
	public final static String CURRENT_ASSET_EDITION 						= "{6577B65F-9C09-4B7E-A233-2528487B45B4}";
	public final static String CURRENT_ASSET_OWNER 							= "{2580D1D7-B025-4C12-9A87-4989EF5B1142}";
	public final static String CUSTOMER_CITY 								= "{5E31B43F-23EF-4C5A-BAC6-9C2EE4FC2F9B}";
	public final static String CUSTOMER_CLASS_ATHLETIC 						= "{E600FCCF-BFE5-4377-8E8F-F24B9F17CDBA}";
	public final static String CUSTOMER_CONFERENCE 							= "{A252800B-6B8E-4402-984F-85B4EE88127F}";
	public final static String CUSTOMER_COUNTRY 							= "{FE523593-8264-480E-963A-2A5058238699}";
	public final static String CUSTOMER_DISTRICT_OR_SECTION				 	= "{81A15B56-BD4F-4A18-99BB-796B42555928}";
	public final static String CUSTOMER_EFFECTIVE_USE_END_DATE 				= "{1ECDDBB2-5B2E-4083-8579-BD9CED220F26}";
	public final static String CUSTOMER_EFFECTIVE_USE_START_DATE_EARLIEST_KNOWN 	= "{DF507904-7583-45DF-9B1C-B3E9907FDB5E}";
	public final static String CUSTOMER_HISTORIC_USE_COLOR 					= "{AD445C2E-40D2-4C1C-98DC-17C0CC10E237}";
	public final static String CUSTOMER_HISTORIC_USE_DESIGN 				= "{B22CCF42-0039-4ED1-80F9-CCC57089D3FD}";
	public final static String CUSTOMER_ID_COMMON 							= "{40D06ADC-25AF-4159-A933-A159770350D0}";
	public final static String CUSTOMER_ID_JUNS 							= "{841E160A-8C45-49DA-A0F9-F7630FCC5FA1}";
	public final static String CUSTOMER_ID_ORACLE 							= "{5A10725A-32B8-4738-BA10-A16871E9230B}";
	public final static String CUSTOMER_ID_WORKFLOW 						= "{AFE1E39D-0D7F-4415-8FB7-45D1AF6898CD}";
	public final static String CUSTOMER_ID_PARENT_ORACLE 					= "{B255630B-595E-4C5E-8BC5-49E21619ED2A}";
	public final static String CUSTOMER_MARKET 								= "{270BF8A4-DE64-4C4A-A360-06D053400BBC}";
	public final static String CUSTOMER_NAME 								= "{358DE11A-7AD0-4CA4-97B2-16F0CAEA1D0E}";
	public final static String CUSTOMER_NAME_WORKFLOW 						= "{9A4300F0-F7D5-40F5-9792-87580FF9C281}";
	public final static String CUSTOMER_STATE 								= "{A52577B4-51BE-4B0A-B0A8-88494CE6E39E}";
	public final static String CUSTOMER_STATUS 								= "{570E9AA9-ABB1-4FAF-AF39-BD7DDCE6C7AC}";
	public final static String CUSTOMER_USE_APPROVAL_DATE 					= "{C01CC305-794B-4F7C-A910-71C3B200A0E8}";
	public final static String CUSTOMER_USE_APPROVER 						= "{2468F90C-683A-4FC0-AFD7-5B8B79DD74FA}";
	public final static String CUSTOMER_USE_CURRENT_AS_OF_DATE 				= "{FFE751E5-BB03-44F1-AAB1-C64A6A95AA56}";
	public final static String DATE_ART_DUE  								= "{2EDBA679-E465-4D80-8BCB-927ED9C61D4D}";
	public final static String DATE_PLANT_NEEDS  							= "{0F5176CF-8140-4093-9FCA-1C95297AE5FD}";
	public final static String DESIGN_CONCEPT_ARTIST 						= "{11FA80AD-E4CC-43D0-93F7-2E3F3D2A8441}";
	public final static String DESIGN_CONCEPT_DATE_CREATED 					= "{C4A65FD9-5F97-4BE8-9E5D-9889BD54247E}";
	public final static String DESIGN_CONCEPT_OWNER 						= "{4D37F460-A70A-4BDC-B48F-1DE22244D53E}";
	public final static String DETAIL_ENHANCEMENT_COLOR  					= "{14357ECA-AE46-449A-81B9-15F58D7DDAEB}";
	public final static String DISPLAYED_INITIALS_AFFILIATION 				= "{800BE802-D14F-45F4-820C-7FC82D52B713}";
	public final static String DISPLAYED_MASCOT_NAME  						= "{D8F96EA7-D0E6-4BF7-9008-9B46C08F12F1}";
	public final static String DISPLAYED_MOTTO  							= "{A44A4F41-D971-47BE-8784-A37DA4084417}";
	public final static String DISPLAYED_NAME_AFFILIATION  					= "{2252EF86-3E7A-4FAA-B0D2-A60ADFF08097}";
	public final static String DISPLAYED_OTHER_INSCRIPTION  				= "{CB7641A7-B574-410C-9CAA-6E489A4B5E92}";
	public final static String DISPLAYED_YEARDATE  							= "{EFD2DAD8-4476-4DE8-856C-5D6AA8EA8819}";
	public final static String EMBROIDERY_STITCH_COUNT  					= "{EFD2DAD8-4476-4DE8-856C-5D6AA8EA8819}";
	public final static String EXTENT_OF_USABILITY  						= "{E9A19B1D-0957-4FB0-9D2B-8E01EB015D1D}";
	public final static String FORMAT_FILE_CONTENT  						= "{1398DCA0-4CE8-406B-9FCF-72AA48EA615F}";
	public final static String FORMAT_SOURCE_ASSET 							= "{159B0068-CA3A-4829-BA3C-DF019DF14BBD}";
	public final static String FUNCTIONAL_INTENT  							= "{8A1C4E9A-9ED6-4F12-BCBD-CCBE5791EA45}";
	public final static String FUNCTIONAL_UNIT 								= "{C1A00192-083E-4264-AD53-FD31ECB31899}";
	public final static String JEM_FACET   									= "{80D50098-50AB-4D06-B5A2-29B3AA12C940}";
	public final static String JOB_ID 										= "{C123E906-E107-47E2-96A4-2BF8907EAD36}";
	public final static String JOB_ID_COMPONENT 							= "{AE8CF652-2650-4092-AB88-EEFE9F3B60E2}";
	public final static String JOB_ID_WORKFLOW 								= "{4B716F28-E5F4-457B-8808-9D13969F2163}";
	public final static String JOB_SCHEDULE_PRIORITY 						= "{E5B92DE6-2D17-4996-A549-52B3A8276BE6}";
	public final static String JOB_STATUS 									= "{A30EF8B0-37EF-42C6-B7BA-353AEA9B4411}";
	public final static String JOSTENS_ASSET_CREATOR 						= "{0CC22E1D-BA34-4359-8091-C772EF891AB2}";
	public final static String JOSTENS_ASSET_DATE_CREATED			 		= "{C266CBCF-A3DA-4995-9B6F-6675CAF81404}";
	public final static String KEYWORD_ALL 									= "{EF6D4804-2937-48C9-969D-0FF9609CFFF1}";
	public final static String KEYWORD_MAIN_SUBJECT 						= "{98D74B16-DE3A-4FEE-9B6A-ECFDEBD38CD2}";
	public final static String KEYWORD_MULTIPLE_MAIN_SUBJECT 				= "{557A1A7C-D3A0-4AD8-BB18-843A793ACAD3}";
	public final static String KEYWORD_PORTION_MAIN_SUBJECT 				= "{5137888F-F244-42CE-964F-479F9BAABF7D}";
	public final static String KEYWORD_VIEW_MAIN_SUBJECT 					= "{409CD336-6462-4909-8725-D3A872B5DBAE}";
	public final static String LICENSE_CONTRACT_EFFECTIVE_USE_END_DATE 		= "{CA3862CE-3D2A-4B82-B25F-3C741A1707F1}";
	public final static String LICENSE_CONTRACT_EFFECTIVE_USE_START_DATE 	= "{172F5AB2-3F37-47E5-AA6F-3F0F9E35563B}";
	public final static String LICENSE_CONTRACT_ID_NUMBER 					= "{A9CA444B-165D-471C-A93B-B4FEEFA00A52}";
	public final static String LICENSE_CONTRACT_REVISION_DATE 				= "{B091111C-1113-40B6-926D-02D4A398FE1A}";
	public final static String LICENSE_CONTRACT_STATUS 						= "{54A84E40-740F-429C-83C9-F4DF5C6987B7}";
	public final static String LICENSE_CONTRACT_USE_MEDIUM 					= "{CDB15979-21E5-415D-9D9F-357F67F1D466}";
	public final static String ORDER_ORIGINATOR_EMAIL 						= "{AB94423C-CD31-455C-9283-FEB13D1F14C0}";
	public final static String ORDER_TYPE 									= "{488AB47A-CB45-4A4D-9DD2-23B853628B3F}";
	public final static String PART_ID 										= "{70DA8124-AA88-430F-A10B-8F9BA8B2530A}";
	public final static String PART_ID_DERIVATIVE 							= "{E5EC56ED-E67F-43D8-81D7-32BCBBD4548D}";
	public final static String PART_ID_PARENT 								= "{1299D5A2-335B-4646-AF83-E6DC32D90E4A}";
	public final static String PART_ID_TYPE 								= "{75222E09-49CA-4CC5-AAFA-4976A03B2D87}";
	public final static String PART_SIZE 									= "{89B7DB67-4752-4F93-A0DB-39E927F4A017}";
	public final static String PART_SIZE_ACCURACY							= "{9895D478-A7BF-4831-B7FA-85B86085804D}";
	public final static String PART_SIZE_HEIGHT						    	= "{43F2D0D2-C4AC-413A-95BB-5DA681172A4E}";
	public final static String PART_SIZE_WIDTH 								= "{89B7DB67-4752-4F93-A0DB-39E927F4A017}";
	public final static String PART_TYPE 									= "{F269F179-BF5C-4F9F-A765-6DB65F98F530}";
	public final static String PART_VALIDATION 								= "{06E7D69B-E018-4379-84D3-47FD8C1C64FE}";
//	public final static String PERMISSION_TO_REUSE_GRANTED_BY_OWNER 		= "{7D3E0D1F-0E91-4499-B2EF-5A5FEED3006E}";
//	public final static String PERMISSION_TO_REUSE_REQUIRED 				= "{1820D6A8-CFF5-476E-8728-6503BDB175F5}";
//	public final static String PERMISSION_TO_USE_GRANTED_BY_OWNER			= "{5EB550F6-6527-4417-B9ED-D9807944324D}";
//	public final static String PERMISSION_TO_USE_REQUIRED 					= "{CBC2203C-69C4-451F-BC8C-A189F20F511A}";
	public final static String PRODUCT_ORDERED_ART 							= "{24C45C1E-CF5E-4DF8-A135-DC7896F13C0C}";
	public final static String PRODUCT_ORDERED_DIE 							= "{4D928052-AC04-48DC-8C83-9FFD1E362276}";
	public final static String QUALITY_LEVEL 								= "{DD08C086-AA3A-4575-B273-4D01C61DB750}";
	public final static String RIGHTS_MANAGEMENT							= "{FBFD3599-5084-4BA8-A55D-9D7DFE854DD1}";
	public final static String RIGHTS_MANAGEMENT_SET_DATE					= "{E2B32DCA-464B-4C99-A234-8C67B3129ACB}";
	public final static String RIGHTS_MANAGEMENT_STATUS						= "{CD51790B-7C9B-4272-8CDE-71BE72708053}";
	public final static String RIGHTS_MANAGEMENT_VALIDATION_DATE			= "{5E792BC5-3A30-4F5D-A16C-F80565B12C5A}";
	public final static String RIGHTS_MANAGEMENT_VALIDATION_SOURCE			= "{395A3C04-8CD1-4963-981F-681EB2E0063E}";
	public final static String SALES_AREA 									= "{389EDEFD-D5EE-408B-84F3-8E2EF61BC87D}";
	public final static String SALES_REGION 								= "{2AA75E4F-E582-41DC-8B99-329B00BE7817}";
	public final static String SALES_REP_ID 								= "{2CA971C6-0927-456D-A05D-6AA941A11268}";
	public final static String SALES_REP_NAME 								= "{F26CE8F2-CE8C-43A2-ADD3-1F807ADBB376}";
	public final static String SCHOOL_COLOR_1 								= "{A0FFC1B3-DC4B-4B96-95A5-2DBAB577AF15}";
	public final static String SCHOOL_COLOR_2 								= "{83B61EA8-1857-4B4C-BECF-9DD88C391E62}";
	public final static String SCHOOL_COLOR_3 								= "{A8C5A591-E892-4E63-8891-B69CD1DB506C}";
	public final static String SCHOOL_INITIALS_OR_ABBREVIATION 				= "{1666BA25-6D0E-42F9-B108-17EC498E9D2E}";
	public final static String SCHOOL_LICENSES_ITS_BRAND					= "{6A86F35B-6EE7-4C67-A75E-48B7183571B4}";
	public final static String SCHOOL_MASCOT 								= "{B7D1BAE4-36F3-4BC3-874F-36B4E4F0353D}";
	public final static String SCHOOL_MOTTO 								= "{52E96484-68F9-4C2C-B699-7F9B5390E252}";
	public final static String STATUS_AUTOMATION 							= "{732B52F5-BFF0-4A33-9210-8F72E941EF93}";
	public final static String STATUS_AUTOMATION_UPDATE						= "{C2600CA5-C4E2-43E1-9718-86FACE544068}";
	public final static String STATUS_AVAILABILITY 							= "{8E7814E0-2C54-45D5-AEED-3D4166E7D436}";
	public final static String STATUS_CATALOGING 							= "{45200345-0C59-43E2-A9D0-9508B1BF6C04}";
	public final static String STATUS_LIFE_CYCLE 							= "{2D850F21-3A41-459E-89EB-E8A151B8781E}";
	public final static String STATUS_METADATA_DEPLOYMENT					= "{66442F91-9BBD-47DA-B853-8E17CC7352B5}";
//	public final static String TRADEMARK_OWNER 								= "{688CED8F-EC49-4FEC-A3C8-91259754B444}";
//	public final static String TRADEMARK_STATUS 							= "{0F4D6FF3-B9E2-4A83-BC6A-5B1D509DD12A}";
	public final static String UPLOAD_QC_USER	 							= "{14D70142-35E3-4E2E-96B2-E541A64FC357}";
	public final static String VERSION_SCENARIO 							= "{F533597E-897D-4535-9E64-8746260615C7}";
	public final static String VISUALLY_IDENTICAL_PART_DERIVATIVE 			= "{8678AA27-ACE6-4E33-BD59-E193339D1D60}";
	public final static String VISUALLY_IDENTICAL_PART 						= "{57D8A047-1E75-4E07-B910-CEAABDC30A02}";
//	public final static String VISUALLY_SIMILAR_PART_DERIVATIVE 			= "{97478B78-BB98-4C45-9360-1AAC059CC9B5}";
//	public final static String VISUALLY_SIMILAR_PART_MASTER 				= "{02EFE23A-EBEA-4A06-856C-0C40E73FF6B3}";
	public final static String WORKFLOW_HISTORY_AUTOMATION					= "{E19C201D-9977-4208-A685-1001B28750B4}";
	public final static String WORKFLOW_ISSUE 								= "{CF457DF9-C3D2-4A91-A118-B43EF921741C}";
	public final static String WORKFLOW_ISSUE_AUTOMATION 					= "{1F857D56-016C-4F09-AA96-5B92C94AD754}";
	public final static String WORKFLOW_ISSUE_COMMENT						= "{978817C5-74CD-4D0C-834F-745B1142E1FF}";
	public final static String WORKFLOW_PROJECT 							= "{DD943529-4FA9-40D1-9D46-E43FA214C14C}";
	public final static String WORKFLOW_STAGE 								= "{81405298-D856-4AD9-B4FE-5F62BC8BB288}";
	public final static String WORKFLOW_TYPE 								= "{C5BD7D98-B6BD-430E-8611-1E1C00845DD2}";
	
	// These GUIDS are potentially new with the Creative services project
	public final static String SCHOOL_YEAR	 								= "{D2F8BF87-D764-45DA-8FCF-A51CC4901180}";
	public final static String PROJECT_NAME	 								= "{DFC0841F-864C-4CF0-986A-911CEC9F922A}";
	public final static String BUSINESS_UNIT								= "{04FFB9A0-4897-445C-B3D4-FF4710055E0F}";
	public final static String PROJECT_FORMAT								= "{DFBD4BD7-E7B2-49F7-B784-72B4F9427B20}";
	public final static String PROJECT_YEAR 								= "{73122E95-4144-4F2B-880F-DB9F7DD8C2C2}";
	public final static String MARKETING_CONTACT							= "{723C668C-E6D0-4553-82FB-2DD375EF2E6F}";
	public final static String METADATA_MAPPED								= "{FA6A9684-6674-40DA-861A-3FE0DE94C8BE}";
	public final static String METADATA_SET_TIME							= "{DCBCD281-2DA0-4DE7-900C-9BF0237AA384}";
	public final static String PROJECT_ART_DIRECTOR							= "{D31F1A55-EA84-4D2A-BBE1-E12E981C2CA9}";
	public final static String ASSET_ARCHIVE								= "{D55E3FAB-F278-4469-910F-AB3F4A9A1C37}";
	public final static String DIGITAL_ID									= "{1FA54933-ABC8-453E-9E9E-1A7354196AEE}";
	public final static String ITEM_ID										= "{13590EB1-6826-4C40-A002-2F6BBAB269E6}";
	public final static String IMAGE_SOURCE									= "{0BD98BC2-C151-42B3-A612-645739A91855}";
	public final static String IMAGE_SUBJECT								= "{E70FBE36-4341-4687-8C20-38DBDCD9C9F1}";
	public final static String LICENSE_AGREEMENT							= "{40ED2924-0430-471A-B26F-8AF901FDD9B8}";
	public final static String PRODUCT_AND_SERVICE							= "{41C8F322-F3B1-49F2-94E9-036617ABCF87}";
	public final static String PROJECT_RELEASE_DATE							= "{4D812F85-5398-4EB7-9C8E-5821B04B7378}";
	public final static String DATE_MODIFIED								= "{FED37468-B0F2-439D-80EE-8A300440CE77}";
	public final static String IMAGE_SOURCE_BUSINESS_CONTEXT				= "{623EDB51-BA6F-41F2-9CB8-06A5A04D302A}";
	
	// These GUIDS are for metadata that are mediabin maintained data which is used in code
	public final static String NAME				 							= "{B297C571-F083-11D4-8200-0060080D8AC2}";
	public final static String LAYER_CRC	 								= "{9D15F3CE-CA77-452A-A4E4-F945DC989FAD}";
	public final static String COMMENT	 									= "{34F7A9C0-D4FC-11D3-A177-A3FF1892D234}";
	public final static String REVISION_COMMENT 							= "{0FA18BF2-D4FC-11D3-A177-A3FF1892D234}";
	public final static String MODIFICATION_TIME 							= "{2644D2F6-0127-4D88-A8E8-0639186545B6}";
	public final static String MODIFICATION_USER 							= "{6689C259-0CEF-4C7D-A3F0-E04A11E76AF7}";
	public final static String CURRENT_REVISION 							= "{19FD09EC-136F-11D3-8B26-00C0F0171F2D}";
	public final static String DIMENSIONS_PIXELS 							= "{5BE195E0-0726-11D3-9DDE-0060081F836F}";
	public final static String RESOLUTION_PPI	 							= "{5BE195E2-0726-11D3-9DDE-0060081F836F}";
	public final static String INSERTION_TIME 								= "{5BE195E7-0726-11D3-9DDE-0060081F836F}";
	public final static String ASSET_TYPE	 								= "{4510D310-E6BB-11D2-BF95-00A024DB7160}";
	public final static String KEYWORDS	 									= "{5BE195ED-0726-11D3-9DDE-0060081F836F}";
	public final static String IMAGE_COLOR_CHANNELS							= "{CB34EFA1-07CE-11D3-9DDF-0060081F836F}";
	public final static String BITS_PER_CHANNEL								= "{5BE195E3-0726-11D3-9DDE-0060081F836F}";
	public final static String ORIGINAL_FILE_SIZE_KB						= "{EF543245-24B4-4E35-B178-7BDB95C3BBE7}";

	// These GUIDS are for Tasks defined in Mediabin that can be called to download or insert assets.
	// Explanations of the download tasks is from support in June 2016
	// [Get original] will download the asset when it was first uploaded to the system and you will see no modification if the 
	// assets has undergone during the time it was in the system like metadata edits, Image edits etc. That is why you see the original size and no XMP metadata.
	public final static String GET_ORIGINAL_TASK_ID 						= "{7D906662-1E20-4dac-B99A-0C14CBFDB2EC}";
	public final static String INSERT_UNMODIFIED_TASK_ID 					= "{1ECCFAEA-5582-412a-A826-763AAF1E0A3F}";
	// [None] task will always include the all the edits the asset has undergone in the system and that is why you see the change in the size and includes the XMP metadata.
	public final static String GET_NONE_TASK_ID 							= "{78EB3F78-0A3C-11D3-8B23-00C0F0171F2D}";

	// Deleted metadata fields - These GUIDS are no longer used and can be found in BaseIngest.removeDeletedGUIDS()
//	public final static String COLORIZATION_METHOD 							= "{E1F4EADC-62C4-461A-881A-4C46F29710B4}";		// On 3/13/2014 = Field Deleted
//	public final static String BUSINESS_IDEAL_USE 	= "{BA277BB8-084A-47CD-8C61-7CA1A16A84FE}";		// On 3/19/2014 - Replaced by Business Default Use Validation Source
//	public final static String WORKFLOW_PROJECT_HISTORY						= "{84AA564D-E278-45BA-A8A6-9A41146E0516}";		// On 5/21/2014 - Finally removed from code base
//	public final static String JOB_OWNER_ARTIST_NAME 						= "{BF6CF224-DB44-417B-9867-BA1E0A32DA26}";		// Moved here 11/5/2015
//	public final static String KEYWORD_CLASS 								= "{3C3695B1-818D-4281-A630-2E32BDA0C99E}";		// Moved here 11/5/2015
//	public final static String KEYWORD_ID 									= "{DAAEDE74-0050-4944-BE65-6A0DC26D430B}";		// Moved here 11/5/2015
//	public final static String ASSET_ARCHIVE								= "{5E318AF8-5507-4555-B49A-6BB8593C727D}";		// Moved here 9/15/2016  Original ASSET_ARCHIVE field that was single select
																															// Replaced with a field by the same name that was multi select
	
	// Repurposed GUIDS - These GUIDS are still used but under a different name
//	public final static String BUSINESS_USE_ID 								= "{69EC8458-2F38-41DD-BB8C-EAFB464E64A0}";		// On 3/19/2014 - Renamed to Business Default Use Validation Date

	/**
	 * Method to remove special characters.  These characters need to be removed for IDOL queries, etc...
	 */
	public static String removeSpecialCharacters(String guid)
	{
		return guid.replaceAll("[{}-]", "");
	}
	/**
	 * Method to add special characters.  These characters need to be added if returned as part of the results for IDOL queries, etc...
	 */
	public static String addSpecialCharacters(String guid)
	{
		StringBuffer sb = new StringBuffer("{");
		sb.append(guid.substring(0, 8) + "-");
		sb.append(guid.substring(8, 12) + "-");
		sb.append(guid.substring(12, 16) + "-");
		sb.append(guid.substring(16, 20) + "-");
		sb.append(guid.substring(20) + "}");
		return sb.toString();
	}
	
	// These Lists will assist in determining if an asset is a date or numeric type
	// Attributes to be called during FilenameTask
	public static final List<String> DATE_METADATA = new ArrayList<String>(Arrays.asList
	(
			MetadataGUIDS.MODIFICATION_TIME,
			MetadataGUIDS.INSERTION_TIME,
			MetadataGUIDS.DATE_MODIFIED,
			MetadataGUIDS.PROJECT_RELEASE_DATE,
			MetadataGUIDS.METADATA_SET_TIME
	));
	public static final List<String> NUMERIC_METADATA = new ArrayList<String>(Arrays.asList
	(
			MetadataGUIDS.IMAGE_COLOR_CHANNELS,
			MetadataGUIDS.BITS_PER_CHANNEL,
			MetadataGUIDS.ORIGINAL_FILE_SIZE_KB,
			MetadataGUIDS.KEYWORD_MULTIPLE_MAIN_SUBJECT,
			MetadataGUIDS.LAYER_CRC
	));

	/**
	 * Some MB maintained metadata that seems to describe the asset image is not stored in IDOL.
	 * Below are the known GUIDS where that is the case.
	 */
	public static final List<String> METADATA_NOT_IN_IDOL = new ArrayList<String>(Arrays.asList
	(
			MetadataGUIDS.RESOLUTION_PPI,
			MetadataGUIDS.DIMENSIONS_PIXELS
	));

	/**
	 * Passed in a GUID and return true if that GUID is considered a Date GUID.
	 * 
	 * This information is needed for IDOL queries to correctly handle.  These GUIDS will then
	 * have a MBD pre-pended to the guid after it has special characters removed. 
	 */
	public static boolean isDateGUID(String GUID)
	{
		return DATE_METADATA.contains(GUID);
	}
	/**
	 * Passed in a GUID and return true if that GUID is considered a Numeric GUID.
	 * 
	 * This information is needed for IDOL queries to correctly handle.  These GUIDS will then
	 * have a MBN pre-pended to the guid after it has special characters removed. 
	 */
	public static boolean isNumericGUID(String GUID)
	{
		return NUMERIC_METADATA.contains(GUID);
	}
	/**
	 * Passed in a GUID and return true if that GUID is not maintained by IDOL.
	 * 
	 * This information is needed for doing metadata comparisions between MB and the customer profile.
	 * Since the MB data is being retrieved from IDOL for performance these values would not be populated
	 * and show up as missing data. 
	 */
	public static boolean isNotIDOLGUID(String GUID)
	{
		return METADATA_NOT_IN_IDOL.contains(GUID);
	}

}
