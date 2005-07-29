<?xml version="1.0"?>

<xsl:stylesheet
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
    version='1.0'>
  <xsl:param name="bug-tracker-url"/>

  <xsl:output method="html" indent="yes" 
              doctype-public="-//W3C//DTD HTML 4.01//EN"
              doctype-system="http://www.w3.org/TR/html401/strict.dtd"/>
  <xsl:template match="test-case">
    <xsl:variable name="testCaseUrl" select="test-case-file/text()"/>
    <html>
      <head>
        <title>
          <xsl:value-of select="test-case-name/text()"/>
        </title>
      </head>
      <body bgcolor="white">
        <h2>
          <xsl:value-of select="test-case-name/text()"/>
        </h2>
        <table>
          <tr bgcolor="lightgrey">
            <td>Test Case Script:</td>
            <td><a href="../../../{$testCaseUrl}" target="_new"><xsl:value-of select="test-case-file/text()"/></a></td>
          </tr>
          <tr>
            <td>Summary:</td>
            <td><xsl:value-of select="test-case-summary/text()"/></td>
          </tr>
          <tr bgcolor="lightgrey">
            <td>Author:</td>
            <td><xsl:value-of select="test-case-author/text()"/></td>
          </tr>
          <tr>
            <td>Feature Tested:</td>
            <td><xsl:value-of select="functional-point-tested/text()"/></td>
          </tr>
          <tr bgcolor="lightgrey">
            <td>Test Level(s):</td>
            <td><xsl:apply-templates select="test-case-level"/></td>
          </tr>
          <tr>
            <td>Bug(s):</td>
            <td><xsl:apply-templates select="test-case-bug"/></td>
          </tr>
          <tr bgcolor="lightgrey">
            <td>Application Tested:</td>
            <td><xsl:value-of select="application-tested/text()"/></td>
          </tr>
          <xsl:if test="test-environment/text()">
          <tr>
              <td>Test Environment</td>
              <td><xsl:value-of select="test-environment/text()"/></td>
          </tr>
          </xsl:if>
          <tr>
            <td>Test Case Requirement:</td>
            <td><xsl:value-of select="test-case-requirement/text()"/></td>
          </tr>
        </table>
        <xsl:apply-templates select="session"/>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="test-case-level">
      <xsl:value-of select="text()"/><xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="test-case-bug">
      <xsl:variable name="bug" select="text()"/>
      <a href="{$bug-tracker-url}{$bug}" target="_new"><xsl:value-of select="text()"/></a><xsl:text> </xsl:text>
  </xsl:template>

  <xsl:template match="session">
      <hr/>
      <table cellspacing="0" cellpadding="0">
        <tr bgcolor="lightgrey">
          <td colspan="2"><b>Execution steps</b> for the <b><xsl:value-of select="application/text()"/></b> application
          <xsl:if test="organization/text()"> of the <b><xsl:value-of select="organization/text()"/></b> organization</xsl:if>.
          </td>
        </tr>
      </table>
      <ol>
        <xsl:apply-templates select="functional-point"/>
      </ol>
  </xsl:template>

  <xsl:template match="functional-point">
    <li>
      <xsl:value-of select="function-id/text()"/>
      <ol>
        <xsl:apply-templates select="steps/step"/>
      </ol>
    </li>
  </xsl:template>

  <xsl:template match="steps/step">
    <li>
      <xsl:copy-of select="."/>
    </li>
  </xsl:template>

</xsl:stylesheet>
