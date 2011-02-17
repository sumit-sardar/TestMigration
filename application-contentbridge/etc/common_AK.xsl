<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:include href="common.xsl"/>

<!--

FILENAME:
       common_AK.xsl

PURPOSE:
	Contains logic common to the 2 AK files

CHANGE HISTORY:
        Created during refactoring 4/27/2003 Edward Hieatt (edward@edwardh.com)
-->

<xsl:template match ="/">

<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

<!-- defines measurements for page layout. There are two page layouts, the
cover-page which is named "first" and test pages which named "simple" -->
<fo:layout-master-set>
<xsl:choose>
  <xsl:when test="self::node()=Assessment or self::node()=SubTest">

<!--defines document page order and layout for an Assessment and SubTest. These will
utilize the first, simple and last-page layouts-->

	<fo:page-sequence-master master-name="page-order">
	  <fo:repeatable-page-master-alternatives>
	    <fo:conditional-page-master-reference
	     page-position="first" master-reference="first"/>
	    <fo:conditional-page-master-reference
	     master-reference="simple"/>
	  </fo:repeatable-page-master-alternatives>
        </fo:page-sequence-master>

	     <fo:simple-page-master master-name="first"
			page-height="11in"
			page-width="8.5in"
			margin-top=".25in"
			margin-bottom="0.0in"
			margin-left=".25in"
			margin-right=".25in">
		<fo:region-body margin-top="1.5cm"
		 margin-bottom="2cm"/>
		<fo:region-before extent="1.5cm"/>
		<fo:region-after extent="2cm"/>
	     </fo:simple-page-master>

	     <fo:simple-page-master master-name="simple"
			page-height="11in"
			page-width="8.5in"
			margin-top="0.5in"
			margin-bottom="0.0in"
			margin-left=".5in"
			margin-right=".5in">
		<fo:region-body margin-top="1.5cm"
		 margin-bottom="4cm"/>
		<fo:region-before extent="3cm"/>
		<fo:region-after extent="3cm"/>
	     </fo:simple-page-master>

</xsl:when>

	<xsl:when test="self::node()=ItemSet or self::node()=Item">

<!--defines document page order and layout for an ItemSet and Item. These will only
utilize the simple page layout for possible preview-->

			<fo:page-sequence-master master-name="page-order">
			  <fo:repeatable-page-master-alternatives>
			    <fo:conditional-page-master-reference
			     master-reference="simple"/>
			  </fo:repeatable-page-master-alternatives>
		        </fo:page-sequence-master>

	     <fo:simple-page-master master-name="simple"
			page-height="11in"
			page-width="8.5in"
			margin-top="0.5in"
			margin-bottom="0.0in"
			margin-left=".5in"
			margin-right=".5in">
		<fo:region-body margin-top="1.5cm"
		 margin-bottom="4cm"/>
		<fo:region-before extent="3cm"/>
		<fo:region-after extent="3cm"/>
	     </fo:simple-page-master>
           </xsl:when>
       </xsl:choose>
     </fo:layout-master-set>

<!--Based on the root node different filepaths will have to be traversed to
reach attribute values that will dynamically populate page layouts. For
XML with Assessment as the root node with have to traverse Assessment first-->


 <xsl:if test="self::node()=Assessment">
