<?xml version="1.0" encoding="US-ASCII"?>
<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">




<xsl:include href="common.xsl"/>

<!--

FILENAME:
       common_IB.xsl

PURPOSE:
	Contains logic common to FOP_Interface.xsl and FOP_Interface_CRIB.xsl

CHANGE HISTORY:
        Created during refactoring 4/29/2003 Edward Hieatt (edward@edwardh.com)
  	Modified to include CR3 similar to CR2 4/3/2005 Sudha Manimaran (sudha_manimaran@ctb.com)
  	Modified to include CR5 similar to CR1 4/3/2005 Sudha Manimaran (sudha_manimaran@ctb.com)
  	Modified to include CR4 similar to CR2 8/12/2004 Giuseppe Gennaro (giuseppe_gennaro@ctb.com)  
  	Modified to incorporate recent changes, see CVS (maxdunn@siliconpublishing.com)
-->

<xsl:variable name="grade">
	<xsl:choose>
		<xsl:when test="self::node()=Assessment"><xsl:value-of select="/Assessment/SubTest/@Grade"/></xsl:when>
		<xsl:when test="self::node()=SubTest"><xsl:value-of select="/SubTest/@Grade"/></xsl:when>
		<xsl:when test="self::node()=ItemSet"><xsl:value-of select="substring-after(/ItemSet/Item//Hierarchy[@Type='Grade'][1]/@Name, 'Grade ')"/></xsl:when>
		<xsl:when test="self::node()=Item"><xsl:value-of select="substring-after(/Item//Hierarchy[@Type='Grade'][1]/@Name, 'Grade ')"/></xsl:when>
	</xsl:choose>
</xsl:variable>

<xsl:variable name="fontsize">
	<xsl:call-template name="lookup">
		<xsl:with-param name="name" select="'fontsize'"/>
                <xsl:with-param name="key" select="$grade"/>
	</xsl:call-template>
</xsl:variable>

<xsl:variable name="headingfont">
	<xsl:call-template name="lookup">
                <xsl:with-param name="name" select="'headingfont'"/>
                <xsl:with-param name="key" select="$grade"/>
	</xsl:call-template>
</xsl:variable>


    <xsl:variable name="passage_graphic_id">
      <xsl:call-template name="find_passage_graphic_id"/>
    </xsl:variable>      

  <xsl:template match="/">

<fo:root>

<!-- defines measurements for page layout. There are three page layouts, the
cover-page which is named "first", test pages which is named "simple" and the
end page which in named "last-page"-->
<fo:layout-master-set>
<xsl:choose>
  <xsl:when test="self::node()=Assessment or self::node()=SubTest">

<!--defines document page order and layout for an Assessment and SubTest. These will
utilize the first, simple and last-page layouts-->

	<fo:page-sequence-master master-name="page-order">
	  <fo:repeatable-page-master-alternatives>
	    <fo:conditional-page-master-reference page-position="first" master-reference="first"/>
	    <fo:conditional-page-master-reference master-reference="simple"/>
	    <fo:conditional-page-master-reference page-position="last" master-reference="last-page"/>
	  </fo:repeatable-page-master-alternatives>
        </fo:page-sequence-master>

	     <fo:simple-page-master master-name="first" page-height="11in" page-width="8.5in" margin-top=".25in" margin-bottom="0.0in" margin-left=".25in" margin-right=".25in">
		<fo:region-body margin-top="1.5cm" margin-bottom="2cm"/>
		<fo:region-before extent="1.5cm"/>
		<fo:region-after extent="1in"/>
	     </fo:simple-page-master>

	     <fo:simple-page-master master-name="simple" page-height="11in" page-width="8.5in" margin-top="0.25in" margin-bottom="0.25in" margin-left=".5in" margin-right=".5in">
		<fo:region-body margin-top=".4in" margin-bottom="1.2in"/>
		<fo:region-before extent=".5in"/>
		<fo:region-after extent="1in"/>
	     </fo:simple-page-master>

	     <fo:simple-page-master master-name="last-page" page-height="11in" page-width="8.5in" margin-top=".5in" margin-bottom="0.0in" margin-left=".5in" margin-right=".5in">
	     <fo:region-body margin-top="1.5cm" margin-bottom="4cm"/>
		<fo:region-before extent="3cm"/>
		<fo:region-after extent="1in"/>
	</fo:simple-page-master>
</xsl:when>

	<xsl:when test="self::node()=ItemSet or self::node()=Item">

	<!--defines document page order and layout for an ItemSet and Item. These will only
	utilize the simple page layout for possible preview-->

				<fo:page-sequence-master master-name="page-order">
				  <fo:repeatable-page-master-alternatives>
				    <fo:conditional-page-master-reference master-reference="simple"/>
				  </fo:repeatable-page-master-alternatives>
				</fo:page-sequence-master>

		     <fo:simple-page-master master-name="simple" page-height="11in" page-width="8.5in" margin-top="0.25in" margin-bottom="0.25in" margin-left=".5in" margin-right=".5in">
			<fo:region-body margin-top=".4in" margin-bottom="1.2in"/>
			<fo:region-before extent=".5in"/>
			<fo:region-after extent="1in"/>
		     </fo:simple-page-master>
	   </xsl:when>
       </xsl:choose>
     </fo:layout-master-set>

<!--Based on the root node different filepaths will have to be traversed to
reach attribute values that will dynamically populate page layouts. For
XML with Assessment as the root node with have to traverse Assessment first-->


 <xsl:if test="self::node()=Assessment">
<fo:page-sequence force-page-count="no-force" master-reference="first" language="en" country="us">

   <fo:static-content flow-name="xsl-region-before">
     <fo:block text-align="start" font-size="12pt" font-family="any" white-space-collapse="false">
     </fo:block>
   </fo:static-content>

   <fo:static-content flow-name="xsl-region-after">
        <xsl:call-template name="copyright">
                <xsl:with-param name="size" select="'4'"/>
        </xsl:call-template>
   </fo:static-content>

   <fo:flow flow-name="xsl-region-body">
     <fo:block font-size="47pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
      <xsl:choose>
	      <xsl:when test="/Assessment/@CustomerName">
	        <xsl:choose>
	        	<xsl:when test="/Assessment/@CustomerName= ''">
	        		CTB/McGraw-Hill
	        	</xsl:when>
	        	<xsl:otherwise>
				<xsl:value-of select="/Assessment/@CustomerName"/>
			</xsl:otherwise>
		</xsl:choose>
	      </xsl:when>
	      <xsl:otherwise>
		 CTB/McGraw-Hill
	      </xsl:otherwise>
      </xsl:choose>
     </fo:block>

     <fo:block font-size="42pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt">
       <xsl:value-of select="/Assessment/SubTest/@ContentArea"/> Test
     </fo:block>


     <fo:block font-size="42pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt">
      Grade:
       <xsl:value-of select="/Assessment/SubTest/@Grade"/>
     </fo:block>

     <xsl:if test="/Assessment/SubTest/@Form != ''">
       <fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt">
      Form: <xsl:value-of select="/Assessment/SubTest/@Form"/>
       </fo:block>
       </xsl:if>

     <xsl:if test="Assessment/SubTest/@Title != ''">
	<xsl:call-template name="title"/>
        <xsl:choose>
		<xsl:when test="string-length(/Assessment/SubTest/@Title) &gt;= 40">
		       <fo:block font-size="20pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt" white-space-collapse="false"><xsl:value-of select="/Assessment/SubTest/@Title"/></fo:block>
		</xsl:when>
		<xsl:when test="string-length(/Assessment/SubTest/@Title) &gt;= 30">
		       <fo:block font-size="24pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt" white-space-collapse="false"><xsl:value-of select="/Assessment/SubTest/@Title"/></fo:block>
		</xsl:when>
		<xsl:otherwise>
			<fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt" white-space-collapse="false"><xsl:value-of select="/Assessment/SubTest/@Title"/></fo:block>
		</xsl:otherwise>
 	</xsl:choose>
	<xsl:call-template name="titleSuffix"/>
     </xsl:if>

     <fo:block font-size="22pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="30pt">
      Student Test Form
     </fo:block>
     <fo:block font-size="14pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
      Student Name _______________________________________________
     </fo:block>
     <fo:block font-size="14pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
      Teacher Name ______________________________________________
     </fo:block>
     <fo:block font-size="14pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
           Date ______________________________________________________
     </fo:block>
   </fo:flow>
</fo:page-sequence>
</xsl:if>

<!-- Traversing the document to attribute values for a SubTest XML will not
traverse an Assessment node because is wont exist.Below is the traversing
logic for a SubTest's "first" page layout-->

<xsl:if test="self::node()=SubTest">
  <fo:page-sequence master-reference="first" language="en" country="us" force-page-count="no-force">
    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="start" font-size="12pt" font-family="any" white-space-collapse="false">
      </fo:block>
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
	<xsl:call-template name="copyright">
                <xsl:with-param name="size" select="'4'"/>
	</xsl:call-template>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
      <fo:block font-size="47pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
            <xsl:choose>
      	      <xsl:when test="/Assessment/@CustomerName">
      	        <xsl:choose>
      	        	<xsl:when test="/Assessment/@CustomerName= ''">
      	        		CTB/McGraw-Hill
      	        	</xsl:when>
      	        	<xsl:otherwise>
      				<xsl:value-of select="/Assessment/@CustomerName"/>
      			</xsl:otherwise>
      		</xsl:choose>
      	      </xsl:when>
      	      <xsl:otherwise>
      		 CTB/McGraw-Hill
      	      </xsl:otherwise>
      </xsl:choose>
      </fo:block>
      <fo:block font-size="42pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt">
        <xsl:value-of select="/SubTest/@ContentArea"/> Test
      </fo:block>

      <fo:block font-size="42pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt">
       Grade:
       <xsl:value-of select="/SubTest/@Grade"/>
      </fo:block>
      <xsl:if test="/SubTest/@Form != ''">
        <fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt">
         Form: <xsl:value-of select="/SubTest/@Form"/>
        </fo:block>
      </xsl:if>
      <xsl:if test="/SubTest/@Title != ''">
	<xsl:call-template name="title"/>
        <xsl:choose>
		<xsl:when test="string-length(/SubTest/@Title) &gt;= 40">
		       <fo:block font-size="20pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt" white-space-collapse="false"><xsl:value-of select="/SubTest/@Title"/></fo:block>
		</xsl:when>
		<xsl:when test="string-length(/SubTest/@Title) &gt;= 30">
		       <fo:block font-size="24pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt" white-space-collapse="false"><xsl:value-of select="/SubTest/@Title"/></fo:block>
		</xsl:when>
		<xsl:otherwise>
			<fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt" white-space-collapse="false"><xsl:value-of select="/SubTest/@Title"/></fo:block>
		</xsl:otherwise>
 	</xsl:choose>
	<xsl:call-template name="titleSuffix"/>
      </xsl:if>

      <fo:block font-size="22pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="30pt">
       Student Test Form
      </fo:block>
      <fo:block font-size="14pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
       Student Name _______________________________________________
      </fo:block>
      <fo:block font-size="14pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
       Teacher Name ______________________________________________
      </fo:block>
      <fo:block font-size="14pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="20pt">
       Date ______________________________________________________
     </fo:block>
   </fo:flow>
 </fo:page-sequence>
</xsl:if>

