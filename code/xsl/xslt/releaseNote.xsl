<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt">


  <xsl:template match="release-note">
    <html>
      <body bgcolor="lightgrey">
        <xsl:choose>
          <xsl:when test="@buildmode=301">
            <h2>Release Based on BUGS Labeled[<xsl:value-of select="@release"/>]</h2>
          </xsl:when>
          <xsl:when test="@buildmode=302">
            <h2>Release Based on Label[<xsl:value-of select="@release"/>]</h2>
          </xsl:when>
        </xsl:choose>
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
        <table>
          <tr>
            <td width="60%">
              <table border="1">
                <tr>
                  <th colspan="2">Traceability level of release <xsl:value-of select="@release"/>
            [<xsl:value-of select="count(defect)"/> defects, <xsl:value-of select="@numberoffiles"/> files]</th>
                </tr>
                <tr>
                  <td>number of files<br/>not meeting feature</td>
                  <td align="center">feature tested</td>
                </tr>
                <tr>
                  <td>
                    <xsl:choose>
                      <xsl:when test="@buildmode=301">
                        <table bgcolor="green">
                          <tr>
                            <th>OK</th>
                          </tr>
                        </table>
                      </xsl:when>
                      <xsl:when test="@buildmode=302">
                        <table bgcolor="yellow">
                          <tr>
                            <th>No, manual label</th>
                          </tr>
                        </table>
                      </xsl:when>
                    </xsl:choose>
                  </td>
                  <td>Automatic generated release based on a strong bi-directional bug-file relationship</td>
                </tr>
                <xsl:call-template name="checkbox">
                  <xsl:with-param name="label">Bugs product name start with Mainline name</xsl:with-param>
                  <xsl:with-param name="checked">
                    <xsl:value-of select="@bugbelongs2samemainline"/>
                  </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="checkbox">
                  <xsl:with-param name="label">Bugs are in fixed, released or closed state</xsl:with-param>
                  <xsl:with-param name="checked">
                    <xsl:value-of select="@bugstatusfixed"/>
                  </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="checkbox">
                  <xsl:with-param name="label">No Cross File Bug dependencies</xsl:with-param>
                  <xsl:with-param name="checked">
                    <xsl:value-of select="@crossfilebugchecked"/>
                  </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="checkbox">
                  <xsl:with-param name="label">Files version == 1 are attached to bugs</xsl:with-param>
                  <xsl:with-param name="checked">
                    <xsl:value-of select="@allfilesattached2bugs"/>
                  </xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="checkbox">
                  <xsl:with-param name="label">Files versions &gt; 1 are attached to bugs</xsl:with-param>
                  <xsl:with-param name="checked">
                    <xsl:value-of select="@allfilesnotv1attached2bugs"/>
                  </xsl:with-param>
                </xsl:call-template>
              </table>
            </td>
            <td>
              <table border="1">
                <tr>
                  <th align="center" colspan="2">Status of Defects part of release <xsl:value-of select="@release"/></th>
                </tr>
                <xsl:call-template name="bugstatuslist">
                  <xsl:with-param name="status">Found</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="bugstatuslist">
                  <xsl:with-param name="status">Open</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="bugstatuslist">
                  <xsl:with-param name="status">Fixed</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="bugstatuslist">
                  <xsl:with-param name="status">Released</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="bugstatuslist">
                  <xsl:with-param name="status">Verified</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="bugstatuslist">
                  <xsl:with-param name="status">Closed</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="bugstatuslist">
                  <xsl:with-param name="status">Rejected</xsl:with-param>
                </xsl:call-template>
              </table>
            </td>
          </tr>
        </table>
        <table>
          <tr>
            <th>Defect</th>
            <th>Release Type</th>
            <th>Summary</th>
            <th>Status</th>
            <th>File versions linked to defect</th>
          </tr>
          <xsl:apply-templates select="defect"/>
        </table>
      </body>
    </html>
  </xsl:template>

  <xsl:template name="bugstatuslist">
    <xsl:param name="status"/>
    <tr>
      <th>
        <xsl:value-of select="$status"/>
      </th>
      <td>
        <xsl:for-each select="defect">
          <xsl:if test="defect-status=$status">
            <xsl:value-of select="defect-number"/>
            <xsl:choose>
              <xsl:when test="position()&lt;last()-1">
                <xsl:text>, </xsl:text>
              </xsl:when>
              <xsl:when test="position()&lt;last()">
                <xsl:text> and </xsl:text>
              </xsl:when>
            </xsl:choose>
          </xsl:if>
        </xsl:for-each>
      </td>
    </tr>

  </xsl:template>

  <xsl:template name="checkbox">
    <xsl:param name="label"/>
    <xsl:param name="checked"/>
    <tr>
      <th>
        <xsl:choose>
          <xsl:when test="$checked='true'">
            <table bgcolor="green">
              <tr>
                <th>OK</th>
              </tr>
            </table>
          </xsl:when>
          <xsl:when test="$checked='false'">
            <table bgcolor="yellow">
              <tr>
                <th>.</th>
              </tr>
            </table>
          </xsl:when>
          <xsl:when test="$checked='0'">
            <table bgcolor="green">
              <tr>
                <th>OK</th>
              </tr>
            </table>
          </xsl:when>
          <xsl:otherwise>
            <table bgcolor="yellow">
              <tr>
                <th>
                  <xsl:value-of select="$checked"/>
                </th>
              </tr>
            </table>
          </xsl:otherwise>
        </xsl:choose>
      </th>
      <td>
        <xsl:value-of select="$label"/>
      </td>
    </tr>

  </xsl:template>

  <xsl:template match="binaryfile">
    <b>Binary file: <A HREF="{href}"><xsl:value-of select="name"/>
      </A>
    </b>
  </xsl:template>

  <xsl:template match="thisfile">
    This file: <A HREF="{href}"><xsl:value-of select="name"/>
    </A>
  </xsl:template>

  <xsl:template match="defect">
    <xsl:param name="numsccfile">
      <xsl:value-of select="count(scc-file)"/>
    </xsl:param>
    <xsl:param name="fix">Fix</xsl:param>
    <tr>
      <xsl:choose>
        <xsl:when test="@isrelease='GoldMaster' or @isrelease='GM' or @isrelease='RC' or @isrelease='R' or @isrelease='Release'">
          <th rowspan="2" bgcolor="#e0e0e8">
            <xsl:value-of select="defect-number"/>
          </th>
          <th rowspan="2" bgcolor="#e0e0e8">
            <span style="color:red">
              <xsl:value-of select="@isrelease"/>
            </span>
          </th>
        </xsl:when>
        <xsl:when test="@isrelease='Alpha' or @isrelease='alpha'">
          <th rowspan="2" bgcolor="#e0e0e8">
            <xsl:value-of select="defect-number"/>
          </th>
          <th rowspan="2" bgcolor="#e0e0e8">
            <span style="color:darkgreen">
              <xsl:value-of select="@isrelease"/>
            </span>
          </th>
        </xsl:when>
        <xsl:when test="@isrelease='Beta' or @isrelease='beta'">
          <th rowspan="2" bgcolor="#e0e0e8">
            <xsl:value-of select="defect-number"/>
          </th>
          <th rowspan="2" bgcolor="#e0e0e8">
            <span style="color:blue">
              <xsl:value-of select="@isrelease"/>
            </span>
          </th>
        </xsl:when>
        <xsl:otherwise>
          <th rowspan="2" colspan="2" bgcolor="#e0e0e8">
            <xsl:value-of select="defect-number"/>
          </th>
        </xsl:otherwise>
      </xsl:choose>
      <th align="left" bgcolor="#e0e0e8">
        <xsl:value-of select="summary"/>
      </th>
      <th bgcolor="#e0e0e8">
        <xsl:value-of select="defect-status"/>
      </th>
      <td bgcolor="#e0e0e8">
        defect <xsl:value-of select="defect-number"/>
      </td>
    </tr>
    <tr>
      <td bgcolor="#e0e0e8">
        [<xsl:value-of select="product"/>] [entered: 
        <xsl:value-of select="date-entered"/>
        ] [<b>fixed: 
        <xsl:value-of select="defect-event[event-name=$fix]/event-date"/>
        </b>]
      </td>
      <td bgcolor="#e0e0e8">
        <xsl:choose>
          <xsl:when test="priority='P1'">
            <b>
              <font color="red">
                <xsl:value-of select="priority"/>
              </font>
            </b>
          </xsl:when>
          <xsl:when test="priority='P2'">
            <font color="blue">
              <xsl:value-of select="priority"/>
            </font>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="priority"/>
          </xsl:otherwise>
        </xsl:choose>

      </td>
      <td rowspan="{$numsccfile+2}" valign="top">
        <table>
          <xsl:apply-templates select="scc-file"/>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="2"/>
      <td colspan="2" bgcolor="#e0e0e8" valign="top">
        <xsl:apply-templates select="reported-by-record/description"/>
      </td>
    </tr>
    <xsl:for-each select="scc-file">
      <tr>
        <td></td>
      </tr>
    </xsl:for-each>
  </xsl:template>


  <xsl:template match="scc-file">
    <xsl:choose>
      <xsl:when test="@last='true' or @last='false'">
        <tr>
          <td></td>
          <td></td>
          <td>
            <xsl:value-of select="."/>
          </td>
          <th>
            <xsl:value-of select="@lastrevision"/>
          </th>
          <td>
            <xsl:choose>
              <xsl:when test="@last='true'">
                <i> last</i>
              </xsl:when>
              <xsl:otherwise></xsl:otherwise>
            </xsl:choose>
          </td>
        </tr>
      </xsl:when>
      <xsl:otherwise>
        <tr>
          <td></td>
          <td></td>
          <td>
            <xsl:choose>
              <xsl:when test="../../@buildmode=301">
                <s>
                  <xsl:value-of select="."/>
                </s>
              </xsl:when>
              <xsl:when test="../../@buildmode=302">
                <i>
                  <xsl:value-of select="."/>
                </i>
              </xsl:when>
            </xsl:choose>
          </td>
          <td>
            <xsl:choose>
              <xsl:when test="../../@buildmode=302">
                <i>
                  <xsl:value-of select="@lastrevision"/>
                </i>
              </xsl:when>
            </xsl:choose>
          </td>
        </tr>

      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="description">
    <xsl:copy-of select="." />
  </xsl:template>

</xsl:stylesheet>

