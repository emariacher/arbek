<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common" xmlns:loop="http://informatik.hu-berlin.de/loop" extension-element-prefixes="exsl">
	<xsl:output method="xml" />
  
  <!--Eric Mariacher 28jan08-->
	
	
	<xsl:template match="doors_xml_export">
		<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
			<Styles>
				<Style ss:ID="Default" ss:Name="Normal">
					<Alignment ss:Vertical="Bottom" ss:WrapText="1"/>
					<Borders/>
					<Font/>
					<Interior/>
					<NumberFormat/>
					<Protection/>
				</Style>
			</Styles>
			
			<Worksheet ss:Name="exportFromDoors">
				<Table>
					<Column ss:AutoFitWidth="0" ss:Width="200"/>
					<Column ss:AutoFitWidth="0" ss:Width="250"/>
					<Column ss:AutoFitWidth="0" ss:Width="50" ss:Span="2"/>
					<Column ss:AutoFitWidth="0" ss:Width="300"/>
					<Row>
						<Cell>
							<Data ss:Type="String">Title</Data>
						</Cell>
						<Cell>
							<Data ss:Type="String">Path</Data>
						</Cell>
						<Cell>
							<Data ss:Type="String">Type</Data>
						</Cell>
						<Cell>
							<Data ss:Type="String">Priority</Data>
						</Cell>
						<Cell>
							<Data ss:Type="String">Finality</Data>
						</Cell>
						<Cell>
							<Data ss:Type="String">Description</Data>
						</Cell>
					</Row>
					<xsl:apply-templates select="object[Type!='Ignore']"/>
				</Table>
			</Worksheet>
		</Workbook>
	</xsl:template>
	
	
	<xsl:template match="object">
		<xsl:for-each select="Description/req">
			<xsl:element name="Row">
				<xsl:attribute name="xmlns">urn:schemas-microsoft-com:office:spreadsheet</xsl:attribute>
				<Cell>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;Data ss:Type=&quot;String&quot;&gt;')" />
					<xsl:choose>
					<xsl:when test="position() &gt; 1">
										<xsl:value-of select="../../Title"/>_<xsl:value-of select="position()"/>
									</xsl:when>
									<xsl:otherwise ><xsl:value-of select="../../Title"/></xsl:otherwise>
									</xsl:choose>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/Data&gt;')" />
				</Cell>
				<Cell>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;Data ss:Type=&quot;String&quot;&gt;')" />
					<xsl:value-of select="../../Path"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/Data&gt;')" />
				</Cell>
				<Cell>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;Data ss:Type=&quot;String&quot;&gt;')" />
					<xsl:value-of select="../../Type"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/Data&gt;')" />
				</Cell>
				<Cell>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;Data ss:Type=&quot;String&quot;&gt;')" />
					<xsl:value-of select="../../Priority"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/Data&gt;')" />
				</Cell>
				<Cell>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;Data ss:Type=&quot;String&quot;&gt;')" />
					<xsl:value-of select="../../Finality"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/Data&gt;')" />
				</Cell>
				<Cell>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;Data ss:Type=&quot;String&quot;&gt;')" />
					<xsl:if test="position() &gt; 1">
						<xsl:value-of select="id"/>[<xsl:value-of select="head"/>]
				</xsl:if>
					<xsl:value-of select="desc"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/Data&gt;')" />
				</Cell>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
