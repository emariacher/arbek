<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:exsl="http://exslt.org/common" xmlns:loop="http://informatik.hu-berlin.de/loop" xmlns:date="http://exslt.org/dates-and-times" extension-element-prefixes="exsl">
	<xsl:output method="html" version="4.0" encoding="UTF-8" indent="yes"/>
	<!-- Detail Report XML Parsing -->
	
	<!--xsl:variable name="now" select="date:date-time()"/-->
	
	<!--use of zedefects variable enables much faster execution of xslt processing as the sort and unique looping is done only once
	- -list all zedefects  Open, assigned to Eric Mariacher-->
	
	<xsl:variable name="zedefects">
		<xsl:for-each select="/TestTrackData/defect">
			<xsl:sort />
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
				<!--META NAME="Author" CONTENT="Eric Mariacher"/>
				<META NAME="Description" CONTENT="Defect Report Eric Mariacher"/-->
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
	
	<xsl:template name="tdstatecolor">
		<xsl:param name="defstat">zobi</xsl:param>
		<xsl:value-of disable-output-escaping="yes" select="string('&lt;td bgcolor=&quot;')"/>
		<xsl:choose>
			<xsl:when test="substring($defstat,1,3)='Ope'">#EEB4B4</xsl:when>
			<xsl:when test="substring($defstat,1,3)='Fix'">yellow</xsl:when>
			<xsl:when test="substring($defstat,1,3)='Rel'">#CAFF70</xsl:when>
			<xsl:when test="substring($defstat,1,3)='Clo'">limegreen</xsl:when>
			<xsl:otherwise>white</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of disable-output-escaping="yes" select="string('&quot; &gt;')"/>
	</xsl:template>
	
	<xsl:template name="tdpriocolor">
		<xsl:param name="defprio">zobi</xsl:param>
		<xsl:value-of disable-output-escaping="yes" select="string('&lt;td bgcolor=&quot;')"/>
		<xsl:choose>
			<xsl:when test="$defprio='P0'">red</xsl:when>
			<xsl:when test="$defprio='P1'">orange</xsl:when>
			<xsl:when test="$defprio='P2'">deepskyblue</xsl:when>
			<xsl:when test="$defprio='P3'">green</xsl:when>
			<xsl:otherwise>white</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of disable-output-escaping="yes" select="string('&quot; &gt;')"/>
	</xsl:template>
	
	<xsl:template name="defect">
		<table border="1">
			<tr>
				<th>Pr io ri ty</th>
				<th>Product</th>
				<th>Status / Bug Number / Type</th>
				<th>Title</th>
				<th>Status / Owner</th>
				<th>Description</th>
				<th>Comments</th>
			</tr>
			<xsl:for-each select="defect">
				<tr>
					<xsl:call-template name="tdpriocolor">
						<xsl:with-param name="defprio">
							<xsl:value-of select="priority"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:value-of select="priority"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/td&gt;')"/>
					
					<xsl:call-template name="tdstatecolor">
						<xsl:with-param name="defstat">
							<xsl:value-of select="defect-status"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:value-of select="product"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/td&gt;')"/>
					
					<xsl:call-template name="tdstatecolor">
						<xsl:with-param name="defstat">
							<xsl:value-of select="defect-status"/>
						</xsl:with-param>
					</xsl:call-template>
					<b>
						<xsl:value-of select="substring(defect-status,1,4)"/>
					</b>:
				<!--table of content target (nam) and return to toc (href)-->
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;a href=&quot;#TOC&quot; name=&quot;')"/>
					<xsl:value-of select="defect-number"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')"/>
					<xsl:value-of select="defect-number"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/a&gt;')"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;br/&gt;')"/>
					<i>
						<xsl:value-of select="type"/>
					</i>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/td&gt;')"/>
					<xsl:call-template name="tdstatecolor">
						<xsl:with-param name="defstat">
							<xsl:value-of select="defect-status"/>
						</xsl:with-param>
					</xsl:call-template>
					<b>
						<xsl:value-of select="summary"/>
					</b>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/td&gt;')"/>
					<xsl:call-template name="tdstatecolor">
						<xsl:with-param name="defstat">
							<xsl:value-of select="defect-status"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:value-of select="defect-status"/>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/td&gt;')"/>
					<xsl:call-template name="tdstatecolor">
						<xsl:with-param name="defstat">
							<xsl:value-of select="defect-status"/>
						</xsl:with-param>
					</xsl:call-template>
					<font size="-">
						<xsl:value-of select="substring(reported-by-record/description,1,500)"/>
					</font>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/td&gt;')"/>
					<xsl:call-template name="tdstatecolor">
						<xsl:with-param name="defstat">
							<xsl:value-of select="defect-status"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:for-each select="defect-event[event-name='Comment']">
					[<xsl:value-of select="event-date"/>:
					<xsl:value-of select="event-author/first-name"/>
						<xsl:value-of select="substring(event-author/last-name,1,1)"/>]
					<xsl:value-of select="substring(notes,1,100)"/>
						<xsl:value-of disable-output-escaping="yes" select="string('&lt;br/&gt;')"/>
					</xsl:for-each>
					<xsl:value-of disable-output-escaping="yes" select="string('&lt;/td&gt;')"/>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