<fo:page-sequence master-reference="first" language="en" country="us" force-page-count="no-force">

   <fo:static-content flow-name="xsl-region-before">
     <fo:block text-align="start" font-size="12pt" font-family="any">
     </fo:block>
   </fo:static-content>

   <fo:static-content flow-name="xsl-region-after">
	<xsl:call-template name="copyright">
                <xsl:with-param name="size" select="'4'"/>
	</xsl:call-template>
   </fo:static-content>

   <fo:flow flow-name="xsl-region-body">
     <fo:block font-size="47pt" font-family="sans-serif" space-after.optimum="6pt"
      text-align="center" padding-top="20pt">
      CTB/McGraw-Hill
     </fo:block>

     <fo:block font-size="42pt" font-family="sans-serif" space-after.optimum="6pt"
      text-align="center" padding-top="3pt">
       <xsl:value-of select="/Assessment/SubTest/@ContentArea"/> Test
     </fo:block>


     <fo:block font-size="42pt" font-family="sans-serif"
      space-after.optimum="6pt" text-align="center" padding-top="3pt">
      Grade:
       <xsl:value-of select="/Assessment/SubTest/@Grade" />
     </fo:block>

     <xsl:if test="/Assessment/SubTest/@Form != ''">
       <fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt"
         text-align="center" padding-top="3pt">
      Form: <xsl:value-of select="/Assessment/SubTest/@Form" />
       </fo:block>
       </xsl:if>

     <xsl:if test="Assessment/SubTest/@Title != ''">
	<xsl:call-template name="title"/>
        <xsl:choose>
		<xsl:when test="string-length(/Assessment/SubTest/@Title) >= 40">
		       <fo:block font-size="20pt" font-family="sans-serif" space-after.optimum="6pt"
			text-align="center" padding-top="3pt">
			<xsl:value-of select="/Assessment/SubTest/@Title" />
		       </fo:block>
		</xsl:when>
		<xsl:when test="string-length(/Assessment/SubTest/@Title) >= 30">
		       <fo:block font-size="24pt" font-family="sans-serif" space-after.optimum="6pt"
			text-align="center" padding-top="3pt">
			<xsl:value-of select="/Assessment/SubTest/@Title" />
		       </fo:block>
		</xsl:when>
		<xsl:otherwise>
			<fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt"
			text-align="center" padding-top="3pt">
			<xsl:value-of select="/Assessment/SubTest/@Title" />
		       </fo:block>
		</xsl:otherwise>
 	</xsl:choose>
     </xsl:if>
   </fo:flow>
</fo:page-sequence>
</xsl:if>

<!-- Traversing the document to attribute values for a SubTest XML will not
traverse an Assessment node because is wont exist.Below is the traversing
logic for a SubTest's "first" page layout-->

<xsl:if test="self::node()=SubTest">
  <fo:page-sequence master-reference="first" language="en" country="us" force-page-count="no-force">
    <fo:static-content flow-name="xsl-region-before">
      <fo:block text-align="start" font-size="12pt" font-family="any">
      </fo:block>
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
	<xsl:call-template name="copyright">
                <xsl:with-param name="size" select="'4'"/>
	</xsl:call-template>
    </fo:static-content>

    <fo:flow flow-name="xsl-region-body">
      <fo:block font-size="47pt" font-family="sans-serif" space-after.optimum="6pt"
       text-align="center" padding-top="20pt">
       CTB/McGraw-Hill
      </fo:block>
      <fo:block font-size="42pt" font-family="sans-serif" space-after.optimum="6pt"
       text-align="center" padding-top="3pt">
        <xsl:value-of select="/SubTest/@ContentArea"/> Test
      </fo:block>

      <fo:block font-size="42pt" font-family="sans-serif"
       space-after.optimum="6pt" text-align="center" padding-top="3pt">
       Grade:
       <xsl:value-of select="/SubTest/@Grade" />
      </fo:block>
      <xsl:if test="/SubTest/@Form != ''">
        <fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt"
         text-align="center" padding-top="3pt">
         Form: <xsl:value-of select="/SubTest/@Form" />
        </fo:block>
      </xsl:if>
      <xsl:if test="/SubTest/@Title != ''">
	<xsl:call-template name="title"/>
        <xsl:choose>
		<xsl:when test="string-length(/SubTest/@Title) >= 40">
		       <fo:block font-size="20pt" font-family="sans-serif" space-after.optimum="6pt"
			text-align="center" padding-top="3pt">
			 <xsl:value-of select="/SubTest/@Title" />
		       </fo:block>
		</xsl:when>
		<xsl:when test="string-length(/SubTest/@Title) >= 30">
		       <fo:block font-size="24pt" font-family="sans-serif" space-after.optimum="6pt"
			text-align="center" padding-top="3pt">
			<xsl:value-of select="/SubTest/@Title" />
		       </fo:block>
		</xsl:when>
		<xsl:otherwise>
			<fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt"
			text-align="center" padding-top="3pt">
			 <xsl:value-of select="/SubTest/@Title" />
		       </fo:block>
		</xsl:otherwise>
 	</xsl:choose>
      </xsl:if>
   </fo:flow>
 </fo:page-sequence>
