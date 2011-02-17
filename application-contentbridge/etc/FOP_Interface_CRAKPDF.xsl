<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

<xsl:include href="etc/common_AK.xsl"/>

<!--

FILENAME:
       FOP_Interface_CRAKPDF.xsl

PURPOSE:
    Translates XML validated by the R2_Flash_UI.dtd to a corresponding FO format
    for Answer Key PDF print output.

CHANGE HISTORY:
  Original created and maintained by Jessica Glissmann
        Refactoring 4/27/2003 Edward Hieatt (edward@edwardh.com)
-->

<xsl:template name="title">
        <fo:block font-size="30pt" font-family="sans-serif" space-after.optimum="6pt"
                 text-align="center" padding-top="100pt">
                 Constructed Response Rubric for Test:
        </fo:block>
</xsl:template>

<xsl:template name="nextPage">
    <fo:block>
            <fo:leader leader-pattern="rule" rule-thickness="3pt"
             rule-style="dashed" leader-length="7.5in" />
        </fo:block>
</xsl:template>

<!--xsl:apply-templates for various XML elements-->

  <xsl:template match="Item">
    <xsl:choose>
      <xsl:when test="ConstructedResponse">
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
                  <xsl:apply-templates select="ConstructedResponse"/>
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
                <xsl:apply-templates select="ConstructedResponse"/>
              </fo:block>
                  </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
    </xsl:otherwise>
  </xsl:choose>

      </xsl:when>
      <xsl:otherwise>

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
          <fo:inline white-space-collapse="false">
            <xsl:attribute name="padding-top">
                 <xsl:choose>
                    <xsl:when test="parent::node()/@ItemType='CR'">
                        <xsl:value-of select="'15pt'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'0.1pt'"/>
                    </xsl:otherwise>
                </xsl:choose>
             </xsl:attribute>
            <xsl:attribute name="font-size">
                 <xsl:choose>
                    <xsl:when test="parent::node()/@ItemType='CR'">
                        <xsl:value-of select="'16pt'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'0.1pt'"/>
                    </xsl:otherwise>
                </xsl:choose>
             </xsl:attribute>
           <xsl:number level="any" format="1.    "/>
          </fo:inline>
  </xsl:template>


</xsl:stylesheet>