<!--Below is the traversing logic for an Assessment's "first" page layout-->
<xsl:if test="self::node()=Assessment">
  <fo:page-sequence master-reference="simple" initial-page-number="1" language="en" country="us">

  <fo:static-content flow-name="xsl-region-before">
    <xsl:if test="/Assessment/SubTest/@Title != ''">
      <fo:block text-align="start" font-weight="bold" font-size="12pt" font-family="any">
        <xsl:value-of select="/Assessment/SubTest/@Title"/>
      </fo:block>
    </xsl:if>
    <fo:block>
      <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
    </fo:block>
  </fo:static-content>

  <fo:static-content flow-name="xsl-region-after">
     <fo:block text-align="end" font-size="14pt" font-family="serif" font-weight="bold">
      Go on to next page
     </fo:block>
     <fo:block>
     <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
     </fo:block>

     <fo:block text-align="end" font-size="12pt" font-family="any">
	  Page 
	  <fo:page-number/>
	  of <fo:page-number-citation ref-id="terminator"/>
     </fo:block>

     <xsl:choose>
     <xsl:when test="/Assessment/SubTest/@Form != '' or /Assessment/SubTest/@Version != ''">
       <fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start" linefeed-treatment="preserve">
	<xsl:if test="/Assessment/SubTest/@Form != ''">Form: <xsl:value-of select="/Assessment/SubTest/@Form "/>&#160;&#160;&#160;&#160;&#160;</xsl:if><xsl:if test="/Assessment/SubTest/@Version != ''">Version/Date: <xsl:value-of select="/Assessment/SubTest/@Version"/></xsl:if>
       </fo:block>
     </xsl:when>
     <xsl:otherwise>
     	<fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start"> </fo:block>
     </xsl:otherwise>
     </xsl:choose>


        <xsl:call-template name="copyright"/>
     </fo:static-content>

     <fo:flow flow-name="xsl-region-body">
       <xsl:apply-templates/>
     </fo:flow>
   </fo:page-sequence>
 </xsl:if>


 <!--Below is the traversing logic for a SubTest's "simple" page layout-->
 <xsl:if test="self::node()=SubTest">
   <fo:page-sequence master-reference="simple" initial-page-number="1" language="en" country="us">

   <fo:static-content flow-name="xsl-region-before">
     <xsl:if test="/SubTest/@Title != ''">
     <fo:block text-align="start" font-weight="bold" font-size="12pt" font-family="any" white-space-collapse="false">
       <xsl:value-of select="/SubTest/@Title"/>
     </fo:block>
     </xsl:if>
     <fo:block>
       <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
     </fo:block>
   </fo:static-content>

   <fo:static-content flow-name="xsl-region-after">
     <fo:block text-align="end" font-size="14pt" font-family="serif" font-weight="bold">
      Go on to next page
     </fo:block>
     <fo:block>
     <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
     </fo:block>

     <fo:block text-align="end" font-size="12pt" font-family="any">
      Page 
      <fo:page-number/>
      of <fo:page-number-citation ref-id="terminator"/>
     </fo:block>


     <xsl:choose>
     <xsl:when test="/SubTest/@Form != '' or /SubTest/@Version != ''">
       <fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start" linefeed-treatment="preserve">
	<xsl:if test="/SubTest/@Form != ''">Form: <xsl:value-of select="/SubTest/@Form "/>&#160;&#160;&#160;&#160;&#160;</xsl:if><xsl:if test="/SubTest/@Version != ''">Version/Date: <xsl:value-of select="/SubTest/@Version"/></xsl:if>
       </fo:block>
     </xsl:when>
     <xsl:otherwise>
     	<fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start"> </fo:block>
     </xsl:otherwise>
     </xsl:choose>


  <xsl:call-template name="copyright"/>
  </fo:static-content>

     <fo:flow flow-name="xsl-region-body">
       <xsl:apply-templates/>
     </fo:flow>
   </fo:page-sequence>
 </xsl:if>

 <xsl:if test="self::node()=ItemSet">
  <fo:page-sequence master-reference="simple" initial-page-number="1" language="en" country="us">

    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="start" font-weight="bold" font-size="12pt" font-family="any" white-space-collapse="false">
            <xsl:value-of select="'Itemset Printout Preview'"/>
      </fo:block>
      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
      </fo:block>
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
      <fo:block text-align="end" font-size="14pt" font-family="serif" font-weight="bold">
       Go on to next page
      </fo:block>
      <fo:block>
      <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
      </fo:block>

      <fo:block text-align="end" font-size="12pt" font-family="any">
                   Page 
                   <fo:page-number/>
                   of <fo:page-number-citation ref-id="terminator"/>
              </fo:block>

                <xsl:call-template name="copyright"/>
      </fo:static-content>

      <fo:flow flow-name="xsl-region-body">
        <xsl:apply-templates/>
        <fo:block id="terminator"/>
      </fo:flow>
    </fo:page-sequence>
 </xsl:if>

 <!--Below is the traversing logic for an Item's simple page layout-->
   <xsl:if test="self::node()=Item">
    <fo:page-sequence master-reference="simple" initial-page-number="1" language="en" country="us">

      <fo:static-content flow-name="xsl-region-before">

        <fo:block>
          <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
        </fo:block>
      </fo:static-content>

      <fo:static-content flow-name="xsl-region-after">
        <!--fo:block text-align="end" font-size="14pt" font-family="serif"
         font-weight="bold">
         Go on to next page
        </fo:block-->
        <fo:block padding-top="15pt">
        <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
        </fo:block>

        <!--fo:block text-align="end" font-size="12pt" font-family="any">
                     Page 
                     <fo:page-number/>
                     of <fo:page-number-citation ref-id="terminator"/>
                </fo:block-->


        <xsl:call-template name="copyright">
                <xsl:with-param name="paddingTop" select="'20pt'"/>
        </xsl:call-template>

        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">
          <xsl:apply-templates/>
          <fo:block id="terminator"/>
        </fo:flow>
      </fo:page-sequence>
 </xsl:if>

<!--Below is the traversing logic for a Assessments's "last-page" layout-->
<xsl:if test="self::node()=Assessment">
  <fo:page-sequence master-reference="last-page" language="en" country="us">

    <fo:static-content flow-name="xsl-region-before">

       <xsl:if test="/Assessment/SubTest/@Title != ''">
       <fo:block text-align="start" font-weight="bold" font-size="12pt" font-family="any" white-space-collapse="false">
         <xsl:value-of select="/Assessment/SubTest/@Title"/>
       </fo:block>
       </xsl:if>


       <fo:block>
         <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
       </fo:block>
    </fo:static-content>

      <fo:static-content flow-name="xsl-region-after">
        <fo:block text-align="end" font-size="14pt" font-family="serif" font-weight="bold">
         Stop Here
        </fo:block>
        <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
        </fo:block>


        <fo:block text-align="end" font-size="12pt" font-family="any">
	         Page 
	         <fo:page-number/>
         of <fo:page-number-citation ref-id="terminator"/>
        <xsl:if test="/Assessment/SubTest/@Number != ''">
        <fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start" white-space-collapse="false">Form: <xsl:value-of select="/Assessment/SubTest/@Number "/>
            </fo:block>
           </xsl:if>

     <xsl:if test="/Assessment/SubTest/@Form != '' or /Assessment/SubTest/@Version != ''">
       <fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start" linefeed-treatment="preserve">
	<xsl:if test="/Assessment/SubTest/@Form != ''">Form: <xsl:value-of select="/Assessment/SubTest/@Form "/>&#160;&#160;&#160;&#160;&#160;</xsl:if><xsl:if test="/Assessment/SubTest/@Version != ''">Version/Date: <xsl:value-of select="/Assessment/SubTest/@Version"/></xsl:if>
       </fo:block>
     </xsl:if>

                <xsl:call-template name="copyright"/>
     </fo:block>
     </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
      <fo:block font-family="any" space-before.optimum="2cm">
        <xsl:attribute name="font-size">
          <xsl:value-of select="$fontsize"/>
        </xsl:attribute>
         This is the end of the test.     
      </fo:block>
      <fo:block id="terminator"/>
    </fo:flow>
  </fo:page-sequence>
  </xsl:if>


<!--Below is the traversing logic for a SubTest's "last-page" layout-->
 <xsl:if test="self::node()=SubTest">
    <fo:page-sequence master-reference="last-page" language="en" country="us">

      <fo:static-content flow-name="xsl-region-before">
         <xsl:if test="/SubTest/@Title != ''">
         <fo:block text-align="start" font-weight="bold" font-size="12pt" font-family="any" white-space-collapse="false">
           <xsl:value-of select="/SubTest/@Title"/>
         </fo:block>
         </xsl:if>

         <fo:block>
           <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
         </fo:block>
      </fo:static-content>

        <fo:static-content flow-name="xsl-region-after">
          <fo:block text-align="end" font-size="14pt" font-family="serif" font-weight="bold">
           Stop Here
          </fo:block>
          <fo:block>
          <fo:leader leader-pattern="rule" rule-thickness="3pt" rule-style="dashed" leader-length="7.5in"/>
          </fo:block>
          <fo:block text-align="end" font-size="12pt" font-family="any">
  	         Page 
  	         <fo:page-number/>
           of <fo:page-number-citation ref-id="terminator"/>
          <xsl:if test="/SubTest/@Number != ''">
          <fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start" white-space-collapse="false">Form: <xsl:value-of select="/SubTest/@Number "/>
              </fo:block>
             </xsl:if>

	     <xsl:if test="/SubTest/@Form != '' or /SubTest/@Version != ''">
	       <fo:block font-size="8pt" font-family="any" space-after.optimum="3pt" text-align="start" linefeed-treatment="preserve">
		<xsl:if test="/SubTest/@Form != ''">Form: <xsl:value-of select="/SubTest/@Form "/>&#160;&#160;&#160;&#160;&#160;</xsl:if><xsl:if test="/SubTest/@Version != ''">Version/Date: <xsl:value-of select="/SubTest/@Version"/></xsl:if>
	       </fo:block>
	     </xsl:if>


        <xsl:call-template name="copyright"/>

       </fo:block>
       </fo:static-content>

      <fo:flow flow-name="xsl-region-body">
        <fo:block font-family="any" space-before.optimum="2cm" white-space-collapse="false">
          <xsl:attribute name="font-size">
            <xsl:value-of select="$fontsize"/>
          </xsl:attribute>
           This is the end of the test.     
        </fo:block>
        <fo:block id="terminator"/>
      </fo:flow>
    </fo:page-sequence>
  </xsl:if>

 </fo:root>
