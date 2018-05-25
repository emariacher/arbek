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

<xsl:template match="mempartdump">
  <xsl:apply-templates select="partdump"/>
</xsl:template>

<xsl:template match="partdump">
  <h3><span style="color:blue"> <xsl:value-of select="name"/> </span>
  : <span> <xsl:value-of select="bfrnbr"/> </span> buffers of
  <span><xsl:value-of select="bfrsize"/></span> bytes, from
  <span><xsl:value-of select="startaddr"/></span> to
  <span><xsl:value-of select="endaddr"/></span>. 
  <span><xsl:value-of select="free"/></span> buffers are still free.<br />
  <xsl:apply-templates select="bfrs"/></h3>
</xsl:template>

<xsl:template match="bfrs">
  <table border="1"><tr><th>index</th><th>address</th><th>status</th><th>count used</th><th>ticks since last use</th>
  <th>queue</th><th>last alloc</th><th>last free</th><th>prev alloc</th><th>prev free</th>
  </tr>
  <xsl:apply-templates select="bfr"/>
  </table>
</xsl:template>

<xsl:template match="bfr">
  <tr>
  <td><xsl:value-of select="index"/></td>
  <td><xsl:value-of select="startaddr"/></td>
  <td><xsl:apply-templates select="bstatus"/></td>
  <td><xsl:value-of select="cpt"/></td>
  <td><xsl:apply-templates select="tick"/></td>
  
  <xsl:choose>
   <xsl:when test="bstatus='0x41'">
    <td><xsl:apply-templates select="qqueue">
        <xsl:with-param name="bold">1</xsl:with-param>
        </xsl:apply-templates></td>
    <td><xsl:apply-templates select="lastalloc/taskline"/></td>
    <td><xsl:apply-templates select="lastfree/taskline"/></td>
    
   </xsl:when>
   <xsl:when test="bstatus='0x21'">
    <td><xsl:apply-templates select="qqueue">
        <xsl:with-param name="bold">2</xsl:with-param>
        </xsl:apply-templates></td>
    <td><xsl:apply-templates select="lastalloc/taskline"/></td>
    <td><xsl:apply-templates select="lastfree/taskline"/></td>
   </xsl:when>
   <xsl:when test="bstatus='0x1'">
    <td><xsl:apply-templates select="qqueue"/></td>
    <td><xsl:apply-templates select="lastalloc/taskline">
        <xsl:with-param name="bold">1</xsl:with-param>
        </xsl:apply-templates></td>
    <td><xsl:apply-templates select="lastfree/taskline"/></td>
  
   </xsl:when>
   <xsl:when test="bstatus='0x0'">
    <td><xsl:apply-templates select="qqueue"/></td>
    <td><xsl:apply-templates select="lastalloc/taskline"/></td>
    <td><xsl:apply-templates select="lastfree/taskline">
        <xsl:with-param name="bold">1</xsl:with-param>
        </xsl:apply-templates></td>
   </xsl:when>
   <xsl:otherwise>
    <td><xsl:apply-templates select="qqueue"/></td>
    <td><xsl:apply-templates select="lastalloc/taskline"/></td>
    <td><xsl:apply-templates select="lastfree/taskline"/></td>
   </xsl:otherwise>
  </xsl:choose>
  
  <td><xsl:apply-templates select="prevalloc/taskline"/></td>
  <td><xsl:apply-templates select="prevfree/taskline"/></td>
  </tr>
</xsl:template>

<xsl:template match="taskline">
   <xsl:param name="bold">0</xsl:param>
   <xsl:if test="line!='0'">
    <xsl:choose>
     <xsl:when test="$bold='1'">
      <P style="font-weight=bolder"><xsl:value-of select="task"/>-<xsl:value-of select="line"/></P>
     </xsl:when>
     <xsl:otherwise>
      <xsl:value-of select="task"/>-<xsl:value-of select="line"/>
     </xsl:otherwise>
    </xsl:choose>
   </xsl:if>
</xsl:template>

<xsl:template match="qqueue">
   <xsl:param name="bold">0</xsl:param>
   <xsl:if test="xline!='0'">
    <xsl:choose>
     <xsl:when test="$bold='1'">
      <span style="font-weight=bolder"><xsl:value-of select="qname"/>
      x<xsl:value-of select="xline"/></span>-&gt;r<xsl:value-of select="rline"/>
     </xsl:when>
     <xsl:when test="$bold='2'">
      <span style="font-weight=bolder"><xsl:value-of select="qname"/></span>
      x<xsl:value-of select="xline"/>-&gt;<span style="font-weight=bolder">r<xsl:value-of select="rline"/></span>
     </xsl:when>
     <xsl:otherwise>
    <xsl:value-of select="qname"/> x<xsl:value-of select="xline"/>-&gt;r<xsl:value-of select="rline"/>
     </xsl:otherwise>
    </xsl:choose>
   </xsl:if>
   
</xsl:template>

<xsl:template match="tick">
    <xsl:choose>
     <xsl:when test=".&gt;'2000' and ../bstatus!='0x0'">
      <span style="color:red"><xsl:value-of select="."/></span>
     </xsl:when>
     <xsl:when test=".&gt;'1000' and ../bstatus!='0x0'">
      <span style="color:magenta"><xsl:value-of select="."/></span>
     </xsl:when>
     <xsl:otherwise>
      <span style="color:green"><xsl:value-of select="."/></span>
     </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template match="bstatus">
    <xsl:choose>
     <xsl:when test=".='0x41'">
      <P style="color:magenta">queued</P>
     </xsl:when>
     <xsl:when test=".='0x21'">
      <P style="color:darkviolet">received</P>
     </xsl:when>
     <xsl:when test=".='0x1'">
      <P style="color:blue">allocated</P>
     </xsl:when>
     <xsl:when test=".='0x0'">
      <P style="color:green">free</P>
     </xsl:when>
     <xsl:otherwise>
     unknown[<xsl:value-of select="."/>]
     </xsl:otherwise>
    </xsl:choose>
</xsl:template>

</xsl:stylesheet>

