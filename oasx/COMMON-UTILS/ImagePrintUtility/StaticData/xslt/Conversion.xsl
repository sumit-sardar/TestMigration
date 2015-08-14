<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="UTF-8" version="4.0"
		indent="no" />

	<xsl:template match="/">
		<table class="split">
			<tr>
				<td align="left" bgcolor="#6699FF" style="width:500px;">
					<table>
						<tr>
							<td id="SessionID">
							</td>
						</tr>
					</table>
				</td>
				<td align="left" bgcolor="#6699FF" style="width:500px;">
					<b>Questions and Answers</b>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<xsl:apply-templates />
				</td>
				<td id="answer">

				</td>
			</tr>
		</table>
	</xsl:template>

	<!-- TAB PANEL -->
	<!-- <xsl:template match="stimulus_tabs_panel"> <div id="tabPanel" style="float:left;"> 
		<xsl:apply-templates /> </div> </xsl:template> -->

	<!-- Single TAB -->
	<xsl:template match="stimulus_tab">
		<xsl:if
			test="not((./@title='Prewriting/Planning') or (./@title='Editing Checklist'))">
			<table>
				<tr>
					<td>
						<div class="ridge"
							style="float:left;margin:0 3px;list-style:none; height:24px;width:200px;text-align:center;">
							<a href="#" style="color:black;text-decoration:none;">
								<b>
									<xsl:value-of select="./@title" />
								</b>
							</a>
						</div>
						<br />
						<div id="tab_content" style="width:500px;float:left;">
							<br />
							<xsl:apply-templates />
						</div>
					</td>
					<!-- <td></td> -->
				</tr>
			</table>
		</xsl:if>
	</xsl:template>

	<!-- Text Area -->
	<xsl:template match="scrolling_text_widget">
		<xsl:apply-templates />
	</xsl:template>

	<!-- DIV -->
	<xsl:template match="scrolling_text_panel">
		<table>
			<tr>
				<td valign="top">
					<div style="width:500px;">
						<xsl:apply-templates />
					</div>
				</td>
				<!-- <td></td> -->
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="table">
		<xsl:choose>
			<xsl:when test="./@showvlines = 'yes' and ./@showhlines = 'yes'">
				<table border="1">
					<xsl:apply-templates />
				</table>
			</xsl:when>
			<xsl:otherwise>
				<table border="0">
					<xsl:apply-templates />
				</table>
			</xsl:otherwise>
		</xsl:choose>

	</xsl:template>
	<xsl:template match="row">
		<tr>
			<xsl:apply-templates />
		</tr>
	</xsl:template>
	<xsl:template match="cell">
		<td>
			<xsl:apply-templates />
		</td>
	</xsl:template>
	<!-- <xsl:template match="table"> <xsl:if test="not(./row/cell/multi_line_answer)"> 
		<xsl:apply-templates /> </xsl:if> </xsl:template> -->
	<xsl:template match="panel">

		<xsl:if test="not(./@stereotype = 'directions') ">
			<xsl:choose>
				<xsl:when test="./@stereotype = 'stem' ">
					<table id="QuestionPartTable">
						<tr>
							<td></td>
						</tr>
						<tr>
							<td>
								<div>
									<xsl:apply-templates />
								</div>
							</td>
						</tr>
					</table>
				</xsl:when>
				<xsl:otherwise>
					<table>
						<tr>
							<td>
								<div>
									<xsl:apply-templates />
								</div>
							</td>
							<!-- <td></td> -->
						</tr>
					</table>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>

	</xsl:template>

	<!-- LABEL -->
	<xsl:template match="text_widget">

		<div>
			<xsl:choose>
				<xsl:when test="parent::panel[@stereotype = 'stem'] ">
					<b>
						<xsl:copy-of select="." />
					</b>
					<div>
						<xsl:text><br /></xsl:text>
					</div>
				</xsl:when>
				<xsl:when test="./@text_magnification">
				<div style="font-size: 11pt;">
					<b>
						<sup>
							<xsl:value-of
								select="count(preceding-sibling::text_widget/@text_magnification) + 1" />
						</sup>
					</b>
					<xsl:copy-of select="." />
				</div>

				</xsl:when>
				<xsl:otherwise>
					<xsl:copy-of select="." />

				</xsl:otherwise>
			</xsl:choose>
		</div>
	</xsl:template>


	<xsl:template match="multi_line_answer|single_line_answer">
		<table>
			<tr>
				<td id="{@id}">
				</td>
			</tr>
			<tr>
				<td width="500px;">
					<xsl:if
						test="(count(following-sibling::multi_line_answer) &gt;=1) or
				(count(following-sibling::single_line_answer) &gt;= 1)">
						<hr />
					</xsl:if>
				</td>
			</tr>
		</table>
	</xsl:template>
	<xsl:template match="image_widget">
		<xsl:text disable-output-escaping="yes">&lt;div width="500px" align="center"&gt;&lt;img src="</xsl:text>
		<xsl:value-of select="concat('..\images\',@src,'.png')" />
		<xsl:text disable-output-escaping="yes">" /&gt;&lt;/div&gt;</xsl:text>
		<!-- <img> <xsl:attribute name="src"> <xsl:value-of select="concat('images\',@src,'.png')" 
			/> </xsl:attribute> </img> -->
	</xsl:template>

	<xsl:template match="text()">
		<RICHTEXT>
			<xsl:apply-templates select="b" />
			<xsl:apply-templates select="i" />
			<xsl:apply-templates select="br" />
			<xsl:apply-templates select="a" />
			<xsl:value-of select="." />
		</RICHTEXT>
	</xsl:template>
	<xsl:template match="b">
		<RICHTEXT>
			<xsl:attribute name="BOLD">true</xsl:attribute>
			<xsl:value-of select="." />
		</RICHTEXT>
	</xsl:template>

	<xsl:template match="i">
		<RICHTEXT>
			<xsl:attribute name="ITALIC">true</xsl:attribute>
			<xsl:value-of select="." />
		</RICHTEXT>
	</xsl:template>
	<xsl:template match="br">
		<xsl:text>&#10;</xsl:text>
	</xsl:template>
	<xsl:template match="footnote//a">
		<xsl:text><xsl:value-of select="text()" /></xsl:text>
	</xsl:template>
</xsl:stylesheet>
