<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt">


  <xsl:template match="/">
    <html>
      <body bgcolor="lightgrey">
        <h2>USB Descriptors</h2>
        <xsl:apply-templates/>
      </body>
    </html>
  </xsl:template>


  <xsl:template match="USBDescriptors">
    <xsl:apply-templates select="Descriptor"/>
  </xsl:template>

  <xsl:template match="Descriptor">
    <h3>
      <xsl:value-of select="TableTitle"/>
    </h3>
    <table>
      <tr>
        <xsl:choose>
          <xsl:when test="Table/@Type='201'">
            <th>Field</th>
            <th>Value</th>
          </xsl:when>
          <xsl:when test="Table/@Type='202'"></xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </tr>
      <xsl:for-each select="Table/Row">
        <tr>
          <td>
            <xsl:value-of select="Comment"/>
          </td>
          <td>
            <xsl:value-of select="Value"/>
          </td>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>

</xsl:stylesheet>

