<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:exsl="http://exslt.org/common"
    xmlns:loop="http://informatik.hu-berlin.de/loop"
    extension-element-prefixes="exsl">
  <xsl:output method="xml" indent="yes" />
  
  <!--Eric Mariacher 22mar06-->

  <xsl:variable name="chapters">
    <item>1.1</item>
    <item>1.2</item>
    <item>1.3</item>
    <item>1.4</item>
    <item>1.5</item>
    <item>2.1.1</item>
    <item>2.1.2</item>
    <item>2.1.3</item>
    <item>2.1.4</item>
    <item>2.2.1</item>
    <item>2.2.2</item>
    <item>2.2.3</item>
    <item>2.3</item>
    <item>2.4</item>
    <item>2.5</item>
    <item>2.6</item>
    <item>3.1</item>
    <item>3.2</item>
    <item>3.3</item>
    <item>3.4.1</item>
    <item>3.4.2</item>
    <item>3.4.3</item>
  </xsl:variable>

  <xsl:variable name="colors">
    <item>#FFFF00</item>
    <item>#FF0000</item>
    <item>#FF00FF</item>
    <item>#00FF00</item>
    <item>#00FFFF</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
    <item>#F0F0F0</item>
  </xsl:variable>

  <xsl:attribute-set name="datastring">
    <xsl:attribute name="ss:Type" namespace="urn:schemas-microsoft-com:office:spreadsheet">String</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="cellhead">
    <xsl:attribute name="ss:StyleID" namespace="urn:schemas-microsoft-com:office:spreadsheet">head</xsl:attribute>
  </xsl:attribute-set>

  <xsl:attribute-set name="cellbold">
    <xsl:attribute name="ss:StyleID" namespace="urn:schemas-microsoft-com:office:spreadsheet">bold</xsl:attribute>
  </xsl:attribute-set>

