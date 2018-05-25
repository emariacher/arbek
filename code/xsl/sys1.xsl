<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:import href="common.xsl"/>
<xsl:import href="memory.xsl"/>
<xsl:import href="task.xsl"/>
<xsl:import href="queue.xsl"/>
<xsl:import href="stat.xsl"/>
<xsl:template match="/">
<html>
<body bgcolor="lightgrey">
<h2>System dump</h2>
<xsl:apply-templates/> 
</body>
</html>
</xsl:template>
</xsl:stylesheet>

