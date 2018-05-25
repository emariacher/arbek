<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" version="4.0" encoding="UTF-8" indent="yes"/>
	<!-- Detail Report XML Parsing -->
	<xsl:template match="/">
		<html>
			<head>
				<META NAME="Author" CONTENT="Eric Mariacher"/>
				<META NAME="Description" CONTENT="Defect Report Eric Mariacher"/>
				<TITLE>Defect Report</TITLE>
			</head>
			<body>
				<h1 align="center">Defect Report</h1> for products:
				<h2>
			<!--list all products--><ul><xsl:for-each select="TestTrackData/defect/product[not(.=preceding::defect/product)]">
			<li><xsl:value-of select="."/></li></xsl:for-each></ul>
				</h2>
				<xsl:for-each select="TestTrackData">
					<p>.</p>
					<xsl:call-template name="defect"/>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>
	<xsl:template name="defect">
		<xsl:for-each select="defect">
			<p>.</p>
			<!--table border="1" bgcolor="wheat" width="100%"-->
			<xsl:value-of disable-output-escaping="yes" select="string('&lt;table border=&quot;1&quot; bgcolor=&quot;')"/>
			<xsl:choose>
				<xsl:when test="substring(defect-status,1,3)='Ope'">#EEB4B4</xsl:when>
				<xsl:when test="substring(defect-status,1,3)='Fix'">yellow</xsl:when>
				<xsl:when test="substring(defect-status,1,3)='Rel'">#CAFF70</xsl:when>
				<xsl:when test="substring(defect-status,1,3)='Clo'">limegreen</xsl:when>
				<xsl:otherwise>white</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of disable-output-escaping="yes" select="string('&quot; width=&quot;100%&quot;&gt;')"/>
			
			<tr>
				<th>
					<xsl:value-of select="defect-number"/>
				</th>
				<th align="left" colspan="2">
					<xsl:value-of select="summary"/>
					<br/>---<xsl:value-of select="product"/>---
				</th>
			</tr>
			<tr>
				<td>Severity/Priority</td>
				<xsl:value-of disable-output-escaping="yes" select="string('&lt;th bgcolor=&quot;')"/>
				<xsl:choose>
					<xsl:when test="priority='P0'">red</xsl:when>
					<xsl:when test="priority='P1'">orange</xsl:when>
					<xsl:when test="priority='P2'">deepskyblue</xsl:when>
					<xsl:when test="priority='P3'">green</xsl:when>
					<xsl:otherwise>white</xsl:otherwise>
				</xsl:choose>
				<xsl:value-of disable-output-escaping="yes" select="string('&quot; width=&quot;40%&quot;&gt;')"/>
							Engineering priority:
						<xsl:value-of select="priority"/>
				<xsl:value-of disable-output-escaping="yes" select="string('&lt;/th&gt;')"/>
				<xsl:value-of disable-output-escaping="yes" select="string('&lt;th bgcolor=&quot;')"/>
				<xsl:choose>
					<xsl:when test="substring(custom-field-value/@field-value[../@field-name='Marketing Severity'],1,1)='0'">red</xsl:when>
					<xsl:when test="substring(custom-field-value/@field-value[../@field-name='Marketing Severity'],1,1)='1'">orange</xsl:when>
					<xsl:when test="substring(custom-field-value/@field-value[../@field-name='Marketing Severity'],1,1)='2'">deepskyblue</xsl:when>
					<xsl:when test="substring(custom-field-value/@field-value[../@field-name='Marketing Severity'],1,1)='3'">green</xsl:when>
					<xsl:otherwise>white</xsl:otherwise>
				</xsl:choose>
				<xsl:value-of disable-output-escaping="yes" select="string('&quot; width=&quot;40%&quot;&gt;')"/>
				<xsl:value-of select="custom-field-value/@field-name[.='Marketing Severity']"/>: 
						<xsl:value-of select="custom-field-value/@field-value[../@field-name='Marketing Severity']"/>
				<xsl:value-of disable-output-escaping="yes" select="string('&lt;/th&gt;')"/>
			</tr>
			<tr>
				<td>Status/Champion</td>
				<td>
					<xsl:value-of select="defect-status"/>
				</td>
			</tr>
			<tr>
				<td>Description</td>
				<td colspan="2">
					<font size="-">
						<xsl:value-of select="substring(reported-by-record/description,1,500)"/>
					</font>
				</td>
			</tr>
			<tr>
				<td>Containment</td>
				<td colspan="2">
					<xsl:value-of select="workaround"/>
				</td>
			</tr>
			<tr>
				<td>Root causes</td>
				<td colspan="2">
					<xsl:value-of select="custom-field-value/@field-value[../@field-name='Root_Cause']"/>, 						
					</td>
			
			</tr>
			<tr>
				<td>Corrective action</td>
				<td colspan="2">
					<xsl:value-of select="custom-field-value/@field-value[../@field-name='Corrective_Action']"/>, 						
					</td>
			</tr>
			<tr>
				<td>Closure</td>
				<td colspan="2">
					<xsl:value-of select="custom-field-value/@field-value[../@field-name='Closure']"/>, 						
					</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="custom-field-value/@field-name[.='user_occurence']"/>
				</td>
				<td colspan="2">
					<xsl:value-of select="custom-field-value/@field-value[../@field-name='user_occurence']"/>, 						
					</td>
			</tr>
			<tr>
				<td>
					<xsl:value-of select="custom-field-value/@field-name[.='user_impact']"/>
				</td>
				<td colspan="2">
					<xsl:value-of select="custom-field-value/@field-value[../@field-name='user_impact']"/>, 						
					</td>
			</tr>
			<xsl:value-of disable-output-escaping="yes" select="string('&lt;/table&gt;')"/>
			<!--/table-->
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>
