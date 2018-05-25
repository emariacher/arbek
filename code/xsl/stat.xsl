<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
<html>
<body bgcolor="lightgrey">
<h2>System dump</h2>
<xsl:apply-templates/> 
</body>
</html>
</xsl:template>

<xsl:template match="statistics">
  <table border="1">
  <tr><th>name</th><th>count</th>
  <th><font color="green">min</font></th><th><font color="blue">mean</font></th><th>max</th></tr>
  <xsl:apply-templates select="stat"/>
  </table>
</xsl:template>

<xsl:template match="stat">
  <tr>
   <xsl:apply-templates select="name"/>
   <td><span><xsl:value-of select="cpt"/></span></td>
   <xsl:if test="cpt!='0'">
    <xsl:choose>
     <xsl:when test="min=moy">
      <xsl:choose>
       <xsl:when test="max=moy">
        <td colspan="3" align="center"><span style="color:blue"><xsl:value-of select="moy"/></span></td>
       </xsl:when>
       <xsl:otherwise>
        <td colspan="2"><span style="color:blue"><xsl:value-of select="moy"/></span></td>
        <td><span><xsl:value-of select="max"/></span></td>
       </xsl:otherwise>
      </xsl:choose>
     </xsl:when>
     <xsl:otherwise>
      <td><span style="color:green"><xsl:value-of select="min"/></span></td>
      <td><span style="color:blue"><xsl:value-of select="moy"/></span></td>
     <td><span><xsl:value-of select="max"/></span></td>
     </xsl:otherwise>
    </xsl:choose>
   </xsl:if>
  </tr>
</xsl:template>

</xsl:stylesheet>

