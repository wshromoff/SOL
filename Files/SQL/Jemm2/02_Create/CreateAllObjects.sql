--
-- Create all JEMM2 Database Objects
--
-- This is just a quick post to share these 2 little shortcuts to convert text from lower case to uppercase and viceversa in Eclipse.
-- Lower case: CTRL+SHIFT+Y (CMD+SHIFT+Y on Mac OS X)
-- Upper case: CTRL+SHIFT+X (CMD+SHIFT+X on Mac OS X)



--
-- DESIGN
-- 		This table is an asset design, the lowest level object
--
DROP TABLE DESIGN;
CREATE TABLE DESIGN 
(
	ID NUMBER NOT NULL ENABLE,
	NAME VARCHAR2(50 BYTE) NOT NULL ENABLE,
	
	CONSTRAINT "DESIGN_PK" PRIMARY KEY ("ID")
);

------------------------------------ Sequences

-- Sequence for ID column of DESIGN table
drop sequence DESIGN_SEQUENCE;
create sequence DESIGN_SEQUENCE 
start with 1 
increment by 1 
nocache
nomaxvalue;