</xsl:template>

  <!--Stem template applies templates for each of the various 20 Stems that can be rendered in
  the XML-->

  <xsl:template match="Stem">

    <fo:list-block>
      <fo:list-item>
	<xsl:call-template name="stemListItem"/>
      <xsl:choose>
        <xsl:when test="not($isCR) or parent::node()/@ItemType='CR'">
   <xsl:choose>
   <xsl:when test="parent::node()/@Template='0' or parent::node()/@Template='TB0' or parent::node()/@Template='1' or parent::node()/@Template='TB1'">
     <fo:list-item-body>
     	   <xsl:for-each select="P">
	       <xsl:if test="position() = 1">
		 <fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		     <xsl:value-of select="$fontsize"/>
		   </xsl:attribute>
		   	<xsl:apply-templates/>
		 </fo:block>
	       </xsl:if>

	       <xsl:if test="position() = 2">
		 <fo:block padding-bottom="10pt" padding-top="6pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  	<xsl:apply-templates/>
		 </fo:block>
	       </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block padding-bottom="10pt" start-indent="0.5in" font-family="any" padding-top="6pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
			<xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	   </xsl:for-each>
     </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='2'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>

	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block start-indent=".5in" font-family="any" padding-top="10pt" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  	<xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block padding-bottom="10pt" start-indent="0.5in" font-family="any" padding-top="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  	<xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
      	 </xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false" text-align="left">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>

    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='3' or parent::node()/@Template='TB3'">
    <fo:list-item-body>
     	 <xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
		       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic">
			       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" text-align="center" white-space-collapse="false">
				 <fo:external-graphic>
				   <xsl:attribute name="src">
				     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
				   </xsl:attribute>
				 </fo:external-graphic>
				</fo:block>
               		</xsl:if>
	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block padding-bottom="10pt" padding-top="10pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  	<xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
      	  </xsl:for-each>
      <fo:block white-space-collapse="false">
        <fo:inline padding-bottom="6pt" padding-top="20pt">
        <fo:inline font-weight="bold" white-space-collapse="false">    </fo:inline>
          <fo:external-graphic>
            <xsl:attribute name="src">
            <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 1]/EPSPrint/@FileName"/>
            </xsl:attribute>
          </fo:external-graphic>
        </fo:inline>

        <fo:inline padding-bottom="6pt" padding-top="20pt">
        <fo:inline font-weight="bold" white-space-collapse="false">     </fo:inline>
          <fo:external-graphic>
           <xsl:attribute name="src">
            <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@FileName"/>
           </xsl:attribute>
          </fo:external-graphic>
        </fo:inline>

        <fo:inline padding-bottom="6pt" padding-top="20pt">
        <fo:inline font-weight="bold" white-space-collapse="false">     </fo:inline>
          <fo:external-graphic>
            <xsl:attribute name="src">
             <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 3]/EPSPrint/@FileName"/>
            </xsl:attribute>
          </fo:external-graphic>
        </fo:inline>

        <fo:inline padding-bottom="6pt" padding-top="20pt">
        <fo:inline font-weight="bold" white-space-collapse="false">     </fo:inline>
          <fo:external-graphic>
            <xsl:attribute name="src">
              <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 4]/EPSPrint/@FileName"/>
            </xsl:attribute>
          </fo:external-graphic>
        </fo:inline>

        <fo:inline padding-bottom="6pt" padding-top="20pt">
        <fo:inline font-weight="bold" white-space-collapse="false">     </fo:inline>
          <fo:external-graphic>
            <xsl:attribute name="src">
             <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 5]/EPSPrint/@FileName"/>
            </xsl:attribute>
          </fo:external-graphic>
        </fo:inline>
      </fo:block>
    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='4' or parent::node()/@Template='8' or parent::node()/@Template='TB8' or parent::node()/@Template='14' or parent::node()/@Template='18'">
    <fo:list-item-body>

       
      <xsl:for-each select="P">

        <xsl:if test="ancestor::*[@Template][1]/@Template='8' or ancestor::*[@Template][1]/@Template='TB8'">
          <xsl:if test="string-length(normalize-space(@graphic-before)) &gt; 0">
            <xsl:call-template name="placed_graphic">
              <xsl:with-param name="where">before</xsl:with-param>
            </xsl:call-template>
          </xsl:if>
        </xsl:if>
        
      	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  	  <xsl:apply-templates/>
		</fo:block>
               <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
               <xsl:if test="(parent::node()/parent::Item/@Template='14' or parent::node()/parent::Item/@Template='18') and parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
                       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
                       <xsl:attribute name="text-align">
                       <xsl:choose>
                            <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
                                   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
                            </xsl:when>
                            <xsl:otherwise>
                                   <xsl:value-of select="'left'"/>
                            </xsl:otherwise>
                        </xsl:choose>
                          </xsl:attribute>
                         <fo:external-graphic>
                           <xsl:attribute name="src">
                             <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
                           </xsl:attribute>
                         </fo:external-graphic>
                        </fo:block>
                </xsl:if>
	      </xsl:if>
	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" font-weight="bold" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	      <xsl:if test="position() = 3">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

    <xsl:if test="position() &gt; 3 and     (parent::Stem/parent::*[@Template='8'] or parent::Stem/parent::*[@Template='TB8'])">
      <fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false" font-size="{$fontsize}">
        <xsl:apply-templates/>
      </fo:block>
    </xsl:if>

  
      
        <xsl:if test="ancestor::*[@Template][1]/@Template='8' or ancestor::*[@Template][1]/@Template='TB8'">
          <xsl:if test="string-length(normalize-space(@graphic-after)) &gt; 0">
            <xsl:call-template name="placed_graphic">
              <xsl:with-param name="where">after</xsl:with-param>
            </xsl:call-template>
          </xsl:if>
        </xsl:if>
      </xsl:for-each>
    
    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='5' or parent::node()/@Template='TB5'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	      <xsl:if test="position() = 3">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	  </xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" text-align="left" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>
      	<xsl:if test="preceding-sibling::Stimulus/Graphic[position() = 2]">
	      <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" text-align="left" white-space-collapse="false">
		<fo:inline padding-bottom="6pt" padding-top="10pt">
		  <fo:external-graphic>
		    <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@FileName"/>
		    </xsl:attribute>
		  </fo:external-graphic>
		</fo:inline>
	      </fo:block>
	 </xsl:if>
	</fo:list-item-body>

  </xsl:when>


  <xsl:when test="parent::node()/@Template='6' or parent::node()/@Template='TB6'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" padding-bottom="10pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
     	 </xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>

     <!-- The image for this template is obtained from the Stimulus >
      <fo:block white-space-collapse="false">
      <fo:inline padding-bottom="6pt" padding-top="20pt">
      <fo:inline font-weight="bold" white-space-collapse="false">    </fo:inline>
          <fo:external-graphic>
            <xsl:attribute name="src">
             <xsl:value-of select="Graphic[position() = 1]/EPSPrint/@FileName"/>
            </xsl:attribute>
          </fo:external-graphic>
        </fo:inline>
      </fo:block-->
    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='7' or parent::node()/@Template='TB7'"> 
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
			<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
			  <xsl:attribute name="font-size">
			   <xsl:value-of select="$fontsize"/>
			  </xsl:attribute>
			  <xsl:apply-templates/>
			</fo:block>
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block start-indent=".5in" font-family="any" font-weight="bold" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block padding-top="10pt" padding-bottom="10pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	</xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>

    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='9' or parent::node()/@Template='TB9'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
			<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
			  <xsl:attribute name="font-size">
			   <xsl:value-of select="$fontsize"/>
			  </xsl:attribute>
			  <xsl:apply-templates/>
			</fo:block>
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       
          <xsl:if test="not(//Item/Stimulus[@ID=current()/ancestor::Item/Stimulus/@ID][@dedupe='yes']) or ( not(preceding::Item[Stimulus/@ID = current()/ancestor::Item/Stimulus/@ID]) and not(following::Item[Stimulus/@ID = current()/ancestor::Item/Stimulus/@ID]) )">
             <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
             
		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		
             </xsl:if>
          </xsl:if>
    
	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block start-indent=".5in" font-family="any" font-weight="bold" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block padding-top="10pt" padding-bottom="10pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

    <xsl:if test="position() &gt; 3">
      <fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false" font-size="{$fontsize}">
        <xsl:apply-templates/>
      </fo:block>
    </xsl:if>

  
	</xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>
	</fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='10' or parent::node()/@Template='TB10'">
    
      <fo:list-item-body>
        <xsl:if test="(//Stimulus[@ID = current()/parent::Item/Stimulus/@ID]/@dedupe='yes')">
          <xsl:if test="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@FileName">
      	       <fo:block padding-top="10pt" padding-bottom="10pt" start-indent="0.5in">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
      		 <fo:external-graphic>
      		   <xsl:attribute name="src">
      		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@FileName"/>
      		   </xsl:attribute>
      		 </fo:external-graphic>
      		</fo:block>
	</xsl:if>
        </xsl:if>
        
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>

	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false" font-weight="bold">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
     	 </xsl:for-each>
       <xsl:if test="Graphic[position() = 1]/EPSPrint/@FileName">
	      <fo:block>
		<fo:inline padding-bottom="6pt" padding-top="20pt">
		<fo:inline font-weight="bold" white-space-collapse="false">    </fo:inline>
		  <fo:external-graphic>
		   <xsl:attribute name="src">
		    <xsl:value-of select="Graphic[position() = 1]/EPSPrint/@FileName"/>
		   </xsl:attribute>
		  </fo:external-graphic>
		</fo:inline>
	      </fo:block>
	</xsl:if>
      <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
        
      <xsl:if test="not( (//Stimulus[@ID = current()/parent::Item/Stimulus/@ID]/@dedupe='yes') )">
        <xsl:if test="preceding-sibling::Stimulus/Graphic[position() = 2]">
      	       <fo:block padding-bottom="10pt" start-indent="0.5in" padding-top="11pt">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
      		 <fo:external-graphic>
      		   <xsl:attribute name="src">
      		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@FileName"/>
      		   </xsl:attribute>
      		 </fo:external-graphic>
      		</fo:block>
	</xsl:if>
      </xsl:if>
    
       
          <xsl:if test="not(//Item/Stimulus[@ID=current()/ancestor::Item/Stimulus/@ID][@dedupe='yes']) or ( not(preceding::Item[Stimulus/@ID = current()/ancestor::Item/Stimulus/@ID]) and not(following::Item[Stimulus/@ID = current()/ancestor::Item/Stimulus/@ID]) )           ">
             <xsl:if test="preceding-sibling::Stimulus/Graphic[position() = 1]">
             
	       <fo:block padding-top="10pt" padding-bottom="10pt" start-indent="0.5in">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 1]/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	
             </xsl:if>
          </xsl:if>
    
    
      </fo:list-item-body>
    
  </xsl:when>

  <xsl:when test="parent::node()/@Template='11' or parent::node()/@Template='TB11'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
		       <fo:block padding-top="20pt" padding-bottom="20pt" start-indent="0.5in">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
     	 </xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="10pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>
    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='12' or parent::node()/@Template='TB12' or parent::node()/@Template='12A' or parent::node()/@Template='13'">
    <fo:list-item-body>
	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>

    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
	      	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	      	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
	      		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
	      		       <xsl:attribute name="text-align">
	      		       <xsl:choose>
	      			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	      				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	      			    </xsl:when>
	      			    <xsl:otherwise>
	      				   <xsl:value-of select="'left'"/>
	      			    </xsl:otherwise>
	      			</xsl:choose>
	      			  </xsl:attribute>
	      			 <fo:external-graphic>
	      			   <xsl:attribute name="src">
	      			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
	      			   </xsl:attribute>
	      			 </fo:external-graphic>
	      			</fo:block>
			</xsl:if>
		<fo:block padding-top="18pt" padding-bottom="10pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	      <xsl:if test="position() = 3">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
     	 </xsl:for-each>
    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='15' or parent::node()/@Template='16' or parent::node()/@Template='17' or parent::node()/@Template='TB17' or parent::node()/@Template='19'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
	      </xsl:if>
	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	      <xsl:if test="position() = 3">
		<fo:block start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
     	 </xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="10pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>
    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='20' or parent::node()/@Template='TB20'">
    <fo:list-item-body>
      <fo:block white-space-collapse="false">
	<fo:inline padding-bottom="6pt" padding-top="20pt">
	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic[position() = 2]">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 2]/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic[position() = 1]">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic[position() = 1]/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic[position() = 1]/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 1]/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic[position() = 1]/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>

        </fo:inline>
      </fo:block>
    </fo:list-item-body>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='CR1' or parent::node()/@Template='CR2' or parent::node()/@Template='CR3' or parent::node()/@Template='CR4' or parent::node()/@Template='CR5' or parent::node()/@Template='CR6'">
  	<fo:list-item-body>
      	<xsl:apply-templates/>
      </fo:list-item-body>
  </xsl:when> 
  
    <xsl:when test="parent::node()/@Template='22' or parent::node()/@Template='TB22' or parent::node()/@Template='24' or parent::node()/@Template='25' or parent::node()/@Template='27'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
		<fo:block padding-top="18pt" padding-bottom="10pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
	      </xsl:if>

	      <xsl:if test="position() = 2">
		<fo:block start-indent="0.5in" font-family="any" font-weight="bold" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		   <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  <xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
     	 </xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>
    </fo:list-item-body>
  </xsl:when>


  <xsl:when test="parent::node()/@Template='23'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
			<fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
			  <xsl:attribute name="font-size">
			   <xsl:value-of select="$fontsize"/>
			  </xsl:attribute>
			  <xsl:apply-templates/>
			</fo:block>
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic[1]/EPSPrint">
		       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic[1]/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic[1]/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic[1]/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
               <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic[2]/EPSPrint">
                       <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
                       <xsl:attribute name="text-align">
                       <xsl:choose>
                            <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic[2]/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
                                   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic[2]/EPSPrint/@Position"/>
                            </xsl:when>
                            <xsl:otherwise>
                                   <xsl:value-of select="'left'"/>
                            </xsl:otherwise>
                        </xsl:choose>
                          </xsl:attribute>
                         <fo:external-graphic>
                           <xsl:attribute name="src">
                             <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic[2]/EPSPrint/@FileName"/>
                           </xsl:attribute>
                         </fo:external-graphic>
                        </fo:block>
                </xsl:if>
	      </xsl:if>
	</xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>

    </fo:list-item-body>
  </xsl:when>
  
    <xsl:when test="parent::node()/@Template='26' or parent::node()/@Template='TB26'">
    <fo:list-item-body>
    	<xsl:for-each select="P">
	      <xsl:if test="position() = 1">
	       <!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	       <xsl:if test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint">
		       <fo:block padding-top="18pt" padding-bottom="6pt" start-indent="0.5in" white-space-collapse="false">
		       <xsl:attribute name="text-align">
		       <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
				   <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
			    </xsl:when>
			    <xsl:otherwise>
				   <xsl:value-of select="'left'"/>
			    </xsl:otherwise>
			</xsl:choose>
			  </xsl:attribute>
			 <fo:external-graphic>
			   <xsl:attribute name="src">
			     <xsl:value-of select="parent::node()/preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
			   </xsl:attribute>
			 </fo:external-graphic>
			</fo:block>
		</xsl:if>
		<fo:block padding-top="20pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
			  <xsl:attribute name="font-size">
			   <xsl:value-of select="$fontsize"/>
			  </xsl:attribute>
			  <xsl:apply-templates/>
			</fo:block>
	      </xsl:if>
	</xsl:for-each>

	<!-- Use Graphic from the Stimulus Element (to be consistent with the swf templates)-->
	<xsl:if test="not(P[1]) and preceding-sibling::Stimulus/Graphic">
	       <fo:block padding-top="15pt" padding-bottom="10pt" start-indent="0.5in" white-space-collapse="false">
	       <xsl:attribute name="text-align">
	       <xsl:choose>
	            <xsl:when test="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position and preceding-sibling::Stimulus/Graphic/EPSPrint/@Position != 'Here'">
	       		   <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@Position"/>
	       	    </xsl:when>
	       	    <xsl:otherwise>
	       	    	   <xsl:value-of select="'left'"/>
	       	    </xsl:otherwise>
	       	</xsl:choose>
		  </xsl:attribute>
		 <fo:external-graphic>
		   <xsl:attribute name="src">
		     <xsl:value-of select="preceding-sibling::Stimulus/Graphic/EPSPrint/@FileName"/>
		   </xsl:attribute>
		 </fo:external-graphic>
		</fo:block>
	</xsl:if>
	</fo:list-item-body>
  </xsl:when>
  
   <xsl:when test="parent::node()/@Template='28' or parent::node()/@Template='TB28'">
     <fo:list-item-body>
     	   <xsl:for-each select="P">
	       <xsl:if test="position() = 1">
		 <fo:block padding-top="18pt" start-indent="0.5in" font-family="any" font-weight="bold" padding-bottom="10pt" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		     <xsl:value-of select="$fontsize"/>
		   </xsl:attribute>
		   	<xsl:apply-templates/>
		 </fo:block>
	       </xsl:if>

	       <xsl:if test="position() = 2">
		 <fo:block padding-bottom="10pt" padding-top="6pt" start-indent="0.5in" font-family="any" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
		  	<xsl:apply-templates/>
		 </fo:block>
	       </xsl:if>

	      <xsl:if test="position() = 3">
		<fo:block padding-bottom="10pt" start-indent="0.5in" font-family="any" padding-top="6pt" font-weight="bold" white-space-collapse="false">
		  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
		  </xsl:attribute>
			<xsl:apply-templates/>
		</fo:block>
	      </xsl:if>
	   </xsl:for-each>
     </fo:list-item-body>
  </xsl:when>

  </xsl:choose>
  </xsl:when>

  <xsl:otherwise>
	<xsl:if test="$isCR">
		<fo:list-item-body/>
	</xsl:if>
  </xsl:otherwise>
  </xsl:choose>
  </fo:list-item>
  </fo:list-block>
  </xsl:template>



