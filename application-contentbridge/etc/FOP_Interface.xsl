<?xml version="1.0" encoding="US-ASCII"?>
<xsl:stylesheet xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">




<xsl:import href="etc/common_IB.xsl"/>

<!--

FILENAME:
       FOP_Interface.xsl

PURPOSE:
	  Translates XML validated by the R2_Flash_UI.dtd to a corresponding FO format
	  for PDF print output.

CHANGE HISTORY:
	Original created and maintained by Demetrius Davillier (demetrius.davillier@accenture.com)
	v1.1 (11/06/02): Changed Title logic to accommodate up to 45 characters by using smaller fonts
	v1.2 (11/08/02): Added <fo:block id="terminator"/>  tag for ItemSet and Item
			 to display total pages properly
	v1.3 (11/11/02): Modifed Stem Logic to apply templates for each P tag to apply text formatting
	v1.4 (11/14/02): Added extra space before word "LLC" in copyright line to show the space
	v1.5 (11/14/02): Modifed P tags to have a padding-before, and BR tag to have no padding-before
			 Modified Stem to have a padding of 18 instead of 20 to align with number.
			 Removed fontsize from text formatting tags (italics, underline, bold)
	v1.6 (11/20/02): Added 3rd line in Stem for Template 0 and 1 per request by MDR
	v1.7 (11/21/02): Moved Graphic Stimulus under stem for templates for templates 2, 3, 6, 7, 9, 10, 14, 15, 16, 17, and 19
	v1.8 (11/22/02): Fixed template 12 to display images next to item number
	v1.9 (11/25/02): Modified Stem Graphic from Stem/Graphic to Stimulus/Graphic
	v2.0 (12/02/02): Modified graphic sizes to be in pixels, and also answer choices for templates 1 and 6
	v2.4 (01/08/03): Modified Student Directions as it only showed the directions for templates 4, 8 after version 2.3
	v2.5 (01/27/03): Added white-area-collapse=false property to retain white space properties
	v2.6 (01/27/03): Added bold font weight to second <P> tag in stem for template 11
	v2.7 (01/27/03): Reduced top margin, and centered graphic for template 8 (reading passage art)
	v2.8 (01/27/03): Added Stimulus logic to display stimulus when it's the first item of a group material, or the item with different student direction than the previous item.
	v2.9 (01/27/03): Modified Margins and took out whitespace recognition in cover page and header/footer.
	v3.0 (01/27/03): Modified student directions so that stimulus id can be empty
	v3.1 (01/30/03): Added CR logic, and removed Go on to Next Page and Page x of y for Item pdf's
	v3.2 (01/31/03): Modified Student direction font size to equal fontsize of items.
	v3.3 (02/07/03): Modified Student direction to also check the DisplayAlways attribute.
	v3.4 (02/20/03): Modified Student direction to check next item's stimulus to see if it's the start of a group material.
  	v3.5 (08/12/04): Modified to support 12A template similar to 12 (giuseppe_gennaro@ctb.com)	
  	v3.6 (08/13/04): Modified with draft revision of changes - see CVS comments (maxdunn@siliconpublishing.com)
	v3.6 (08/13/04): Added "TB" templates
	v3.7 (08/21/04): Eliminated extra page for SubTests, fixed keeps associated with dedupe change (maxdunn@siliconpublishing.com)
        Refactoring 4/27/2003 Edward Hieatt (edward@edwardh.com)
-->

<xsl:variable name="isCR" select="false()"/>

<xsl:template name="title">
        <fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt" text-align="center" padding-top="3pt">
	               Title:
        </fo:block>
</xsl:template>

<xsl:template name="titleSuffix"/>

<xsl:template name="stemListItem">
        <fo:list-item-label>
          <fo:block padding-top="15pt" font-size="16pt" white-space-collapse="false">
           <xsl:number level="any" format="1.    "/>
          </fo:block>
        </fo:list-item-label>
</xsl:template>

<!--xsl:apply-templates for various XML elements-->

  <xsl:template match="Item">
