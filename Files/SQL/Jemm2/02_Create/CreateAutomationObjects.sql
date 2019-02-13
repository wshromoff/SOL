--
-- CREATE ALL JEMM2 AUTOMATION DATABASE OBJECTS
--
-- THIS IS JUST A QUICK POST TO SHARE THESE 2 LITTLE SHORTCUTS TO CONVERT TEXT FROM LOWER CASE TO UPPERCASE AND VICEVERSA IN ECLIPSE.
-- LOWER CASE: CTRL+SHIFT+Y (CMD+SHIFT+Y ON MAC OS X)
-- UPPER CASE: CTRL+SHIFT+X (CMD+SHIFT+X ON MAC OS X)

--
-- INCOMING_PACKAGE
-- 		THIS TABLE HOLDS INFORMATION ABOUT ASSET PACKAGES BEING PROCESSED BY AUTOMATIONS
--
DROP TABLE INCOMING_PACKAGE;
CREATE TABLE INCOMING_PACKAGE 
(
	ID NUMBER NOT NULL ENABLE,
	NAME VARCHAR2(250 BYTE) NOT NULL ENABLE,
	REVISION NUMBER,
	STATUSAUTOMATION VARCHAR2(300 BYTE),
	ERROR VARCHAR2(300 BYTE),
	
	CONSTRAINT "INCOMING_PACKAGE_PK" PRIMARY KEY ("ID")
);

--
-- INCOMING_ASSET
-- 		THIS TABLE HOLDS INFORMATION ABOUT INCOMING ASSETS  BEING PROCESSED BY AUTOMATIONS
--
DROP TABLE INCOMING_ASSET;
CREATE TABLE INCOMING_ASSET 
(
	PACKAGEID NUMBER NOT NULL ENABLE,
	NAME VARCHAR2(250 BYTE) NOT NULL ENABLE,
	
    CONSTRAINT "INCOMING_ASSET_UNQ1" UNIQUE ("PACKAGEID", "NAME"),
    CONSTRAINT fk1_ID FOREIGN KEY (PACKAGEID) REFERENCES INCOMING_PACKAGE(ID) ON DELETE CASCADE

);

--
-- INCOMING_METADATA
-- 		THIS TABLE HOLDS INFORMATION ABOUT INCOMING METADATA  BEING PROCESSED BY AUTOMATIONS
--
DROP TABLE INCOMING_METADATA;
CREATE TABLE INCOMING_METADATA 
(
	PACKAGEID NUMBER NOT NULL ENABLE,
	KEY VARCHAR2(250 BYTE) NOT NULL ENABLE,
	VALUE VARCHAR2(250 BYTE) NOT NULL ENABLE,
	
    CONSTRAINT "INCOMING_METADATA_UNQ1" UNIQUE ("PACKAGEID", "KEY"),
    CONSTRAINT fk12_ID FOREIGN KEY (PACKAGEID) REFERENCES INCOMING_PACKAGE(ID) ON DELETE CASCADE
);

------------------------------------ SEQUENCES

-- SEQUENCE FOR ID COLUMN OF INCOMINGPACKAGE TABLE
DROP SEQUENCE INCOMING_PACKAGE_SEQUENCE;
CREATE SEQUENCE INCOMING_PACKAGE_SEQUENCE 
START WITH 1 
INCREMENT BY 1 
NOCACHE
NOMAXVALUE;
