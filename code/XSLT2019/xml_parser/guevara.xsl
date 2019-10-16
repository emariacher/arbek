<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" version = "1.0" >
<xsl:output method = "xml" indent="yes"/>    
<xsl:template match="/Protocol">
<protocol>
    <zob><xsl:value-of select="@name"/></zob>
    <xsl:for-each select="Packet">
		<xsl:element name = "frame" >
			<xsl:attribute name = "parameter" ><xsl:value-of select="@name"/></xsl:attribute>
		</xsl:element>
	</xsl:for-each>
</protocol>
</xsl:template>

</xsl:stylesheet> 