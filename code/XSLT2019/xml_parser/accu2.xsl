<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math"
    exclude-result-prefixes="xs math"
    expand-text="yes"
    version="3.0">

    <xsl:mode streamable="yes" use-accumulators="#all" on-no-match="shallow-skip"/>
    
    <xsl:output indent="yes"/>

    <xsl:accumulator name="artist-count" as="xs:integer" initial-value="0" streamable="yes">
        <xsl:accumulator-rule match="artist" select="$value + 1"/>
    </xsl:accumulator>
    
<xsl:template match="/">
<html> 
<body>
<xsl:variable name="i" select="111"/>
  <h2>My CD Collection</h2>
  <table border="1">
    <tr bgcolor="#9acd32">
      <th style="text-align:left">Title</th>
      <th style="text-align:left">Artist</th>
    </tr>
    <xsl:for-each select="catalog/cd">
    <tr>
      <td><xsl:value-of select="title"/></td>
      <td><xsl:value-of select="artist"/></td>
    </tr>
    </xsl:for-each>
  </table>
  
  zorg
  <xsl:value-of select="$i"/>
  <xsl:value-of select="{accumulator-before('artist-count')}"/>
  zarg
</body>
</html>
</xsl:template>
</xsl:stylesheet>

