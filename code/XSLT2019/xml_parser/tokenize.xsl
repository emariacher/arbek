<xsl:template match="/">
    <root>
        <xsl:apply-templates select="root/*"/>
    </root>
</xsl:template>

<xsl:template match="item">
    <p>
        <xsl:call-template name="tokenize">
            <xsl:with-param name="text" select="."/>
        </xsl:call-template>
    </p>
</xsl:template>

<xsl:template name="tokenize">
    <xsl:param name="text"/>
    <xsl:choose>
        <xsl:when test="contains($text, '§')">
            <xsl:if test="substring-before($text, '§') != ''">
                <text><xsl:value-of select="substring-before($text, '§')"/></text>
            </xsl:if>
            <xsl:call-template name="buildParagraphAnchor">
                <xsl:with-param name="tail" select="substring-after($text, '§')"/>
            </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
            <text><xsl:value-of select="$text"/></text>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template name="buildParagraphAnchor">
    <xsl:param name="tail"/>
    <xsl:variable name="paragraphNumber">
        <xsl:choose>
            <xsl:when test="contains($tail, ' ')">
                <xsl:value-of select="substring-before($tail, ' ')"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$tail"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:variable>
    <a href="/link#{$paragraphNumber}"><xsl:text>§</xsl:text><xsl:value-of select="$paragraphNumber"/></a>
    <xsl:if test="contains($tail, ' ')">
        <xsl:call-template name="tokenize">
            <xsl:with-param name="text" select="concat(' ', substring-after($tail, ' '))"/>
        </xsl:call-template>
    </xsl:if>
</xsl:template>