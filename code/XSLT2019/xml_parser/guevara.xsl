<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" version = "1.0" >
<xsl:output method = "xml" indent="yes"/>    
<xsl:template match="/Protocol">
<protocol>
    <zob><xsl:value-of select="@name"/></zob>
    <xsl:for-each select="Packet">
		<xsl:element name = "frame">
			<xsl:attribute name = "parameter" ><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:attribute name = "name" ><xsl:value-of select="@file"/></xsl:attribute>
			<xsl:attribute name = "description" ><xsl:value-of select="@comment"/></xsl:attribute>
			<xsl:element name = "{//@typerw}">
				<xsl:element name = "{//@typersq}">
					<xsl:for-each select="Enum/Value">
						<xsl:element name = "field">
							<xsl:attribute name = "type" >enumerable</xsl:attribute>
							<xsl:attribute name = "name" ><xsl:value-of select="@name"/></xsl:attribute>							
							<xsl:attribute name = "description" ><xsl:value-of select="@comment"/></xsl:attribute>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:for-each>
</protocol>
</xsl:template>

</xsl:stylesheet> 