<xsl:template match="Assessment">
      <xsl:for-each select="SubTest">
            <xsl:apply-templates/>
          </xsl:for-each>
  </xsl:template>

<xsl:template match="SubTest">
    <xsl:for-each select="ItemSet">
      <xsl:apply-templates/>
    </xsl:for-each>
  </xsl:template>

  <!-- Student Directions template. DO NOT MODIFY TABS and WHITESPACE for the text as whitespace is recognized -->
  <xsl:template match="StudentDirections">
        <xsl:choose>
      <xsl:when test="$isCR=true() and not(following-sibling::Stimulus[1][@ID] = preceding::Stimulus[@ID])">
              <fo:block padding-top="15pt" font-family="any" font-weight="bold" white-space-collapse="false" text-align="start">
			 <xsl:attribute name="font-size">
					<xsl:value-of select="$fontsize"/>
			</xsl:attribute>Student Directions:
<xsl:apply-templates/>
				  </fo:block>
      </xsl:when>
	<xsl:when test="$isCR=false() or parent::node()/@ItemType='CR'">
    <xsl:choose>
    <!-- if DisplayAlways is Yes, SD should be displayed and disregard stimulus logic -->
         <xsl:when test="@DisplayAlways='Yes'">
              <fo:block padding-top="15pt" font-family="any" font-weight="bold" white-space-collapse="false" text-align="start">
			 <xsl:attribute name="font-size">
					<xsl:value-of select="$fontsize"/>
			</xsl:attribute>Student Directions:
<xsl:apply-templates/>
				  </fo:block>
	 </xsl:when>
	 <xsl:otherwise>
	    <xsl:variable name="thisStim">
	       <xsl:choose>
			  <xsl:when test="following-sibling::Stimulus/@ID='' or not (parent::node()/preceding-sibling::Item[1]/Stimulus/@ID)">
			       <xsl:value-of select="'empty'"/>
			  </xsl:when>
			  <xsl:otherwise>
				<xsl:value-of select="following-sibling::Stimulus/@ID"/>
			  </xsl:otherwise>
		</xsl:choose>
	    </xsl:variable>

		  <xsl:variable name="beforeStim">
			    <xsl:choose>
			    <xsl:when test="parent::node()/preceding-sibling::Item[1]">
			       <xsl:choose>
				  <xsl:when test="parent::node()/preceding-sibling::Item[1]/Stimulus/@ID='' or not (parent::node()/preceding-sibling::Item[1]/Stimulus/@ID)">
				       <xsl:value-of select="'prev_empty'"/>
				  </xsl:when>
				  <xsl:otherwise>
					<xsl:value-of select="parent::node()/preceding-sibling::Item[1]/Stimulus/@ID"/>
				  </xsl:otherwise>
				</xsl:choose>
			    </xsl:when>
			    <xsl:otherwise>
				  <xsl:value-of select="'no_prev_item'"/>
			    </xsl:otherwise>
			    </xsl:choose>
		    </xsl:variable>

		  <xsl:variable name="bebeforeStim">
			  <xsl:choose>
			  <xsl:when test="parent::node()/preceding-sibling::Item[2]">
			       <xsl:choose>
			          <xsl:when test="following-sibling::Stimulus/@ID='' or not (parent::node()/preceding-sibling::Item[1]/Stimulus/@ID)">
				       <xsl:value-of select="'prev_prev_empty'"/>
				  </xsl:when>
				  <xsl:otherwise>
					<xsl:value-of select="parent::node()/preceding-sibling::Item[2]/Stimulus/@ID"/>
				  </xsl:otherwise>
				</xsl:choose>
			  </xsl:when>
			  <xsl:otherwise>
				  <xsl:value-of select="'no_prev_prev_item'"/>
			  </xsl:otherwise>
			  </xsl:choose>
		   </xsl:variable>
		   <!-- Use for testing the variables >
		<fo:block >
		GRADE:	<xsl:value-of select="/ItemSet/Item/Hierarchy[@Type='Grade'][1]/@Name"/>
		   GOT: bebeforestim: <xsl:value-of select="$bebeforeStim"/>
		   GOT: beforestim: <xsl:value-of select="$beforeStim"/>
		   GOT: thisstim: <xsl:value-of select="$thisStim"/>
		  </fo:block-->
		   <xsl:choose>
			<!-- checks stimulus of previous 2 items. if different-->
			<xsl:when test="$bebeforeStim != $beforeStim and ($bebeforeStim != 'no_prev_prev_item' or $beforeStim != 'no_prev_item')">
				<!-- if previous item and current item stimuli are different-->
				<xsl:if test="$thisStim != $beforeStim">
				     <xsl:if test="parent::Item/@Template='4' or parent::Item/@Template='8' or parent::Item/@Template='TB8'">
					  <xsl:if test="parent::node()/preceding-sibling::Item[1]">
						<fo:block break-before="page"/>
					  </xsl:if>
				     </xsl:if>
					<!-- This item is also a discrete item, so check text of SD to suppress if they are the same-->
					<xsl:variable name="currDir">
					     <xsl:value-of select="."/>
					</xsl:variable>

					<xsl:variable name="prevDir">

						<xsl:choose>
							<xsl:when test="parent::node()/preceding-sibling::Item[1]">
								<xsl:value-of select="parent::node()/preceding-sibling::Item[1]/StudentDirections"/>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="nodirections"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:variable>
					<xsl:choose>
						<!-- show SD if previous item and current item stimuli are different (discrete items) and student directions are different -->
						<xsl:when test="$currDir != $prevDir">
						  <fo:block padding-top="15pt" font-family="any" font-weight="bold" white-space-collapse="false" text-align="start">
							 <xsl:attribute name="font-size">
								<xsl:value-of select="$fontsize"/>
							</xsl:attribute>Student Directions:
<xsl:apply-templates/></fo:block>
					 	</xsl:when>
					 	<!-- show SD if next item uses the same stimulus (group material) -->
						<xsl:when test="parent::node()/following-sibling::Item[1]">
							    <xsl:variable name="nextStim">
							       <xsl:choose>
									  <xsl:when test="parent::node()/following-sibling::Item[1]/Stimulus/@ID='' or not (parent::node()/following-sibling::Item[1]/Stimulus/@ID)">
									       <xsl:value-of select="'next_empty'"/>
									  </xsl:when>
									  <xsl:otherwise>
										<xsl:value-of select="parent::node()/following-sibling::Item[1]/Stimulus/@ID"/>
									  </xsl:otherwise>
								</xsl:choose>
	    						     </xsl:variable>
	    						  <xsl:if test="$thisStim = $nextStim and $thisStim !='empty'">
							  <fo:block padding-top="15pt" font-family="any" font-weight="bold" white-space-collapse="false" text-align="start">
								 <xsl:attribute name="font-size">
									<xsl:value-of select="$fontsize"/>
							</xsl:attribute>Student Directions:
