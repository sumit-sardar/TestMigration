<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template match="/">
        <xsl:apply-templates select="//Graphic"/>
</xsl:template>

<xsl:template match="//Graphic">
        <xsl:if test="BMPPrint/@FileName">
                <xsl:value-of select="BMPPrint/@FileName"/><xsl:text>
</xsl:text>
        </xsl:if>
        <xsl:if test="Flash/@FileName">
                <xsl:value-of select="BMPPrint/@FileName"/><xsl:text>
</xsl:text>
        </xsl:if>
        <xsl:if test="EPSPrint/@FileName">
                <xsl:value-of select="BMPPrint/@FileName"/><xsl:text>
</xsl:text>
        </xsl:if>
</xsl:template>

</xsl:stylesheet>




















