<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="queuedump">
  <table border="1"><tr><th>Qid</th><th>Size</th><th>Msgs in queue</th><th>EventGrp</th><th>wait list</th></tr>
  <xsl:apply-templates select="queue"/>
  </table>
</xsl:template>


<xsl:template match="queue">
  <tr>
  <xsl:apply-templates select="name"/>
  <xsl:apply-templates select="size"/>
  <xsl:apply-templates select="nmsg"/>
  <xsl:apply-templates select="oseventgrp"/>
  <td><xsl:apply-templates select="oseventitem"/></td>
  <xsl:apply-templates select="msgsptr"/>
  <xsl:apply-templates select="waitmsg"/>
  </tr>
</xsl:template>

<xsl:template match="oseventgrp">
  <td>
    <span><xsl:value-of select="."/></span>
  </td>
</xsl:template>

<xsl:template match="msgsptr">
  <tr><td></td><td colspan="4"><b>MsgsPtr</b>:
    <span style="font-family:courier; font-size:10px"><xsl:value-of select="."/></span>
  </td></tr>
</xsl:template>

<xsl:template match="waitmsg">
  <tr><td></td><td colspan="4">
    <span style="font-family:courier; font-size:10px; color:green"><xsl:value-of select="."/></span>
  </td></tr>
</xsl:template>

<xsl:template match="oseventitem">
      <xsl:if test=".!='0x0'">
       <xsl:value-of select="position()-1"/>:
       <xsl:value-of select="."/>
       <xsl:text>, </xsl:text>
      </xsl:if>
</xsl:template>

<xsl:template match="nmsg">
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

</xsl:stylesheet>

