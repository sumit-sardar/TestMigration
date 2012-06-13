#! /bin/sh
#Program Name         :file_split.sh                                                           #
#Author Name          :Wipro Technologies                                                      #
#Program Description  :This program generates the EISS file for each distrct in the            #
#                      format expected by the Mainframe team.                                  #
#                      This programs needs two arguments.One is the target file directory      #
#                      and the second one is the archive file directory path.                  #
#                      This program extracts data from eiss.dat file which is present in the   #
#                      directory.This file is mandatory for the script to generate files .     #
#Created date         : 1st March 2006                                                      #

#Set the environment for the below mentioned environment variables.                              #

#Set the ORACLE_HOME                                                                           #
ORACLE_HOME=/appl/oracle/product/10.2.0.4
export ORACLE_HOME

#Set the default and Oracle PATH                                                               #
PATH=$PATH:$ORACLE_HOME/bin
export PATH

#Set the default and Oracle LD_LIBRARY_PATH                                                    #
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$ORACLE_HOME/lib
export LD_LIBRARY_PATH

#Set the USERNAME of OAS Schema                                                                #
DB_OAS_USER=oas_stage

#Set the PASSWORD of OAS Schema                                                                #
DB_OAS_PASSWD=oasstg8stg

#Set the DB Connection String                                                                  #
DB_CONN_STRING=@OASR5S.CTB

#Set the environment for the below mentioned environment variables.                            #
ENVIR_TYPE=P

#Set the MVSDSN Type                                                                           #
MVS_TYP=WSU

#Set the MVSDSN Expiration                                                                     #
MVS_EXP=STW5Y

# Checking for the Target directory path as an argument.
if test -z "$1"
then
        echo "Target directory path  is expected as an first argument "
        exit 1
fi
# Checking for the Archive directory path as second argument.
if test -z "$2" 
then
        echo "Archive  directory path  is expected as argument "
        exit 1
fi

# Checking for the Parameter file directory path as third argument.
if test -z "$3" 
then
        echo "Parameter file directory path  is expected as argument "
        exit 1
fi

#Assigning the Date value to this variable
RUN_DATE=`date '+%Y%m%d'`

# IDentifying the Computer name from the parameter file oas_eiss.par
V_COMPUTER_NAME=`grep "COMPUTER_NAME" $3/oas_eiss.par|sed 's/COMPUTER_NAME=//'`

# IDentifying the Version Number from the parameter file oas_eiss.par
V_VERSION_NUM=`grep "VERSION_NUM"  $3/oas_eiss.par|sed 's/VERSION_NUM=//'`


# IDentifying the Build_Date from the parameter file oas_eiss.par
#V_BUILD_DATE=`grep "BUILD_DATE"  $3/oas_eiss.par|sed 's/BUILD_DATE=//'`
V_BUILD_DATE=`date '+%h %d %Y'`
V_OPUNIT=00$4


# Making End Record

V_ER=`echo "Select 'ER '||to_char(max(created_date_time), 'Dy Mon DD YYYY HH:MI:SS AM')||', '||(Trunc (mod((sysdate - max(created_date_time)) * 24, 24)) || ':' ||Trunc (mod((sysdate - max(created_date_time)) * 24 * 60, 60)) || ':' ||	Trunc (mod((sysdate - max(created_date_time)) * 24 * 60 * 60, 60))) as Total_time From DATA_IMPORT_HISTORY Where IMPORT_FILENAME = 's_m_O_Eiss_Validity_Check';"| sqlplus -s $DB_OAS_USER/$DB_OAS_PASSWD$DB_CONN_STRING | tail -2|head -1 `



# Final End Record , which will be the last line to all the files generated below
# V_ER=$V_ER,$V_COMPUTER_NAME,$V_VERSION_NUM$V_BUILD_DATE
V_ER="$V_ER,$V_COMPUTER_NAME"
echo V_ER
#Replace space with ~ and re directing the data to new file eiss1.dat
cat $1/eiss_ISTEP.dat|sed s/' '/~/g > $1/eiss1_ISTEP.dat

# Reading the eiss1.dat file line by line in the do loop and if 'OU' record is found a new file is created and moving the records to the new file name.

