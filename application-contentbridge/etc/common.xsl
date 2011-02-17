<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:fo="http://www.w3.org/1999/XSL/Format">

<!-- Templates and variables common to several XSL files -->

<xsl:template name="copyright">
	<xsl:param name="size" select="'8pt'"/>
	<xsl:param name="paddingTop"/>
	<fo:block text-align="center" font-size="{$size}" font-family="serif" white-space-collapse="false">
		<xsl:if test="$paddingTop">
			<xsl:attribute name="padding-top"><xsl:value-of select="$paddingTop"/></xsl:attribute>
		</xsl:if>Copyright &#x00A9; 2002 by CTB/McGraw-Hill  LLC. All rights reserved</fo:block>
</xsl:template>

<xsl:template name="lookup">
	<xsl:param name="name"/>
        <xsl:param name="key" select="'default'"/>
	<xsl:variable name="lookupsTree" select="document('lookups.xml')"/>
        <xsl:choose>
                <xsl:when test="$lookupsTree/lookups/lookup[@name=$name]/entry[@key=$key]"><xsl:value-of select="$lookupsTree/lookups/lookup[@name=$name]/entry[@key=$key]/@value"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="$lookupsTree/lookups/lookup[@name=$name]/entry[@key='default']/@value"/></xsl:otherwise>
	</xsl:choose>
</xsl:template>

  <xsl:template match="U">
  <!--Code will apply following fo:inline attribute values to all "U"(underline) tags-->
     <fo:inline  font-family="any"  text-decoration="underline"
      space-before.optimum="1cm">
     <xsl:apply-templates/>
     </fo:inline>
  </xsl:template>

</xsl:stylesheet>
