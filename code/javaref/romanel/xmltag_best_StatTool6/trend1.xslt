<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" indent="yes"/>
	<!-- Detail Report XML Parsing -->
	
	
	<xsl:template match="/">
		<coucou>
			<xsl:for-each select="TestTrackData">
				<xsl:call-template name="defect"/>
			</xsl:for-each>
		</coucou>
	</xsl:template>
	<xsl:template name="defect">
		<xsl:for-each select="defect">
			<defect>
				<defect-number>
					<xsl:value-of select="defect-number"/>
				</defect-number>
				<product>
					<xsl:value-of select="product"/>
				</product>
				<type>
					<xsl:value-of select="type"/>
				</type>
				<priority>
					<xsl:value-of select="priority"/>
				</priority>
				<s_open>
					<xsl:value-of select="reported-by-record/date-found"/>
				</s_open>
				<xsl:for-each select="defect-event">
					<xsl:choose>
						<xsl:when test="contains(event-name,'ssign') or contains(event-name,'omment')">
							<xsl:value-of disable-output-escaping="yes" select="string('&lt;a_')"/>
							<xsl:value-of select="translate(event-name,' ()/','')"/>
							<xsl:value-of disable-output-escaping="yes" select="string('&gt;')"/>
							<xsl:value-of select="substring-before(event-date,' ')"/>
							<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a_')"/>
							<xsl:value-of select="translate(event-name,' ()/','')"/>
							<xsl:value-of disable-output-escaping="yes" select="string('&gt;')"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of disable-output-escaping="yes" select="string('&lt;s_')"/>
							<xsl:value-of select="translate(event-name,' ()/','')"/>
							<xsl:value-of disable-output-escaping="yes" select="string('&gt;')"/>
							<xsl:value-of select="substring-before(event-date,' ')"/>
							<xsl:value-of disable-output-escaping="yes" select="string('&lt;/s_')"/>
							<xsl:value-of select="translate(event-name,' ()/','')"/>
							<xsl:value-of disable-output-escaping="yes" select="string('&gt;')"/>
						</xsl:otherwise>
					</xsl:choose>
				
				</xsl:for-each>
			</defect>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>