</xsl:if>

<!--Below is the traversing logic for an Assessment's "simple" page layout-->
<xsl:if test="self::node()=Assessment">
  <fo:page-sequence master-reference="simple" initial-page-number="1"
  language="en" country="us">

  <fo:static-content flow-name="xsl-region-before">
    <xsl:if test="/Assessment/SubTest/@Title != ''">
      <fo:block text-align="start" font-weight="bold" font-size="12pt" font-family="any">
        <xsl:value-of select="/Assessment/SubTest/@Title"/>
      </fo:block>
    </xsl:if>
    <fo:block>
      <fo:leader leader-pattern="rule" rule-thickness="3pt"
       rule-style="dashed" leader-length="7.5in" />
    </fo:block>
  </fo:static-content>

  <fo:static-content flow-name="xsl-region-after">
     <fo:block>
     <fo:leader leader-pattern="rule" rule-thickness="3pt"
      rule-style="dashed" leader-length="7.5in" />
     </fo:block>

     <fo:block text-align="end" font-size="12pt" font-family="any">
	  Page 
	  <fo:page-number/>
	  of <fo:page-number-citation ref-id="terminator"/>
     </fo:block>


     <xsl:if test="/Assessment/SubTest/@Form != ''">
       <fo:block  font-size="8pt" font-family="any"
	space-after.optimum="3pt" text-align="start"
	white-space-collapse="false">Form: <xsl:value-of select="/Assessment/SubTest/@Form "/>
       </fo:block>
     </xsl:if>

     <xsl:if test="/Assessment/SubTest/@Version != ''">
       <fo:block font-size="8pt" font-family="any"
	     space-after.optimum="6pt"
	text-align="start" padding-top="1pt" white-space-collapse="false">Version/Date: <xsl:value-of select="/Assessment/SubTest/@Version" /></fo:block>
     </xsl:if>

	<xsl:call-template name="copyright"/>
     </fo:static-content>

     <fo:flow flow-name="xsl-region-body">
             <xsl:apply-templates/>
             <fo:block id="terminator"/>
      </fo:flow>

   </fo:page-sequence>
 </xsl:if>

 <!--Below is the traversing logic for a SubTest's "simple" page layout-->
 <xsl:if test="self::node()=SubTest">
   <fo:page-sequence master-reference="simple" initial-page-number="1"
    language="en" country="us">

   <fo:static-content flow-name="xsl-region-before">
     <xsl:if test="/SubTest/@Title != ''">
     <fo:block text-align="start" font-weight="bold" font-size="12pt" font-family="any">
       <xsl:value-of select="/SubTest/@Title"/>
     </fo:block>
     </xsl:if>
     <fo:block>
       <fo:leader leader-pattern="rule" rule-thickness="3pt"
        rule-style="dashed" leader-length="7.5in" />
     </fo:block>
   </fo:static-content>

   <fo:static-content flow-name="xsl-region-after">
     <fo:block>
     <fo:leader leader-pattern="rule" rule-thickness="3pt"
      rule-style="dashed" leader-length="7.5in" />
     </fo:block>

     <fo:block text-align="end" font-size="12pt" font-family="any">
      Page 
      <fo:page-number/>
      of <fo:page-number-citation ref-id="terminator"/>
     </fo:block>

   <xsl:if test="/SubTest/@Form != ''">
     <fo:block  font-size="8pt" font-family="any"
      space-after.optimum="3pt" text-align="start"
      white-space-collapse="false">Form: <xsl:value-of select="/SubTest/@Form "/>
     </fo:block>
   </xsl:if>

   <xsl:if test="/SubTest/@Version != ''">
     <fo:block font-size="8pt" font-family="any"
      space-after.optimum="6pt"
      text-align="start" padding-top="1pt" white-space-collapse="false">Version/Date: <xsl:value-of select="/SubTest/@Version" /></fo:block>
   </xsl:if>

	<xsl:call-template name="copyright"/>
  </fo:static-content>

  <fo:flow flow-name="xsl-region-body">
          <xsl:apply-templates/>
          <fo:block id="terminator"/>
      </fo:flow>

   </fo:page-sequence>
 </xsl:if>

 <xsl:if test="self::node()=ItemSet">
  <fo:page-sequence master-reference="simple" initial-page-number="1"
   language="en" country="us">

    <fo:static-content flow-name="xsl-region-before">

      <fo:block>
        <fo:leader leader-pattern="rule" rule-thickness="3pt"
         rule-style="dashed" leader-length="7.5in" />
      </fo:block>
    </fo:static-content>

    <fo:static-content flow-name="xsl-region-after">
    	<xsl:call-template name="nextPage"/>
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
    <fo:page-sequence master-reference="simple" initial-page-number="1"
     language="en" country="us">

      <fo:static-content flow-name="xsl-region-before">

        <fo:block>
          <fo:leader leader-pattern="rule" rule-thickness="3pt"
           rule-style="dashed" leader-length="7.5in" />
        </fo:block>
      </fo:static-content>

      <fo:static-content flow-name="xsl-region-after">
	<xsl:call-template name="nextPage"/>
	<xsl:call-template name="copyright"/>
        </fo:static-content>

        <fo:flow flow-name="xsl-region-body">
          <xsl:apply-templates/>
          <fo:block id="terminator"/>
        </fo:flow>
      </fo:page-sequence>
 </xsl:if>

 </fo:root>
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

  <xsl:template match="B">
  <!--Code will apply following fo:inline attribute values to all "B"(bold) tags-->
    <fo:inline font-weight="bold">
      <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>

  <xsl:template match="P">
  <xsl:choose>
  <xsl:when test="position() = 1">
        <fo:inline padding-bottom="20pt" padding-top="0pt" white-space-collapse="false">
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
     </xsl:attribute><xsl:apply-templates />
        </fo:inline>
  </xsl:when>
  <xsl:otherwise>
  <fo:block start-indent="0.5in" padding-top="12pt" font-family="any" font-weight="normal" font-size="12pt" white-space-collapse="false">
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
     </xsl:attribute><xsl:apply-templates />
  </fo:block>
  </xsl:otherwise>
  </xsl:choose>
  </xsl:template>

  <xsl:template match="Rubric">
  <!-- <fo:block  start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt"  font-weight="normal"> -->
       <xsl:apply-templates/>
  <!-- </fo:block> -->
  </xsl:template>

  <xsl:template match="ConstructedResponse">
  	<xsl:apply-templates select="Rubric"/>
  </xsl:template>

 <xsl:template match="UnorderedList">
   <fo:list-block>
                <xsl:apply-templates select="ListItem"/>
   </fo:list-block>
  </xsl:template>

 <xsl:template match="ListItem">
      <fo:list-item>
        <fo:list-item-label>
                <fo:block font-family="any" white-space-collapse="false" font-size="'12pt'">
                        <fo:inline>»  </fo:inline>
                </fo:block>
        </fo:list-item-label>
        <fo:list-item-body>
                <fo:block padding-top="1pt" start-indent="0.65in" font-weight="normal" font-family="any" white-space-collapse="false">
                        <xsl:apply-templates/>
                </fo:block>
        </fo:list-item-body>
        </fo:list-item>
  </xsl:template>

  <!-- The Graphic Template does not have height and width defined, therefore will render images
  in their native size-->

      <xsl:template match="Graphic">
        <fo:block padding-top="20pt" padding-bottom="6pt" start-indent="0.5in">
            <xsl:attribute name="text-align">
               <xsl:choose>
                    <xsl:when test="EPSPrint/@Position='Center'">
                           <xsl:value-of select="'center'"/>
                    </xsl:when>
                    <xsl:when test="EPSPrint/@Position='Right'">
                           <xsl:value-of select="'right'"/>
                    </xsl:when>
                    <xsl:otherwise>
                           <xsl:value-of select="'left'" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
          <fo:external-graphic>
            <xsl:attribute name="src">
              <xsl:value-of select="EPSPrint/@FileName"/>
            </xsl:attribute>
          </fo:external-graphic>
        </fo:block>
       </xsl:template>

  <xsl:template match="I">
  <!--Code will apply following fo:block attribute values to all "I"(Italic) tags-->
    <fo:inline font-family="any" font-style="italic">
       <xsl:apply-templates/>
    </fo:inline>
  </xsl:template>



</xsl:stylesheet>
