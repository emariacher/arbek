<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" version = "1.0" >
<xsl:output method = "xml" indent="yes"/> 

<xsl:template name="string-replace-all">
    <xsl:param name="text" />
    <xsl:param name="replace" />
    <xsl:param name="by" />
    <xsl:choose>
        <xsl:when test="$text = '' or $replace = ''or not($replace)" >
            <!-- Prevent this routine from hanging -->
            <xsl:value-of select="$text" />
        </xsl:when>
        <xsl:when test="contains($text, $replace)">
            <xsl:value-of select="substring-before($text,$replace)" />
            <xsl:value-of select="$by" />
            <xsl:call-template name="string-replace-all">
                <xsl:with-param name="text" select="substring-after($text,$replace)" />
                <xsl:with-param name="replace" select="$replace" />
                <xsl:with-param name="by" select="$by" />
            </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$text" />
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template match="/Protocol">
<protocol>
    <zob><xsl:value-of select="@name"/></zob>
    <xsl:for-each select="Packet">
		<xsl:element name = "frame">
			<xsl:attribute name = "description" ><xsl:value-of select="@comment"/></xsl:attribute>
			<xsl:attribute name = "name" ><xsl:value-of select="@file"/></xsl:attribute>
			<xsl:attribute name = "parameter" ><xsl:value-of select="@name"/></xsl:attribute>
			<xsl:element name = "{//@typerw}">
				<xsl:element name = "{//@typersq}">
					<xsl:for-each select="Enum">
						<xsl:element name = "field">
							<xsl:attribute name = "default" ><xsl:value-of select="@default"/></xsl:attribute>
							<xsl:attribute name = "size" >
								<!--xsl:variable name="myVariable ">
									<xsl:call-template name="string-replace-all">
										<xsl:with-param name="text" select="'@encodedType'" />
										<xsl:with-param name="replace" select="'bitEnum'" />
										<xsl:with-param name="by" select="''" />
									</xsl:call-template>
								</xsl:variable-->							
							</xsl:attribute>
							<xsl:attribute name = "description" ><xsl:value-of select="@comment"/></xsl:attribute>
							<xsl:attribute name = "name" ><xsl:value-of select="@name"/></xsl:attribute>							
							<xsl:attribute name = "type" >enumerate</xsl:attribute>
							<xsl:for-each select="Value">
								<xsl:element name = "enumerator">
									<xsl:attribute name = "description" ><xsl:value-of select="@comment"/></xsl:attribute>
									<xsl:attribute name = "name" ><xsl:value-of select="@name"/></xsl:attribute>							
									<xsl:value-of select="text()"/>
								</xsl:element>
							</xsl:for-each>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
			</xsl:element>
		</xsl:element>
	</xsl:for-each>
</protocol>
</xsl:template>

</xsl:stylesheet> 