<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt">


  <xsl:template match="release-note">
    <html>
      <body bgcolor="lightgrey">
        <h2>Release from Branch [<font color="blue"><xsl:value-of select="@branch"/>
          </font>] from Mainline[<font color="blue"><xsl:value-of select="@mainline"/>
          </font>] 
        Based on Label[<font color="blue"><xsl:value-of select="@release"/>
          </font>] generated on <xsl:value-of select="@today"/>
        </h2>
        <table>
          <tr>
            <td valign="middle">
              <xsl:apply-templates select="binaryfile"/>
            </td>
            <td valign="middle">
              <xsl:apply-templates select="thisfile"/>
            </td>
          </tr>
        </table>
        <xsl:apply-templates select="laius"/>
        <p/>
        <table border="2" WIDTH="100%">
          <xsl:apply-templates select="stillopenbugs"/>
          <xsl:apply-templates select="label"/>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="binaryfile">
    Binary file: <A HREF="{href}"><xsl:value-of select="name"/>
    </A>
  </xsl:template>

  <xsl:template match="thisfile">
    This file: <A HREF="{href}"><xsl:value-of select="name"/>
    </A>
  </xsl:template>

  <xsl:template match="laius">
    <table border="1">
      <tr>
        <th width="50%">Target Audience</th>
        <th>
          <font color="red">Warning</font>
        </th>
      </tr>
      <tr>
        <td>
          <pre>
            <xsl:value-of select="targetaudience"/>
          </pre>
        </td>
        <td>
          <font color="red">
            <pre>
              <xsl:value-of select="warning"/>
            </pre>
          </font>
        </td>
      </tr>
      <tr>
        <th>Tested Features</th>
        <th>Un-Tested Features</th>
      </tr>
      <tr>
        <td>
          <pre>
            <xsl:value-of select="testedfeature"/>
          </pre>
        </td>
        <td>
          <pre>
            <xsl:value-of select="untestedfeature"/>
          </pre>
        </td>
      </tr>
      <tr>
        <th>Missing Features</th>
        <th>Known Bugs</th>
      </tr>
      <tr>
        <td>
          <pre>
            <xsl:value-of select="missingfeature"/>
          </pre>
        </td>
        <td>
          <pre>
            <xsl:value-of select="knownbug"/>
          </pre>
        </td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template match="defect">
    <xsl:choose>
      <xsl:when test="priority=0">
        <xsl:call-template name="coloredefect">
          <xsl:with-param name="zepriobgcolor">red</xsl:with-param>
          <xsl:with-param name="zebgcolor">red</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="priority=1">
        <xsl:call-template name="coloredefect">
          <xsl:with-param name="zepriobgcolor">orange</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="priority=2">
        <xsl:call-template name="coloredefect">
          <xsl:with-param name="zepriobgcolor">lightblue</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="priority=3">
        <xsl:call-template name="coloredefect">
          <xsl:with-param name="zepriobgcolor">#4eee94</xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="coloredefect"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="coloredefect">
    <xsl:param name="zepriobgcolor">lightgrey</xsl:param>
    <xsl:param name="zebgcolor">lightgrey</xsl:param>
    <tr>
      <th bgcolor="{$zepriobgcolor}">
        P<xsl:value-of select="priority"/>
      </th>
      <th bgcolor="{$zebgcolor}">
        <xsl:value-of select="defect-number"/>
      </th>
      <td bgcolor="{$zebgcolor}">
        <xsl:value-of select="summary"/>
      </td>
      <td bgcolor="{$zebgcolor}">
        <FONT SIZE="2">
          <xsl:value-of select="type"/>
        </FONT>
      </td>
      <td bgcolor="{$zebgcolor}">
        <FONT SIZE="2">
          <xsl:value-of select="status"/>
        </FONT>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="file">
    <tr>
      <td>
        <tt>
          <FONT SIZE="2">
            <xsl:value-of select="filename"/>
          </FONT>
        </tt>
      </td>
      <td>
        <tt>
          <FONT SIZE="2">
            <xsl:value-of select="version"/>
          </FONT>
        </tt>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="stillopenbugs">
    <tr>
      <th colspan="2" align="center">
        <font color="red">Still Open Bugs
            </font>
      </th>
    </tr>
    <tr>
      <td WIDTH="67%">
        <table border="1">
          <xsl:apply-templates select="defect">
            <xsl:sort select="defect-number" data-type="number" order="descending"/>
          </xsl:apply-templates>
        </table>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="nottouchedfiles">
    <tr>
      <td>
        <FONT SIZE="2">no bug attached to these files check-in.</FONT>
      </td>
      <td WIDTH="67%" align="right">
        <table>
          <xsl:apply-templates select="file"/>
        </table>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="nottouchedbugs">
    <tr>
      <td WIDTH="67%">
        <table border="1">
          <xsl:apply-templates select="defect">
            <xsl:sort select="defect-number" data-type="number" order="descending"/>
          </xsl:apply-templates>
        </table>
      </td>
      <td align="right">
        <FONT SIZE="2">no file check-in attached to these changes.</FONT>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="label">
    <xsl:param name="color">magenta</xsl:param>
    <tr>
      <xsl:choose>
        <xsl:when test="@state='before'">
          <th colspan="2" align="center">
            <font color="blue">PREVIOUS Label Build <xsl:value-of select="@labelnumber"/>
            </font> [<xsl:value-of select="@labeldate"/>]</th>
        </xsl:when>
        <xsl:when test="@state='after'">
          <th colspan="2" align="center">
            <font color="green">AFTER Label Build <xsl:value-of select="@labelnumber"/>
            </font> [<xsl:value-of select="@labeldate"/>]</th>
        </xsl:when>
        <xsl:otherwise>
          <th colspan="2" align="center" bgcolor="wheat">
            <font color="green">The Label Build <xsl:value-of select="@labelnumber"/>
            </font> [<xsl:value-of select="@labeldate"/>]
                  </th>
        </xsl:otherwise>
      </xsl:choose>
    </tr>
    <tr>
      <td WIDTH="67%" valign="top">
        <table border="1">
          <xsl:apply-templates select="defect">
            <xsl:sort select="defect-number" data-type="number" order="descending"/>
          </xsl:apply-templates>
        </table>
      </td>
      <td align="right">
        <table>
          <xsl:apply-templates select="touchedfiles/file"/>
        </table>
      </td>
    </tr>
    <xsl:apply-templates select="nottouchedfiles"/>
    <xsl:apply-templates select="nottouchedbugs"/>
  </xsl:template>
</xsl:stylesheet>

