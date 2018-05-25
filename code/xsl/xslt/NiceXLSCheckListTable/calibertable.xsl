<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt">
  <xsl:output method="xml" indent="yes" />

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
          <Interior ss:Color="#f5ffb3" ss:Pattern="Solid"/>
          <Borders>
            <Border ss:Position="Top" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Left" ss:LineStyle="Continuous" ss:Weight="1"/>
            <Border ss:Position="Bottom" ss:LineStyle="Continuous" ss:Weight="1"/>
          </Borders>
        </Style>
        <Style ss:ID="headv">
          <Alignment ss:Vertical="Bottom" ss:Rotate="90"/>
        </Style>
      </Styles>

      <Worksheet ss:Name="Caliber_Table">
        <Table>
          <Column ss:AutoFitWidth="0" ss:Width="150"/>
          <Column ss:AutoFitWidth="0" ss:Width="15" ss:Span="300"/>
          <xsl:apply-templates select="Traceability[1]">
            <xsl:with-param name="titre">1</xsl:with-param>
          </xsl:apply-templates>
          <xsl:apply-templates select="Traceability"/>
        </Table>
        <WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
          <FreezePanes/>
          <FrozenNoSplit/>
          <SplitHorizontal>1</SplitHorizontal>
          <SplitVertical>1</SplitVertical>
          <TopRowBottomPane>2</TopRowBottomPane>
          <SplitVertical>1</SplitVertical>
          <Panes>
            <Pane>
              <Number>3</Number>
            </Pane>
            <Pane>
              <Number>1</Number>
              <ActiveCol>0</ActiveCol>
            </Pane>
            <Pane>
              <Number>2</Number>
              <ActiveRow>0</ActiveRow>
            </Pane>
            <Pane>
              <Number>0</Number>
              <ActiveRow>0</ActiveRow>
              <ActiveCol>0</ActiveCol>
            </Pane>
          </Panes>
        </WorksheetOptions>

      </Worksheet>
    </Workbook>
  </xsl:template>

  <xsl:template match="Traceability">
    <xsl:param name="titre">0</xsl:param>
    <Row>
      <xsl:choose>
        <xsl:when test="$titre=1">
          <Cell/>
          <xsl:apply-templates select="Trace">
            <xsl:with-param name="titre">1</xsl:with-param>
          </xsl:apply-templates>
        </xsl:when>
        <xsl:otherwise>
          <Cell>
            <Data ss_Type="String">
              <xsl:value-of select="@name"/>
            </Data>
          </Cell>
          <xsl:apply-templates select="Trace"/>
        </xsl:otherwise>
      </xsl:choose>
    </Row>
  </xsl:template>

  <xsl:template match="Trace">
    <xsl:param name="titre">0</xsl:param>
    <xsl:choose>
      <xsl:when test="$titre=1">
        <Cell ss_StyleID="headv">
          <Data ss_Type="String">
            <xsl:value-of select="@name"/>
          </Data>
        </Cell>
      </xsl:when>
      <xsl:otherwise>
        <xsl:choose>
          <xsl:when test="@type='-1'">
            <Cell/>
          </xsl:when>
          <xsl:when test="@type='1'">
            <Cell>
              <Data ss_Type="String">
                &#94;
              </Data>
            </Cell>
          </xsl:when>
          <xsl:when test="@type='3'">
            <Cell>
              <Data ss_Type="String">
                (&#94;)
              </Data>
            </Cell>
          </xsl:when>
          <xsl:when test="@type='5'">
            <Cell>
              <Data ss_Type="String">
                &lt;-
              </Data>
            </Cell>
          </xsl:when>
          <xsl:otherwise>
            <Cell>
              <Data ss_Type="String">
                <xsl:value-of select="@type"/>
              </Data>
            </Cell>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
