<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:msxsl="urn:schemas-microsoft-com:xslt">
	
	<xsl:variable name="tracecolors">
		<it>
			<n>E</n>
			<fg>font-weight=bolder; color:red</fg>
			<bg>#f0e0e0</bg>
			<v>128</v>
		</it>
		<it>
			<n>T</n>
			<fg>font-style=italic</fg>
			<bg>#e0e0e0</bg>
			<v>64</v>
		</it>
		<it>
			<n>1</n>
			<fg>color:blue</fg>
			<bg>#e0e0e0</bg>
			<v>1</v>
		</it>
		<it>
			<n>1</n>
			<fg>font-weight=bolder; color:blue</fg>
			<bg>#e0e0e0</bg>
			<v>256</v>
		</it>
		<it>
			<n>2</n>
			<fg>color:darkviolet</fg>
			<bg>#e0e0e0</bg>
			<v>2</v>
		</it>
		<it>
			<n>3</n>
			<fg>color:darkgreen</fg>
			<bg>#e0e0e0</bg>
			<v>4</v>
		</it>
		<it>
			<n>4</n>
			<fg>color:darkmagenta</fg>
			<bg>#e0e0e0</bg>
			<v>8</v>
		</it>
		<it>
			<n>5</n>
			<fg>color:darkcyan</fg>
			<bg>#e0e0e0</bg>
			<v>16</v>
		</it>
		<it>
			<n>6</n>
			<fg>color:firebrick</fg>
			<bg>#e0e0e0</bg>
			<v>32</v>
		</it>
	</xsl:variable>
	
	<xsl:template match="/">
		<html>
			<body bgcolor="lightgrey">
				<p>traces by <a HREF="mailto:eric_mariacher@eu.logitech.com">Eric Mariacher</a>
				</p>
				
				<table border="1">
					<tr>
						<td></td>
						<th>components</th>
						<th>levels</th>
					</tr>
					<xsl:apply-templates select="trace/traceattr"/>
					<xsl:apply-templates select="trace/vector"/>
					<tr>
						<th colspan="3">
							<xsl:value-of select="trace/ts"/>
						</th>
					</tr>
					<tr>
						<td colspan="3">
							<xsl:value-of select="trace/header"/>
						</td>
					</tr>
					<xsl:for-each select="trace/footer">
						<tr>
							
							<xsl:choose>
								<xsl:when test="contains(.,'regular')">
									<td colspan="3">
										<xsl:value-of select="."/>
									</td>
								</xsl:when>
								<xsl:otherwise>
									<td colspan="3" bgcolor="#e0f0e0">
										<xsl:value-of select="."/>
									</td>
								</xsl:otherwise>
							</xsl:choose>
						
						</tr>
					</xsl:for-each>
				</table>
				
				<table cellspacing="0">
					<xsl:call-template name="tracetable">
						<xsl:with-param name="filter">all</xsl:with-param>
					</xsl:call-template>
					<xsl:apply-templates select="trace/re">
						<xsl:sort data-type="number" select="id"/>
					</xsl:apply-templates>
				</table>
				
				<hr />
				<table cellspacing="0">
					<xsl:call-template name="tracetable">
						<xsl:with-param name="filter">Errors only</xsl:with-param>
					</xsl:call-template>
					<xsl:apply-templates select="trace/re[ty=128]">
						<xsl:sort data-type="number" select="id"/>
					</xsl:apply-templates>
				</table>
				
				<hr />
				<table cellspacing="0">
					<xsl:call-template name="tracetable">
						<xsl:with-param name="filter">select="re[contains(tr,'device')]"</xsl:with-param>
					</xsl:call-template>
					<xsl:apply-templates select="trace/re[contains(tr,'device')]">
						<xsl:with-param name="all">0</xsl:with-param>
						<xsl:sort data-type="number" select="id"/>
					</xsl:apply-templates>
				</table>
				
				<hr />
				<table cellspacing="0">
					<xsl:call-template name="tracetable">
						<xsl:with-param name="filter">Device 05</xsl:with-param>
					</xsl:call-template>
					<xsl:apply-templates select="trace/re[contains(tr,': 10 05') or contains(tr,': 11 05')]">
						<xsl:with-param name="all">0</xsl:with-param>
						<xsl:sort data-type="number" select="id"/>
					</xsl:apply-templates>
				</table>
				
				<hr />
				<table cellspacing="0">
					<xsl:call-template name="tracetable">
						<xsl:with-param name="filter">Xmt &amp; Rcv</xsl:with-param>
					</xsl:call-template>
					<xsl:apply-templates select="trace/re[ty=2 or ty=4]">
						<xsl:with-param name="all">0</xsl:with-param>
						<xsl:sort data-type="number" select="id"/>
					</xsl:apply-templates>
				</table>
			
			</body>
		</html>
	</xsl:template>
	
	
	<xsl:template name="tracetable">
		<xsl:param name="filter">zobi</xsl:param>
		
		<tr>
			<th bgcolor="#f0e0e0" colspan="10">
				<xsl:value-of select="$filter"/>
			</th>
		</tr>
		<tr>
			<th>source file</th>
			<th>sou rce line</th>
			<th WIDTH="8%">time</th>
			<th>ms</th>
			<th>D e v i c e</th>
			<th>Fe at ure Ind ex</th>
			<th>Fu nc ti on Ind ex</th>
			<th>T r a c e l e v e l</th>
			<th bgcolor="#e0e0e0" WIDTH="70%">trace</th>
		</tr>
	</xsl:template>
	
	
	<xsl:template match="name" priority="2">
		<th>
			<span style="color:blue">
				<xsl:value-of select="."/>
			</span>
		</th>
	</xsl:template>
	
	<xsl:template match="size">
		<td>
			<span>
				<xsl:value-of select="."/>
			</span>
		</td>
	</xsl:template>
	
	<xsl:template match="//tr/font">
		<font color="{@color}">**************<xsl:value-of select="."/>
		</font>
	</xsl:template>
	
	<xsl:template match="vector">
		<tr>
			<td style="font-weight=bolder">
				<xsl:value-of select="name"/>
			</td>
			<td>
				<xsl:value-of select="comp"/>
			</td>
			<td>
				<xsl:value-of select="lvl"/>
			</td>
		</tr>
	</xsl:template>
	
	
	<xsl:template match="traceattr">
		<tr>
			<td style="font-weight=bolder">
				<xsl:value-of select="name"/>
			</td>
			<td>
				<xsl:apply-templates select="comp/it/n"/>
			</td>
			<td>
				<xsl:apply-templates select="lvl/it/n"/>
			</td>
		</tr>
	</xsl:template>
	
	
	<xsl:template match="n | lvl">
		<span>
			<xsl:value-of select="."/>, </span>
	</xsl:template>
	
	
	<xsl:template match="re">
		<xsl:param name="all">1</xsl:param>
		<tr>
			<xsl:apply-templates select="fi">
				<xsl:with-param name="size">10</xsl:with-param>
				<xsl:with-param name="all">
					<xsl:value-of select="$all"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select="fl">
				<xsl:with-param name="size">10</xsl:with-param>
				<xsl:with-param name="all">
					<xsl:value-of select="$all"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select="fu">
				<xsl:with-param name="size">10</xsl:with-param>
				<xsl:with-param name="all">
					<xsl:value-of select="$all"/>
				</xsl:with-param>
			</xsl:apply-templates>
			
			<xsl:apply-templates select="id">
				<xsl:with-param name="size">10</xsl:with-param>
			</xsl:apply-templates>
			
			<xsl:apply-templates select="tc">
				<xsl:with-param name="all">
					<xsl:value-of select="$all"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select="ti"/>
