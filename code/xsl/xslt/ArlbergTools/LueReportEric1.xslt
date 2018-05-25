<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common" xmlns:math="http://xsltsl.org/math" extension-element-prefixes="exsl">
	<xsl:import href="http://xsltsl.sourceforge.net/modules/math.xsl"/>
	<xsl:output method="html" version="4.0" encoding="UTF-8" indent="yes"/>
	
	
	
	<xsl:variable name="key_stroke_time">
		<xsl:for-each select="*/*[@Key]">
			<xsl:if test="contains(name(),'KeyboardKey')">
				<item>
					<xsl:call-template name="hex2dec">
						<xsl:with-param name="hex" select="@Time"/>
					</xsl:call-template>
				</item>
			</xsl:if>
		</xsl:for-each>
	</xsl:variable>
	
	
	<xsl:template match="/">
		<html>
			<body>
				<table border="1">
					<tr>
						<th>application</th>
						<th>count</th>
						<th>duration in ms</th>
						<th>mean rate keystroke / ms</th>
					</tr>
					<xsl:for-each select="*/*[@Duration]">
						<tr>
							<td>
								<xsl:value-of select="@FocusedApplication"/>
							</td>
							<td>
								<xsl:value-of select="@Count"/>
							</td>
							<td>
								<xsl:value-of select="@Duration"/>
							</td>
							<td>
								<xsl:call-template name="stripdot">
									<xsl:with-param name="val" select="number(@Duration) div number(@Count)"/>
								</xsl:call-template>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<xsl:for-each select="exsl:node-set($key_stroke_time)/item">
					<xsl:if test="position() != 1">
						<xsl:value-of select="."/> - 
						<xsl:call-template name="stripdot">
						<!-- diff with immediate preceding item (axis preceding goes into reverse) -->
						<xsl:with-param name="val" select=". - preceding::item[1]"/>
						</xsl:call-template>
						<br/>
					</xsl:if>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name="stripdot">
		<xsl:param name="val"/>
		<xsl:choose>
			<xsl:when test="contains($val,'.')">
				<xsl:value-of select="substring-before($val,'.')"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$val"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	
	<xsl:template name="hex2dec">
		<xsl:param name="hex"/>
		<xsl:variable name="hexlength">
			<xsl:value-of select="string-length($hex)"/>
		</xsl:variable>
		
		<xsl:variable name="byte0">
			<xsl:choose>
				<xsl:when test="number($hexlength)>3">
					<xsl:call-template name="math:cvt-hex-decimal">
						<xsl:with-param name="value" select="substring($hex,number($hexlength)-1,2)"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="byte1">
			<xsl:choose>
				<xsl:when test="number($hexlength)>5">
					<xsl:call-template name="math:cvt-hex-decimal">
						<xsl:with-param name="value" select="substring($hex,number($hexlength)-3,2)"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="byte2">
			<xsl:choose>
				<xsl:when test="number($hexlength)>7">
					<xsl:call-template name="math:cvt-hex-decimal">
						<xsl:with-param name="value" select="substring($hex,number($hexlength)-5,2)"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="byte3">
			<xsl:choose>
				<xsl:when test="number($hexlength)>9">
					<xsl:call-template name="math:cvt-hex-decimal">
						<xsl:with-param name="value" select="substring($hex,number($hexlength)-7,2)"/>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:value-of select="($byte3*16777216)+($byte2*65536)+($byte1*256)+$byte0"/>
	</xsl:template>
</xsl:stylesheet>

