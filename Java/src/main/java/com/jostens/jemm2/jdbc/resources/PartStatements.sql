
-- INSERT ROW INTO PART
INSERT_PART
{
	INSERT INTO PART (ID, NAME, DESIGNID_STRING, JOSTENSID_STRING, PARTID, PARTID_DERIVATIVE, PART_VALIDATION, DESIGNID) 
	VALUES (?, ?, ?, ?, ?, ?, ?, ?)
}

-- GET PART BY ID
GET_PART
{
	SELECT ID, NAME, DESIGNID_STRING, JOSTENSID_STRING, PARTID, PARTID_DERIVATIVE, PART_VALIDATION, DESIGNID
	FROM PART WHERE ID = ?
}

-- SELECT PART ID BASED ON PART NAME
GET_PART_ID
{
	SELECT ID FROM PART WHERE NAME = '[NAME]'
}

-- DELETE PART BY SPECIFIED ID
DELETE_PART
{
	DELETE FROM PART WHERE ID = ?
}