<xsl:apply-templates/></fo:block>
							  </xsl:if>
					 	</xsl:when>
					</xsl:choose>
				</xsl:if>

			</xsl:when>
			<xsl:otherwise>
			<!-- previous 2 items use the same stimulus, if this item is different, show student directions-->
				<xsl:if test="$thisStim != $beforeStim and $beforeStim != 'prev_empty'">
					<!-- This item is separate from the previous item, so check text of SD to suppress if they are the same>
							<xsl:variable name="currDir">
							     <xsl:value-of select="." />
							</xsl:variable>

							<xsl:variable name="prevDir">

								<xsl:choose>
									<xsl:when test="parent::node()/preceding-sibling::Item[1]">
										<xsl:value-of select="parent::node()/preceding-sibling::Item[1]/StudentDirections"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:value-of select="nodirections"/>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:variable>

					<xsl:if test="$currDir != $prevDir"-->
				     	     <xsl:if test="parent::Item/@Template='4' or parent::Item/@Template='8' or parent::Item/@Template='TB8'">
						  <xsl:if test="parent::node()/preceding-sibling::Item[1]">
							<fo:block break-before="page"/>
						  </xsl:if>
					     </xsl:if>
						  <fo:block padding-top="15pt" font-family="any" font-weight="bold" white-space-collapse="false" text-align="start">
							 <xsl:attribute name="font-size">
								<xsl:value-of select="$fontsize"/>
							 </xsl:attribute>Student Directions:
<xsl:apply-templates/>
						  </fo:block>
					</xsl:if>
				<!--/xsl:if-->
			</xsl:otherwise>
		    </xsl:choose>
		</xsl:otherwise>
		</xsl:choose>
      </xsl:when>
        </xsl:choose>
  </xsl:template>


     <xsl:template match="Stimulus">
<!--if Stimulus is repeated, redundant Stimulus elements will not show in the
print output-->
 <!-- don't display stimulus here for templates 2, 3, 5, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16, 17, and 19, 20 -->
 <xsl:if test="
