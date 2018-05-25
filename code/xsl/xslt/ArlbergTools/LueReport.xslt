<?xml version="1.0" encoding="us-ascii" ?>
<!--/////////////////////////////////////////////////////////////////////////////-->
<!-- Presents a .xml file representing a list of loto draws                      -->
<!--                                                                             -->
<!-- Author: Thierry Cattel, 28 Novembre 2007                                    -->
<!-- - needs to be localized (watch the step : no concatenation!)                -->
<!--/////////////////////////////////////////////////////////////////////////////-->

<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:lue="www.logitech.com/UserExperience">
  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Produce Xhtml as far as possible                                           -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:output method="xml" encoding="UTF-8" indent='yes'/>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <LogitechUserExperience> element with children transformations     -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:LogitechUserExperience'>
    <html xmlns="http://www.w3.org/1999/xhtml">
      <head>
        <title>Logitech User Experience</title>
      </head>
      <body>
        <div>
          <a onmousedown="window.history.back()" href="">Back</a>
        </div>
        <h2 style="text-align:center">Logitech User Experience</h2>
        <div style="text-align:center">
          <table
          style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
          cellspacing="4"
          cellpadding="3"
          width="80%">
            <tbody>
              <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
                <td>Version</td>
                <td>UserId</td>
                <td>ReportIndex</td>
              </tr>
              <tr style="background-color:White; text-align:center">
                <td>
                  <xsl:value-of select = "./@Version"/>
                </td>
                <td>
                  <xsl:value-of select = "./@UserId"/>
                </td>
                <td>
                  <xsl:value-of select = "./@ReportIndex"/>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <br/>
        <br/>
        <xsl:apply-templates/>
        <br/>
      </body>
    </html>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Applications> element with a table                                -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Applications'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium" >Installed Applications</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Name</td>
            <td>Publisher</td>
            <td>Version</td>
          </tr>
          <xsl:apply-templates/>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Application> elements with a line in the table                    -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Application'>
    <tr style="background-color:White; text-align:center">
      <td>
        <xsl:value-of select = "./@Name"/>
      </td>
      <td>
        <xsl:value-of select = "./@Publisher"/>
      </td>
      <td>
        <xsl:value-of select = "./@Version"/>
      </td>
    </tr>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Documents> element with a table                                   -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Documents'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Recent Documents</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Extension</td>
            <td>Count</td>
          </tr>
          <xsl:apply-templates/>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Document> elements with a line in the table                       -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Document'>
    <tr style="background-color:White; text-align:center">
      <td>
        <xsl:value-of select = "./@Extension"/>
      </td>
      <td>
        <xsl:value-of select = "./@Count"/>
      </td>
    </tr>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Monitors> element with a table                                    -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Monitors'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Monitors</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Horizontal Resolution</td>
            <td>Vertical Resolution</td>
            <td>Horizontal Start Point</td>
            <td>Vertical Start Point</td>
            <td>Color Depth</td>
            <td>Display Frequency</td>
          </tr>
          <xsl:apply-templates/>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Monitor> elements with a line in the table                        -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Monitor'>
    <tr style="background-color:White; text-align:center">
      <td>
        <xsl:value-of select = "./@XRes"/>px
      </td>
      <td>
        <xsl:value-of select = "./@YRes"/>px
      </td>
      <td>
        <xsl:value-of select = "./@XStartPoint"/>px
      </td>
      <td>
        <xsl:value-of select = "./@YStartPoint"/>px
      </td>
      <td>
        <xsl:value-of select = "./@ColorDepth"/>Bits
      </td>
      <td>
        <xsl:value-of select = "./@DisplayFrequency"/>Hz
      </td>
    </tr>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Machine> element with a one line table                            -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Machine'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Machine</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Type</td>
            <td>Operating System</td>
            <td>Language</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Type"/>
            </td>
            <td>
              <xsl:value-of select = "./@OS"/>
            </td>
            <td>
              <xsl:value-of select = "./@Language"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Cpus> element with a table                                        -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Cpus'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Cpus</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Processor Count</td>
            <td>Architecture</td>
            <td>Level</td>
            <td>Revision</td>
            <td>PageSize</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@NbProc"/>
            </td>
            <td>
              <xsl:value-of select = "./@Architecture"/>
            </td>
            <td>
              <xsl:value-of select = "./@Level"/>
            </td>
            <td>
              <xsl:value-of select = "./@Revision"/>
            </td>
            <td>
              <xsl:value-of select = "./@PageSize"/>
            </td>
          </tr>
        </tbody>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Frequency</td>
            <td>Id</td>
            <td>PlatForm Id</td>
            <td>Processor Name</td>
            <td>Vendor Id</td>
          </tr>
          <xsl:apply-templates/>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Cpu> elements with a line in the table                            -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Cpu'>
    <tr style="background-color:White; text-align:center">
      <td>
        <xsl:value-of select = "./@Freq"/>Hz
      </td>
      <td>
        <xsl:value-of select = "./@Id"/>
      </td>
      <td>
        <xsl:value-of select = "./@PlaformId"/>
      </td>
      <td>
        <xsl:value-of select = "./@ProcName"/>
      </td>
      <td>
        <xsl:value-of select = "./@VendorId"/>
      </td>
    </tr>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Ram> element with a one line table                                -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Ram'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Memory</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Amount</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Amount"/>Mb
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Devices> element with a table                                     -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Devices'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Devices</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Device</td>
            <td>IsInternal</td>
            <td>Vendor Id</td>
            <td>Product Id</td>
            <td>Model</td>
            <td>ConnectionType</td>
            <td>Layout</td>
            <td>Free Wheeling</td>
            <td>Wheel Status</td>
          </tr>
          <xsl:apply-templates/>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Mouse> elements with a line in the table                          -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Mouse'>
    <tr style="background-color:White; text-align:center">
      <td>
        Mouse
      </td>
      <td>
        <xsl:value-of select = "./@IsInternal"/>
      </td>
      <td>
        <xsl:value-of select = "./@Vid"/>
      </td>
      <td>
        <xsl:value-of select = "./@Pid"/>
      </td>
      <td>
        <xsl:value-of select = "./@Model"/>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="starts-with(./@ConnectionType, '0')">
            Unknown
          </xsl:when>
          <xsl:when test="starts-with(./@ConnectionType, '1')">
            Ps2
          </xsl:when>
          <xsl:when test="starts-with(./@ConnectionType, '2')">
            Usb
          </xsl:when>
          <xsl:when test="starts-with(./@ConnectionType, '3')">
            Bluetooth
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select = "./@ConnectionType"/>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
      </td>
      <td>
        <xsl:value-of select = "./@FreeWheeling"/>
      </td>
      <td>
        <xsl:value-of select = "./@WheelStatus"/>
      </td>
    </tr>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Keyboard> elements with a line in the table                       -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Keyboard'>
    <tr style="background-color:White; text-align:center">
      <td>
        Keyboard
      </td>
      <td>
        <xsl:value-of select = "./@IsInternal"/>
      </td>
      <td>
        <xsl:value-of select = "./@Vid"/>
      </td>
      <td>
        <xsl:value-of select = "./@Pid"/>
      </td>
      <td>
        <xsl:value-of select = "./@Model"/>
      </td>
      <td>
        <xsl:choose>
          <xsl:when test="starts-with(./@ConnectionType, '0')">
            Unknown
          </xsl:when>
          <xsl:when test="starts-with(./@ConnectionType, '1')">
            Ps2
          </xsl:when>
          <xsl:when test="starts-with(./@ConnectionType, '2')">
            Usb
          </xsl:when>
          <xsl:when test="starts-with(./@ConnectionType, '3')">
            Bluetooth
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select = "./@ConnectionType"/>
          </xsl:otherwise>
        </xsl:choose>
      </td>
      <td>
        <xsl:value-of select = "./@Layout"/>
      </td>
    </tr>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <KeyboardKey> element with a one line table (group them all?)              -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template name='KeyProc'>
    <xsl:param name="currentKey" />

    <tr style="background-color:White; text-align:center">
      <td>
        <xsl:value-of select = "$currentKey/@Time"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Vid"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Pid"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Model"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Key"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Modifiers"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@State"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@FunctionMapped"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@FocusedApplication"/>
      </td>
    </tr>
    <xsl:variable name="nextKey" select="$currentKey/following-sibling::*[1][self::lue:KeyboardKey]" />
    <xsl:if test="$nextKey">
      <xsl:call-template name="KeyProc">
        <xsl:with-param name="currentKey" select="$nextKey" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <KeyboardKey> element with a one line table (group them all)               -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:KeyboardKey[not(preceding-sibling::*[1][self::lue:KeyboardKey])]'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Keyboard Key</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Vendor Id</td>
            <td>Product Id</td>
            <td>Model</td>
            <td>Key</td>
            <td>Modifiers</td>
            <td>State</td>
            <td>Function Mapped</td>
            <td>Focused Application</td>
          </tr>
          <xsl:call-template name="KeyProc">
            <xsl:with-param name="currentKey" select="." />
          </xsl:call-template>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <MouseButton> element with a one line table (group them all?)              -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template name='MouseButtonProc'>
    <xsl:param name="currentKey" />

    <tr style="background-color:White; text-align:center">
      <td>
        <xsl:value-of select = "$currentKey/@Time"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Vid"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Pid"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Model"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Key"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@Modifiers"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@State"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@FunctionMapped"/>
      </td>
      <td>
        <xsl:value-of select = "$currentKey/@FocusedApplication"/>
      </td>
    </tr>
    <xsl:variable name="nextKey" select="$currentKey/following-sibling::*[1][self::lue:MouseButton]" />
    <xsl:if test="$nextKey">
      <xsl:call-template name="MouseButtonProc">
        <xsl:with-param name="currentKey" select="$nextKey" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <MouseButton> element with a one line table (group them all)               -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:MouseButton[not(preceding-sibling::*[1][self::lue:MouseButton])]'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Mouse Button</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Vendor Id</td>
            <td>Product Id</td>
            <td>Model</td>
            <td>Button</td>
            <td>Modifiers</td>
            <td>State</td>
            <td>Function Mapped</td>
            <td>Focused Application</td>
          </tr>
          <xsl:call-template name="MouseButtonProc">
            <xsl:with-param name="currentKey" select="." />
          </xsl:call-template>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <CursorActivity> element with a one line table (group them all?)   -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:CursorActivity'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Cursor Activity</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Distance</td>
            <td>Duration</td>
            <td>Focused Application</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
            <td>
              <xsl:value-of select = "./@Distance"/>
            </td>
            <td>
              <xsl:value-of select = "./@Duration"/>
            </td>
            <td>
              <xsl:value-of select = "./@FocusedApplication"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>



  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Wheel> element with a one line table (group them all?)            -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template name='WheelProc'>
    <xsl:param name="currentWheel" />

    <tr style="background-color:White; text-align:center">
      <td>
        <xsl:value-of select = "$currentWheel/@Time"/>
      </td>
      <td>
        <xsl:value-of select = "$currentWheel/@Distance"/>
      </td>
      <td>
        <xsl:value-of select = "$currentWheel/@Unit"/>
      </td>
      <td>
        <xsl:value-of select = "$currentWheel/@Duration"/>
      </td>
      <td>
        <xsl:value-of select = "$currentWheel/@FocusedApplication"/>
      </td>
    </tr>
    <xsl:variable name="nextWheel" select="$currentWheel/following-sibling::*[1][self::lue:WheelActivity]" />
    <xsl:if test="$nextWheel">
      <xsl:call-template name="WheelProc">
        <xsl:with-param name="currentWheel" select="$nextWheel" />
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <WheelActivity> element with a one line table (group them all)     -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:WheelActivity[not(preceding-sibling::*[1][self::lue:WheelActivity])]'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Wheel Activity</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Distance</td>
            <td>Unit</td>
            <td>Duration</td>
            <td>Focused Application</td>
          </tr>
          <xsl:call-template name="WheelProc">
            <xsl:with-param name="currentWheel" select="." />
          </xsl:call-template>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>


  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <KeyActivity> element with a one line table                        -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:KeyActivity'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Key Activity</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Count</td>
            <td>Duration</td>
            <td>Focused Application</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
            <td>
              <xsl:value-of select = "./@Count"/>
            </td>
            <td>
              <xsl:value-of select = "./@Duration"/>
            </td>
            <td>
              <xsl:value-of select = "./@FocusedApplication"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <FocusedApplication> element with a one line table                 -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:FocusedApplication'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Focused Application</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Name</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
            <td>
              <xsl:value-of select = "./@Name"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <ConsoleConnect> element with a one line table                     -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:ConsoleConnect'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">ConsoleConnect</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <ConsoleDisconnect> element with a one line table                  -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:ConsoleDisconnect'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">ConsoleDisconnect</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <SessionLock> element with a one line table                        -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:SessionLock'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">SessionLock</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <SessionUnlock> element with a one line table                      -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:SessionUnlock'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">SessionUnlock</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Suspend> element with a one line table                            -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Suspend'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Suspend</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <Wakeup> element with a one line table                             -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:Wakeup'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Wakeup</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Source</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
            <td>
              <xsl:value-of select = "./@Source"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <EndSession> element with a one line table                         -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:EndSession'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">EndSession</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <InactivityDiagram> element with a table                           -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:InactivityDiagram'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">
          <xsl:if test="starts-with(./@DeviceType, 'Mouse')">
            Mouse Inactivity
          </xsl:if>
          <xsl:if test="starts-with(./@DeviceType, 'Keyboard')">
            Keyboard Inactivity
          </xsl:if>
        </caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>
              Histogram (Delay,TotalCount,MaxConsoCount)  StartTime = <xsl:value-of select = "./@StartTime"/>, Total Duration = <xsl:value-of select = "./@TotalDuration"/>
            </td>
          </tr>
          <tr style="background-color:White" align="left">
            <td>
              <xsl:value-of select = "./@Histogram"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>

  <!--///////////////////////////////////////////////////////////////////////////-->
  <!--Replace <MouseScrollModeChange> element with a one line table              -->
  <!--///////////////////////////////////////////////////////////////////////////-->
  <xsl:template match='lue:MouseScrollModeChange'>
    <div style="text-align:center">
      <table
       style="background-color: #FAEBD7; border-color: #696969; border-width: 8; border-style:solid"
       cellspacing="4"
       cellpadding="3"
       width="80%">
        <caption style="font-weight: bold; font-size: medium">Mouse ScrollMode Change</caption>
        <tbody>
          <tr align="center" style="font-weight:bold; background-color: #FFB5C5">
            <td>Time</td>
            <td>Vendor Id</td>
            <td>Product Id</td>
            <td>Model</td>
            <td>Free Wheeling</td>
            <td>Wheel Status</td>
            <td>Focused Application</td>
          </tr>
          <tr style="background-color:White; text-align:center">
            <td>
              <xsl:value-of select = "./@Time"/>
            </td>
            <td>
              <xsl:value-of select = "./@Vid"/>
            </td>
            <td>
              <xsl:value-of select = "./@Pid"/>
            </td>
            <td>
              <xsl:value-of select = "./@Model"/>
            </td>
            <td>
              <xsl:value-of select = "./@FreeWheeling"/>
            </td>
            <td>
              <xsl:value-of select = "./@WheelStatus"/>
            </td>
            <td>
              <xsl:value-of select = "./@FocusedApplication"/>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <br/>
    <br/>
  </xsl:template>
</xsl:stylesheet>
