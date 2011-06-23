create or replace procedure insertIntoAttr17 as
  counter integer;
  sortCounter integer;
  begin
  
   for counter in 10..99
    loop
    sortCounter := counter + 1;
    insert into attr17_dim (attr17id, name, type, product_typeid) 
    values (sortCounter, counter, 'Home Language', 4);    
    end loop;
    end;