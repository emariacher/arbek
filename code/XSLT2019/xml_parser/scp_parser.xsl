<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math"
    exclude-result-prefixes="xs math"
    expand-text="yes"
    version="3.0">
    <!--xsl:output method = "text" indent = "yes" /-->
    
              <xsl:template name = "bytesplit" >
               <xsl:param name = "sizeTable1" />
               <xsl:param name = "sizeTable2" />
               <xsl:param name = "sizeTable3" />
               <xsl:param name = "sizeTable4" />
               <xsl:param name = "sizeTable5" />
               <xsl:param name = "sizeTable7" />
               <xsl:param name = "sizeTable8" />
               <xsl:param name = "sizeTable9" />
               <xsl:param name = "sizeTable10" />
               <xsl:param name = "sizeTable11" />
               <xsl:param name = "sizeTable12" />
               <xsl:param name = "sizeTable13" />
               <xsl:param name = "pos" />
  [<xsl:value-of select = "$sizeTable1" /> - <xsl:value-of select = "$sizeTable2" /> - 
  <xsl:value-of select = "$sizeTable3" /> - <xsl:value-of select = "$sizeTable4" /> - <xsl:value-of select = "$sizeTable5" /> - 
  <xsl:value-of select = "$pos" />]
          </xsl:template>


<xsl:template match="/">
    <html>
    <head>
        <link rel="stylesheet" href="../xml_parser/style.css"></link>
    </head>
    <body >
        <h1>SCP protocol
			<xsl:value-of select="concat(protocol/parameter_set/@name,' ',protocol/parameter_set/@major, '.', protocol/parameter_set/@minor)"/> 
		</h1>
<!--	************	SCP parameter Fields	************		-->
        <xsl:for-each select="protocol/frame">
			<scp_param_desc> ___________________________________________________________________________________________________________________________ </scp_param_desc>
		    <scp_param_title> parameter <xsl:value-of select="concat(@parameter,' : ',@name)"/> </scp_param_title>
			<scp_param_desc> <xsl:value-of select = "@description" /> </scp_param_desc>			
<!--	************************************************************		-->
<!--	******************		Read Fields			****************		-->
<!--	************************************************************		-->				
				<xsl:choose>
					<xsl:when test="read">
<!--	************	Read Request Field		************		-->
<!--	************	Table containing all field	********		-->
						<table>
							<tr> <th colspan="3"> <scptitle> Read Request </scptitle> </th> </tr>
							<xsl:choose>
								<xsl:when test="read/request/*">
									<tr>
										<th> Name </th>
										<th> Type </th>
										<th> Size (bits) </th>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<tr> <td colspan="3" align="center"> No data </td> </tr>
								</xsl:otherwise>
							</xsl:choose>

							<xsl:for-each select="read/request/*">
								<xsl:choose>
									<xsl:when test="name() = 'field'">
										<tr>
											<td> <xsl:value-of select="@name"/> </td>
											<td> <xsl:value-of select="@type"/> </td>
											<xsl:choose>
												<xsl:when test="@type = 'boolean'">
													<td> 1 </td>
												</xsl:when>
												<xsl:otherwise>
													<td> <xsl:value-of select="@size"/> </td>
												</xsl:otherwise>
											</xsl:choose>
										</tr>
									</xsl:when>
									<xsl:when test="name() = 'sequence'">
										<tr>
											<td colspan="3"> <xsl:value-of select="concat(@name, ', sequence, length = ', @length_field, ' (range ', @min_occurrence, ' to ', @max_occurrence, '), composed of: ' )"/> </td>
										</tr>
										<xsl:for-each select="field">
											<tr>
												<td> <xsl:value-of select="concat('> ', @name)"/> </td>
												<td> <xsl:value-of select="@type"/> </td>
												<xsl:choose>
													<xsl:when test="@type = 'boolean'">
														<td> 1 </td>
													</xsl:when>
													<xsl:otherwise>
														<td> <xsl:value-of select="@size"/> </td>
													</xsl:otherwise>
												</xsl:choose>								
											</tr>
										</xsl:for-each>
									</xsl:when>
								</xsl:choose>
							</xsl:for-each>
						</table>
