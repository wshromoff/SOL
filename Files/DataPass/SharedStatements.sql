--
-- These SQL statements should be of value to all applications.
--

-- This query returns the GUID for the TOP MOST folder in the folder hierchy.  The name
-- of this folder is 'Media Database'
MEDIA_DATABASE_GUID
{
	select parentid 
	from assetrefs
	where name = 'Media Database'
}

-- This query returns the GUID for the Deleted Items folder in the folder hierchy.  The name
-- of this folder is 'Media Database'
DELETED_ITEMS_GUID
{
	select parentid 
	from assetrefs
	where name = 'Deleted Items'
}

--
-- From a starting folder GUID return the complete sub folder tree.  The root
-- topmost folder will be first found.
--
FIND_COMPLETE_FOLDER_TREE
{
	select ar.parentid as parentid, ar.parentid as id, ar.name as name, 0 as depth
	from assetrefs ar
	where ar.parentid = '[PARENTID_GUID]'
	and ar.ISCONTAINER = 1
	union all
	select s1.parentid as parentid, s1.id as id, ar.name as name, 1 as depth
	from assetrefs ar,
		(select distinct ct.parentid, ct.id
		from containertree ct
		where depth = 1 
		start with ct.parentid = '[PARENTID_GUID]' 
		connect by ct.parentid = prior ct.id) s1
	where ar.PARENTID = s1.id
	and ar.iscontainer = 1
	order by depth, parentid, name
}

--
-- From a starting folder GUID return all assets found for the complete sub folder tree.  
--
FIND_ALL_ASSETS_FOR_FOLDER_TREE
{
	select ar.parentid, ar.name, ar.id, ar.isoriginal
	from assetrefs ar
	where
	ar.ISCONTAINER = 0
	and ar.PARENTID in 
	(
		select ar.parentid as id
		from assetrefs ar
		where ar.parentid = '[PARENTID_GUID]'
		and ar.ISCONTAINER = 1
		union all
		select s1.id as id
		from assetrefs ar,
			(select distinct ct.parentid, ct.id
			from containertree ct
			where depth = 1 
			start with ct.parentid = '[PARENTID_GUID]' 
			connect by ct.parentid = prior ct.id) s1
		where ar.PARENTID = s1.id
		and ar.iscontainer = 1
	)
	[NAME_RESTRICTED]
	order by ar.parentid, ar.name
}

-- Return the row count of containertree rows who match the provided PARENT folder GUID and CHILD folder ID values
FIND_CONTAINERTREE_COUNT
{
	select count(*)
	from containertree ct
	where ct.parentid = '[PARENT_FOLDER_GUID]'
	and ct.id = '[CHILD_FOLDER_GUID]'	
}

-- Via SQL get the parent GUID for an assets GUID
FIND_PARENT_FOLDER_GUID
{
	SELECT parentID 
	from assetrefs 
	where id = '[ASSET_GUID]'
}

-- Find asset files within a root folder tree that are missing a metadata GUID on them.
-- This is used to find assets missing ASSET_CLASS and METADATA_SET_TIME.
-- The database object can add in a time requirement where the asset missing the metadata
-- has been modified at least previous to a date.  This added line will look something like
--     and ar.REFMODIFIED < TO_DATE('[EARLIER_THAN_DATE]', 'HH24:MI:SS DD/MM/YYYY')
--
-- Also: [ROOT_FOLDER_GUID] is a comma separated list of folder guids which must be wrapped by single quotes
FIND_ASSETS_MISSING_METADATA_FIELD
{
	select distinct ar.name, ar.id, ar.PARENTID
	from containertree ct
	inner join ASSETREFS ar on ar.PARENTID = ct.id
	inner join assets ma on ma.id = ar.baseid
	inner join revisions rv on rv.baseid = ar.baseid
	left outer join metadata md on md.revisionid = rv.id and md.factoryid = '[METADATA_GUID]'
	where (ct.PARENTID in ( [STARTING_FOLDER_GUIDS] ) or ct.id in ( [STARTING_FOLDER_GUIDS] ) )
	and ar.iscontainer = 0
	[DATE_RESTRICTION]
	and rv.revision = ma.maxrevision
	and md.factoryid is null
	order by ar.parentid
}

-- Find asset files within a root folder tree that are missing a metadata GUID on them.
-- This is used to find assets missing ASSET_CLASS and METADATA_SET_TIME.
-- The database object can add in a time requirement where the asset missing the metadata
-- has been modified at least previous to a date.  This added line will look something like
--     and ar.REFMODIFIED < TO_DATE('[EARLIER_THAN_DATE]', 'HH24:MI:SS DD/MM/YYYY')
--
-- Also: [ROOT_FOLDER_GUID] is a comma separated list of folder guids which must be wrapped by single quotes
-- This version does not include assets in the top level folder
FIND_ASSETS_MISSING_METADATA_FIELD_ORIGINAL
{
	select ar.name, ar.id, ar.PARENTID
	from containertree ct
	inner join ASSETREFS ar on ar.PARENTID = ct.id
	inner join assets ma on ma.id = ar.baseid
	inner join revisions rv on rv.baseid = ar.baseid
	left outer join metadata md on md.revisionid = rv.id and md.factoryid = '[METADATA_GUID]'
	where ct.PARENTID in ( [STARTING_FOLDER_GUIDS] )
	and ar.iscontainer = 0
	[DATE_RESTRICTION]
	and rv.revision = ma.maxrevision
	and md.factoryid is null
	order by ar.parentid
}

-- Find parent folders in a tree ordered from lowest level (starting folder) up the folder tree to upmost parent.  The supplied
-- GUID must be the folder GUID for the tree being requested. The system absolute top level
-- folder 'Media Database' is not included.
-- Union all was needed because need to include:
--		1) Information about starting folder - This folder is not found in containertree table - Setting this depth to 0
--		2) Parent folders of the starting folder.  Each will have a depth increasing by 1.
-- The higher the depth value, the higher in the MB folder hierarchy the folder is found.  Value of 0 represents information abou the starting foler
-- returned in the query results.
FIND_PARENT_FOLDERS
{
	select ar.name, ar.parentid, 0 as depth
	from assetrefs ar
	where ar.parentid = '[STARTING_FOLDER_GUID]'
	and ar.ISCONTAINER = 1
	union all
	select ar.name, ar.parentid, ct.depth 
	from containertree ct
	inner join ASSETREFS ar on ar.parentID = ct.parentid
	where ct.id = '[STARTING_FOLDER_GUID]'
	and ar.ISCONTAINER = 1
	and ar.name != 'Media Database'
	order by depth
}
