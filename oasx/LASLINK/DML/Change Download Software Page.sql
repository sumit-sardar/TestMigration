--Story Id : OAS 2463 Change Download Software Page 
insert into resource_type (resource_type_code , resource_type_desc) values ('JRE_URL','Java SE Runtime Environment URL');
insert into resource_type (resource_type_code , resource_type_desc) values ('ADOBE_AIR','Adobe AIR Runtime URL');
insert into resource_type (resource_type_code , resource_type_desc) values ('FLASH_PL','Flash player URL');
insert into resource_type (resource_type_code , resource_type_desc) values ('FLASH_PGPC','PC Flash Player Plugin content debugger URL');
insert into resource_type (resource_type_code , resource_type_desc) values ('FLASH_PGMC','Macintosh Flash Player Plugin content debugger URL');
insert into product_resource (product_id, resource_type_code , resource_uri) 
values ( 70000 , 'JRE_URL','http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html#jre-7u45-oth-JPR' );
insert into product_resource (product_id, resource_type_code , resource_uri) 
values ( 70000 , 'ADOBE_AIR','http://helpx.adobe.com/air/kb/archived-air-sdk-version.html' );
insert into product_resource (product_id, resource_type_code , resource_uri) 
values ( 70000 , 'FLASH_PL','http://helpx.adobe.com/flash-player/kb/archived-flash-player-versions.html#Flash%20Player%20archives' );
insert into product_resource (product_id, resource_type_code , resource_uri) 
values ( 70000 , 'FLASH_PGPC','https://www.adobe.com/support/flashplayer/downloads.html' );
insert into product_resource (product_id, resource_type_code , resource_uri) 
values ( 70000 , 'FLASH_PGMC','https://www.adobe.com/support/flashplayer/' );




  update product_resource p
     set p.resource_uri = 'https://helpx.adobe.com/flash-player/kb/archived-flash-player-versions.html#Flash%20Player%20archives'
   where p.product_id = 70000
     and p.resource_type_code = 'FLASH_PL';

  update product_resource p
     set p.resource_uri = 'https://helpx.adobe.com/air/kb/archived-air-sdk-version.html#main_Archived_Adobe_AIR_downloads'
   where p.product_id = 70000
     and p.resource_type_code = 'ADOBE_AIR';

  update product_resource p
     set p.resource_uri = 'https://java.com/en/download/'
   where p.product_id = 70000
     and p.resource_type_code = 'JRE_URL';