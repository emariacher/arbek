<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt">

  <xsl:key name="zeid" match="compounddef" use="@id" />
  <xsl:key name="zename" match="compounddef" use="compoundname" />
  <xsl:key name="zekind" match="compounddef" use="@kind" />

  <xsl:template match="doxygen">
    <html>
      <body bgcolor="lightgrey">
        <ul>
          <li>
            <a href="#introduction" name="INFO">introduction</a>
          </li>
          <li>
            <a href="#enums and defines">enums and defines</a>
          </li>
          <li>
            <a href="#structures">structures</a>
          </li>
          <li>
            <a href="#unions">unions</a>
          </li>
        </ul>
        <xsl:call-template name="titre">
          <xsl:with-param name="lvl">1</xsl:with-param>
          <xsl:with-param name="name">introduction</xsl:with-param>
        </xsl:call-template>
        <xsl:apply-templates select="compounddef/detaileddescription/sect1"/>
        <xsl:call-template name="titre">
          <xsl:with-param name="lvl">1</xsl:with-param>
          <xsl:with-param name="name">enums and defines</xsl:with-param>
        </xsl:call-template>
        <xsl:apply-templates select="key('zename','root')"/>
        <xsl:call-template name="titre">
          <xsl:with-param name="lvl">1</xsl:with-param>
          <xsl:with-param name="name">structures</xsl:with-param>
        </xsl:call-template>
        <xsl:apply-templates select="key('zekind','struct')"/>
        <xsl:call-template name="titre">
          <xsl:with-param name="lvl">1</xsl:with-param>
          <xsl:with-param name="name">unions</xsl:with-param>
        </xsl:call-template>
        <xsl:apply-templates select="key('zekind','union')"/>
      </body>
    </html>
  </xsl:template>



  <xsl:template match="compounddef">
    <h2>
      <xsl:value-of select="title"/>[<xsl:value-of select="compoundname"/>/<xsl:value-of select="@kind"/>]
        </h2>
    <table border="1" width="60%">
      <tr>
        <th>
          <xsl:apply-templates select="briefdescription"/>
        </th>
      </tr>
      <tr>
        <td>
          <xsl:apply-templates select="detaileddescription"/>
        </td>
      </tr>
    </table>
    <xsl:choose>
      <xsl:when test="@kind='group'">
        <xsl:apply-templates select="sectiondef"/>
      </xsl:when>
      <xsl:when test="@kind='union'">
        <table border="1" width="100%">
          <xsl:apply-templates select="sectiondef"/>
        </table>
      </xsl:when>
      <xsl:when test="@kind='struct'">
        <table border="1" width="100%">
          <xsl:apply-templates select="sectiondef"/>
        </table>
      </xsl:when>
      <xsl:otherwise></xsl:otherwise>
    </xsl:choose>
    <table border="1">
      <xsl:for-each select="innergroup">
        <tr>
          <td>innergroup:
            <xsl:apply-templates select="key('zeid',@refid)"/>
          </td>
        </tr>
      </xsl:for-each>
    </table>
  </xsl:template>

  <xsl:template match="title">
    <h2>
      title= <xsl:value-of select="."/>
    </h2>
  </xsl:template>

  <xsl:template match="itemizedlist">
    <ul>
      <xsl:apply-templates select="listitem"/>
    </ul>
  </xsl:template>
  <xsl:template match="listitem">
    <li>
      <xsl:value-of select="."/>
    </li>
  </xsl:template>


  <xsl:template match="sectiondef">
      sectiondef@kind = <xsl:value-of select="@kind"/>
    <xsl:apply-templates select="memberdef"/>
  </xsl:template>

  <xsl:template match="memberdef">
    <xsl:choose>
      <xsl:when test="@kind='define'">
        <tr>
          <th align="left" valign="top">
            <xsl:value-of select="name" />
          </th>
          <td>
            <xsl:value-of select="briefdescription/para"/>
          </td>
        </tr>
      </xsl:when>
      <xsl:when test="@kind='variable'">
        <tr>
          <th align="left" valign="top" width="25%">
            <xsl:value-of select="substring-before(definition,' ')" />
          </th>
          <th align="left" valign="top" width="35%">
            <xsl:value-of select="substring-after(definition,' ')" />
          </th>
          <td>
            <xsl:value-of select="briefdescription/para"/>
          </td>
        </tr>
      </xsl:when>
      <xsl:when test="@kind='enum'">
        <table border="1" width="100%">
          <tr>
            <th align="left" valign="top">
              <xsl:value-of select="name"/>
            </th>
            <td>
              <xsl:value-of select="@kind"/>
            </td>
          </tr>
          <tr>
            <td align="left" valign="top" width="40%">
              <xsl:apply-templates select="detaileddescription"/>
              <xsl:apply-templates select="briefdescription"/>
            </td>
            <td>
              <table>
                <xsl:apply-templates select="enumvalue"/>
              </table>
            </td>
          </tr>
        </table>
      </xsl:when>
      <xsl:otherwise></xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="enumvalue">
    <tr>
      <th align="left">
        <xsl:value-of select="name"/>
      </th>
      <td align="left">
        <xsl:value-of select="briefdescription/para"/>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="para">
    <p>
      <xsl:apply-templates/>
    </p>
  </xsl:template>

  <xsl:template name="titre">
    <xsl:param name="lvl">2</xsl:param>
    <xsl:param name="name"/>
    <xsl:param name="href">info</xsl:param>
    <xsl:choose>
      <xsl:when test="$lvl=1">
        <hr/>
      </xsl:when>
    </xsl:choose>
    <xsl:text disable-output-escaping="yes">&lt;h</xsl:text>
    <xsl:value-of select="$lvl"/>
    <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
    <a name="{$name}" href="#{$href}">
      <xsl:value-of select="$name"/>
    </a>
    <xsl:text disable-output-escaping="yes">&lt;/h</xsl:text>
    <xsl:value-of select="$lvl"/>
    <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
  </xsl:template>

</xsl:stylesheet>