<!--<xsl:apply-templates select="ti"/>-->
			<xsl:apply-templates select="td">
				<xsl:with-param name="all">
					<xsl:value-of select="$all"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select="co">
				<xsl:with-param name="all">
					<xsl:value-of select="$all"/>
				</xsl:with-param>
			</xsl:apply-templates>
			
			<xsl:apply-templates select="tr">
				<xsl:with-param name="lvl">
					<xsl:value-of select="ty"/>
				</xsl:with-param>
			</xsl:apply-templates>
		
		</tr>
	</xsl:template>
	
	
	<xsl:template match="id">
		<xsl:param name="size">12</xsl:param>
		<xsl:choose>
			<xsl:when test="../tu=1">
				<td bgcolor="#e0f0e0">
					<span style="font-size={$size}">
						<xsl:value-of select="."/>
					</span>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td>
					<span style="font-size={$size}">
						<xsl:value-of select="."/>
					</span>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template match="ti">
		<xsl:param name="size">12</xsl:param>
		<td bgcolor="#e0e0e0">
			<span style="font-size={$size}">
				<xsl:value-of select="."/>
			</span>
		</td>
	</xsl:template>
	
	<xsl:template match="fl">
		<xsl:param name="size">12</xsl:param>
		<td>
			<xsl:if test=".!=preceding::fl[position()=1]">
				<span style="font-size={$size}">
					<xsl:value-of select="."/>
				</span>
			</xsl:if>
		</td>
	</xsl:template>
	
	<xsl:template match="fu">
		<xsl:param name="size">12</xsl:param>
		<xsl:param name="all">1</xsl:param>
		<td>
			<xsl:choose>
				<xsl:when test="../ty=128">
					<span style="font-size={$size}; color:red">
						<xsl:value-of select="."/>()</span>
				</xsl:when>
				<xsl:when test="$all=1">
					<xsl:if test=".!=preceding::re[position()=1]/fu">
						<span style="font-size={$size}">
							<xsl:value-of select="."/>()</span>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<span style="font-size={$size}">
						<xsl:value-of select="."/>()</span>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	
	<xsl:template match="fi">
		<xsl:param name="size">12</xsl:param>
		<xsl:param name="all">1</xsl:param>
		<td>
			<xsl:choose>
				<xsl:when test="$all=1">
					<xsl:if test=".!=preceding::re[position()=1]/fi">
						<span style="font-size={$size}">
							<xsl:value-of select="."/>
						</span>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<span style="font-size={$size}">
						<xsl:value-of select="."/>
					</span>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	
	<xsl:template match="fl">
		<xsl:param name="size">12</xsl:param>
		<xsl:param name="all">0</xsl:param>
		<td>
			<xsl:choose>
				<xsl:when test="$all=1">
					<xsl:if test=".!=preceding::re[position()=1]/fl">
						<span style="font-size={$size}">
							<xsl:value-of select="."/>
						</span>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<span style="font-size={$size}">
						<xsl:value-of select="."/>
					</span>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	
	<xsl:template match="tc">
		<xsl:param name="size">12</xsl:param>
		<xsl:param name="all">0</xsl:param>
		<td bgcolor="#e0e0e8">
			<xsl:choose>
				<xsl:when test="$all=1">
					<xsl:if test=".!=../preceding-sibling::*[position()=1]/tc">
						<span style="font-size={$size}">
							<xsl:value-of select="."/>
						</span>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<span style="font-size={$size}">
						<xsl:value-of select="."/>
					</span>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	
	<xsl:template match="co">
		<xsl:param name="size">12</xsl:param>
		<xsl:param name="all">0</xsl:param>
		<td bgcolor="#e0e0e0">
			<xsl:choose>
				<xsl:when test="$all=1">
					<xsl:if test=".!=../preceding-sibling::*[position()=1]/co">
						<span style="font-size={$size}">
							<xsl:call-template name="component">
								<xsl:with-param name="val">
									<xsl:value-of select="."/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<span style="font-size={$size}">
						<xsl:call-template name="component">
							<xsl:with-param name="val">
								<xsl:value-of select="."/>
							</xsl:with-param>
						</xsl:call-template>
					</span>
				</xsl:otherwise>
			</xsl:choose>
		</td>
	</xsl:template>
	
	<xsl:template match="td">
		<xsl:param name="size">12</xsl:param>
		<xsl:param name="all">0</xsl:param>
		<xsl:choose>
			<xsl:when test="../tu=1">
				<td bgcolor="#e0f0e0">
					<xsl:if test=".!=../preceding::td[position()=1]">
						<span style="font-size={$size}">
							<xsl:call-template name="task">
								<xsl:with-param name="val">
									<xsl:value-of select="."/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</xsl:if>
				</td>
			</xsl:when>
			<xsl:when test="$all=1">
				<td>
					<xsl:if test=".!=../preceding::td[position()=1]">
						<span style="font-size={$size}">
							<xsl:call-template name="task">
								<xsl:with-param name="val">
									<xsl:value-of select="."/>
								</xsl:with-param>
							</xsl:call-template>
						</span>
					</xsl:if>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<td>
					<span style="font-size={$size}">
						<xsl:call-template name="task">
							<xsl:with-param name="val">
								<xsl:value-of select="."/>
							</xsl:with-param>
						</xsl:call-template>
					</span>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
	<xsl:template name="component">
		<xsl:param name="val">16</xsl:param>
		<span>
			<xsl:value-of select="//traceattr/comp/it[v=$val]/n"/>
		</span>
	</xsl:template>
	
	<xsl:template name="task">
		<xsl:param name="val">16</xsl:param>
		<span>
			<xsl:value-of select="//tasklist/it[v=$val]/n"/>
		</span>
	</xsl:template>
	
	<xsl:template match="tr">
		<xsl:param name="lvl">0</xsl:param>
		<xsl:choose>
			<xsl:when test="$lvl=2 or $lvl=4">
				<xsl:call-template name="parsehidpp20">
					<xsl:with-param name="string">
						<xsl:value-of select="."/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<td bgcolor="#e0f0e0"/>
				<td bgcolor="#d0e0d0"/>
				<td bgcolor="#e8f0e8"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:call-template name="trz">
			<xsl:with-param name="string">
				<xsl:value-of select="."/>
			</xsl:with-param>
			<xsl:with-param name="fg">
				<xsl:value-of select="msxsl:node-set($tracecolors)/it[v=$lvl]/fg"/>
			</xsl:with-param>
			<xsl:with-param name="bg">
				<xsl:value-of select="msxsl:node-set($tracecolors)/it[v=$lvl]/bg"/>
			</xsl:with-param>
			<xsl:with-param name="lvl">
				<xsl:value-of select="msxsl:node-set($tracecolors)/it[v=$lvl]/n"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	
	
	
	<xsl:template name="trz">
		<xsl:param name="fg">color:blue</xsl:param>
		<xsl:param name="bg">#e0e0e0</xsl:param>
		<xsl:param name="lvl">0</xsl:param>
		<xsl:param name="string">rien</xsl:param>
		<td>
			<p style="{$fg}; font-size=11">
				<xsl:value-of select="$lvl"/>
			</p>
		</td>
		<td bgcolor="{$bg}">
			<p style="{$fg}; font-size=11; font-family=courier">
				<xsl:value-of select="$string"/>
			</p>
		</td>
	</xsl:template>
	
	
	
	
	<xsl:template name="parsehidpp20">
		<xsl:param name="bg1">#e0f0e0</xsl:param>
		<xsl:param name="bg2">#d0e0d0</xsl:param>
		<xsl:param name="bg3">#e8f0e8</xsl:param>
		<xsl:param name="string">rien</xsl:param>
		<xsl:variable name="bfr">
			<xsl:value-of select="substring-after($string,': ')"/>
		</xsl:variable>
		<xsl:variable name="sl">
			<xsl:value-of select="substring-before($bfr,' ')"/>
		</xsl:variable>
		<xsl:variable name="bfr1">
			<xsl:value-of select="substring-after($bfr,' ')"/>
		</xsl:variable>
		<xsl:variable name="dev">
			<xsl:value-of select="substring-before($bfr1,' ')"/>
		</xsl:variable>
		<xsl:variable name="bfr2">
			<xsl:value-of select="substring-after($bfr1,' ')"/>
		</xsl:variable>
		<xsl:variable name="featindex">
			<xsl:value-of select="substring-before($bfr2,' ')"/>
		</xsl:variable>
		<xsl:variable name="bfr3">
			<xsl:value-of select="substring-after($bfr2,' ')"/>
		</xsl:variable>
		<xsl:variable name="funcindex">
			<xsl:value-of select="substring-before($bfr3,' ')"/>
		</xsl:variable>
		<td bgcolor="{$bg1}">
			<p style="font-size=10; font-family=courier">
				<xsl:value-of select="$dev"/>
			</p>
		</td>
		<td bgcolor="{$bg2}">
			<xsl:choose>
				<xsl:when test="$featindex='8F'">
					<p style="color:firebrick; font-size=10; font-family=courier">
				 HID++1.0 Error
				 </p>
				</xsl:when>
				<xsl:when test="$featindex='FF'">
					<p style="color:red; font-size=10; font-family=courier">
				 HID++2.0 Error
				 </p>
				</xsl:when>
				<xsl:otherwise>
					<p style="font-size=10; font-family=courier">
						<xsl:value-of select="$featindex"/>
					</p>
				</xsl:otherwise>
			</xsl:choose>
		</td>
		<td bgcolor="{$bg3}">
			<p style="font-size=10; font-family=courier">
				<xsl:value-of select="substring($funcindex,1,1)"/>
			</p>
		</td>
	
	</xsl:template>


</xsl:stylesheet>
