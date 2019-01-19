
--
-- Create Synonyms for SVC_DAMASSET user so view objects owned by DAMAsset can be accessed without
-- the need to supply a schema identifier.
--
create or replace synonym reconciliation for DAMAsset.reconciliation; 
