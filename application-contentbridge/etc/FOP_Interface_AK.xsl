<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:include href="etc/common_AK.xsl"/>

<!--

FILENAME:
       FOP_Interface_AK.xsl

PURPOSE:
    Translates XML validated by the R2_Flash_UI.dtd to a corresponding FO format
    for Answer Key PDF print output.

CHANGE HISTORY:
  Original created and maintained by Jessica Glissmann
        Refactoring 4/27/2003 Edward Hieatt (edward@edwardh.com)
-->

<xsl:template name="title">
        <fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt"
                 text-align="center" padding-top="3pt">
                 Answer Key for Test:
        </fo:block>
</xsl:template>

<xsl:template name="nextPage">
      <fo:block>
      <fo:leader leader-pattern="rule" rule-thickness="3pt"
       rule-style="dashed" leader-length="7.5in" />
      </fo:block>

     <fo:block text-align="end" font-size="12pt" font-family="any">
    Page 
    <fo:page-number/>
    of <fo:page-number-citation ref-id="terminator"/>
     </fo:block>

</xsl:template>

<!--xsl:apply-templates for various XML elements-->

  <xsl:template match="Item">
  <xsl:choose>

    <xsl:when test="@Template='UA'">
      <fo:table table-layout="fixed">
        <fo:table-column column-width="7.5in"/>
        <fo:table-body>
          <fo:table-row keep-together="always">
            <fo:table-cell>Sorry, this item is unavailable.</fo:table-cell>
          </fo:table-row>
        </fo:table-body>
      </fo:table>
    </xsl:when>
  
      <xsl:when test="ConstructedResponse/Rubric/BR/@Type='Page'">
                <fo:block  start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt"  font-weight="bold">
                <xsl:apply-templates select="Stem"/>
                <xsl:choose>
                        <xsl:when test="SelectedResponse">
                                <xsl:apply-templates select="SelectedResponse"/>
                        </xsl:when>
                        <xsl:when test="ConstructedResponse">
                                <xsl:apply-templates select="ConstructedResponse"/>
                        </xsl:when>
                </xsl:choose>
                </fo:block>
  </xsl:when>
  <xsl:otherwise>
                <fo:table table-layout="fixed">
                <fo:table-column column-width="7.5in"/>
                <fo:table-body>
                  <fo:table-row keep-together="always">
                <fo:table-cell>
            <fo:block  start-indent="0.5in" font-family="any" padding-top="10pt" padding-bottom="10pt"  font-weight="bold">
            <xsl:apply-templates select="Stem"/>
            <xsl:choose>
              <xsl:when test="SelectedResponse">
                     <xsl:apply-templates select="SelectedResponse"/>
              </xsl:when>
              <xsl:when test="ConstructedResponse">
                      <xsl:apply-templates select="ConstructedResponse"/>
              </xsl:when>
            </xsl:choose>
            </fo:block>
                </fo:table-cell>
              </fo:table-row>
            </fo:table-body>
          </fo:table>
  </xsl:otherwise>
  </xsl:choose>
    </xsl:template>


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

  <xsl:template match="Stem">
    <xsl:number level="any" format="1.    "/>
  </xsl:template>

  <xsl:template match="SelectedResponse">
  <xsl:choose>

  <xsl:when test="AnswerChoice[position() = 1]/@Type='Correct'">
    <fo:inline padding-bottom="20pt" padding-top="0pt">
          <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    A </fo:inline>
        </fo:inline>
  </xsl:when>
  <xsl:when test="AnswerChoice[position() = 2]/@Type='Correct'">
    <fo:inline padding-bottom="20pt" padding-top="0pt">
          <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    B </fo:inline>
        </fo:inline>
  </xsl:when>
  <xsl:when test="AnswerChoice[position() = 3]/@Type='Correct'">
    <fo:inline padding-bottom="20pt" padding-top="0pt">
          <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    C </fo:inline>
        </fo:inline>
  </xsl:when>
  <xsl:when test="AnswerChoice[position() = 4]/@Type='Correct'">
  <fo:inline padding-bottom="20pt" padding-top="0pt">
    <fo:inline font-weight="bold" font-family="any" white-space-collapse="false">    D </fo:inline>
  </fo:inline>
  </xsl:when>

  </xsl:choose>

</xsl:template>




</xsl:stylesheet>

