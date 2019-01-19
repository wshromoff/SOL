

--
-- Return all data for reconcilliation processing using access.
--
CREATE OR REPLACE VIEW reconciliation AS
select
	ma.customer_id, ma.asset_name as name, ma.asset_id_jostens, ma.prefix, ma.suffix, ma.extension, ma.path_to_file,
	mm.AFFILIATION_BY_DEPICTION, mm.AFFILIATION_BY_USE, mm.ASSET_CLASS, mm.BASE_COLOR_TONES_RENDERED, mm.BRAND_ASSET_TYPE, mm.BRAND_VALIDATION, mm.BUSINESS_DEFAULT_USE, 
	mm.COLOR_SCHEME, mm.COLORIZATION_LEVEL, mm.COLORIZATION_TECHNIQUE, mm.CREATIVE_INTENT_COLOR, mm.CREATIVE_INTENT_DESIGN, mm.CUSTOMER_HISTORIC_USE_COLOR, mm.CUSTOMER_HISTORIC_USE_DESIGN, 
	mm.DETAIL_ENHANCEMENT_COLOR, mm.DISPLAYED_YEARDATE, mm.EXTENT_OF_USABILITY, mm.JEM_FACET, mm.KEYWORD_MAIN_SUBJECT, mm.PART_ID, mm.PART_ID_DERIVATIVE, 
	mm.STATUS_AVAILABILITY, mm.STATUS_LIFE_CYCLE, mm.WORKFLOW_PROJECT,
	ma.id, to_char(ma.added, 'mm/dd/yy hh24:mi') added, to_char(ma.updated, 'mm/dd/yy hh24:mi') updated, to_char(ma.modification_time, 'mm/dd/yy hh24:mi') modification_time
from customers.MB_ASSETS ma
inner join customers.MB_METADATA mm on mm.id = ma.id
ORDER BY ma.asset_name;

#
# Grant read access to svc_DAMAsset
#
grant select on reconciliation to svc_DAMasset;
