declare lint number;
begin
sp_importdatatoquery('2011-06-01','2011-07-31','PEK',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','SHA',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','CAN',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','SZX',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','HKG',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','HGH',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','CTU',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','NKG',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','HDQ',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','HBQ',lint);
sp_importdatatoquery('2011-06-01','2011-07-31','',lint);

dbms_output.put_line(lint);
end;
