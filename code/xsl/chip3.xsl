<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html>
<body bgcolor="lightgrey">
<h2>My registers</h2>
<xsl:apply-templates/> 
</body>
</html>
</xsl:template>

<xsl:template match="list">
  <h4>
  </h4>
  <table border="1"><tr><th colspan="8">
  <xsl:apply-templates select="name"/> 
  <xsl:apply-templates select="mempart"/>
        <xsl:text> [</xsl:text>
  <xsl:number count="*" format="01"/>, 
  <xsl:value-of select="position()"/>,
        <xsl:text>]</xsl:text>
  </th></tr><tr>
  <xsl:apply-templates select="register"/>
  </tr></table>
  
  <xsl:text>Just for fun: </xsl:text>
  <xsl:for-each select="register">
      <xsl:value-of select="name"/>
      <xsl:if test="position()!=last()">
        <xsl:text>, </xsl:text>
      </xsl:if>
      <xsl:if test="position()=last()-1">
        <xsl:text> and </xsl:text>
      </xsl:if>
      <xsl:if test="position()=last()">
        <xsl:text>! [</xsl:text>
      <xsl:value-of select="position()"/>
        <xsl:text>]</xsl:text>
      </xsl:if>
  </xsl:for-each>
  
</xsl:template>


<xsl:template match="register">
<td>
<xsl:choose>
  <xsl:when test="itemtype='2'">
    <span style="color:blue">
    <xsl:value-of select="name"/>: </span>
  </xsl:when>
  <xsl:otherwise>
    <span style="color:darkgreen">
    <xsl:value-of select="name"/>: </span>
  </xsl:otherwise>
</xsl:choose>
  <xsl:apply-templates select="value"/>
</td>
</xsl:template>

<xsl:template match="name">
    <span style="color:blue">
    <xsl:value-of select="."/></span>
</xsl:template>

<xsl:template match="value">
<xsl:choose>
  <xsl:when test=".='0x0'">
    <span>
    <xsl:value-of select="."/></span>
  </xsl:when>
  <xsl:otherwise>
    <span style="color:red">
    <xsl:value-of select="."/></span>
  </xsl:otherwise>
</xsl:choose>
</xsl:template>

<xsl:template match="mempart">
<xsl:choose>
  <xsl:when test=".&gt;'10'">
    Direct access
  </xsl:when>
  <xsl:otherwise>
    Memory partition used: <span style="color:darkgreen">
    <xsl:value-of select="."/> </span>
  </xsl:otherwise>
</xsl:choose>
</xsl:template>

</xsl:stylesheet>