<!--Each of the 20 different Item templates must be encapsulated in a
table-row with a "keep-together" attribute in order to maintain appropraite
pagebreaks. All other templates for Item elements will apply within an
Item template (ie. Stem, Stimulus, SelectedResponse, P, U, I, etc.-->
  <xsl:choose>
    <xsl:when test="@Template='UA'">
	    <fo:table table-layout="fixed">
	    <fo:table-column column-width="7.5in"/>
	    <fo:table-body>
	      <fo:table-row keep-together="always">
		<fo:table-cell>
		  <fo:list-item><xsl:call-template name="stemListItem"/>
		    <fo:list-item-body>
		      <fo:block white-space-collapse="false" padding-bottom="10pt" font-weight="bold" font-family="any" start-indent="0.5in" padding-top="18pt" font-size="14pt" font-style="italic">Sorry, this item is unavailable.</fo:block>                	
		    </fo:list-item-body>
		  </fo:list-item>
		</fo:table-cell>
	      </fo:table-row>
	    </fo:table-body>
     </fo:table>
     </xsl:when>  
    <xsl:when test="@Template='0' or @Template='TB0' or @Template='2' or @Template='6' or @Template='TB6' or @Template='12' or @Template='TB12' or @Template='12A' or @Template='13' or @Template='15' or @Template='16' or @Template='17' or @Template='TB17' or @Template='18' or @Template='19' or @Template='28' or @Template='TB28' or @Template='CR1' or @Template='CR5'">
    <fo:table table-layout="fixed">
          	<fo:table-column column-width="7.5in"/>
          	<fo:table-body>
          	  <fo:table-row keep-together="always">
                <fo:table-cell>
                  <xsl:apply-templates/>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
     </fo:table>
     </xsl:when>


<!--Template 4 and 8 puts the Student Directions and the Stimulus outside
the table so the passage can run over a page without causing an error.-->
     <xsl:when test="@Template='4' or @Template='8' or @Template='TB8'">
      <fo:block white-space-collapse="false">
      <xsl:apply-templates select="StudentDirections"/>
       <xsl:apply-templates select="Stimulus"/>
       </fo:block>
          <fo:table table-layout="fixed">
          <fo:table-column column-width="7.5in"/>
            <fo:table-body>
              <fo:table-row keep-together="always">
                <fo:table-cell>
                  <xsl:apply-templates select="Stem"/>
		  <xsl:choose>
			<xsl:when test="@ItemType='CR'">
                  		<xsl:apply-templates select="ConstructedResponse"/>
		  	</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="SelectedResponse"/>
			</xsl:otherwise>
		  </xsl:choose>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
      </xsl:when>



<!--No Tables used-->
     <xsl:when test="( not(//Item/Stimulus[@ID=current()/Stimulus/@ID][@dedupe='yes']) and ( @Template='1' or @Template='TB1' or @Template='3' or @Template='TB3' or @Template='5' or @Template='TB5' or @Template='7' or @Template='TB7' or @Template='9' or @Template='TB9' or @Template='10' or @Template='TB10' or @Template='11' or @Template='TB11' or @Template='14' or @Template='20' or @Template='TB20' or @Template='23' or @Template='24' or @Template='25' or @Template='CR2' or @Template='CR3' or @Template='CR4' or @Template='CR6') ) or ( (//Item/Stimulus[@ID=current()/Stimulus/@ID][@dedupe='yes']) and ( @Template='1' or @Template='TB1' or @Template='3' or @Template='TB3' or @Template='5' or @Template='TB5' or @Template='11' or @Template='TB11' or @Template='20' or @Template='TB20' or @Template='23' or @Template='24' or @Template='25' ) )">
	<fo:block break-before="page"/>
      <fo:block white-space-collapse="false">
      <xsl:apply-templates select="StudentDirections"/>
       <xsl:apply-templates select="Stimulus"/>
       </fo:block>
                  <xsl:apply-templates select="Stem"/>
                  <xsl:choose>
                        <xsl:when test="@ItemType='CR'">
                                <xsl:apply-templates select="ConstructedResponse"/>
                        </xsl:when>
                        <xsl:otherwise>
                                <xsl:apply-templates select="SelectedResponse"/>
                        </xsl:otherwise>
                  </xsl:choose>
      </xsl:when>

   
    <xsl:otherwise>

     <xsl:choose>
       <xsl:when test="(//Item/Stimulus[@ID=current()/Stimulus/@ID][@dedupe='yes'])        and not(preceding-sibling::Item/Stimulus[@ID = current()/Stimulus/@ID])        and (following-sibling::Item/Stimulus[@ID = current()/Stimulus/@ID])">
        <fo:table table-layout="fixed">
          <fo:table-column column-width="7.5in"/>
          <fo:table-body>
            <fo:table-row keep-together="always">
              <fo:table-cell>
                    <xsl:apply-templates select="Stimulus|StudentDirections"/>
              </fo:table-cell>
            </fo:table-row>
            <fo:table-row keep-together="always">
            <fo:table-cell>
              <xsl:apply-templates select="node()[not(self::Stimulus)and               not(self::StudentDirections)]"/>
            </fo:table-cell>
          </fo:table-row>
        </fo:table-body>
        </fo:table>
       </xsl:when>
       <xsl:otherwise>
         
     <fo:table table-layout="fixed">
           	<fo:table-column column-width="7.5in"/>
           	<fo:table-body>
           	  <fo:table-row keep-together="always">
                 <fo:table-cell>
                   <xsl:apply-templates/>
                 </fo:table-cell>
               </fo:table-row>
             </fo:table-body>
     </fo:table>
   
       </xsl:otherwise>
     </xsl:choose>

    </xsl:otherwise> 
    
   </xsl:choose>
  </xsl:template>


  <xsl:template match="ConstructedResponse">
  <!--defines and applies answerarea specified in the XML-->
	<xsl:apply-templates select="AnswerArea"/>
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



  <xsl:template match="Graphic[ancestor::*[@Template][1]/@Template='8']">
    <xsl:variable name="ignore_which">all</xsl:variable>
    <xsl:variable name="preceding" select="count(preceding::Graphic[@ID=current()/@ID])"/>
    <xsl:choose>
      
      <xsl:when test=" //*[ @graphic-before=current()/@ID or @graphic-after=current()/@ID ] and ( ($ignore_which='all') or ($preceding = ($ignore_which - 1)) )       "/>
      <xsl:otherwise>
        <xsl:apply-imports/>
      </xsl:otherwise>
    </xsl:choose> 
  </xsl:template>
  
    <xsl:template match="Graphic[ancestor::*[@Template][1]/@Template='TB8']">
      <xsl:variable name="ignore_which">all</xsl:variable>
      <xsl:variable name="preceding" select="count(preceding::Graphic[@ID=current()/@ID])"/>
      <xsl:choose>
        
        <xsl:when test=" //*[ @graphic-before=current()/@ID or @graphic-after=current()/@ID ] and ( ($ignore_which='all') or ($preceding = ($ignore_which - 1)) )       "/>
        <xsl:otherwise>
          <xsl:apply-imports/>
        </xsl:otherwise>
      </xsl:choose> 
  </xsl:template>

    </xsl:stylesheet>
