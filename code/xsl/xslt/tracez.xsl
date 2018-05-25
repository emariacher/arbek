<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="common.xsl"/>

<!--xsl:variable name="tracecolors">
 <it><n>E</n><fg>font-weight=bolder; color:red</fg><bg>#f0e0e0</bg><v>128</v></it>
 <it><n>T</n><fg>font-style=italic</fg><bg>#e0e0e0</bg><v>64</v></it>
 <it><n>1</n><fg>color:blue</fg><bg>#e0e0e0</bg><v>1</v></it>
 <it><n>1</n><fg>font-weight=bolder; color:blue</fg><bg>#e0e0e0</bg><v>256</v></it>
 <it><n>2</n><fg>color:darkviolet</fg><bg>#e0e0e0</bg><v>2</v></it>
 <it><n>3</n><fg>color:darkgreen</fg><bg>#e0e0e0</bg><v>4</v></it>
 <it><n>4</n><fg>color:darkmagenta</fg><bg>#e0e0e0</bg><v>8</v></it>
 <it><n>5</n><fg>color:darkcyan</fg><bg>#e0e0e0</bg><v>16</v></it>
 <it><n>6</n><fg>color:firebrick</fg><bg>#e0e0e0</bg><v>32</v></it>
</xsl:variable-->

<xsl:template match="/">
<html>
<body bgcolor="lightgrey">
<h2>traces</h2>
<xsl:apply-templates/> 
</body>
</html>
</xsl:template>

 
<xsl:template match="trace">
  <table border="1">
  <tr><th colspan="3"><xsl:value-of select="ts"/></th></tr>
  <tr><td colspan="3"><xsl:value-of select="header"/></td></tr>
  <tr><td colspan="3"><xsl:value-of select="footer"/></td></tr>
  <tr><td></td><th>components</th><th>levels</th></tr>
  <xsl:apply-templates select="traceattr"/>
  <xsl:apply-templates select="vector"/>
  </table>
  
  <hr /><table>
   <xsl:call-template name="tracetable">
    <xsl:with-param name="filter">select="re[ty=64 or ty=128 or co!=1 and co!=4]</xsl:with-param>
   </xsl:call-template>
   <xsl:apply-templates select="re[ty=64 or ty=128 or co!=1 and co!=4]"/>
  </table>
  
  <hr /><table>
   <xsl:call-template name="tracetable">
    <xsl:with-param name="filter">select="re[contains(tr,'[BFR64][0]')]"</xsl:with-param>
   </xsl:call-template>
   <xsl:apply-templates select="re[contains(tr,'[BFR64][0]')]"/>
  </table>
  
  <hr /><table>
   <xsl:call-template name="tracetable">
    <xsl:with-param name="filter">all available traces</xsl:with-param>
   </xsl:call-template>
   <xsl:apply-templates select="re">
    <xsl:with-param name="all">1</xsl:with-param>
   </xsl:apply-templates>
  </table>
  
</xsl:template>
 
 
<xsl:template name="tracetable">
 <xsl:param name="filter">zobi</xsl:param>
  <tr><th bgcolor="#f0e0e0" colspan="10"><xsl:value-of select="$filter"/></th></tr><tr>
  <th>source file</th><th>sou rce line</th><th>function</th><th>tra ce in dex</th>
  <th bgcolor="#e0e0e8">tick</th><th bgcolor="#e0e0e0">time in &#x3bc;s</th><th>task</th><th bgcolor="#e0e0e0">com pon ent</th>
  <th>l e v e l</th><th bgcolor="#e0e0e0" width="70%">trace</th></tr>
</xsl:template>
 
 
<xsl:template match="vector">
  <tr><td style="font-weight=bolder"><xsl:value-of select="name"/></td>
  <td><xsl:value-of select="comp"/></td>
  <td><xsl:value-of select="lvl"/></td></tr>
</xsl:template>
 
 
<xsl:template match="traceattr">
  <tr><td style="font-weight=bolder"><xsl:value-of select="name"/></td>
  <td><xsl:apply-templates select="comp/it/n"/></td>
  <td><xsl:apply-templates select="lvl/it/n"/></td></tr>
</xsl:template>
 
 
<xsl:template match="n | lvl">
  <span><xsl:value-of select="."/>, </span>
</xsl:template>
 
 
<xsl:template match="re">
 <xsl:param name="all">0</xsl:param>
  <tr>
  <xsl:apply-templates select="fi">
   <xsl:with-param name="all"><xsl:value-of select="$all"/></xsl:with-param>
  </xsl:apply-templates>
  <xsl:apply-templates select="fl">
   <xsl:with-param name="all"><xsl:value-of select="$all"/></xsl:with-param>
  </xsl:apply-templates>
  <xsl:apply-templates select="fu">
   <xsl:with-param name="all"><xsl:value-of select="$all"/></xsl:with-param>
  </xsl:apply-templates>
  <xsl:apply-templates select="id">
   <xsl:with-param name="size">10</xsl:with-param>
  </xsl:apply-templates>
  <xsl:apply-templates select="tc">
   <xsl:with-param name="all"><xsl:value-of select="$all"/></xsl:with-param>
  </xsl:apply-templates>
  <xsl:apply-templates select="ti"/>
  <!--<xsl:apply-templates select="ti"/>-->
  <xsl:apply-templates select="td">
   <xsl:with-param name="all"><xsl:value-of select="$all"/></xsl:with-param>
  </xsl:apply-templates>
  <xsl:apply-templates select="co">
   <xsl:with-param name="all"><xsl:value-of select="$all"/></xsl:with-param>
  </xsl:apply-templates>
  
  <xsl:apply-templates select="tr">
   <xsl:with-param name="lvl"><xsl:value-of select="ty"/></xsl:with-param>
  </xsl:apply-templates>
  
  </tr>
