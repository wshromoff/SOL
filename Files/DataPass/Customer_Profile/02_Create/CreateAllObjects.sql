--
-- MB_ACTIONS
-- 		This table contains 'actions' that need to be performed based on activity within MB.  Actions can involve
-- New Asset, Delete Asset, Metadata Update
--
DROP TABLE MB_ACTIONS;
CREATE TABLE MB_ACTIONS 
(
	ID NUMBER NOT NULL ENABLE, 
	ACTION VARCHAR2(20 BYTE) NOT NULL ENABLE, 
	ASSET_NAME VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	CUSTOMER_ID VARCHAR2(20 BYTE), 
	ASSET_ID_JOSTENS VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	PRIORITY VARCHAR2(20 BYTE),
	MB_GUID VARCHAR2(40 BYTE),
	METADATA VARCHAR2(2000 BYTE), 
	INSERTED DATE NOT NULL ENABLE,
	COMPLETED DATE,
	MESSAGE VARCHAR2(200 BYTE), 
CONSTRAINT "MB_ACTIONS_PK" PRIMARY KEY ("ID")
);

--
-- MB_ASSETS
-- 		This table contains general information about an asset that is in the customer profile.  These
-- values define the asset and where it's located in the customer profile.
--
DROP TABLE MB_ASSETS;
CREATE TABLE MB_ASSETS 
(
	ID NUMBER NOT NULL ENABLE, 
	CUSTOMER_ID VARCHAR2(20 BYTE), 
	ASSET_NAME VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	ASSET_ID_JOSTENS VARCHAR2(100 BYTE) NOT NULL ENABLE, 
	COLOR VARCHAR2(300),
	PREFIX VARCHAR2(200),
	SUFFIX VARCHAR2(200),
	EXTENSION VARCHAR2(50),
	PATH_TO_FILE VARCHAR2(200 BYTE),
	PATH_TO_BESTCOLOR VARCHAR2(200 BYTE),
	ADDED DATE NOT NULL ENABLE,
	UPDATED DATE NOT NULL ENABLE,
	MODIFICATION_TIME DATE NOT NULL ENABLE,
CONSTRAINT "MB_ASSETS_PK" PRIMARY KEY ("ID"),
CONSTRAINT "MB_ASSETS_UNQ1" UNIQUE ("ASSET_NAME"),
CONSTRAINT "MB_ASSETS_UNQ2" UNIQUE ("ASSET_ID_JOSTENS")
);

--
-- MB_METADATA
-- 		This table contains metadata information about an asset that is in the customer profile.  These
-- values define metadata for an asset that will determine assets being selected by users of the customer profile.
--
DROP TABLE MB_METADATA;
CREATE TABLE MB_METADATA 
(
	ID NUMBER NOT NULL ENABLE, 
	AFFILIATION_BY_DEPICTION VARCHAR2(1000 BYTE),
	AFFILIATION_BY_USE VARCHAR2(1000 BYTE),
	ASSET_CLASS VARCHAR2(100 BYTE),
	BASE_COLOR_TONES_RENDERED VARCHAR2(100 BYTE),
	BRAND_ASSET_TYPE VARCHAR2(100 BYTE),
	BRAND_VALIDATION VARCHAR2(100 BYTE),
	BUSINESS_DEFAULT_USE VARCHAR2(100 BYTE),
	COLOR_SCHEME VARCHAR2(200 BYTE),
	COLORIZATION_LEVEL VARCHAR2(100 BYTE),
	COLORIZATION_TECHNIQUE VARCHAR2(100 BYTE),
	CREATIVE_INTENT_COLOR VARCHAR2(100 BYTE),
	CREATIVE_INTENT_DESIGN VARCHAR2(100 BYTE),
	CUSTOMER_HISTORIC_USE_COLOR VARCHAR2(1000 BYTE),
	CUSTOMER_HISTORIC_USE_DESIGN VARCHAR2(1000 BYTE),
	DETAIL_ENHANCEMENT_COLOR VARCHAR2(100 BYTE),
	DIMENSIONS_PIXELS VARCHAR2(100 BYTE),
	DISPLAYED_YEARDATE VARCHAR2(100 BYTE),
	EXTENT_OF_USABILITY VARCHAR2(100 BYTE),
	JEM_FACET VARCHAR2(100 BYTE),
	KEYWORD_MAIN_SUBJECT VARCHAR2(1000 BYTE),
	PART_ID VARCHAR2(100 BYTE),
	PART_ID_DERIVATIVE VARCHAR2(100 BYTE),
	RESOLUTION_PPI VARCHAR2(100 BYTE),
	STATUS_AVAILABILITY VARCHAR2(100 BYTE),
	STATUS_LIFE_CYCLE VARCHAR2(100 BYTE),
	WORKFLOW_PROJECT VARCHAR2(1000 BYTE),
CONSTRAINT "MB_METADATA_PK" PRIMARY KEY ("ID"),
CONSTRAINT fk_ID FOREIGN KEY (ID) REFERENCES MB_ASSETS(ID) ON DELETE CASCADE
);

------------------------------------ Indexes

-- INDEXES on MB_ASSETS table
-- Not Unique Index 1 on Column: Color
CREATE INDEX MB_ASSETS_N1 ON MB_ASSETS (COLOR);
-- Not Unique Index 2 on Column: Customer ID, Extension, COLOR
CREATE INDEX MB_ASSETS_N2 ON MB_ASSETS (CUSTOMER_ID, EXTENSION, COLOR);

-- INDEXES on MB_METADATA table
-- Not Unique Index 1 on Column: Business Default Use
CREATE INDEX MB_METADATA_N1 ON MB_METADATA (BUSINESS_DEFAULT_USE);
-- Not Unique Index 2 on Column: WORKFLOW_PROJECT
CREATE INDEX MB_METADATA_N2 ON MB_METADATA (WORKFLOW_PROJECT);
-- Add an index on the modification_time column on the mb_assets table
CREATE INDEX MB_ASSETS_N3 ON MB_ASSETS (MODIFICATION_TIME);

------------------------------------ Sequences

-- Sequence for ID column of MB_ACTIONS table
create sequence MB_ACTIONS_SEQUENCE 
start with 1 
increment by 1 
nocache
nomaxvalue;

-- Sequence for ID column of MB_ASSETS table
create sequence MB_ASSETS_SEQUENCE 
start with 1 
increment by 1 
nocache
nomaxvalue;

------------------------------------ Grants

--
-- Grant read access to svc_DAMAsset and DAMAsset users
--
grant select on MB_ASSETS to svc_DAMasset;
grant select on MB_METADATA to svc_DAMasset;
grant select on MB_ASSETS to DAMasset;
grant select on MB_METADATA to DAMasset;
grant select on MB_ASSETS to DAMasset with grant option;
grant select on MB_METADATA to DAMasset with grant option;

grant select, update, insert, delete on MB_ASSETS to J2C;
grant select, update, insert, delete on MB_METADATA to J2C; 
grant select, update, insert, delete on MB_ACTIONS to J2C;
grant select on MB_ACTIONS_SEQUENCE to J2C;
grant select on MB_ASSETS_SEQUENCE to J2c;