<!--	************	Detailed description of all fields	********		-->
						<xsl:for-each select="read/request/*">
							<xsl:choose>
								<xsl:when test="name() = 'sequence'">
									<scpdesc> <xsl:value-of select="concat('sequence: ', @name,' , length:',@length_field, ' (range ', @min_occurence, ' to ', @max_occurence, '), composed of')"/> </scpdesc>
									<xsl:for-each select="field">
										<xsl:choose>
											<xsl:when test="@type='uinteger'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
											</xsl:when>
											<xsl:when test="@type='enumerate'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="enumerator">
													<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='mask'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="group">
													<scpdesc> <xsl:value-of select="concat(' - ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='boolean'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
											</xsl:when>
										</xsl:choose>									
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="@type='uinteger'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
										</xsl:when>
										<xsl:when test="@type='enumerate'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="enumerator">
												<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='mask'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="group">
												<scpdesc> <xsl:value-of select="concat('>>',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='boolean'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
										</xsl:when>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
<!--	************	End of Read Request Field		************		-->

<!--	************	Read Response Field		************		-->
<!--	************	Table containing all field	********		-->
zobi
						<xsl:variable name="sizeTable" select="read/response/field/@size"/>
						<xsl:variable name="sizeTable3" select="sum($sizeTable[position() >= 1 and not(position() > 3)])"/>
						<xsl:variable name="sizeTable4" select="sum($sizeTable[position() >= 1 and not(position() > 4)])"/>
						<xsl:variable name="sizeTable5" select="sum($sizeTable[position() >= 1 and not(position() > 5)])"/>

						<table>
							<tr> <th colspan="3"> <scptitle> Read Response </scptitle> </th> </tr>
							<xsl:choose>
								<xsl:when test="read/response/*">
									<tr>
										<th> Name </th>
										<th> Type </th>
										<th> Size (bits) </th>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<tr> <td colspan="3" align="center"> No data </td> </tr>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:for-each select="read/response/*">
								<xsl:choose>
									<xsl:when test="name() = 'field'">
										<tr>
											<td> <xsl:value-of select="@name"/> </td>
											<td> <xsl:value-of select="@type"/> </td>
											<td>
											<xsl:choose>											
												<xsl:when test="@type = 'boolean'">
													 1 
												</xsl:when>
												<xsl:otherwise>
													 <xsl:value-of select="@size"/>
													 
													 <xsl:call-template name="bytesplit">
      <xsl:with-param name="sizeTable1"><xsl:value-of select="sum($sizeTable[not(position() > 1)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable2"><xsl:value-of select="sum($sizeTable[not(position() > 2)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable3"><xsl:value-of select="sum($sizeTable[not(position() > 3)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable4"><xsl:value-of select="sum($sizeTable[not(position() > 4)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable5"><xsl:value-of select="sum($sizeTable[not(position() > 5)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable6"><xsl:value-of select="sum($sizeTable[not(position() > 6)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable7"><xsl:value-of select="sum($sizeTable[not(position() > 7)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable8"><xsl:value-of select="sum($sizeTable[not(position() > 8)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable9"><xsl:value-of select="sum($sizeTable[not(position() > 9)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable10"><xsl:value-of select="sum($sizeTable[not(position() > 10)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable11"><xsl:value-of select="sum($sizeTable[not(position() > 11)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable12"><xsl:value-of select="sum($sizeTable[not(position() > 12)])"/></xsl:with-param>
      <xsl:with-param name="sizeTable13"><xsl:value-of select="sum($sizeTable[not(position() > 13)])"/></xsl:with-param>
      <xsl:with-param name="pos" select = "position()" />
    </xsl:call-template>
												</xsl:otherwise>
											</xsl:choose>
											</td>
										</tr>
											<xsl:choose>											
												<xsl:when test="position() = 3 and $sizeTable3 = 8">
													  <tr> <td colspan="3" align="center"> BYTE2</td> </tr>  
												</xsl:when>
												<xsl:when test="position() = 3 and $sizeTable3 = 16">
													 <tr> <td colspan="3" align="center"> BYTE3</td> </tr> 
												</xsl:when>
												<xsl:when test="position() = 4 and $sizeTable4 = 8">
													 <tr> <td colspan="3" align="center"> BYTE2</td> </tr> 
												</xsl:when>
												<xsl:when test="position() = 5 and $sizeTable5 = 8">
													 <tr> <td colspan="3" align="center"> BYTE2</td> </tr> 
												</xsl:when>
											</xsl:choose>
									</xsl:when>
									<xsl:when test="name() = 'sequence'">
										<tr>
											<td colspan="3"> <xsl:value-of select="concat(@name, ', sequence, length = ', @length_field, ' (range ', @min_occurrence, ' to ', @max_occurrence, '), composed of: ' )"/> </td>
										</tr>
										<xsl:for-each select="field">
											<tr>
												<td> <xsl:value-of select="concat('> ', @name)"/> </td>
												<td> <xsl:value-of select="@type"/> </td>
												<xsl:choose>
													<xsl:when test="@type = 'boolean'">
														<td> 1 </td>
													</xsl:when>
													<xsl:otherwise>
														<td> <xsl:value-of select="@size"/> </td>
													</xsl:otherwise>
												</xsl:choose>								
											</tr>
										</xsl:for-each>
									</xsl:when>
								</xsl:choose>
							</xsl:for-each>
						</table>