<!--use of testersloop variable enables much faster execution of xslt processing as the sort and unique looping is done only once-->
  <xsl:variable name="testersloop">
    <xsl:for-each select="/Report/Grid/Requirement/@four[not(.=preceding::Requirement/@four)]">
      <xsl:if test="string-length() > 1 and string-length(../@three) > 1 ">
        <item>
          <xsl:value-of select="."/>
        </item>
      </xsl:if>
    </xsl:for-each>
  </xsl:variable>

  <xsl:variable name="teststatesloop">
    <xsl:for-each select="/Report/Grid/Requirement/@five[not(.=preceding::Requirement/@five)]">
      <xsl:if test="string-length() > 1 and string-length(../@three) > 1 ">
        <item>
          <xsl:value-of select="."/>
        </item>
      </xsl:if>
    </xsl:for-each>
  </xsl:variable>

  <xsl:template match="Report">
    <Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
      <DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
        <Title>Caliber Table</Title>
        <Subject>Caliber Table</Subject>
        <Author>Eric Mariacher</Author>
      </DocumentProperties>
      <Styles>
        <Style ss:ID="Default" ss:Name="Normal">
          <Alignment ss:Vertical="Bottom" ss:WrapText="1"/>
          <Borders/>
          <Font/>
          <Interior/>
          <NumberFormat/>
          <Protection/>
        </Style>
        <Style ss:ID="head">
          <Font ss:Bold="1"/>
          <Borders>
            <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
          </Borders>
        </Style>
        <Style ss:ID="head1">
          <Font ss:Bold="1" ss:Size="14"/>
          <Interior ss:Color="#FFFF99" ss:Pattern="Solid"/>
          <Borders>
            <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
          </Borders>
        </Style>
        <Style ss:ID="head2">
          <Font ss:Bold="1" ss:Size="12"/>
          <Interior ss:Color="#FFCC99" ss:Pattern="Solid"/>
          <Borders>
            <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
          </Borders>
        </Style>
        <Style ss:ID="headv">
          <Alignment ss:Vertical="Bottom" ss:Rotate="90"/>
        </Style>
        <Style ss:ID="numfixed">
          <NumberFormat ss:Format="Fixed"/>
        </Style>
        <xsl:for-each select="exsl:node-set($colors)/item">
          <xsl:element name="Style">
            <xsl:attribute name="ss:ID" namespace="urn:schemas-microsoft-com:office:spreadsheet">s<xsl:value-of select="position()"/>
            </xsl:attribute>
            <xsl:element name="Interior">
              <xsl:attribute name="ss:Color" namespace="urn:schemas-microsoft-com:office:spreadsheet">
                <xsl:value-of select="."/>
              </xsl:attribute>
              <xsl:attribute name="ss:Pattern" namespace="urn:schemas-microsoft-com:office:spreadsheet">Solid</xsl:attribute>
            </xsl:element>
            <Font ss:Bold="1"/>
            <Borders>
              <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
              <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
              <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
              <Border ss:Position="Right" ss:LineStyle="Continuous" ss:Weight="1"/>
            </Borders>
          </xsl:element>
        </xsl:for-each>
      </Styles>

      <Worksheet ss:Name="Caliber_Table">
        <Table>
          <Column ss:AutoFitWidth="0" ss:Width="60"/>
          <Column ss:AutoFitWidth="0" ss:Width="150"/>
          <Column ss:AutoFitWidth="0" ss:Width="60" ss:Span="30"/>
          <Row>
            <Cell ss:Index="9"/>
            <xsl:for-each select="exsl:node-set($chapters)/item">
              <Cell>
                <Data ss:Type="String">
                  <xsl:value-of select="."/>
                </Data>
              </Cell>
            </xsl:for-each>
          </Row>
          <Row>
            <Cell ss:Index="9"/>
            <xsl:for-each select="exsl:node-set($chapters)/item">
              <Cell ss:Formula="=VLOOKUP(R[-1]C,R1C1:R300C2,2)"/>
            </xsl:for-each>
            <xsl:for-each select="exsl:node-set($testersloop)/item">
              <Cell>
                <xsl:message>
                  <xsl:value-of select="."/>, </xsl:message>
                <xsl:element name="Data" use-attribute-sets="datastring">
                  <xsl:value-of select="."/>
                </xsl:element>
              </Cell>
            </xsl:for-each>
          </Row>
          <xsl:for-each select="exsl:node-set($teststatesloop)/item">
            <xsl:sort/>
            <xsl:variable name="index">
              <xsl:value-of select="position()"/>
            </xsl:variable>
            <xsl:variable name="last">
              <xsl:value-of select="last()"/>
            </xsl:variable>
            <Row>
              <Cell ss:Index="9">
                <Data ss:Type="String">
                  <xsl:value-of select="."/>
                </Data>
              </Cell>
              <xsl:for-each select="exsl:node-set($chapters)/item">
                <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Formula=&quot;=COUNTIF(R')" />
                <xsl:value-of select="$last*2 + 3"/>
                <xsl:value-of disable-output-escaping="yes" select="string('C:R200C,')" />
                <xsl:value-of select="$index"/>
                <xsl:value-of disable-output-escaping="yes" select="string(')&quot;/&gt;')" />
              </xsl:for-each>
              <xsl:for-each select="exsl:node-set($testersloop)/item">
                <xsl:sort/>
                <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Formula=&quot;=COUNTIF(R')" />
                <xsl:value-of select="$last*2 + 3"/>
                <xsl:value-of disable-output-escaping="yes" select="string('C:R200C,')" />
                <xsl:value-of select="exsl:node-set($index)"/>
                <xsl:value-of disable-output-escaping="yes" select="string(')&quot;/&gt;')" />
              </xsl:for-each>
            </Row>
          </xsl:for-each>
          <xsl:for-each select="exsl:node-set($teststatesloop)/item">
            <xsl:sort/>
            <xsl:variable name="index">
              <xsl:value-of select="position()"/>
            </xsl:variable>
            <xsl:variable name="last">
              <xsl:value-of select="last()"/>
            </xsl:variable>
            <Row>
              <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Index=&quot;9&quot; ss:Formula=&quot;=R[-')" />
              <xsl:value-of select="exsl:node-set($last)"/>
              <xsl:value-of disable-output-escaping="yes" select="string(']C&quot;/&gt;')" />
              <xsl:for-each select="exsl:node-set($chapters)/item">
                <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Formula=&quot;=SUMIF(R')" />
                <xsl:value-of select="$last*2 + 3"/>
                <xsl:value-of disable-output-escaping="yes" select="string('C:R200C,')" />
                <xsl:value-of select="exsl:node-set($index)"/>
                <xsl:value-of disable-output-escaping="yes" select="string(',R')" />
                <xsl:value-of select="$last*2 + 3"/>
                <xsl:value-of disable-output-escaping="yes" select="string('C5:R200C5)&quot;/&gt;')" />
              </xsl:for-each>
              <xsl:for-each select="exsl:node-set($testersloop)/item">
                <xsl:sort/>
                <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Formula=&quot;=SUMIF(R')" />
                <xsl:value-of select="$last*2 + 3"/>
                <xsl:value-of disable-output-escaping="yes" select="string('C:R200C,')" />
                <xsl:value-of select="exsl:node-set($index)"/>
                <xsl:value-of disable-output-escaping="yes" select="string(',R')" />
                <xsl:value-of select="$last*2 + 3"/>
                <xsl:value-of disable-output-escaping="yes" select="string('C5:R200C5)&quot;/&gt;')" />
              </xsl:for-each>
            </Row>
          </xsl:for-each>
          <!--xsl:apply-templates select="Grid[1]">
            <xsl:with-param name="titre">1</xsl:with-param>
          </xsl:apply-templates-->
          <xsl:apply-templates select="Grid"/>
        </Table>
      </Worksheet>
    </Workbook>
  </xsl:template>

  <xsl:template match="Grid">
    <xsl:param name="titre">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$titre=1">
        <xsl:element name="Row">
          <xsl:attribute name="xmlns">urn:schemas-microsoft-com:office:spreadsheet</xsl:attribute>
          <xsl:element name="Cell" use-attribute-sets="cellhead">
            <xsl:element name="Data" use-attribute-sets="datastring">
              <xsl:value-of select="@one"/>
            </xsl:element>
          </xsl:element>
          <xsl:element name="Cell" use-attribute-sets="cellhead">
            <xsl:element name="Data" use-attribute-sets="datastring">
              <xsl:value-of select="@two"/>
            </xsl:element>
          </xsl:element>
          <xsl:element name="Cell" use-attribute-sets="cellhead">
            <xsl:element name="Data" use-attribute-sets="datastring">
              <xsl:value-of select="@five"/>
            </xsl:element>
          </xsl:element>
          <Cell/>
          <xsl:element name="Cell" use-attribute-sets="cellhead">
            <xsl:element name="Data" use-attribute-sets="datastring">
              <xsl:value-of select="@seven"/>
            </xsl:element>
          </xsl:element>
          <xsl:element name="Cell" use-attribute-sets="cellhead">
            <xsl:element name="Data" use-attribute-sets="datastring">
              <xsl:value-of select="@four"/>
            </xsl:element>
          </xsl:element>
          <xsl:element name="Cell" use-attribute-sets="cellhead">
            <xsl:element name="Data" use-attribute-sets="datastring">
              <xsl:value-of select="@three"/>
            </xsl:element>
          </xsl:element>
          <xsl:element name="Cell" use-attribute-sets="cellhead">
            <xsl:element name="Data" use-attribute-sets="datastring">
              <xsl:value-of select="@eight"/>
            </xsl:element>
          </xsl:element>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="Requirement"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="Requirement">
    <xsl:if test="@six &gt;= 0 or @seven &gt;= 0">
      <xsl:element name="Row">
        <xsl:attribute name="xmlns">urn:schemas-microsoft-com:office:spreadsheet</xsl:attribute>
        <Cell>
          <xsl:element name="Data" use-attribute-sets="datastring">
            <xsl:value-of select="@one"/>
          </xsl:element>
        </Cell>
        <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:StyleID=&quot;head')" />
        <xsl:choose>
          <xsl:when test="string-length(@one)=1">1</xsl:when>
          <xsl:otherwise>
            <xsl:variable name="hierarchy">
              <xsl:value-of select="@one"/>
            </xsl:variable>
            <xsl:for-each select="exsl:node-set($chapters)/item">
              <xsl:if test="$hierarchy=.">2</xsl:if>
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')" />
        <xsl:element name="Data" use-attribute-sets="datastring">
          <xsl:value-of select="@two"/>
        </xsl:element>
        <xsl:value-of disable-output-escaping="yes" select="string('&lt;/Cell&gt;')" />
        <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:StyleID=&quot;s')" />
        <xsl:variable name="state">
          <xsl:value-of select="@five"/>
        </xsl:variable>
        <xsl:for-each select="exsl:node-set($teststatesloop)/item">
          <xsl:sort/>
          <xsl:if test="$state=.">
            <xsl:value-of select="position()"/>
          </xsl:if>
        </xsl:for-each>
        <xsl:value-of disable-output-escaping="yes" select="string('&quot;&gt;')" />
        <xsl:element name="Data" use-attribute-sets="datastring">
          <xsl:value-of select="@five"/>
        </xsl:element>
        <xsl:value-of disable-output-escaping="yes" select="string('&lt;/Cell&gt;')" />
        <Cell/>
        <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:StyleID=&quot;numfixed&quot;&gt;&lt;Data ss:Type=&quot;Number&quot;&gt;')" />
        <xsl:value-of select="@seven+@six"/>
        <xsl:value-of disable-output-escaping="yes" select="string('&lt;/Data&gt;&lt;/Cell&gt;')" />
        <Cell>
          <xsl:element name="Data" use-attribute-sets="datastring">
            <xsl:value-of select="@four"/>
          </xsl:element>
        </Cell>
        <Cell>
          <xsl:element name="Data" use-attribute-sets="datastring">
            <xsl:value-of select="@three"/>
          </xsl:element>
        </Cell>
        <Cell>
          <xsl:element name="Data" use-attribute-sets="datastring">
            <xsl:value-of select="@eight"/>
          </xsl:element>
        </Cell>
        <xsl:choose>
          <xsl:when test="count(exsl:node-set($teststatesloop)/item) &lt;=5 or position()!=1">
            <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Formula=&quot;=IF(EXACT(RC[-6],R3C9),1,IF(EXACT(RC[-6],R4C9),2,IF(EXACT(RC[-6],R5C9),3,IF(EXACT(RC[-6],R6C9),4,IF(EXACT(RC[-6],R7C9),5)))))&quot;/&gt;')" />
          </xsl:when>
          <xsl:otherwise>
            <Cell>
              <xsl:element name="Data" use-attribute-sets="datastring">
              Too many States[
              <xsl:for-each select="exsl:node-set($teststatesloop)/item">
                <xsl:sort/>
                  <xsl:value-of select="."/>
                  <xsl:if test="position()!=last()">
                    <xsl:text>, </xsl:text>
                  </xsl:if>
                </xsl:for-each>
              ]. Review Formula in XSLT Transform File!
              </xsl:element>
            </Cell>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:for-each select="exsl:node-set($chapters)/item">
          <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Formula=&quot;=PRODUCT(IF(LEN(RC1)>LEN(R1C),1,0),IF(EXACT(LEFT(RC1,LEN(R1C)),R1C),1,0),RC9)&quot;/&gt;')" />
        </xsl:for-each>
        <xsl:for-each select="exsl:node-set($testersloop)/item">
          <xsl:value-of disable-output-escaping="yes" select="string('&lt;Cell ss:Formula=&quot;=PRODUCT(IF(EXACT(LEFT(RC6,LEN(R2C)),R2C),1,0),RC9)&quot;/&gt;')" />
        </xsl:for-each>
      </xsl:element>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
