<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'
                xmlns="http://www.w3.org/TR/xhtml1/transitional"
                exclude-result-prefixes="#default">

    <xsl:import href="doc.docbook-xsl.home/html/chunk.xsl"/> <!-- will be resolved by <xmlcatalog> in <xslt> tag -->
    <xsl:import href="vars.xsl"/>

    <xsl:variable name="chunk.quietly">0</xsl:variable>
    <xsl:variable name="chunk.first.sections">1</xsl:variable>
    <xsl:variable name="chunk.fast">1</xsl:variable>

</xsl:stylesheet>