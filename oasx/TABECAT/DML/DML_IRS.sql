--product_type_dim
insert into product_type_dim values (5, 'TABE CAT')
/

--level_dim
insert into level_dim values (23, 'CAT', 5)
/
insert into level_dim values (24, 'N/A', 5)
/
insert into level_dim values (25,'L',5)
/
insert into level_dim values (26,'E',5)
/
insert into level_dim values (27,'M',5)
/
insert into level_dim values (28,'D',5)
/
insert into level_dim values (29,'A',5)
/

--form_dim
insert into form_dim values (13, 'CAT', 5)
/
insert into form_dim values (14, 'N/A', 5)
/

--grade_dim
insert into grade_dim values (28, 'AD', 5)
/
insert into grade_dim values (29, 'JV', 5)
/

--attr1_dim
insert into attr1_dim values (5,'Yes','ELL',5)
/
insert into attr1_dim values (6,'No','ELL',5)
/

--attr2_dim
insert into attr2_dim values (35,'Asian/Pacific Islander','Ethnicity',5)
/
insert into attr2_dim values (36,'American Indian or Alaska Native','Ethnicity',5)
/
insert into attr2_dim values (37,'African American or Black','Ethnicity',5)
/
insert into attr2_dim values (38,'Hispanic or Latino','Ethnicity',5)
/
insert into attr2_dim values (39,'Caucasian','Ethnicity',5)
/
insert into attr2_dim values (40,'Multi-ethnic','Ethnicity',5)
/
insert into attr2_dim values (41,'Ethnicity Unknown','Ethnicity',5)
/

--attr3_dim
insert into attr3_dim values (5,'Yes','Free or Reduced Lunch',5)
/
insert into attr3_dim values (6,'No','Free or Reduced Lunch',5)
/

--attr4_dim
insert into attr4_dim values (7,'Female','Gender',5)
/
insert into attr4_dim values (8,'Male','Gender',5)
/
insert into attr4_dim values (9,'Gender Unknown','Gender',5)
/

--attr5_dim
insert into attr5_dim values (5,'Yes','IEP',5)
/
insert into attr5_dim values (6,'No','IEP',5)
/

--attr6_dim
insert into attr6_dim values (10,'Employed','Labor Force Status',5)
/
insert into attr6_dim values (11,'Unemployed','Labor Force Status',5)
/
insert into attr6_dim values (12,'Not in Labor Force','Labor Force Status',5)
/
insert into attr6_dim values (13,'Labor Force Status Unknown','Labor Force Status',5)
/

--attr7_dim
insert into attr7_dim values (5,'Yes','LEP',5)
/
insert into attr7_dim values (6,'No','LEP',5)
/

--attr8_dim
insert into attr8_dim values (5,'Yes','Migrant',5)
/
insert into attr8_dim values (6,'No','Migrant',5)
/

--attr9_dim
insert into attr9_dim values (7,'Yes','Screen Magnifier',5)
/
insert into attr9_dim values (8,'No','Screen Magnifier',5)
/

--attr10_dim
insert into attr10_dim values (5,'Yes','Section 504',5)
/
insert into attr10_dim values (6,'No','Section 504',5)
/

--attr11_dim
insert into attr11_dim values (7,'Yes','Screen Reader',5)
/
insert into attr11_dim values (8,'No','Screen Reader',5)
/

--attr12_dim
insert into attr12_dim values (7,'Yes','Calculator',5)
/
insert into attr12_dim values (8,'No','Calculator',5)
/

--attr13_dim
insert into attr13_dim values (7,'Yes','Allow Pause',5)
/
insert into attr13_dim values (8,'No','Allow Pause',5)
/

--attr14_dim
insert into attr14_dim values (7,'Yes','Untimed Test',5)
/
insert into attr14_dim values (8,'No','Untimed Test',5)
/

--attr15_dim
insert into attr15_dim values (7,'Yes','Adjusted Colors',5)
/
insert into attr15_dim values (8,'No','Adjusted Colors',5)
/

--attr16_dim
insert into attr16_dim values (7,'Yes','Adjusted Font Size',5)
/
insert into attr16_dim values (8,'No','Adjusted Font Size',5)
/

--mastery_level_dim
insert into mastery_level_dim values (5,'Beginner')
/
insert into mastery_level_dim values (6,'Advanced')
/