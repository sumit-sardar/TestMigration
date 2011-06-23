create or replace procedure insertIntoAttr19 as
  counter integer;
  sortCounter integer;
  begin
  
   for counter in 1900..2020
    loop
    sortCounter := counter - 1899;
    insert into attr19_dim (attr19id, name, type, product_typeid) 
    values (sortCounter, counter, 'USA School Enrollment', 4);    
    end loop;
    end;
