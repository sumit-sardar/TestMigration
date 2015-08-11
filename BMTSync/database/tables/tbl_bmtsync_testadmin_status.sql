CREATE TABLE BMTSYNC_TESTADMIN_STATUS (
                        Customer_ID           INTEGER NOT NULL,
                        Test_Admin_ID         INTEGER NOT NULL,
			App_Name              VARCHAR2(50) not null,
			Exported_On           DATE DEFAULT SYSDATE,
			Export_Status         VARCHAR2(10),
			No_Of_Attempts        INTEGER DEFAULT 0,
                        NEXT_RETRY_DATETIME   DATE,
			Error_Code            VARCHAR2(10),
			Error_Message         VARCHAR2(200) )
                        PARTITION BY LIST(Export_Status) 
                        (
                        PARTITION Export_New VALUES ('New'),
                        PARTITION Export_Failed VALUES ('Failed'),
                        PARTITION Export_InProgress VALUES ('Inprogress'),
                        PARTITION Export_Other VALUES (DEFAULT)
		);


ALTER TABLE BMTSYNC_TESTADMIN_STATUS ENABLE ROW MOVEMENT;

CREATE UNIQUE INDEX BMTSYNC_TESTADMIN_STATUS_IDX ON BMTSYNC_TESTADMIN_STATUS (Test_Admin_ID);

