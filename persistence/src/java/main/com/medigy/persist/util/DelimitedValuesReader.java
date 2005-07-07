package com.medigy.persist.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DelimitedValuesReader
{
    /**
     * Pass this special line handler into the constructor to use DelimitedValuesReader.getNextLine() within your own
     * loop. If you do your own manual line handling and don't use the handler interface then you will be responsible
     * for closing the reader after you're done.
     */
    public static final LineHandler MANUAL_LINE_HANDLER = null;
    public static final int AUTO_CALC_EXPECTED_FIELDS = -1;

    public interface LineHandler
    {
        /**
         * Called for each header line that is parsed. If any exceptions are thrown they are caught by
         * the reader and either rethrown or added to the errors list (based upon the reader's throwExceptionOnValidationError
         * setting).
         * @param dvReader The reader that did the parsing. You can use this to get the line number or errors list.
         * @param line The parsed header line (each field is a list element)
         */
        public void handleHeaderLine(final DelimitedValuesReader dvReader, final List<String> line);

        /**
         * Called for each body (non header) line that is parsed. If any exceptions are thrown they are caught by
         * the reader and either rethrown or added to the errors list (based upon the reader's throwExceptionOnValidationError
         * setting).
         * @param dvReader The reader that did the parsing. You can use this to get the line number or errors list.
         * @param line The parsed line (each field is a list element)
         */
        public void handleLine(final DelimitedValuesReader dvReader, final List<String> line);
    }

    public interface IgnoredLineHandler
    {
        /**
         * Called for each header line that matches the ignore line regular expression.
         * @param dvReader The reader that did the parsing. You can use this to get the line number or errors list.
         * @param line The entire header line that was ignored.
         */
        public void handleIgnoredHeaderLine(final DelimitedValuesReader dvReader, final String line);

        /**
         * Called for each body line that matches the ignore line regular expression.
         * @param dvReader The reader that did the parsing. You can use this to get the line number or errors list.
         * @param line The body line that was ignored.
         */
        public void handleIgnoredLine(final DelimitedValuesReader dvReader, final String line);
    }

    private final LineHandler lineHandler;
    private final DelimitedValuesParser parser;
    private final LineNumberReader reader;
    private final String readerId;
    private final int skipFirstRows;
    private final boolean ignoreBlankLines;
    private final boolean allowIgnoreLines;
    private final boolean throwExceptionOnValidationError;
    private final boolean validateFieldsCount;
    private final Pattern ignoreLinePattern;
    private final IgnoredLineHandler ignoredLineHandler;
    private final List<String> errors = new ArrayList<String>();
    private final int maxErrors;

    private List<List<String>> headerLines;
    private int expectedFieldsPerlLineCount = AUTO_CALC_EXPECTED_FIELDS;

    public DelimitedValuesReader(final LineHandler lineHandler, // send in MANUAL_LINE_HANDLER to manually call getNextLine()
                                 final Reader reader, final String readerId,
                                 final DelimitedValuesParser parser,
                                 final int skipFirstRows,
                                 final boolean ignoreBlankLines,  // if this true it's a bit more expensive since each line will be trimmed and then checked to see if it's blank
                                 final String ignoreLineRegExprText,
                                 final IgnoredLineHandler ignoredLineHandler,
                                 final boolean validateFieldsCount,
                                 final int expectedFieldsCount, // send in AUTO_CALC_EXPECTED_FIELDS to auto-calculate based on first line
                                 final boolean throwExceptionOnValidationError,
                                 final int maxErrors)
    {
        this.lineHandler = lineHandler;
        this.reader = new LineNumberReader(reader);
        this.readerId = readerId;
        this.parser = parser;
        this.skipFirstRows = skipFirstRows;
        this.ignoreBlankLines = ignoreBlankLines;
        this.allowIgnoreLines = ignoreLineRegExprText != null;
        this.ignoreLinePattern = allowIgnoreLines ? Pattern.compile(ignoreLineRegExprText) : null;
        this.ignoredLineHandler = ignoredLineHandler;
        this.validateFieldsCount = validateFieldsCount;
        this.throwExceptionOnValidationError = throwExceptionOnValidationError;
        this.expectedFieldsPerlLineCount = expectedFieldsCount;
        this.maxErrors = maxErrors;
    }

    public void readAll() throws IOException, DelimitedValuesReaderException
    {
        if(skipFirstRows > 0)
        {
            headerLines = new ArrayList<List<String>>();
            parseHeader(skipFirstRows);
        }
        else
            headerLines = null;

        // if we've got a callback method, parse the body otherwise the caller is responsible for reading a line at a time
        if(lineHandler != null)
        {
            try
            {
                parseBody();
            }
            finally
            {
                close();
            }
        }
    }

    public DelimitedValuesReader(final LineHandler lineHandler, final URL resource, boolean headerRow) throws IOException
    {
        this(lineHandler, new InputStreamReader(resource.openStream()), resource.toExternalForm(),
             new DelimitedValuesParser(), headerRow ? 1 : 0, true, null, null, true, AUTO_CALC_EXPECTED_FIELDS, true,
                10);
    }

    public DelimitedValuesReader(final LineHandler lineHandler, final File file, boolean headerRow) throws IOException
    {
        this(lineHandler, new FileReader(file), file.getAbsolutePath(),
             new DelimitedValuesParser(), headerRow ? 1 : 0, true, null, null, true, AUTO_CALC_EXPECTED_FIELDS, true,
                10);
    }

    protected void parseHeader(final int skipFirstRows) throws IOException
    {
        while(true)
        {
            final String headerLine = this.reader.readLine();
            if(headerLine == null)
                return;

            if(ignoreBlankLines && headerLine.trim().length() == 0)
                continue;

            if(allowIgnoreLines)
            {
                final Matcher m = ignoreLinePattern.matcher(headerLine);
                if(m.matches())
                {
                    if(ignoredLineHandler != null)
                        ignoredLineHandler.handleIgnoredHeaderLine(this, headerLine);
                    continue;
                }
            }

            final List<String> fields = parser.parseAsList(headerLine);
            if(validateFieldsCount)
            {
                if(this.expectedFieldsPerlLineCount == AUTO_CALC_EXPECTED_FIELDS)
                    this.expectedFieldsPerlLineCount = fields.size();
                else
                {
                    if(fields.size() != expectedFieldsPerlLineCount)
                    {
                        final String validationErrorMsg = "Header line " + reader.getLineNumber() + " in '"+ readerId +"' has " + fields.size() + " fields but " + expectedFieldsPerlLineCount + " were expected.";
                        if(throwExceptionOnValidationError)
                            throw new DelimitedValuesReaderException(validationErrorMsg);
                        else
                        {
                            errors.add(validationErrorMsg);
                            continue;
                        }
                    }
                }
            }

            headerLines.add(fields);

            if(lineHandler != null)
            {
                try
                {
                    lineHandler.handleHeaderLine(this, fields);
                }
                catch (Exception e)
                {
                    final String validationErrorMsg = "Header line " + reader.getLineNumber() + " in '"+ readerId +"' encountered error: " + e.getMessage();
                    if(throwExceptionOnValidationError)
                        throw new DelimitedValuesReaderException(validationErrorMsg, e);
                    else
                        errors.add(validationErrorMsg);
                }
            }

            // we stop when we have read the total number of header lines we're expecting
            if(headerLines.size() == skipFirstRows)
                return;
        }
    }

    private void parseBody() throws IOException
    {
        while(true)
        {
            final List<String> line = getNextLine();
            if(line == null)
                break;

            try
            {
                lineHandler.handleLine(this, line);

                if(! throwExceptionOnValidationError)
                {
                    if(errors.size() > maxErrors)
                    {
                        errors.add("Maximum error count " + maxErrors + " reached.");
                        return;
                    }
                }
            }
            catch (Exception e)
            {
                final String validationErrorMsg = "Body line " + reader.getLineNumber() + " in '"+ readerId +"' encountered error: " + e.getMessage();
                if(throwExceptionOnValidationError)
                    throw new DelimitedValuesReaderException(validationErrorMsg, e);
                else
                {
                    errors.add(validationErrorMsg);
                    if(errors.size() > maxErrors)
                    {
                        errors.add("Maximum error count " + maxErrors + " reached.");
                        return;
                    }
                    else
                        continue;
                }
            }
        }
    }

    public void close() throws IOException
    {
        reader.close();
    }

    public LineNumberReader getReader()
    {
        return reader;
    }

    public int getExpectedFieldsPerlLineCount()
    {
        return expectedFieldsPerlLineCount;
    }

    public List<List<String>> getHeaderLines()
    {
        return headerLines;
    }

    public List<String> getErrors()
    {
        return errors;
    }

    public List<String> getNextLine() throws IOException
    {
       while(true)
       {
           final String line = this.reader.readLine();
           if(line == null)
               return null;

           if(ignoreBlankLines && line.trim().length() == 0)
               continue;

           if(allowIgnoreLines)
           {
               final Matcher m = ignoreLinePattern.matcher(line);
               if(m.matches())
               {
                   if(ignoredLineHandler != null)
                       ignoredLineHandler.handleIgnoredLine(this, line);
                   continue;
               }
           }

           final List<String> fields = parser.parseAsList(line);
           if(validateFieldsCount)
           {
               if(expectedFieldsPerlLineCount == AUTO_CALC_EXPECTED_FIELDS)
                   expectedFieldsPerlLineCount = fields.size();
               else
               {
                   if(fields.size() != expectedFieldsPerlLineCount)
                   {
                       final String validationErrorMsg = "Line " + reader.getLineNumber() + " in '"+ readerId +"' has " + fields.size() + " fields but " + expectedFieldsPerlLineCount + " were expected.";
                       if(throwExceptionOnValidationError)
                           throw new DelimitedValuesReaderException(validationErrorMsg);
                       else
                           errors.add(validationErrorMsg);
                   }
               }
           }

           return fields;
       }
    }
}
