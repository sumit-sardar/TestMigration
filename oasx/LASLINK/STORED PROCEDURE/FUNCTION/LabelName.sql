create or replace function LabelName(str_in IN NUMBER) return varchar2 as
  
 
  LANGUAGE JAVA NAME 'LabelName.getLanguage (java.lang.Integer) return java.lang.String';
  