</xsl:template>
 
 
<xsl:template match="id">
  <xsl:param name="size">12</xsl:param>
  <td>
  <span style="font-size={$size}"><xsl:value-of select="."/></span>
  </td>
</xsl:template>
 
<xsl:template match="ti">
  <xsl:param name="size">12</xsl:param>
  <td bgcolor="#e0e0e0">
  <span style="font-size={$size}"><xsl:value-of select="."/></span>
  </td>
</xsl:template>
 
<xsl:template match="fl">
 <xsl:param name="size">12</xsl:param>
 <td>
 <xsl:if test=".!=preceding::fl[position()=1]">
  <span style="font-size={$size}"><xsl:value-of select="."/></span>
 </xsl:if>
 </td>
</xsl:template>
 
<xsl:template match="fu">
 <xsl:param name="size">12</xsl:param>
 <xsl:param name="all">0</xsl:param>
 <td>
   <xsl:choose>
    <xsl:when test="../ty=128">
     <span style="font-size={$size}; color:red"><xsl:value-of select="."/>()</span>
    </xsl:when>
    <xsl:when test="$all=1">
     <xsl:if test=".!=preceding::re[position()=1]/fu">
      <span style="font-size={$size}"><xsl:value-of select="."/></span>
     </xsl:if>
    </xsl:when>
    <xsl:otherwise>
     <span style="font-size={$size}"><xsl:value-of select="."/>()</span>
    </xsl:otherwise>
   </xsl:choose>
 </td>
</xsl:template>
 
<xsl:template match="fi">
 <xsl:param name="size">12</xsl:param>
 <xsl:param name="all">0</xsl:param>
 <td>
 <xsl:choose>
  <xsl:when test="$all=1">
   <xsl:if test=".!=preceding::re[position()=1]/fi">
    <span style="font-size={$size}"><xsl:value-of select="."/></span>
   </xsl:if>
  </xsl:when>
  <xsl:otherwise>
   <span style="font-size={$size}"><xsl:value-of select="."/></span>
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
    <span style="font-size={$size}"><xsl:value-of select="."/></span>
   </xsl:if>
  </xsl:when>
  <xsl:otherwise>
   <span style="font-size={$size}"><xsl:value-of select="."/></span>
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
    <span style="font-size={$size}"><xsl:value-of select="."/></span>
   </xsl:if>
  </xsl:when>
  <xsl:otherwise>
   <span style="font-size={$size}"><xsl:value-of select="."/></span>
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
      <xsl:with-param name="val"><xsl:value-of select="."/></xsl:with-param>
     </xsl:call-template>
    </span>
   </xsl:if>
  </xsl:when>
  <xsl:otherwise>
   <span style="font-size={$size}">
    <xsl:call-template name="component">
     <xsl:with-param name="val"><xsl:value-of select="."/></xsl:with-param>
    </xsl:call-template>
   </span>
  </xsl:otherwise>
 </xsl:choose>
 </td>
</xsl:template>
 
<xsl:template match="td">
 <xsl:param name="size">12</xsl:param>
 <xsl:param name="all">0</xsl:param>
 <td>
 <xsl:choose>
  <xsl:when test="$all=1">
   <xsl:if test=".!=../preceding::td[position()=1]">
    <span style="font-size={$size}">
     <xsl:call-template name="task">
      <xsl:with-param name="val"><xsl:value-of select="."/></xsl:with-param>
     </xsl:call-template>
    </span>
   </xsl:if>
  </xsl:when>
  <xsl:otherwise>
   <span style="font-size={$size}">
    <xsl:call-template name="task">
     <xsl:with-param name="val"><xsl:value-of select="."/></xsl:with-param>
    </xsl:call-template>
   </span>
  </xsl:otherwise>
 </xsl:choose>
 </td>
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
  <xsl:param name="fg">color:blue</xsl:param>
  <xsl:param name="bg">#e0f0f0</xsl:param>
  <td><span style="{$fg}; font-size=11"><xsl:value-of select="/trace/tracecolors/it[v=$lvl]/n"/></span></td>
  <td><span style="{$fg}; font-size=11"><xsl:value-of select="/trace/tracecolors"/></span></td>
  <td><span style="{$fg}; font-size=11"><xsl:value-of select="$tracecolors/it[1]"/></span></td>
  <td bgcolor="{$bg}"><span style="{$fg}; font-size=11; font-family=courier"><xsl:value-of select="."/></span></td>
</xsl:template>
 
 
</xsl:stylesheet>
