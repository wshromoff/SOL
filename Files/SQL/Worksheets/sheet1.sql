select count(*) from design;
select count(*) from keyword;
select count(*) from design_keyword;
select count(*) from part;
select count(*) from customer;
select count(*) from package;
select count(*) from asset;
select count(*) from customer_package;

-- Show descending keyword count usage
select b.keyword, count(*) from design_keyword a
inner join keyword b on b.id = a.keyword_id
group by b.keyword
order by count(*) desc;

-- Show descending packageid count usage
select packageid, count(*) from customer_package
group by packageid
order by count(*) desc;


-- Show descending designid count usage
select designid, count(*) from part
group by designid
order by count(*) desc;

-- Show keywords for design ID
select b.keyword from design_keyword a
inner join keyword b on b.id = a.keyword_id
where a.design = 3814;



-- Find designs not referenced by a part
SELECT t1.id, t1.name
FROM design t1
LEFT JOIN part t2 ON t2.designid = t1.id
WHERE t2.name IS NULL;

-- Find parts with no design ID
SELECT * from part where designid = 0;

-- Delete the 697 designs with no part usage
delete from design
where
id in (SELECT t1.id
FROM design t1
LEFT JOIN part t2 ON t2.designid = t1.id
WHERE t2.name IS NULL
);

-- Show descending mascot count usage for customers
select mascot, count(*) from customer
group by mascot
order by count(*) desc;


