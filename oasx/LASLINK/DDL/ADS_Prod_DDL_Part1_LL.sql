/
create table AA_ITEM_DECRYPTED_TEMP
(
  AA_ITEM_ID         VARCHAR2(32) not null,
  ITEM_RENDITION_XML BLOB,
  CREATED_DATE_TIME  DATE default sysdate not null
)
/