<!--	************	Detailed description of all fields	********		-->
						<xsl:for-each select="read/response/*">
							<xsl:choose>
								<xsl:when test="name() = 'sequence'">
									<scpdesc> <xsl:value-of select="concat('sequence: ', @name,' , length:',@length_field, ' (range ', @min_occurence, ' to ', @max_occurence, '), composed of')"/> </scpdesc>
									<xsl:for-each select="field">
										<xsl:choose>
											<xsl:when test="@type='uinteger'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
											</xsl:when>
											<xsl:when test="@type='enumerate'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="enumerator">
													<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='mask'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="group">
													<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='boolean'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
											</xsl:when>
										</xsl:choose>									
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="@type='uinteger'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
										</xsl:when>
										<xsl:when test="@type='enumerate'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="enumerator">
												<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='mask'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="group">
												<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='boolean'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
										</xsl:when>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
<!--	************	End of Read Response Field		************		-->
<!--	************	Read Response Detailed errors Field	********		-->
						<xsl:if test = "read/response_detailed_error">
							<table>
								<tr> <th colspan="3"> <scptitle> Response detailed errors </scptitle> </th> </tr>
								<tr> <th> name </th> <th> value </th> </tr>
								<xsl:for-each select="read/response_detailed_error/enumerator">
									<tr>
										<td> <xsl:value-of select="@name"/> </td>
										<td> <xsl:value-of select="current()"/> </td>
									</tr>
								</xsl:for-each>
							</table>
						</xsl:if>
					</xsl:when>
				</xsl:choose>
<!--	************************************************************		-->
<!--	************	End of Read Fields				************		-->
<!--	************************************************************		-->

<!--	************************************************************		-->
<!--	***************		Write Fields			****************		-->
<!--	************************************************************		-->
				<xsl:choose>
					<xsl:when test="write">
<!--	************	Write Request Field		************		-->
<!--	************	Table containing all field	********		-->
						<table>
							<tr> <th colspan="3"> <scptitle> Write Request </scptitle> </th> </tr>
							<xsl:choose>
								<xsl:when test="write/request/*">
									<tr>
										<th> Name </th>
										<th> Type </th>
										<th> Size (bits) </th>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<tr> <td colspan="3" align="center"> No data </td> </tr>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:for-each select="write/request/*">
								<xsl:choose>
									<xsl:when test="name() = 'field'">
										<tr>
											<td> <xsl:value-of select="@name"/> </td>
											<td> <xsl:value-of select="@type"/> </td>
											<xsl:choose>
												<xsl:when test="@type = 'boolean'">
													<td> 1 </td>
												</xsl:when>
												<xsl:otherwise>
													<td> <xsl:value-of select="@size"/> </td>
												</xsl:otherwise>
											</xsl:choose>
										</tr>
									</xsl:when>
									<xsl:when test="name() = 'sequence'">
										<tr>
											<td colspan="3"> <xsl:value-of select="concat(@name, ', sequence, length = ', @length_field, ' (range ', @min_occurrence, ' to ', @max_occurrence, '), composed of: ' )"/> </td>
										</tr>
										<xsl:for-each select="field">
											<tr>
												<td> <xsl:value-of select="concat('> ', @name)"/> </td>
												<td> <xsl:value-of select="@type"/> </td>
												<xsl:choose>
													<xsl:when test="@type = 'boolean'">
														<td> 1 </td>
													</xsl:when>
													<xsl:otherwise>
														<td> <xsl:value-of select="@size"/> </td>
													</xsl:otherwise>
												</xsl:choose>								
											</tr>
										</xsl:for-each>
									</xsl:when>
								</xsl:choose>
							</xsl:for-each>
						</table>
<!--	************	Detailed description of all fields	********		-->
						<xsl:for-each select="write/request/*">
							<xsl:choose>
								<xsl:when test="name() = 'sequence'">
									<scpdesc> <xsl:value-of select="concat('sequence: ', @name,' , length:',@length_field, ' (range ', @min_occurence, ' to ', @max_occurence, '), composed of')"/> </scpdesc>
									<xsl:for-each select="field">
										<xsl:choose>
											<xsl:when test="@type='uinteger'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
											</xsl:when>
											<xsl:when test="@type='enumerate'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="enumerator">
													<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='mask'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="group">
													<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='boolean'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
											</xsl:when>
										</xsl:choose>									
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="@type='uinteger'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
										</xsl:when>
										<xsl:when test="@type='enumerate'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="enumerator">
												<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='mask'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="group">
												<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='boolean'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
										</xsl:when>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
