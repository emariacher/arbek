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

<xsl:template match="taskdump">
  <table border="1"><tr><th rowspan="2">tid</th><th rowspan="2">prio</th><th rowspan="2">status</th>
  <th rowspan="2">Dly</th><th colspan="2">Time runned in &#x3bc;s</th><th colspan="4">Stack</th></tr>
  <tr><th>total runned</th><th>runned in last second</th><th>Free</th>
  <th>Size</th><th>Bot/ Ptr</th><th>Dump</th></tr>
            <xsl:apply-templates select="task">
              <xsl:sort select="prio" data-type="number" order="ascending"/>
            </xsl:apply-templates>
          
  </table>
</xsl:template>


<xsl:template match="task">
  <tr>
  <xsl:apply-templates select="name"/> 
  <xsl:apply-templates select="prio"/>
  <xsl:apply-templates select="status"/>
  <xsl:apply-templates select="ostcbdly"/>
  <xsl:apply-templates select="timerunnedtot"/>
  <xsl:apply-templates select="timerunnedprev"/>
  <xsl:apply-templates select="stack"/>
  </tr>
</xsl:template>

<xsl:template match="stack">
  <xsl:choose>
    <xsl:when test="free * 100 div size &lt;='30'">
     <td><span style="color:red"><xsl:value-of select="free"/></span></td>
    </xsl:when>
    <xsl:when test="free * 100 div size &lt;='50'">
     <td><span style="color:magenta"><xsl:value-of select="free"/></span></td>
    </xsl:when>
    <xsl:otherwise>
    <td>
    <span><xsl:value-of select="free"/></span>
    </td>
    </xsl:otherwise>
  </xsl:choose>
  
  <xsl:apply-templates select="size"/>
  <td>
    @<span style="font-family:courier; font-size:10px"><xsl:value-of select="bottom"/></span>/
    @<span style="font-family:courier; font-size:10px"><xsl:value-of select="pointer"/></span>
  </td>
  <td>
  <span style="font-family:courier; font-size:10px; color:green"><xsl:value-of select="dump"/></span>
  </td>
</xsl:template>

<xsl:template match="prio | free">
    <td>
    <span><xsl:value-of select="."/></span>
    </td>
</xsl:template>

<xsl:template match="status">
    <td>
    <xsl:choose>
    <xsl:when test=".='RUN!'">
     <span style="color:blue"><xsl:value-of select="."/></span>
    </xsl:when>
    <xsl:otherwise>
     <span><xsl:value-of select="."/></span>
    </xsl:otherwise>
    </xsl:choose>
    </td>
</xsl:template>

<xsl:template match="ostcbdly">
    <td>
    <xsl:choose>
    <xsl:when test=".='0'">
    </xsl:when>
    <xsl:otherwise>
     <span><xsl:value-of select="."/></span>
    </xsl:otherwise>
    </xsl:choose>
    </td>
</xsl:template>

<xsl:template match="timerunnedtot | timerunnedprev">
    <td>
    <xsl:choose>
    <xsl:when test=".>'50.0'">
     <span style="color:magenta">
     <xsl:value-of select="."/>%</span>
    </xsl:when>
    <xsl:when test=".>'30.0'">
     <span style="color:darkviolet">
     <xsl:value-of select="."/>%</span>
    </xsl:when>
    <xsl:when test=".>'10.0'">
     <span style="color:green">
     <xsl:value-of select="."/>%</span>
    </xsl:when>
    <xsl:when test=".>'1.0'">
     <span style="color:blue">
     <xsl:value-of select="."/>%</span>
    </xsl:when>
    <xsl:when test=".>'0.0'">
     <span><xsl:value-of select="."/>%</span>
    </xsl:when>
    <xsl:otherwise>
    </xsl:otherwise>
    </xsl:choose>
    </td>
</xsl:template>

</xsl:stylesheet>

