<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:math="http://www.w3.org/2005/xpath-functions/math"
    exclude-result-prefixes="xs math"
    expand-text="yes"
    version="3.0">

    <xsl:mode streamable="yes" use-accumulators="#all" on-no-match="shallow-skip"/>
    
    <xsl:output indent="yes"/>

    <xsl:accumulator name="row-count" as="xs:integer" initial-value="0" streamable="yes">
        <xsl:accumulator-rule match="row" select="$value + 1"/>
    </xsl:accumulator>

    <xsl:accumulator name="col-count" as="xs:integer" initial-value="0" streamable="yes">
        <xsl:accumulator-rule match="row" select="0"/>
        <xsl:accumulator-rule match="row/col" select="$value + 1"/>
    </xsl:accumulator>

    <xsl:template match="root">
    <html><body>
    <h1>titre</h1>
            <!--xsl:apply-templates/-->
        </body></html>
    </xsl:template>
    
    <xsl:template match="col">
        <data row="{accumulator-before('row-count')}" col="{accumulator-before('col-count')}">{.}</data>
    </xsl:template>
    
</xsl:stylesheet>