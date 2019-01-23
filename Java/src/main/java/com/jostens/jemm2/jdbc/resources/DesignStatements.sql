
-- Count rows in the DESIGN table
DESIGN_COUNT
{
	select count(*) 
	from design
}

-- INSERT ROW INTO DESIGN_KEYWORD
INSERT_DESIGN_KEYWORD
{
	INSERT INTO DESIGN_KEYWORD (DESIGN, KEYWORD_ID, SEQUENCE) 
	VALUES (?, ?, ?)
}

-- SELECT KEYWORD ID BASED ON KEYWORD
GET_KEYWORD_ID
{
	SELECT ID FROM KEYWORD WHERE KEYWORD = '[KEYWORD]'
}

-- INSERT ROW INTO KEYWORD
INSERT_KEYWORD
{
	INSERT INTO KEYWORD (ID, KEYWORD) 
	VALUES (?, ?)
}

-- DELETE FROM DESIGN_KEYWORD TABLE FOR SPECIFIED DESIGN
DELETE_DESIGN_KEYWORDS
{
	DELETE FROM DESIGN_KEYWORD WHERE DESIGN = ?
}

-- SELECT DESIGN ID BASED ON DESIGN NAME
GET_DESIGN_ID
{
	SELECT ID FROM DESIGN WHERE NAME = '[NAME]'
}

-- DELETE DESIGN BY SPECIFIED ID
DELETE_DESIGN
{
	DELETE FROM DESIGN WHERE ID = ?
}

-- INSERT ROW INTO DESIGN
INSERT_DESIGN
{
	INSERT INTO DESIGN (ID, NAME, AFFILITION_BY_DEPICTION, DESIGNID, JOSTENSID, BRAND_ASSET_TYPE, CREATIVE_INTENT_DESIGN, DISPLAYED_INITIALS, DISPLAYED_NAME, DISPLAYED_MASCOT, DISPLAYED_MOTTO, DISPLAYED_INSCRIPTION, DISPLAYED_YEAR_DATE, EXTEND_OF_USABILITY, FUNCTIONAL_INTENT, MAIN_SUBJECT, MULTIPLE_MAIN_SUBJECT, PORTION_MAIN_SUBJECT, VIEW_MAIN_SUBJECT) 
	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
}

-- GET DESIGN BY ID
GET_DESIGN
{
	SELECT ID, NAME, AFFILITION_BY_DEPICTION, DESIGNID, JOSTENSID, BRAND_ASSET_TYPE, CREATIVE_INTENT_DESIGN, DISPLAYED_INITIALS, DISPLAYED_NAME, DISPLAYED_MASCOT, DISPLAYED_MOTTO, DISPLAYED_INSCRIPTION, DISPLAYED_YEAR_DATE, EXTEND_OF_USABILITY, FUNCTIONAL_INTENT, MAIN_SUBJECT, MULTIPLE_MAIN_SUBJECT, PORTION_MAIN_SUBJECT, VIEW_MAIN_SUBJECT
	FROM DESIGN WHERE ID = ?
}

-- GET KEYWORDS FOR A DESIGN
GET_DESIGN_KEYWORDS
{
	SELECT B.KEYWORD FROM DESIGN_KEYWORD A
    INNER JOIN KEYWORD B ON B.ID = A.KEYWORD_ID
 	WHERE DESIGN = ?
    ORDER BY SEQUENCE
}

