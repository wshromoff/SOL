Folder contains any database statements related to setting up Customer Profile
tables, indexes, sequences etc... in the customers schema of the PRDN database.

Following are the explanations of sub folders, they are ordered in how they should be used:

01_Reset - Drops tables, Sequences - Run as user that currently owns the tables

02_Create - Creates all customer profile tables, indexes, sequences and grants for the owner
		of the profile.  This should be the customers schema.

03_damasset - Creates objects required for damasset account after customer schema objects are created.
		Access to these objects are then granted to other accounts like svc_damasset

04_svc_damasset - Creates objects required for svc_damasset user and should be executed after 03_damasset
		scripts have been completed.
		
05_J2C - Creates objects required for J2C user.

06_Public - Create some public synonyms for accessing mediabin tables without needing a schema

-----------------------------------------------------------------
All folders beyond 06_Public make changes to the original objects of the customer profile

07_March-2017 - 2 changes
	1) Increase ASSET_NAME from 100 to 300 columns
	2) Add new columns to MB_METADATA related to rights management
