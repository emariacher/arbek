<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common" xmlns:loop="http://informatik.hu-berlin.de/loop" extension-element-prefixes="exsl" xmlns:dxl="http://www.lotus.com/dxl">
	<xsl:output method="xml" />
  
  <!--Eric Mariacher 2apr9-->
	
	<xsl:template match="db/root">
		<xsl:apply-templates select="doc"/>
	</xsl:template>
	
	
	<xsl:template match="doc">
		<branch>
			<lvl>
				<xsl:value-of select="level"/>
			</lvl>
			<children>
				<xsl:value-of select="count(child::*)"/>
			</children>
			<xsl:apply-templates select="dxl:document"/>
			<xsl:apply-templates select="doc"/>
		</branch>
	</xsl:template>
	
	<xsl:template match="dxl:document">
		<leaf>
			<xsl:for-each select="dxl:item[@name='Subject']">
				<label>
					<xsl:value-of select="."/>
				</label>
			</xsl:for-each>
			<xsl:for-each select="dxl:item[@name='postbody_Bug_numbers']">
				<bugs>
					<xsl:value-of select="."/>
				</bugs>
			</xsl:for-each>
			<xsl:for-each select="dxl:item[@name='postbody_Duration']">
				<duration>
					<xsl:value-of select="."/>
				</duration>
			</xsl:for-each>
		</leaf>
	</xsl:template>

</xsl:stylesheet>