<!--	************	End of Write Request Field		************		-->

<!--	************	Write Response Field		************		-->
<!--	************	Table containing all field	********		-->
						<table>
							<tr> <th colspan="3"> <scptitle> Write Response </scptitle> </th> </tr>
							<xsl:choose>
								<xsl:when test="write/response/*">
									<tr>
										<th> Name </th>
										<th> Type </th>
										<th> Size (bits) </th>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<tr> <td colspan="3" align="center"> No data </td> </tr>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:for-each select="write/response/*">
								<xsl:choose>
									<xsl:when test="name() = 'field'">
										<tr>
											<td> <xsl:value-of select="@name"/> </td>
											<td> <xsl:value-of select="@type"/> </td>
											<xsl:choose>
												<xsl:when test="@type = 'boolean'">
													<td> 1 </td>
												</xsl:when>
												<xsl:otherwise>
													<td> <xsl:value-of select="@size"/> </td>
												</xsl:otherwise>
											</xsl:choose>
										</tr>
									</xsl:when>
									<xsl:when test="name() = 'sequence'">
										<tr>
											<td colspan="3"> <xsl:value-of select="concat(@name, ', sequence, length = ', @length_field, ' (range ', @min_occurrence, ' to ', @max_occurrence, '), composed of: ' )"/> </td>
										</tr>
										<xsl:for-each select="field">
											<tr>
												<td> <xsl:value-of select="concat('> ', @name)"/> </td>
												<td> <xsl:value-of select="@type"/> </td>
												<xsl:choose>
													<xsl:when test="@type = 'boolean'">
														<td> 1 </td>
													</xsl:when>
													<xsl:otherwise>
														<td> <xsl:value-of select="@size"/> </td>
													</xsl:otherwise>
												</xsl:choose>								
											</tr>
										</xsl:for-each>
									</xsl:when>
								</xsl:choose>
							</xsl:for-each>
						</table>
<!--	************	Detailed description of all fields	********		-->
						<xsl:for-each select="write/response/*">
							<xsl:choose>
								<xsl:when test="name() = 'sequence'">
									<scpdesc> <xsl:value-of select="concat('sequence: ', @name,' , length:',@length_field, ' (range ', @min_occurence, ' to ', @max_occurence, '), composed of')"/> </scpdesc>
									<xsl:for-each select="field">
										<xsl:choose>
											<xsl:when test="@type='uinteger'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
											</xsl:when>
											<xsl:when test="@type='enumerate'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="enumerator">
													<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='mask'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description)"/> </scpdesc>
												<xsl:for-each select="group">
													<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
												</xsl:for-each>
											</xsl:when>
											<xsl:when test="@type='boolean'">
												<scpdesc> <xsl:value-of select="concat('>', @name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
											</xsl:when>
										</xsl:choose>									
									</xsl:for-each>
								</xsl:when>
								<xsl:otherwise>
									<xsl:choose>
										<xsl:when test="@type='uinteger'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (range ', @min, ' to ', @max, ')')"/> </scpdesc>
										</xsl:when>
										<xsl:when test="@type='enumerate'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="enumerator">
												<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='mask'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description)"/> </scpdesc>
											<xsl:for-each select="group">
												<scpdesc> <xsl:value-of select="concat('>> ',@name, ' : ')"/> <xsl:value-of select="current()"/>  </scpdesc>
											</xsl:for-each>
										</xsl:when>
										<xsl:when test="@type='boolean'">
											<scpdesc> <xsl:value-of select="concat(@name,' : ',@description, ' (default = ', @default, ')')"/> </scpdesc>
										</xsl:when>
									</xsl:choose>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
<!--	************	End of Write Response Field		************		-->
<!--	************	Write Response Detailed errors Field	********		-->
						<xsl:if test = "write/response_detailed_error">
							<table>
								<tr> <th colspan="3"> <scptitle> Response detailed errors (Wr) </scptitle> </th> </tr>
								<tr> <th> name </th> <th> value </th> </tr>
								<xsl:for-each select="write/response_detailed_error/enumerator">
									<tr>
										<td> <xsl:value-of select="@name"/> </td>
										<td> <xsl:value-of select="current()"/> </td>
									</tr>
								</xsl:for-each>
							</table>
						</xsl:if>
					</xsl:when>
				</xsl:choose>			
        </xsl:for-each>
    </body>
    </html>
</xsl:template>

</xsl:stylesheet> 