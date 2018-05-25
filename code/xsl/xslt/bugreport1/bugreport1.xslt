<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common" xmlns:loop="http://informatik.hu-berlin.de/loop" xmlns:date="http://exslt.org/dates-and-times"
extension-element-prefixes="exsl">
	<xsl:output method="html" version="4.0" encoding="UTF-8" indent="yes"/>
	<!-- Detail Report XML Parsing -->
	
	<!--xsl:variable name="now" select="date:date-time()"/-->
	
	<!--use of zedefects variable enables much faster execution of xslt processing as the sort and unique looping is done only once
	- -list all zedefects  Open, assigned to Eric Mariacher-->
	
	<xsl:variable name="zedefects">
		<xsl:for-each select="/TestTrackData/defect">
			<defect>
				<owner>
					<xsl:choose>
						<xsl:when test="contains(defect-status,';')">
							<xsl:value-of select="substring-before(substring-after(defect-status,'to'),';')"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="substring-after(defect-status,'to')"/>
						</xsl:otherwise>
					</xsl:choose>
				</owner>
				<zedefect-number>
					<xsl:value-of select="defect-number"/>
				</zedefect-number>
			</defect>
		</xsl:for-each>
	</xsl:variable>
	
	<xsl:template match="/">
		<html>
			<head>
				<META NAME="Author" CONTENT="Eric Mariacher"/>
				<META NAME="Description" CONTENT="Defect Report Eric Mariacher"/>
				<TITLE>Defect Report</TITLE>
			</head>
			<body>
				<h1 align="center">
					<A NAME="TOC">Defect Report</A>
				<!--generated at <xsl:text/>
					<xsl:value-of select="concat(date:hour-in-day($now), ':',date:minute-in-hour($now), ':',date:second-in-minute($now))"/>
					<xsl:text> on </xsl:text>
					<xsl:value-of select="concat(date:day-in-month($now), ' ',date:month-name($now), ' ',date:year($now))"/-->
				</h1>
				
				<table border="1">
					<tr>
						<th>Products</th>
						<th>Champions/Owners</th>
						<th>Marketing Priorities</th>
					</tr>
					<tr>
						<td rowspan="5">
			<!--list all products-->
							<ul>
								<xsl:for-each select="TestTrackData/defect/product[not(.=preceding::defect/product)]">
									<li>
										<b>
											<xsl:value-of select="."/>: 
										</b>
										<xsl:variable name="product">
											<xsl:value-of select="."/>
										</xsl:variable>
										<xsl:for-each select="/TestTrackData/defect[product=$product]">
											<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
											<xsl:value-of select="defect-number"/>
											<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
											<xsl:value-of select="defect-number"/>
											<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
									</li>
								</xsl:for-each>
							</ul>
						</td>
						<td rowspan="5">
			<!--list all zedefects  Open, assigned to Eric Mariacher-->
							<ul>
								<xsl:for-each select="exsl:node-set($zedefects)/defect[not(owner=preceding::owner)]">
									<xsl:sort/>
									<li>
										<b>
											<xsl:value-of select="owner"/>: 
										</b>
										<!--xsl:variable name="zowner">
											<xsl:value-of select="owner"/>
										</xsl:variable>
										<xsl:for-each select="exsl:node-set($zedefects)/defect[owner=$zowner]">
											<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
											<xsl:value-of select="zedefect-number"/>
											<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
											<xsl:value-of select="zedefect-number"/>
											<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each-->
									</li>
								</xsl:for-each>
							</ul>
						</td>
						<td bgcolor="red">
							<b>0, Show stopper: </b>
							<xsl:for-each select="/TestTrackData/defect[substring(custom-field-value/@field-value[../@field-name='Marketing Severity'],1,1)='0']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					</tr>
					<tr>
						<td bgcolor="orange">
							<b>1, Must be fixed: </b>
							<xsl:for-each select="/TestTrackData/defect[substring(custom-field-value/@field-value[../@field-name='Marketing Severity'],1,1)='1']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					</tr>
					<tr>
						<td bgcolor="deepskyblue">
							<b>2, Nice to see fixed: </b>
							<xsl:for-each select="/TestTrackData/defect[contains(custom-field-value/@field-value[../@field-name='Marketing Severity'],'2')]">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					</tr>
					<tr>
						<td bgcolor="green">
							<b>3, Do not care: </b>
							<xsl:for-each select="/TestTrackData/defect[contains(custom-field-value/@field-value[../@field-name='Marketing Severity'],'3')]">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					</tr>
					<tr>
						<td bgcolor="white">
							<b>no priority: </b>
							<xsl:for-each select="/TestTrackData/defect[not(string-length(custom-field-value/@field-value[../@field-name='Marketing Severity'])>1)]">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					</tr>
				</table>
				<p>.</p>
				<table border="1">
					<tr>
						<th>Eng Priority vs Status</th>
						<th bgcolor="red">P0</th>
						<th bgcolor="orange">P1</th>
						<th bgcolor="deepskyblue">P2</th>
						<th bgcolor="green">P3</th>
					</tr>
					<tr>
						<th bgcolor="#EEB4B4">Open</th>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P0' and substring(defect-status,1,3)='Ope']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P1' and substring(defect-status,1,3)='Ope']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P2' and substring(defect-status,1,3)='Ope']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P3' and substring(defect-status,1,3)='Ope']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					
					
					</tr>
					<tr>
						<th bgcolor="yellow">Fixed</th>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P0' and substring(defect-status,1,3)='Fix']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P1' and substring(defect-status,1,3)='Fix']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P2' and substring(defect-status,1,3)='Fix']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P3' and substring(defect-status,1,3)='Fix']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					
					
					</tr>
					<tr>
						<th bgcolor="#CAFF70">Released</th>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P0' and substring(defect-status,1,3)='Rel']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P1' and substring(defect-status,1,3)='Rel']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P2' and substring(defect-status,1,3)='Rel']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P3' and substring(defect-status,1,3)='Rel']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					
					
					</tr>
					<tr>
						<th bgcolor="limegreen">Closed</th>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P0' and substring(defect-status,1,3)='Clo']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P1' and substring(defect-status,1,3)='Clo']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P2' and substring(defect-status,1,3)='Clo']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
						<td>
							<xsl:for-each select="/TestTrackData/defect[priority='P3' and substring(defect-status,1,3)='Clo']">
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
								<xsl:value-of select="defect-number"/>
								<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
						</td>
					
					
					</tr>
					<tr>
						<th bgcolor="white">Other</th>
						<td colspan="4">
							<ul>
								<li>
									<b>Delayed: </b>
									<xsl:for-each select="/TestTrackData/defect[substring(defect-status,1,3)='Del']">
										<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
										<xsl:value-of select="defect-number"/>
										<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
										<xsl:value-of select="defect-number"/>
										<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
								</li>
								<li>
									<b>Rejected: </b>
									<xsl:for-each select="/TestTrackData/defect[substring(defect-status,1,3)='Rej']">
										<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
										<xsl:value-of select="defect-number"/>
										<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
										<xsl:value-of select="defect-number"/>
										<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
								</li>
								<li>
									<b>Verified: </b>
									<xsl:for-each select="/TestTrackData/defect[substring(defect-status,1,3)='Ver']">
										<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#')"/>
										<xsl:value-of select="defect-number"/>
										<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
										<xsl:value-of select="defect-number"/>
										<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>, 
 </xsl:for-each>
								</li>
							</ul>
						
						</td>
					</tr>
				</table>
				
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
				<!--table of content target (nam) and return to toc (href)-->
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#TOC&quot; name=&quot;')"/>
					<xsl:value-of select="defect-number"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
					<xsl:value-of select="defect-number"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>
				</th>
				<td align="left" colspan="2">
					<b><xsl:value-of select="summary"/></b>
					<br/>---<xsl:value-of select="product"/>---
				</td>
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
				<xsl:choose><!--test <custom-field-value field-name="Marketing Severity" field-value="1 XXXX"/>-->
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
					<b><xsl:value-of select="substring(defect-status,1,4)"/></b>---
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
					<xsl:value-of select="custom-field-value/@field-name[contains(.,'user_occurence')]"/>
				</td>
				<td colspan="2">
					<xsl:value-of select="custom-field-value/@field-value[contains(../@field-name,'user_occurence')]"/>, 						
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