parent::node()/@Template='0' or
parent::node()/@Template='TB0' or
parent::node()/@Template='1' or
parent::node()/@Template='TB1' or
parent::node()/@Template='4' or
parent::node()/@Template='8' or
parent::node()/@Template='TB8' or
parent::node()/@Template='18' or
parent::node()/@Template='CR1' or
parent::node()/@Template='CR2' or
parent::node()/@Template='CR3' or
parent::node()/@Template='CR4' or
parent::node()/@Template='CR5' or
parent::node()/@Template='CR6' or
(
  (
  parent::node()/@Template='9' or
  parent::node()/@Template='TB9' or
  parent::node()/@Template='10' or
  parent::node()/@Template='TB10'
  )
  and
  (//Item/Stimulus[@ID=current()/@ID][@dedupe='yes'])
  and
  (following::Item[Stimulus/@ID = current()/@ID])
)">
	  <xsl:variable name="currStim" select="@ID"/>

	  <xsl:variable name="prevStim">
          <xsl:choose>
          <xsl:when test="$isCR and
          (parent::node()/preceding-sibling::Item[1])">
              <xsl:value-of
              select="parent::node()/preceding-sibling::Item[ConstructedResponse][1]/Stimulus/@ID"/>
          </xsl:when>
          <xsl:when test="parent::node()/preceding-sibling::Item[1]">
              <xsl:value-of select="parent::node()/preceding-sibling::Item[1]/Stimulus/@ID"/>
          </xsl:when>
          <xsl:otherwise>
	          <xsl:value-of select="none"/>
          </xsl:otherwise>
          </xsl:choose>
          </xsl:variable>

 <!-- below logic: if the Stimulus of the prior Item is different than the
 current Stimulus, then print the current Stimulus-->
          <xsl:if test="$currStim != $prevStim">
	    <xsl:apply-templates/>
	  </xsl:if>
	</xsl:if>
    </xsl:template>

    <xsl:template match="Heading">
    <!--Code will apply following fo:block attribute values to all "Heading" tags-->
        <xsl:variable name="headingcheck">
           <xsl:value-of select="."/>
    	</xsl:variable>
    <xsl:if test="$headingcheck!='' and $headingcheck!=' ' and $headingcheck!='  '">
      <fo:block font-family="any" font-weight="bold" text-align="center" space-before.optimum="1cm" keep-together="always" white-space-collapse="false">
	<xsl:attribute name="font-size">
	 <xsl:value-of select="$headingfont"/>
	</xsl:attribute>
       <xsl:apply-templates/>
      </fo:block>
    </xsl:if>
  </xsl:template>

    <xsl:template match="Passage">
    <!--Code will apply following fo:block attribute values to all "Passage" tags-->
      <fo:block font-family="any" space-before.optimum=".5cm" white-space-collapse="false">
        <xsl:attribute name="font-size">
       	 <xsl:value-of select="$fontsize"/>
        </xsl:attribute>
        <xsl:apply-templates/>
      </fo:block>
    </xsl:template>

    <xsl:template match="StudentDirections/P">
    <!-- don't want block formatting within StudentDirections -->
      <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="P">
    <!--Code will apply following fo:block attribute values to all "P" tags-->
      <fo:block font-family="any" start-indent="0.5in" end-indent="0.5in" white-space-collapse="false">
        <xsl:attribute name="font-size">
        <xsl:choose>
   	<xsl:when test="parent::Acknowledgement">
       		<xsl:value-of select="'8pt'"/>
        </xsl:when>
        <xsl:otherwise>
        	<xsl:value-of select="$fontsize"/>
        </xsl:otherwise>
        </xsl:choose>
        </xsl:attribute>

        <xsl:attribute name="space-before.optimum">
        <xsl:choose>
            <xsl:when test="parent::Heading">
		<xsl:choose>
			<xsl:when test="position() = 1">
			     <xsl:value-of select="'0cm'"/>
			</xsl:when>
			<xsl:otherwise>
		                <xsl:value-of select="'0.5cm'"/>
		        </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
        <xsl:otherwise>
                <xsl:value-of select="'0.5cm'"/>
        </xsl:otherwise>
        </xsl:choose>
        </xsl:attribute>
         <xsl:attribute name="text-align">
           	<xsl:choose>
        		<xsl:when test="parent::Heading">
				<xsl:value-of select="'center'"/>
			</xsl:when>
                	<xsl:otherwise>
			<xsl:choose>
				<xsl:when test="@ALIGN='CENTER' or @ALIGN='center'">
                      				<xsl:value-of select="'center'"/>
				</xsl:when>
                                <xsl:when test="@ALIGN='LEFT' or @ALIGN='left'">
                                               <xsl:value-of select="'left'"/>
                                </xsl:when>
                                <xsl:when test="@ALIGN='RIGHT' or @ALIGN='right'">
                                               <xsl:value-of select="'right'"/>
                                </xsl:when>
				<xsl:otherwise>
					 <xsl:value-of select="'left'"/>
				</xsl:otherwise>
			</xsl:choose>
                	</xsl:otherwise>
            </xsl:choose>
     </xsl:attribute><xsl:apply-templates/></fo:block>
    </xsl:template>


    <xsl:template match="Comments"/>
    <xsl:template match="Comment"/>

    <xsl:template match="BR">
    <!--Code will apply following fo:block attribute values to all "BR"(break) tags-->
        <xsl:choose>
             <xsl:when test="@Type='Page'">
                <fo:block break-before="page"/>
             </xsl:when>
             <xsl:otherwise>
                 <fo:block/>
                <!--xsl:if test="preceding-sibling::*[1]/self::BR1"> <fo:block><fo:leader/></fo:block> </xsl:if-->
             </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

  <xsl:template match="B">
  <!--Code will apply following fo:inline attribute values to all "B"(bold) tags-->
    <fo:inline font-weight="bold">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  

  <xsl:template match="Acknowledgement">
    <xsl:apply-templates/>
    <xsl:if test=" (ancestor::Item[@Template='8'] or ancestor::Item[@Template='TB8']) and (string-length(normalize-space($passage_graphic_id)) &gt; 0) and (not(following-sibling::Graphic[@ID=$passage_graphic_id])) and (//*[   string-length(normalize-space(@graphic-before)) &gt; 0   or   string-length(normalize-space(@graphic-after)) &gt; 0   ])     ">


      <fo:block start-indent="0.5in" padding-top="20pt" padding-bottom="6pt" text-align="center">
        <fo:external-graphic src="{//Graphic[@ID=$passage_graphic_id]/EPSPrint/@FileName}">
        </fo:external-graphic>
      </fo:block>
    </xsl:if>
  </xsl:template>

    

  <xsl:template match="Para">
  <!--Code will apply following fo:block attribute values to all "Para" tags.
  This <Para> tag should not be in any R2 xml, but is represented in the XSL
  just in case this tag filters through-->
    <fo:inline font-family="any" start-indent="0.5in">
       <xsl:attribute name="font-size">
       	<xsl:value-of select="$fontsize"/>
         </xsl:attribute>
	<xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="Line">
        <xsl:variable name="indent">
           <xsl:choose>
           <xsl:when test="@IndentLevel">
           <xsl:value-of select="@IndentLevel"/>
           </xsl:when>
           <xsl:otherwise>
                <xsl:value-of select="'0'"/>
           </xsl:otherwise>
           </xsl:choose>
        </xsl:variable>
    <!--Code will display the Line-->
     <fo:block font-family="any" end-indent="0.5in" white-space-collapse="false">
             <xsl:attribute name="font-size">
                <xsl:value-of select="$fontsize"/>
               </xsl:attribute>
               <xsl:attribute name="start-indent">
                        <xsl:value-of select="concat(($indent + 0.5), 'in')"/>
               </xsl:attribute>
               <xsl:attribute name="padding-top">
                        <!--xsl:value-of select="$fontsize"/-->
                        <xsl:value-of select="concat(((substring-before($fontsize, 'pt')) * 1 ), 'pt')"/>
                      </xsl:attribute>
        <xsl:apply-templates/>
    </fo:block>
  </xsl:template>

  <!--The Graphic template provides a catch-all for Stimulus graphics. The Graphic
  Tempalte does not have height and width defined, therefore will render images
  in their native size-->

      
      <xsl:template match="Graphic"> 
        <xsl:choose>
          <xsl:when test="(preceding-sibling::Graphic) and (//Stimulus[@ID = current()/parent::Stimulus/@ID]/@dedupe='yes') and (ancestor::*[@Template]/@Template='10' or ancestor::*[@Template]/@Template='TB10')"/>
          <xsl:otherwise>
            
        

        <xsl:variable name="padding_bottom">
          <xsl:choose>
            <xsl:when test="(//Stimulus[@ID = current()/parent::Stimulus/@ID]/@dedupe='yes') and (ancestor::*[@Template]/@Template='10' or ancestor::*[@Template]/@Template='TB10')">20pt</xsl:when>
            <xsl:otherwise>6pt</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>
      <fo:block padding-top="20pt" start-indent="0.5in" padding-bottom="{$padding_bottom}">
      
            <xsl:attribute name="text-align">
	       <xsl:choose>
		    <xsl:when test="parent::node()/parent::Item/@Template='8' or parent::node()/parent::Item/@Template='TB8'">
			   <xsl:value-of select="'center'"/>
		    </xsl:when>
		    <xsl:when test="EPSPrint/@Position='Center'">
			   <xsl:value-of select="'center'"/>
		    </xsl:when>
		    <xsl:when test="EPSPrint/@Position='Right'">
			   <xsl:value-of select="'right'"/>
		    </xsl:when>
		    <xsl:otherwise>
			   <xsl:value-of select="'left'"/>
		    </xsl:otherwise>
		</xsl:choose>
	    </xsl:attribute>
          <fo:external-graphic>
            <xsl:attribute name="src">
              <xsl:value-of select="EPSPrint/@FileName"/>
            </xsl:attribute>
          </fo:external-graphic>
        
      </fo:block>
    
       
          </xsl:otherwise>
        </xsl:choose>
      </xsl:template>
    

  <xsl:template match="SelectedResponse">
  <!--defines and applies the various 20 SelectedResponse templates that can be rendered
  in the XML-->
        <xsl:if test="not($isCR) or parent::node()/@ItemType='CR'">

    <xsl:choose>

      <xsl:when test="parent::node()/@Template='0' or parent::node()/@Template='TB0' or parent::node()/@Template='2' or parent::node()/@Template='4' or parent::node()/@Template='8' or parent::node()/@Template='TB8' or parent::node()/@Template='9' or parent::node()/@Template='TB9' or parent::node()/@Template='12' or parent::node()/@Template='TB12' or parent::node()/@Template='12A' or parent::node()/@Template='14' or parent::node()/@Template='15' or parent::node()/@Template='17' or parent::node()/@Template='TB17' or parent::node()/@Template='18' or parent::node()/@Template='19' or parent::node()/@Template='24' or  parent::node()/@Template='27'">
         <fo:list-block>
	     <xsl:for-each select="AnswerChoice">
	     <fo:list-item>

	       <fo:list-item-label>
	         <fo:block font-family="any" font-weight="bold" padding-top="10pt" padding-bottom="4pt" white-space-collapse="false" start-indent="0.5in">
	       	   <xsl:attribute name="font-size">
	   	    <xsl:value-of select="$fontsize"/>
	           </xsl:attribute>
	       	   <xsl:number format="(A)      "/>
	         </fo:block>
	       </fo:list-item-label>

	      <fo:list-item-body>
	        <fo:block font-family="any" padding-top="10pt" padding-bottom="4pt" white-space-collapse="false" start-indent="1in">
	       	  <xsl:attribute name="font-size">
		    <xsl:value-of select="$fontsize"/>
                  </xsl:attribute>
	          <xsl:apply-templates/>
	        </fo:block>
	      </fo:list-item-body>

	   </fo:list-item>
	 </xsl:for-each>
	</fo:list-block>
	<fo:block padding-bottom="20pt"/>
       </xsl:when>

      <xsl:when test="parent::node()/@Template='1' or parent::node()/@Template='TB1' or parent::node()/@Template='3' or parent::node()/@Template='TB3' or parent::node()/@Template='5' or parent::node()/@Template='TB5' or parent::node()/@Template='7' or parent::node()/@Template='23'">
        <xsl:choose>
          <xsl:when test="@layout='two_rows_two_columns'">
            <xsl:call-template name="two_rows_two_columns">
              <xsl:with-param name="first_column_width_plus" select="@first-column-width-plus"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            
      <fo:block padding-bottom="10pt" padding-top="10pt" start-indent=".5in" white-space-collapse="false">
            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (A)    </fo:inline>
                    <fo:external-graphic>
                       <xsl:attribute name="src">
                         <xsl:value-of select="AnswerChoice[position() = 1]/Graphic/EPSPrint/@FileName"/>
                       </xsl:attribute>
               </fo:external-graphic>
            </fo:inline>

            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (C)    </fo:inline>

                    <fo:external-graphic>
                       <xsl:attribute name="src">
                         <xsl:value-of select="AnswerChoice[position() = 3]/Graphic/EPSPrint/@FileName"/>
                       </xsl:attribute>
               </fo:external-graphic>
            </fo:inline>
            </fo:block>

          <fo:block padding-bottom="20pt" padding-top="10pt" start-indent="0.5in" white-space-collapse="false">
            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (B)    </fo:inline>
                    <fo:external-graphic>
                       <xsl:attribute name="src">
                         <xsl:value-of select="AnswerChoice[position() = 2]/Graphic/EPSPrint/@FileName"/>
                       </xsl:attribute>
               </fo:external-graphic>
            </fo:inline>

            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (D)    </fo:inline>
                    <fo:external-graphic>
                       <xsl:attribute name="src">
                         <xsl:value-of select="AnswerChoice[position() = 4]/Graphic/EPSPrint/@FileName"/>
                       </xsl:attribute>
               </fo:external-graphic>
            </fo:inline>
          </fo:block>

      <!--fo:block start-indent=".3in">
       <fo:inline padding-bottom="20pt" padding-top="20pt">
       <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (A) </fo:inline>
         <fo:external-graphic>
   	   <xsl:attribute name="src">
   	     <xsl:value-of select="AnswerChoice[position() = 1]/Graphic/EPSPrint/@FileName"/>
   	   </xsl:attribute>
         </fo:external-graphic>
       </fo:inline>

       <fo:inline  padding-bottom="20pt" padding-top="20pt">
       <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (B) </fo:inline>
         <fo:external-graphic>
   	   <xsl:attribute name="src">
   	     <xsl:value-of select="AnswerChoice[position() = 2]/Graphic/EPSPrint/@FileName"/>
   	   </xsl:attribute>
         </fo:external-graphic>
       </fo:inline>

       <fo:inline  padding-bottom="20pt" padding-top="20pt">
       <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (C) </fo:inline>
         <fo:external-graphic>
   	   <xsl:attribute name="src">
   	    <xsl:value-of select="AnswerChoice[position() = 3]/Graphic/EPSPrint/@FileName"/>
   	   </xsl:attribute>
         </fo:external-graphic>
       </fo:inline>

       <fo:inline padding-bottom="20pt" padding-top="20pt">
       <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (D) </fo:inline>
         <fo:external-graphic>
   	   <xsl:attribute name="src">
   	    <xsl:value-of select="AnswerChoice[position() = 4]/Graphic/EPSPrint/@FileName"/>
   	   </xsl:attribute>
         </fo:external-graphic>
       </fo:inline>
      </fo:block-->
     
          </xsl:otherwise>
        </xsl:choose> 
      </xsl:when>

  <xsl:when test="parent::node()/@Template='6' or parent::node()/@Template='TB6' or parent::node()/@Template='22' or parent::node()/@Template='TB22' or parent::node()/@Template='25' or parent::node()/@Template='26' or parent::node()/@Template='TB26' or parent::node()/@Template='28' or parent::node()/@Template='TB28'">
     <xsl:if test="AnswerChoice/Graphic">
     <fo:list-block>
      <fo:list-item>
   	<fo:list-item-label>
   	  <fo:block font-family="any" font-weight="bold" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false" start-indent="0.5in">
   	    <xsl:attribute name="font-size">
   	    <xsl:value-of select="$fontsize"/>
   	    </xsl:attribute>(A)   </fo:block>
   	</fo:list-item-label>
   	<fo:list-item-body>
   	  <fo:block start-indent="1in" padding-top="5pt" padding-bottom="10pt" white-space-collapse="false">
   	    <fo:external-graphic>
   	      <xsl:attribute name="src">
   		<xsl:value-of select="AnswerChoice[position() = 1]/Graphic/EPSPrint/@FileName"/>
   	      </xsl:attribute>
   	    </fo:external-graphic>
   	  </fo:block>
   	</fo:list-item-body>
      </fo:list-item>

      <fo:list-item>
   	<fo:list-item-label>
   	  <fo:block font-family="any" font-weight="bold" padding-bottom="10pt" padding-top="10pt" white-space-collapse="false" start-indent="0.5in">
   	    <xsl:attribute name="font-size">
   	      <xsl:value-of select="$fontsize"/>
   	    </xsl:attribute>(B)   </fo:block>
   	</fo:list-item-label>
   	<fo:list-item-body>
   	  <fo:block padding-bottom="10pt" padding-top="5pt" start-indent="1in" white-space-collapse="false">
   	    <fo:external-graphic>
   	      <xsl:attribute name="src">
   	       	<xsl:value-of select="AnswerChoice[position() = 2]/Graphic/EPSPrint/@FileName"/>
   	      </xsl:attribute>
   	    </fo:external-graphic>
   	  </fo:block>
   	</fo:list-item-body>
      </fo:list-item>

      <fo:list-item>
   	<fo:list-item-label>
   	  <fo:block font-family="any" font-weight="bold" padding-bottom="10pt" padding-top="10pt" white-space-collapse="false" start-indent="0.5in">
   	    <xsl:attribute name="font-size">
   	      <xsl:value-of select="$fontsize"/>
   	    </xsl:attribute>(C)   </fo:block>
   	</fo:list-item-label>
   	<fo:list-item-body>
   	  <fo:block start-indent="1in" padding-bottom="10pt" padding-top="5pt" white-space-collapse="false">
   	    <fo:external-graphic>
   	     <xsl:attribute name="src">
   	       <xsl:value-of select="AnswerChoice[position() = 3]/Graphic/EPSPrint/@FileName"/>
   	     </xsl:attribute>
   	    </fo:external-graphic>
   	  </fo:block>
   	</fo:list-item-body>
      </fo:list-item>

      <fo:list-item>
   	<fo:list-item-label>
   	  <fo:block font-family="any" font-weight="bold" padding-bottom="20pt" padding-top="10pt" white-space-collapse="false" start-indent="0.5in">
   	    <xsl:attribute name="font-size">
   	     <xsl:value-of select="$fontsize"/>
   	    </xsl:attribute>(D)   </fo:block>
   	</fo:list-item-label>
   	<fo:list-item-body>
   	  <fo:block start-indent="1in" padding-bottom="10pt" padding-top="5pt" white-space-collapse="false">
   	    <fo:external-graphic>
   	      <xsl:attribute name="src">
   	       	<xsl:value-of select="AnswerChoice[position() = 4]/Graphic/EPSPrint/@FileName"/>
   	      </xsl:attribute>
   	    </fo:external-graphic>
   	  </fo:block>
   	</fo:list-item-body>
      </fo:list-item>
    </fo:list-block>
    </xsl:if>

    <xsl:if test="not(AnswerChoice/Graphic)">
        <fo:list-block>
             <xsl:for-each select="AnswerChoice">
             <fo:list-item>
               <fo:list-item-label>
                 <fo:block font-family="any" font-weight="bold" padding-bottom="4pt" white-space-collapse="false" start-indent="0.5in">
               	 <xsl:attribute name="font-size">
           	 <xsl:value-of select="$fontsize"/>
                 </xsl:attribute>
               	 <xsl:number format="(A)      "/>
               </fo:block>
             </fo:list-item-label>
             <fo:list-item-body>
               <fo:block font-family="any" padding-bottom="4pt" white-space-collapse="false" start-indent="1in">
               	  <xsl:attribute name="font-size">
        	   <xsl:value-of select="$fontsize"/>
                 </xsl:attribute>
                 <xsl:apply-templates/>
               </fo:block>
             </fo:list-item-body>
           </fo:list-item>
         </xsl:for-each>
       </fo:list-block>
	<fo:block padding-bottom="20pt"/>
     </xsl:if>
  </xsl:when>

  <xsl:when test="parent::node()/@Template='10' or parent::node()/@Template='TB10' or parent::node()/@Template='20' or parent::node()/@Template='TB20'">
     <xsl:if test="AnswerChoice/Graphic">
       <fo:list-block>
         <fo:list-item>
   	   <fo:list-item-label>
   	     <fo:block font-family="any" font-weight="bold" padding-top="10pt" padding-bottom="10pt" white-space-collapse="false" start-indent="0.5in">
   	       <xsl:attribute name="font-size">
   	         <xsl:value-of select="$fontsize"/>
   	       </xsl:attribute>(A)   </fo:block>
   	   </fo:list-item-label>
   	   <fo:list-item-body>
   	     <fo:block start-indent="1in" padding-top="10pt" padding-bottom="10pt">
   	       <fo:external-graphic>
   	         <xsl:attribute name="src">
   	       <xsl:value-of select="AnswerChoice[position() = 1]/Graphic/EPSPrint/@FileName"/>
   	     </xsl:attribute>
   	   </fo:external-graphic>
   	 </fo:block>
       </fo:list-item-body>
     </fo:list-item>

      <fo:list-item>
   	<fo:list-item-label>
   	  <fo:block font-family="any" font-weight="bold" padding-bottom="10pt" padding-top="10pt" white-space-collapse="false" start-indent="0.5in">
   	    <xsl:attribute name="font-size">
   	      <xsl:value-of select="$fontsize"/>
   	    </xsl:attribute>(B)   </fo:block>
   	</fo:list-item-label>
   	<fo:list-item-body>
   	  <fo:block start-indent="1in" padding-top="10pt" padding-bottom="10pt">
   	    <fo:external-graphic>
   	      <xsl:attribute name="src">
   	       	<xsl:value-of select="AnswerChoice[position() = 2]/Graphic/EPSPrint/@FileName"/>
   	      </xsl:attribute>
   	    </fo:external-graphic>
   	  </fo:block>
   	</fo:list-item-body>
      </fo:list-item>

      <fo:list-item>
   	<fo:list-item-label>
   	  <fo:block font-family="any" font-weight="bold" padding-bottom="10pt" padding-top="10pt" white-space-collapse="false" start-indent="0.5in">
   	    <xsl:attribute name="font-size">
   	      <xsl:value-of select="$fontsize"/>
   	    </xsl:attribute>(C)   </fo:block>
   	</fo:list-item-label>
   	<fo:list-item-body>
   	  <fo:block start-indent="1in" padding-top="10pt" padding-bottom="10pt">
   	    <fo:external-graphic>
   	     <xsl:attribute name="src">
   	       <xsl:value-of select="AnswerChoice[position() = 3]/Graphic/EPSPrint/@FileName"/>
   	     </xsl:attribute>
   	    </fo:external-graphic>
   	  </fo:block>
   	</fo:list-item-body>
      </fo:list-item>

      <fo:list-item>
   	<fo:list-item-label>
   	  <fo:block font-family="any" font-weight="bold" padding-bottom="20pt" padding-top="10pt" white-space-collapse="false" start-indent="0.5in">
   	    <xsl:attribute name="font-size">
   	     <xsl:value-of select="$fontsize"/>
   	    </xsl:attribute>(D)   </fo:block>
   	</fo:list-item-label>
   	<fo:list-item-body>
   	  <fo:block start-indent="1in" padding-top="10pt" padding-bottom="20pt">
   	    <fo:external-graphic>
   	      <xsl:attribute name="src">
   	       	<xsl:value-of select="AnswerChoice[position() = 4]/Graphic/EPSPrint/@FileName"/>
   	      </xsl:attribute>
   	    </fo:external-graphic>
   	  </fo:block>
   	</fo:list-item-body>
      </fo:list-item>
    </fo:list-block>
   </xsl:if>

   <xsl:if test="not(AnswerChoice/Graphic)">
     <fo:list-block>
       <xsl:for-each select="AnswerChoice">
         <fo:list-item>
           <fo:list-item-label>
             <fo:block font-family="any" font-weight="bold" padding-bottom="4pt" white-space-collapse="false" start-indent="0.5in">
               <xsl:attribute name="font-size">
       	         <xsl:value-of select="$fontsize"/>
               </xsl:attribute>
               <xsl:number format="(A)      "/>
             </fo:block>
           </fo:list-item-label>
           <fo:list-item-body>
             <fo:block font-family="any" padding-bottom="4pt" white-space-collapse="false" start-indent="1in">
              <xsl:attribute name="font-size">
    	        <xsl:value-of select="$fontsize"/>
              </xsl:attribute>
              <xsl:apply-templates/>
            </fo:block>
          </fo:list-item-body>
        </fo:list-item>
      </xsl:for-each>
    </fo:list-block>
  </xsl:if>
</xsl:when>



    <xsl:when test="parent::node()/@Template='11' or parent::node()/@Template='TB11'">
        <xsl:choose>
          <xsl:when test="@layout='one_row'">
            <xsl:call-template name="one_row"/>
          </xsl:when>
          <xsl:when test="@layout='two_rows_two_columns'">
            <xsl:call-template name="two_rows_two_columns">
              <xsl:with-param name="first_column_width_plus" select="@first-column-width-plus"/>
            </xsl:call-template>
          </xsl:when>
          <xsl:otherwise>
            
       <fo:block padding-bottom="10pt" padding-top="10pt" start-indent=".5in" white-space-collapse="false">
            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (A)    </fo:inline>
                    <fo:external-graphic>
       	               <xsl:attribute name="src">
       	                 <xsl:value-of select="AnswerChoice[position() = 1]/Graphic/EPSPrint/@FileName"/>
       	               </xsl:attribute>
               </fo:external-graphic>
            </fo:inline>

            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (C)    </fo:inline>
            <fo:inline>
                    <fo:external-graphic>
       	               <xsl:attribute name="src">
       	                 <xsl:value-of select="AnswerChoice[position() = 3]/Graphic/EPSPrint/@FileName"/>
       	               </xsl:attribute>
               </fo:external-graphic>
            </fo:inline> </fo:inline>
            </fo:block>

          <fo:block padding-bottom="20pt" padding-top="10pt" start-indent=".5in" white-space-collapse="false">
            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (B)    </fo:inline>
                    <fo:external-graphic>
       	               <xsl:attribute name="src">
       	                 <xsl:value-of select="AnswerChoice[position() = 2]/Graphic/EPSPrint/@FileName"/>
       	               </xsl:attribute>
               </fo:external-graphic>
            </fo:inline>

            <fo:inline>
            <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    (D)    </fo:inline>
                    <fo:external-graphic>
       	               <xsl:attribute name="src">
       	                 <xsl:value-of select="AnswerChoice[position() = 4]/Graphic/EPSPrint/@FileName"/>
       	               </xsl:attribute>
               </fo:external-graphic>
            </fo:inline>
          </fo:block>
   
          </xsl:otherwise>
        </xsl:choose> 
      </xsl:when>

   <xsl:when test="parent::node()/@Template='13' or parent::node()/@Template='16'">
     <xsl:if test="AnswerChoice/Graphic">
       <fo:list-block>
         <fo:list-item>
      	   <fo:list-item-label>
      	     <fo:block font-family="any" font-weight="bold" padding-bottom="20pt" white-space-collapse="false" start-indent="0.5in">
      	       <xsl:attribute name="font-size">
      	         <xsl:value-of select="$fontsize"/>
      	       </xsl:attribute>(A)   </fo:block>
      	     </fo:list-item-label>
      	     <fo:list-item-body>
      	       <fo:block start-indent="0.75in" padding-bottom="20pt">
      	         <fo:external-graphic>
      	           <xsl:attribute name="src">
      		     <xsl:value-of select="AnswerChoice[position() = 1]/Graphic/EPSPrint/@FileName"/>
      	           </xsl:attribute>
      	         </fo:external-graphic>
      	       </fo:block>
      	     </fo:list-item-body>
         </fo:list-item>

         <fo:list-item>
      	   <fo:list-item-label>
      	     <fo:block font-family="any" font-weight="bold" padding-bottom="20pt" white-space-collapse="false" start-indent="0.5in">
      	       <xsl:attribute name="font-size">
      	         <xsl:value-of select="$fontsize"/>
      	       </xsl:attribute>(B)   </fo:block>
      	   </fo:list-item-label>
      	   <fo:list-item-body>
      	     <fo:block start-indent="0.75in" padding-bottom="20pt">
      	         <fo:external-graphic>
      	           <xsl:attribute name="src">
      	           <xsl:value-of select="AnswerChoice[position() = 2]/Graphic/EPSPrint/@FileName"/>
      	         </xsl:attribute>
      	       </fo:external-graphic>
      	     </fo:block>
      	   </fo:list-item-body>
         </fo:list-item>

         <fo:list-item>
      	   <fo:list-item-label>
      	     <fo:block font-family="any" font-weight="bold" padding-bottom="20pt" white-space-collapse="false" start-indent="0.5in">
      	       <xsl:attribute name="font-size">
      	         <xsl:value-of select="$fontsize"/>
      	       </xsl:attribute>(C)   </fo:block>
           </fo:list-item-label>
      	   <fo:list-item-body>
      	     <fo:block start-indent="0.75in" padding-bottom="20pt">
      	         <fo:external-graphic>
      	         <xsl:attribute name="src">
      	           <xsl:value-of select="AnswerChoice[position() = 3]/Graphic/EPSPrint/@FileName"/>
      	         </xsl:attribute>
      	       </fo:external-graphic>
      	     </fo:block>
      	   </fo:list-item-body>
         </fo:list-item>

         <fo:list-item>
      	   <fo:list-item-label>
      	     <fo:block font-family="any" font-weight="bold" padding-bottom="20pt" white-space-collapse="false" start-indent="0.5in">
      	       <xsl:attribute name="font-size">
      	         <xsl:value-of select="$fontsize"/>
      	       </xsl:attribute>(D)   </fo:block>
      	   </fo:list-item-label>
      	  <fo:list-item-body>
      	    <fo:block start-indent="0.75in" padding-bottom="20pt">
      	      <fo:external-graphic>
      	        <xsl:attribute name="src">
      	          <xsl:value-of select="AnswerChoice[position() = 4]/Graphic/EPSPrint/@FileName"/>
      	        </xsl:attribute>
      	      </fo:external-graphic>
      	    </fo:block>
      	  </fo:list-item-body>
        </fo:list-item>

        <fo:list-item>
      	  <fo:list-item-label>
      	    <fo:block font-family="any" font-weight="bold" padding-bottom="20pt" white-space-collapse="false" start-indent="0.5in">
      	      <xsl:attribute name="font-size">
      	        <xsl:value-of select="$fontsize"/>
      	      </xsl:attribute>(E)   </fo:block>
      	    </fo:list-item-label>
      	  <fo:list-item-body>
      	  <fo:block start-indent="0.75in" padding-bottom="20pt">
      	      <fo:external-graphic>
      	      <xsl:attribute name="src">
      	       	<xsl:value-of select="AnswerChoice[position() = 5]/Graphic/EPSPrint/@FileName"/>
      	      </xsl:attribute>
      	    </fo:external-graphic>
      	  </fo:block>
      	</fo:list-item-body>
      </fo:list-item>
    </fo:list-block>
  </xsl:if>

  <xsl:if test="not(AnswerChoice/Graphic)">
    <fo:list-block>
      <xsl:for-each select="AnswerChoice">
        <fo:list-item>
          <fo:list-item-label>
            <fo:block font-family="any" font-weight="bold" padding-bottom="4pt" white-space-collapse="false" start-indent="0.5in">
              <xsl:attribute name="font-size">
              	 <xsl:value-of select="$fontsize"/>
              </xsl:attribute>
              <xsl:number format="(A)      "/>
            </fo:block>
          </fo:list-item-label>
          <fo:list-item-body>
            <fo:block font-family="any" padding-bottom="4pt" white-space-collapse="false" start-indent="1in">
              <xsl:attribute name="font-size">
           	<xsl:value-of select="$fontsize"/>
              </xsl:attribute>
              <xsl:apply-templates/>
            </fo:block>
          </fo:list-item-body>
        </fo:list-item>
      </xsl:for-each>
    </fo:list-block>
  </xsl:if>
</xsl:when>
</xsl:choose>
</xsl:if>
</xsl:template>


  <xsl:template match="AnswerArea">
  <!--defines and applies answerarea specified in the XML-->
        <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="BoxDirection">
      <fo:block padding-top="2pt" start-indent="0.6in" font-family="any" white-space-collapse="false" text-align="start">
                 <xsl:attribute name="font-size">
                                <xsl:value-of select="$fontsize"/>
                </xsl:attribute>
                <xsl:apply-templates/>
                <!--xsl:value-of select="BoxDirection"/-->
      </fo:block>
 </xsl:template>

<xsl:template match="BoxAnswer">
        <xsl:variable name="boxheight2">
           <xsl:value-of select="parent::node()/@LineCount"/>
        </xsl:variable>
                      <fo:block start-indent="0.6in" font-family="any" white-space-collapse="false" text-align="start">
                                 <xsl:attribute name="font-size">
                                                <xsl:value-of select="$fontsize"/>
                                </xsl:attribute>
                                <xsl:attribute name="padding-top">
                                                <xsl:value-of select="concat(((substring-before($fontsize, 'pt')) * $boxheight2), 'pt')"/>
                                </xsl:attribute>
                                <xsl:apply-templates/>
                                <!--xsl:value-of select="BoxAnswer"/-->
                      </fo:block>
</xsl:template>

  <xsl:template match="TextBox">
	<xsl:variable name="boxheight">
	   <xsl:value-of select="@LineCount"/>
	</xsl:variable>

        <fo:block start-indent="0.5in" space-before.optimal="0.5cm" margin-right="0.5in" text-align="left" padding="0pt" padding-bottom="0pt" border-width="1pt" border-style="solid" border-color="black">
            <xsl:if test="BoxDirection">
		<xsl:apply-templates select="BoxDirection"/>
	    </xsl:if>
            <xsl:choose>
                 <xsl:when test="BoxAnswer">
			<xsl:apply-templates select="BoxAnswer"/>
		</xsl:when>
		<xsl:otherwise>
	 	 	<fo:block start-indent="0.6in" font-family="any" font-weight="bold" white-space-collapse="false" text-align="start">
		 				 <xsl:attribute name="font-size">
		 						<xsl:value-of select="$fontsize"/>
		 				</xsl:attribute>
		 				<xsl:attribute name="padding-top">
		 						<xsl:value-of select="concat(((substring-before($fontsize, 'pt')) * $boxheight), 'pt')"/>
		 				</xsl:attribute>
		      </fo:block>
	 	 </xsl:otherwise>
	</xsl:choose>
      </fo:block>
      <fo:block padding-bottom="10pt" font-size="0pt" start-indent="0.6in" font-family="any" font-weight="bold" white-space-collapse="false" text-align="start">.        </fo:block>
  </xsl:template>

 <xsl:template match="Sup">
  <!--Code will apply following fo:block attribute values to all "Para" tags.
  This <Para> tag should not be in any R2 xml, but is represented in the XSL
  just in case this tag filters through-->
	<fo:inline font-size="10pt" vertical-align="super">
    	<xsl:value-of select="."/></fo:inline>
 </xsl:template>

 <xsl:template match="Sub">
  <!--Code will apply following fo:block attribute values to all "Para" tags.
  This <Para> tag should not be in any R2 xml, but is represented in the XSL
  just in case this tag filters through-->
        <fo:inline font-size="10pt" vertical-align="sub">
                <!--xsl:attribute name="font-size">
                        <xsl:value-of select="$fontsize"/>
                </xsl:attribute-->
        <xsl:apply-templates/>
    </fo:inline>
 </xsl:template>


<xsl:template match="ListItem">
      <fo:list-item>
        <fo:list-item-label>
                <fo:block start-indent="0.5in" font-family="any" white-space-collapse="false">
                  <xsl:attribute name="font-size">
                    <xsl:value-of select="$fontsize"/>
                  </xsl:attribute>
                        <fo:inline>&#187;  </fo:inline>
                </fo:block>
        </fo:list-item-label>
        <fo:list-item-body>
                <fo:block padding-top="1pt" start-indent="0.65in" font-weight="normal" font-family="any" white-space-collapse="false">
                  <xsl:attribute name="font-size">
                    <xsl:value-of select="$fontsize"/>
                  </xsl:attribute>
                        <xsl:apply-templates/>
                </fo:block>
        </fo:list-item-body>
        </fo:list-item>
  </xsl:template>

 <xsl:template match="UnorderedList">
   <fo:list-block>
                <xsl:apply-templates select="ListItem"/>
   </fo:list-block>
  </xsl:template>

  <xsl:template match="I">
  <!--Code will apply following fo:block attribute values to all "I"(Italic) tags-->
    <fo:inline font-family="any" font-style="italic" white-space-collapse="false">
       <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>



  <xsl:template name="placed_graphic">
    <xsl:param name="where"/>
    
    
    <xsl:variable name="which" select="1"/>
    
    <xsl:variable name="ID">
      <xsl:choose>
        <xsl:when test="$where='before'">
          <xsl:value-of select="@graphic-before"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="@graphic-after"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="referenced_graphic_ref" select="//Graphic [@ID=$ID] [count(preceding::Graphic[@ID=$ID])=($which - 1)]     "/>
    
    <xsl:variable name="padding_top">
      <xsl:choose>
        
        
        <xsl:when test="ancestor::Stem and following-sibling::node() and ( (parent::P and not(parent::P/preceding-sibling::P)) )">
          <xsl:text>0pt</xsl:text>
        </xsl:when>
        <xsl:when test="parent::Stem and not(preceding-sibling::*)">
          <xsl:text>20pt</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>20pt</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="start_indent">
      <xsl:choose>
        
        <xsl:when test="ancestor::Stem">
          <xsl:text>0pt</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>0.5in</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:variable name="padding_bottom">
      <xsl:choose>
        
        <xsl:when test="ancestor::Stem">
          <xsl:text>20pt</xsl:text>
        </xsl:when>
        <xsl:otherwise>
          <xsl:text>6pt</xsl:text>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <fo:block padding-top="{$padding_top}" padding-bottom="{$padding_bottom}" start-indent="{$start_indent}">

    
      <xsl:attribute name="text-align">
        <xsl:choose>
          <xsl:when test="$referenced_graphic_ref/parent::node()/parent::Item/@Template='8' or $referenced_graphic_ref/parent::node()/parent::Item/@Template='TB8'">
            <xsl:value-of select="'center'"/>
          </xsl:when>
          <xsl:when test="$referenced_graphic_ref/EPSPrint/@Position='Center'">
            <xsl:value-of select="'center'"/>
          </xsl:when>
          <xsl:when test="$referenced_graphic_ref/EPSPrint/@Position='Right'">
            <xsl:value-of select="'right'"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="'left'"/>
          </xsl:otherwise>
         </xsl:choose>
      </xsl:attribute>
      <fo:external-graphic>
        <xsl:attribute name="src">
          <xsl:value-of select="$referenced_graphic_ref/EPSPrint/@FileName"/>
        </xsl:attribute>
      </fo:external-graphic>
    </fo:block>

  </xsl:template>
      
    

      <xsl:template name="find_passage_graphic_id">
        <!-- currently: find most frequent graphic reference -->
        <!-- inefficient but short and simple -->
        <xsl:for-each select="//Graphic/@ID">
          <xsl:variable name="self_id" select="."/>
          <xsl:for-each select="//Graphic/@ID[. != $self_id]">
            <xsl:variable name="other_id" select="."/>
            
            <xsl:if test=" ( count(//Graphic/@ID[. = $self_id]) &gt; count(//Graphic/@ID[. = $other_id]) ) and (not ($self_id/parent::Graphic/preceding::Graphic[@ID=$self_id]))               ">
              <xsl:value-of select="$self_id"/>
            </xsl:if>
          </xsl:for-each>
        </xsl:for-each>
      </xsl:template>

    

      <xsl:template name="one_row">
        <fo:table table-layout="fixed" padding-top="0.1in" start-indent="0.5in" margin-bottom="20pt">
          <fo:table-column column-width="1.5in"/>
          <fo:table-column column-width="1.5in"/>
          <fo:table-column column-width="1.5in"/>
          <fo:table-column column-width="1.5in"/>
          <fo:table-body>
            <fo:table-row keep-together="always">
              <fo:table-cell>
                <fo:block text-align="center">
                  <fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 1]/ Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block text-align="center">
                  <fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 2]/ Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block text-align="center">
                  <fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 3]/ Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell>
                <fo:block text-align="center">
                  <fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 4]/ Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row keep-together="always">
              <fo:table-cell>
              <fo:block text-align="center" padding-top="0.2in">
                <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">(A)</fo:inline>
              </fo:block>
              </fo:table-cell>
              <fo:table-cell>
              <fo:block text-align="center" padding-top="0.2in">
                <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">(B)</fo:inline>
              </fo:block>
              </fo:table-cell>
              <fo:table-cell>
              <fo:block text-align="center" padding-top="0.2in">
                <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">(C)</fo:inline>
              </fo:block>
              </fo:table-cell>
              <fo:table-cell>
              <fo:block text-align="center" padding-top="0.2in">
                <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">(D)</fo:inline>
              </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
      </xsl:template>

    

      <xsl:template name="two_rows_two_columns">
        <xsl:param name="first_column_width_plus"/>
        <xsl:variable name="actual_first_column_width_plus">
          <xsl:choose>
            <xsl:when test="string-length(normalize-space($first_column_width_plus))&gt;0">
              <xsl:value-of select="$first_column_width_plus"/>
            </xsl:when>
            <xsl:otherwise>0in</xsl:otherwise>
          </xsl:choose>
        </xsl:variable>      
        <fo:table table-layout="fixed" width="100%">
          <fo:table-column column-width="proportional-column-width(1)+{$actual_first_column_width_plus}">
          </fo:table-column>
          <fo:table-column column-width="proportional-column-width(1)"/>
          <fo:table-body font-family="any" font-weight="bold" start-indent=".5in">
            <fo:table-row keep-together="always">
              <fo:table-cell margin-right="0in">
                <fo:block padding-top="10pt" padding-bottom="10pt">
                  <fo:inline>
                    <fo:inline white-space-collapse="false">    (A)    </fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 1]/Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell margin="0in">
                <fo:block padding-top="10pt" padding-bottom="10pt">
                  <fo:inline>
                    <fo:inline white-space-collapse="false">    (C)    </fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 3]/Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row keep-together="always">
              <fo:table-cell margin-right="0in">
                <fo:block padding-top="10pt" padding-bottom="20pt">
                  <fo:inline>
                    <fo:inline white-space-collapse="false">    (B)    </fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 2]/Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
              <fo:table-cell margin="0in">
                <fo:block padding-top="10pt" padding-bottom="20pt">
                  <fo:inline>
                    <fo:inline white-space-collapse="false">    (D)    </fo:inline>
                    <fo:external-graphic src="{AnswerChoice[position() = 4]/Graphic/EPSPrint/@FileName}">
                    </fo:external-graphic>
                  </fo:inline>
                </fo:block>
              </fo:table-cell>
            </fo:table-row>
          </fo:table-body>
        </fo:table>
      </xsl:template>
    
    </xsl:stylesheet>
