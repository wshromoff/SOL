
-- Build query to find path to image for part

select b.folder_path from package a
inner join asset b on b.packageid = a.id
where partid = 9
and (isblack = 1 or isbestavailable = 1);