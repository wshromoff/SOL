select count(*) from design;
select count(*) from keyword;
select count(*) from design_keyword;
select count(*) from part;

-- Show descending keyword count usage
select b.keyword, count(*) from design_keyword a
inner join keyword b on b.id = a.keyword_id
group by b.keyword
order by count(*) desc;

-- Show descending designid count usage
select designid, count(*) from part
group by designid
order by count(*) desc;

-- Find designs not referenced by a part
SELECT t1.id, t1.name
FROM design t1
LEFT JOIN part t2 ON t2.designid = t1.id
WHERE t2.name IS NULL;

-- Find parts with no design ID
SELECT * from part where designid = 0;

