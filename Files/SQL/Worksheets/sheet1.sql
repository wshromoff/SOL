select count(*) from design;
select count(*) from keyword;
select count(*) from design_keyword;

select b.keyword, count(*) from design_keyword a
inner join keyword b on b.id = a.keyword_id
group by b.keyword
order by count(*) desc;