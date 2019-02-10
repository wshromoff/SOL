
-- Build query to find path to image for part

select b.folder_path from package a
inner join asset b on b.packageid = a.id
where partid = 9
and (isblack = 1 or isbestavailable = 1);

-- Build query to find path to image for Customers customer default image

select a.business_default_use, b.folder_path from customer_package a
inner join asset b on b.packageid = a.packageid
where a.customerid = 7
and b.isbestavailable = 1

-- Find customer IDs from a PARTID
select c.customerid from package a
inner join customer_package b on b.packageid = a.id
inner join customer c on c.id = b.customerid
where a.name like 'BR006786%'