exec < $1/eiss1_ISTEP.dat
while read ln
do   
            S_TMP=`echo "$ln" | cut -d"," -f1`

            if test "$S_TMP"  =   'OU' 
            then
              if [ -f  $1/$V_FILE_NAME ]
               then
		    echo "$V_ER" >> $1/$V_FILE_NAME
        #added one line code for format conversion 05/17/2007
		    unix2dos $1/$V_FILE_NAME
	  
		    echo "$V_ER" >> $2/$V_FILE_NAME.$RUN_DATE
              fi
           
            # Identify the Org_name and replace ~ with space
            V_ORG_NAME=`echo "$ln" | cut -d "," -f7|sed s/~/' '/g `
             echo "  V_ORG_NAME = $V_ORG_NAME"

	     V_ORG=`echo "$ln" | cut -d "," -f2|sed s/~/' '/g `
	     V_EISS_ORG=`expr substr "$V_ORG" 1 7`
            #generate the file name based on org_name
            V_FILE_NAME=`echo "
	     SELECT DISTINCT ('.T'||LPAD(P.EISS_TESTING_PROGRAM,3,0))||'.'||'L'||SUBSTR(E.STRUC_LVL,1,1)||LPAD(E.STRUC_ELM,5,0)||'.'||SUBSTR(C.SEASON_YEAR,1,1)||SUBSTR(C.SEASON_YEAR,5,6)||'~'||S.OU_ID AS FILE_NAME
            FROM
            ORG_NODE O, STG_EISS_TB S, EISS_ORG_BC_TPE E, PRODUCT P, CUSTOMER C 
            WHERE 
            O.ORG_NODE_ID=S.OU_ID AND S.CUSTOMER_ID=C.CUSTOMER_ID AND S.PRODUCT_ID=P.PRODUCT_ID AND UPPER(O.ORG_NODE_CODE)=UPPER(SPCL_CD)
            AND UPPER(E.ELM_NAME)=UPPER('$V_ORG_NAME') AND S.OPUNIT = $4
            AND S.LEVEL_TYPE='OU' AND O.CUSTOMER_ID=C.CUSTOMER_ID AND E.CUSTOMER_ID = O.CUSTOMER_ID AND E.PRODUCT_ID = S.PRODUCT_ID;" | sqlplus -s $DB_OAS_USER/$DB_OAS_PASSWD$DB_CONN_STRING| tail -2|head -1 `

	    echo $V_FILE_NAME
	    V_STRING=$V_FILE_NAME

	    pos=`expr index $V_STRING '~'`
	    val=`expr $pos - 1`
	    echo "position of ~ is" $pos
	    V_FILE_NAME=`expr substr $V_STRING 1 $val`
	    echo $V_FILE_NAME
	    val=`expr $pos + 1`
	    end_of_line=`expr length $V_STRING`
	    v_OU_ID=`expr substr $V_STRING $val $end_of_line`
	    echo $v_OU_ID
		V_FILE_NAME=$V_EISS_ORG$V_FILE_NAME
            V_FILE_NAME=CSC$ENVIR_TYPE.$MVS_TYP.$MVS_EXP.$V_FILE_NAME$V_OPUNIT
         
              # Strip off the Org_name
	        ln=`echo "$ln" | cut -d"," -f1,2,3,4,5,6`
          
              # Replace OU, with OU
	        ln=`echo "$ln"| sed s/\OU,/'OU '/g`
              #added by Puru to have one opunit for FCAT 
	       rm $1/$V_FILE_NAME
               rm  $2/$V_FILE_NAME.$RUN_DATE          
             fi;      
             #Replace ~ with space
	       echo "$ln"| sed s/~/' '/g  >> $1/$V_FILE_NAME
		 
              echo "$ln"| sed s/~/' '/g  >> $2/$V_FILE_NAME.$RUN_DATE          
done;

#Create ER record for last file
echo "$V_ER" >> $1/$V_FILE_NAME
echo "$V_ER" >> $2/$V_FILE_NAME.$RUN_DATE

#added one line code for format conversion 05/17/2007
unix2dos $1/$V_FILE_NAME

#Copy file to tmp dir
cp $1/$V_FILE_NAME /appl/informatica/PowerCenter8.1.1/server/infa_shared/TgtFiles/HWDI/Track_A2/EISS/tmp

#Remove the intermediate file
rm $1/eiss1_ISTEP.dat
  

