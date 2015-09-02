package cn.com.esrichina.gcloud.commons.license;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;



 
/**
 * SuperProperties enhances standard properties files with the following features.
 * <P>
 * <UL>
 *	<LI>line continuations</LI>
 *	<LI>#include [[+]prefix] file</LI>
 *	<LI># comment lines</LI>
 *  <LI>${variable} replacement in resource key and value, and #include prefix and file</LI>
 *  <LI>nested variable references are supported</LI>
 *  <LI>by default, variable references in values are evaluated when the property value is requested</LI>
 *  <LI>variable references in keys and #include directives are always evaluated as the properties file is read</LI>
 *	<LI>replace ++ with auto-incremented number in key++ = value</LI>
 * </UL>
  <P>
 * SuperProperties can also read xml format properties and supports
 * <P>
 * <UL>
 *   <LI>&lt;?include [[+]prefix] file?&gt; processing instruction</LI>
 *  </UL>
 * <P>
 * Known restrictions with this version:
 * <P>
 * <UL>
 *   <LI>save() method cannot save auto-numbered keys as auto-numbered keys
 *   - an auto-numbered key is saved with the assigned auto-number</LI>
 * </UL>
 * <P>
 * The xml support uses software developed by the JDOM Project (http://www.jdom.org/).
 * <P>
 *
 * @author Colin Prosser
 */

public class SuperProperties
	extends Properties
	implements Serializable
{
//	static Category cat = Category.getInstance(SuperProperties.class.getName());

	static final long serialVersionUID = 702902917052304938L;

	/**
	 * Option values for handling ${variable} referemces in values.
	 * Note that VAR_KEEP_REFERENCE or VAR_SET_VALUE_ON_GET allow variable references to be preserved when
	 * properties are saved, but VAR_SET_VALUE_ON_LOAD means that the expanded value will be saved.
	 */
	public static final int VAR_SET_VALUE_ON_GET = 0; // defer evaluation until getProperty() call
	public static final int VAR_SET_VALUE_ON_LOAD = 1; // evaluate when loading (reading) properties file
	public static final int VAR_KEEP_REFERENCE = 2;
	private int _variableOption = VAR_SET_VALUE_ON_GET;

	/**
	 * Option values for handling unset ${variable} references
	 */
	public static final int UNSETVAR_THROW_EXCEPTION = 3;
	public static final int UNSETVAR_KEEP_REFERENCE = 4;
	public static final int UNSETVAR_SET_EMPTY = 5;
	private int _unsetVariableOption = UNSETVAR_THROW_EXCEPTION;

	/**
	 * Whether to restrict characters in key to letter or digit or _ or -
	 * NB: ? is always allowed as last char for auto-numbering
	 */
	private	boolean _useRestrictedKeys = false;

	/**
	 * Most recent numbers allocated auto-numbered keys
	 */
	private Hashtable _autoNumberedKeys = new Hashtable();

	/**
	 * A ConfigData object containing all hierarchical keys/values.
	 */
	protected ConfigData cfgData = new ConfigData();


    protected boolean isEscapeSupportedInRead = true;  // default true in TI

    protected String encoding = "UTF-8";
	/**
	 * Creates an empty property list with no default values.
	 */
	public SuperProperties()
	{
	}

	/**
	 * Creates an empty property list with the specified defaults.
	 *
	 * @param   defaults   the defaults.
	 */
	public SuperProperties(Properties defaults)
	{
		if ( defaults != null )
		{
			SuperProperties props = new SuperProperties();

			for ( Enumeration e = defaults.propertyNames(); e.hasMoreElements(); )
			{
				String key = (String)e.nextElement();
				if(defaults.getProperty(key) != null) { //add by DPF 2005..05.30
					props.setProperty(key, defaults.getProperty(key));
				}
			}

			this.defaults = props;
		}
	}

	/**
	 * Creates a property list initialised from the specified
	 * properties objects and with the specified defaults.
	 *
	 * @param props	the initial set of properties
	 * @param defaults	the defaults to use
	 */
	public SuperProperties(Properties props, Properties defaults)
	{
		this(defaults);

		if ( props != null )
		{
			for ( Enumeration e = props.propertyNames(); e.hasMoreElements(); )
			{
				String key = (String)e.nextElement();
				if(defaults.getProperty(key) != null) { //add by DPF 2005..05.30
					setProperty(key, props.getProperty(key), true);
				}
			}
		}
	}

	/**
	 * Reads a standard or xml format properties list from the specified file name.
	 *
	 * @param      filename   the input files name.
	 * @exception  IOException  if an error occurred when reading from the input stream.
	 */
	public void load(String  filename)
		throws IOException
	{
		load(new File(filename));
	}

	/**
	 * Reads a standard or xml format properties list from the specified file.
	 *
	 * @param      name   the input File object.
	 * @exception  IOException  if an error occurred when reading from the input stream.
	 */
	public void load(File name)
		throws IOException
	{
		load(name, null);
	}

	/**
	 * Reads a standard or xml format properties list from the specified file.
	 *
	 * @param      name   the input File object.
	 * @param      prefix the prefix to insert before each entry.
	 * @exception  IOException  if an error occurred when reading from the input stream.
	 */
	public void load(File name, String prefix)
		throws IOException
	{
		FileInputStream fin = null;

		name = new File(name.getCanonicalPath());

		try
		{
			fin = new FileInputStream(name);
			load(fin, name, prefix);
		}
		finally
		{
			if ( fin != null )
			{
				fin.close();
			}
		}
	}

	/**
	 * Reads a standard or xml format properties list from an input stream.
	 *
	 * @param      in   the input stream.
	 * @exception  IOException  if an error occurred when reading from the input stream.
	 */
	public void load (InputStream in)
		throws IOException
	{
		load(in, null, "");
	}

	/**
	 * Reads a standard or xml format properties list from an input stream.
	 *
	 * @param      in           the input stream
	 * @param      prefix       the prefix, if any, to prepend to resource names
	 *
	 * @exception  IOException  if an error occurred when reading from the input stream
	 * @exception  PropertyFormatException if a badly formed #include directive,
	 *                                     variable reference, or resource setting
	 *                                     is found
	 */
	protected /*synchronized*/ void load(InputStream in, File srcFile, String prefix)
		throws IOException, PropertyFormatException
	{
		load( new BufferedReader( new InputStreamReader(in) ), srcFile, prefix );
	}

	/**
	 * Reads standard or xml format properties from a string.
	 */
	public void loadXML(String xml)
		throws IOException, PropertyFormatException
	{
		load( new BufferedReader( new StringReader( xml ) ), null, "" );
	}

	/**
	 * Reads a standard or xml format properties list from a Reader.
	 *
	 * @param      rdr           the input stream
	 * @param      prefix       the prefix, if any, to prepend to resource names
	 *
	 * @exception  IOException  if an error occurred when reading from the input stream
	 * @exception  PropertyFormatException if a badly formed #include directive,
	 *                                     variable reference, or resource setting
	 *                                     is found
	 */
	protected /*synchronized*/ void load(BufferedReader rdr, File srcFile, String prefix)
		throws IOException, PropertyFormatException
	{
		String filename = (srcFile == null ? "<stdin>" : srcFile.getPath());
		ConfigEntry file = new ConfigEntry(null, 0, ConfigEntry.FILE, null, filename);
		String line = null;

		if ( rdr.markSupported() )
		{
			char cbuf[] = new char[5];
			rdr.mark(cbuf.length);
			rdr.read(cbuf, 0, cbuf.length);
			rdr.reset();
			line = new String(cbuf);
		}

			// read classic properties format file
			Vector commentv = new Vector();
			int lineNo = 0;

			for ( line = rdr.readLine(); line != null; line = rdr.readLine() )
			{
				lineNo++;

				// Handle continuation lines (including comment lines)
				StringBuffer logicalLine = new StringBuffer(line);
				int continuationLines = unfold(logicalLine, rdr);

				int length = logicalLine.length();
				int index = 0;
				char ch = '\n';

				// skip whitespace to first token, if any
				for ( ; index < length; index++ )
				{
					ch = logicalLine.charAt(index);
					if ( !Character.isWhitespace(ch) )
						break;
				}

				switch ( ch )
				{
				case '\n':
				case ' ':
				case '\t':
					// empty line, or only whitespace
					commentv.addElement(logicalLine.toString());
					break;

				case '!': // old-style comment line
					commentv.addElement(logicalLine.toString());
					break;

				case '#': // comment line or possible directive
					// skip whitespace to next token, if any
					for ( index++; index < length; index++ )
					{
						ch = logicalLine.charAt(index);
						if ( !Character.isWhitespace(ch) )
							break;
					}

					// Process directive. If not really a directive will be treated as comment.
					switch ( ch )
					{
					case 'i': // possible #include directive
						boolean isInclude = readIncludeDirective(file, lineNo, logicalLine, index, prefix, null, commentv);

						if ( isInclude )
						{
							commentv = new Vector();
						}
						else
						{
							commentv.addElement(logicalLine.toString());
						}
						break;

					default:  // comment
						commentv.addElement(logicalLine.toString());
						break;
					}
					break;

				case '=':
				case ':':
					throw new PropertyFormatException(file.name(), lineNo, "Property key name missing");

				case '.':
					throw new PropertyFormatException(file.name(), lineNo, "Property key word missing");

				default:  // resource line
					// first deal with any preceding comments
					commentv = readComments(file, lineNo, commentv, null);
					readResource(file, lineNo, logicalLine, index, prefix);
				}

				lineNo += continuationLines;
			}

			// finally deal with any trailing comments
			commentv = readComments(file, lineNo, commentv, null);
	}

	/**
	 * Reads property settings from XML content
	 *
	 * @param      content   the XML content
	 * @param      file      ConfigEntry for the file name
	 * @param      prefix    the prefix, if any, to prepend to resource names
	 * @param      xmlPath   any additional prefix implied by nested xml element path
	 *
	 * @exception  IOException if error reading <?include file?>
	 */
/*	protected void readXMLContent(List content, ConfigEntry file, String prefix, String xmlPath)
		throws IOException
	{
		if ( content != null )
		{
			Iterator iterator = content.iterator();
			// FIXME: is there any way to get the line number in the xml document?
			int lineNo = 0;

			if ( xmlPath == null )
				xmlPath = "";

			while ( iterator.hasNext() )
			{
				Object obj = iterator.next();

				if ( obj instanceof Element )
				{
					Element elem = (Element) obj;

					String newXmlPath = xmlPath;

					if ( !elem.isRootElement() )
					{
						if ( newXmlPath.length() > 0 && !newXmlPath.endsWith(".") )
							newXmlPath = newXmlPath + "." + elem.getName();
						else
							newXmlPath = elem.getName();
					}

					readXMLAttributes ((List) elem.getAttributes(), file, prefix, newXmlPath );
					readXMLContent ( (List)elem.getContent(), file, prefix, newXmlPath );
				}
				else if ( obj instanceof Comment )
				{
					Comment comment = (Comment) obj;
					Vector commentv = new Vector();
					commentv.addElement( comment.getText() );
					commentv = readComments( file, lineNo, commentv, xmlPath );
				}
				else if ( obj instanceof ProcessingInstruction )
				{
					ProcessingInstruction inst = (ProcessingInstruction) obj;
					String name = inst.getTarget();
					String value = inst.getData();

					if ( name.equalsIgnoreCase("include") )
					{
						StringBuffer line = new StringBuffer("include ");
						line.append(value);
						readIncludeDirective(file, lineNo, line, 0, prefix, xmlPath, new Vector());
					}
					else
					{
						setProcessingInstruction(file, lineNo, name, value, xmlPath);
					}
				}
				else if (obj instanceof Text )
				{
					Text text = (Text)obj;
					String value;

					// Check if xml:space=preserve attribute is set.
					// If it is keep any leading and trailing spaces.
					// Otherwise discard them.
					// Note that the SuperProperties convention is never
					// to "normalize" internal spacing. This saves the extra noise
					// of littering xml documents with xml:space=preserve attributes
					// or sacrificing nice formatting.
					Element parent = text.getParent();
					Attribute attr = parent.getAttribute("space", Namespace.XML_NAMESPACE);

					if ( attr != null && "preserve".equals(attr.getValue()) )
						value = text.getText();
					else
						value = text.getTextTrim();

					if ( value.length() > 0 )
					{
						String newPrefix = ( prefix != null ? prefix : "");

						if ( newPrefix.length() > 0 )
						{
							if ( xmlPath.length() > 0 )
								newPrefix += "." + xmlPath;
						}
						else
							newPrefix = xmlPath;

						StringBuffer valBuf = new StringBuffer();
						readPropertyValue( newPrefix, file, lineNo, new StringBuffer(value), 0, valBuf, true );
						setProperty(newPrefix, valBuf.toString(), file, lineNo, true, ConfigEntry.RESOURCE);
					}
				}
				else if ( obj instanceof String )
				{
					String value = ((String) obj).trim();

					if ( value.length() > 0 )
					{
						String newPrefix = ( prefix != null ? prefix : "");

						if ( newPrefix.length() > 0 )
						{
							if ( xmlPath.length() > 0 )
								newPrefix += "." + xmlPath;
						}
						else
							newPrefix = xmlPath;

						StringBuffer valBuf = new StringBuffer();
						readPropertyValue( newPrefix, file, lineNo, new StringBuffer(value), 0, valBuf, true );
						setProperty(newPrefix, valBuf.toString(), file, lineNo, true, ConfigEntry.RESOURCE);
					}
				}
				else if ( obj instanceof CDATA )
				{
					String newPrefix = ( prefix != null ? prefix : "");

					if ( newPrefix.length() > 0 )
					{
						if ( xmlPath.length() > 0 )
							newPrefix += "." + xmlPath;
					}
					else
						newPrefix = xmlPath;

					StringBuffer valBuf = new StringBuffer();
					readPropertyValue( newPrefix, file, lineNo, new StringBuffer(((CDATA)obj).getText()), 0, valBuf, true );
					setProperty(newPrefix, valBuf.toString(), file, lineNo, true, ConfigEntry.XML_CDATA);
				}
				else
				{
					cat.debug( " SuperProperties.readXMLContent() - content list has unexpected type: " + obj.getClass().getName() + " [" + obj + "]" );
				}
			}
		}
	}
*/

    /**
	 * Unfolds input continuation lines.
	 *
	 * Lines ending with an unescaped '\' continue to the next real line.
	 * This method contructs a single logical line from a sequence
	 * of continuation lines. Leading whitespace on a continuation line
	 * is discarded.
	 *
	 * @param line  start line of possible continuation sequence
	 * @param rdr   input reader
	 *
	 * @return      number of continuation lines unfolded
	 *
	 * @exception  IOException  if an error occurred when reading from the input stream
	 *
	 */
	protected int unfold(StringBuffer line, BufferedReader rdr)
		throws IOException
	{
		int lineCount = 0;
		boolean isFolded = false;

		do
		{
			isFolded = false;

	    	// if line is folded (ends with unescaped \)
    		// then append the next real line
			int length = line.length();
			int index = length - 1;

			while ( index >= 0 && line.charAt(index) == '\\' )
			{
				index--;
			}

			if ( (length - index) % 2 == 0 )
			{
				// get continuation line
				line.setLength(length-1);

				String continuation = rdr.readLine();
				isFolded = (continuation == null ? false : true);

				if ( isFolded )
				{
					lineCount++;

					int foldedIndex = 0;
					int foldedLength = continuation.length();

					for ( ; foldedIndex < foldedLength; foldedIndex++ )
					{
						char ch = continuation.charAt(foldedIndex);
						if ( !Character.isWhitespace(ch) )
							break;
					}

					if ( foldedIndex < foldedLength )
					{
						line.append(continuation.substring(foldedIndex, foldedLength));
					}
				}
			}
		}
		while ( isFolded );

		return lineCount;
    }

    /**
	 * Adds a comment block to the ConfigEntry chain.
	 *
	 * @param file       the ConfigEntry for the file containing the comment block
	 * @param lineNo     the line number  in the containing file of the last line in the comment block
	 * @param commentv   the comment block
	 * @param path	     if non-null, the prefix the element path is associated with
	 *
	 * @return           an empty comment block vector
     */
	protected Vector readComments(ConfigEntry file, int lineNo, Vector commentv, String path)
	{
		setComment(file, lineNo, commentv, path);

		// NB: we reference the commentv vector in the ConfigEntry rather than making a copy
		//     and so return a new Vector instead of calling commentv.removeAllElements()
		return new Vector();
	}

	/**
	 * Process a #include directive.
	 *
	 * @param file      the ConfigEntry for the file containing the comment block
	 * @param lineNo    the line number  in the containing file of the last line in the comment block
	 * @param line      the input line to process
	 * @param index	    the index in line to start parsing
	 * @param prefix    the prefix to insert before each entry.
	 * @param xmlPath	any additional prefix implied by nested xml element path
	 * @param commentv  any comment block preceding the input line
	 *
	 * @return     true if really an #include directive, else false (really a comment)
	 *             or, an empty comment block vector if it contained a #include directive
	 *
	 * @exception  IOException  if an error occurred when reading from the input stream.
	 * @exception  PropertyFormatException if the #include directive is badly formed
	 */
	protected boolean readIncludeDirective(ConfigEntry file, int lineNo, StringBuffer line, int index, String prefix, String xmlPath, Vector commentv)
		throws IOException, PropertyFormatException
	{
		boolean isInclude = true;
		String directive = "include";
		int directiveLength = directive.length();
		int length = line.length();

/**********************2003.12.25****************/
		if(line.length() < directiveLength+1){
			return false;
		}
/**********************2003.12.25****************/
		if ( line.toString().substring(index, index+directiveLength).equals(directive) )
		{
			if ( length <= index+directiveLength )
			{
				// #include
				throw new PropertyFormatException(file.name(), lineNo, "Badly formed #" + directive + " directive - no filename");
			}
			else if ( !Character.isWhitespace(line.charAt(index+directiveLength)) )
			{
				// #includeXXX (e.g. # includes the ...)
				// so really a comment, not #include directive
				isInclude = false;
			}
			else
			{
				StringBuffer prefixBuf = new StringBuffer("");
				StringBuffer filenameBuf = new StringBuffer("");
				char ch = ' ';

				// #include ??? - skip whitespace to beginning of token, if any
				for ( index += directiveLength; index < length; index++ )
				{
					ch = line.charAt(index);
					if ( !Character.isWhitespace(ch) )
						break;
				}

				// check if leading '+' on potential prefix
				int firstTokenAt = index;
				boolean leadingPlus = false;

				if ( ch == '+' )
				{
					leadingPlus = true;
					index++;
				}

				// Initially we don't know if we have the optional prefix.
				// Assume we do, but scan without applying character set restrictions
				// in case it really is a filename. Later rescan will correct if required.
				index = readPropertyKeyUnRestricted(file, lineNo, line, index, prefixBuf, false);

				// skip whitespace to beginning of next token, if any
				for ( ; index < length; index++ )
				{
					ch = line.charAt(index);
					if ( !Character.isWhitespace(ch) )
						break;
				}

				// read filename, if any
				index = readPropertyValue(null, file, lineNo, line, index, filenameBuf, false, VAR_SET_VALUE_ON_LOAD);

				// Now we can check if we really have the optional prefix
				// and rescan as necessary
				if ( filenameBuf.length() == 0 )
				{
					// #include filename (no prefix)
					// re-scan filename (including any leading '+' in the filename)
					prefixBuf.setLength(0);
					leadingPlus = false;
					index = readPropertyValue(null, file, lineNo, line, firstTokenAt, filenameBuf, false, VAR_SET_VALUE_ON_LOAD);
				}
				else
				{
					// #include prefix filename
					if ( _useRestrictedKeys )
					{
						// rescan prefix, applying character checks
						prefixBuf.setLength(0);
						readPropertyKeyRestricted(file, lineNo, line, (leadingPlus ? firstTokenAt+1 : firstTokenAt), prefixBuf, false);
					}
				}

				// trim trailing whitespace from filename
				int filenameEnd = filenameBuf.length() - 1;

				for ( ; filenameEnd >= 0; filenameEnd-- )
				{
					ch = filenameBuf.charAt(filenameEnd);
					if ( !Character.isWhitespace(ch) )
						break;
				}

				filenameBuf.setLength(filenameEnd + 1);

				int filenameLength = filenameBuf.length();

				if ( filenameLength == 0 )
				{
					// #include
					throw new PropertyFormatException(file.name(), lineNo, "Badly formed #" + directive + " directive - no filename");
				}

				// trim optional matching " around filename
				if ( filenameBuf.charAt(0) == '"' && filenameBuf.charAt(filenameLength-1) == '"' )
				{
					if ( filenameLength == 1 )
					{
						// #include "
						throw new PropertyFormatException(file.name(), lineNo, "Badly formed #" + directive + " directive - single quote '\"'");
					}

					if ( filenameLength == 2 )
					{
						// #include ""
						throw new PropertyFormatException(file.name(), lineNo, "Badly formed #" + directive + " directive - empty filename string '\"\"'");
					}

					filenameBuf = new StringBuffer(filenameBuf.toString().substring(1, filenameLength-1));
				}

				// deal with any preceding comments
				commentv = readComments(file, lineNo, commentv, null);

				// add #include [[+]newPrefix] filename to config entry
				String filename = filenameBuf.toString();
				String newPrefix = prefixBuf.toString();

				Vector vec = new Vector();
				vec.addElement(xmlPath == null ? "" : xmlPath);
				ConfigEntry entry = new ConfigEntry(file, lineNo, ConfigEntry.FILE, vec, filename, (leadingPlus ? "+" : "") + newPrefix);
				cfgData.insertChainEntry(entry, file, null);

				// set newPrefix and adjust if it begins with +
				if ( xmlPath != null && xmlPath.length() > 0 )
				{
					if ( newPrefix.length() == 0  )
					{
						newPrefix = xmlPath;
					}
					else
					{
						newPrefix = xmlPath + "." + newPrefix;
					}
				}

				if ( leadingPlus && prefix != null )
				{
					if ( newPrefix.length() == 0  )
					{
						newPrefix = prefix;
					}
					else if ( prefix.length() > 0 )
					{
						newPrefix = prefix + "." + newPrefix;
					}
				}

				// If filename is not absolute, construct path relative to directory containing current file.
				// In future, may add facility for #include search path and option to search CLASSPATH.
				File includeFile = new File(filename);

				if ( !includeFile.isAbsolute() )
				{
					String parent = file.name();

					if ( parent != null && parent.length() > 0 && !parent.equals("<stdin>") )
					{
						parent = new File(parent).getParent();

						if ( parent != null && parent.length() > 0 )
						{
							includeFile = new File(parent + '/' + filename);
						}
					}
				}

				// load #include file
				load(includeFile, newPrefix);

				isInclude = true;
			}
		}
		else
		{
			// really a comment, not #include directive
			isInclude = false;
		}

		return isInclude;
	}

	/**
	 * Process a resource setting.
	 *
	 * @param      file    the ConfigEntry for the file containing the comment block
	 * @param      lineNo  the line number  in the containing file of the last line in the comment block
	 * @param      line    the input line to process
	 * @param      index   the index in line to start parsing
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected void readResource(ConfigEntry file, int lineNo, StringBuffer line, int index, String prefix)
		throws PropertyFormatException
	{
		StringBuffer keyBuf = new StringBuffer("");
		StringBuffer valBuf = new StringBuffer("");

		index = readPropertyKey(file, lineNo, line, index, keyBuf, prefix, false);
		index = readAssignmentOperator(file, lineNo, line, index);

		String key = keyBuf.toString();
		index = readPropertyValue(key, file, lineNo, line, index, valBuf, true);
/************2003.11.03**************/
//		setProperty(key, valBuf.toString(), file, lineNo, true);
		setProperty(key, valBuf.toString(), file, lineNo, false);
/************2003.11.03**************/
	}

	/**
	 * Process key name part of resource setting.
	 *
	 * @param      file    the ConfigEntry for the file containing the comment block
	 * @param      lineNo  the line number  in the containing file of the last line in the comment block
	 * @param      line    the input line to process
	 * @param      index   the index in line to start parsing
	 * @param      keyBuf  storage for the key name
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected int readPropertyKey(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer keyBuf, String prefix, boolean isVariable)
		throws PropertyFormatException
	{
		// Check positioned at start of resource key
		char ch = line.charAt(index);

		switch ( ch )
		{
		case ' ':
		case '\t':
		case '=':
		case ':':
		case '.':
			throw new PropertyFormatException(file.name(), lineNo, "Internal error - readPropertyKey not at key position");
		}

		// Get the property key
		if ( prefix != null && prefix.length() > 0 )
		{
			keyBuf.append(prefix + ".");
		}

		if ( _useRestrictedKeys )
		{
			index = readPropertyKeyRestricted(file, lineNo, line, index, keyBuf, isVariable);
		}
		else
		{
			index = readPropertyKeyUnRestricted(file, lineNo, line, index, keyBuf, isVariable);
		}

		return index;
	}

	/**
	 * Process key name part of resource setting or variable name using restricted character set.
	 *
	 * @param      file    the ConfigEntry for the file containing the comment block
	 * @param      lineNo  the line number  in the containing file of the last line in the comment block
	 * @param      line    the input line to process
	 * @param      index   the index in line to start parsing
	 * @param      keyBuf  storage for the key name
	 * @param      isVariable true if processing property name in context of ${variable} name
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected int readPropertyKeyRestricted(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer keyBuf, boolean isVariable)
		throws PropertyFormatException
	{
		boolean gotkey = false;
		int length = line.length();
		char ch = '\n';

		for ( ; index < length && !gotkey; index++ )
		{
			ch = line.charAt(index);

			switch ( ch )
			{
			case '_':
			case '-':
			case '.':
				// allowed
				keyBuf.append(ch);
				break;

			case '+':
				// possible auto-number indicator: ends key
				if ( (index+1) < length && line.charAt(index+1) == '+' )
				{
					index++;

					// really got auto-number indicator
					if ( isVariable )
					{
						// '++' not allowed in variable name
						throw new PropertyFormatException(file.name(), lineNo, "Auto-number indicator in variable");
					}
					else
					{
						autoNumberPropertyKey(file, lineNo, line, index, keyBuf);
						index++;
					}
				}
				gotkey = true;
				break;

			case '$':
				// variable expansion - look for and replace value of ${name}
				index = readVariable(file, lineNo, line, index, keyBuf, VAR_SET_VALUE_ON_LOAD);
				break;

			case '}':
				gotkey = true;
				break;

			default:
				if ( Character.isLetterOrDigit(ch) )
				{
					// allowed
					keyBuf.append(ch);
				}
				else
				{
					// end of key
					gotkey = true;
				}
				break;
			}
		}

		if ( index == length )
		{
			// end of key
			gotkey = true;
		}

		if ( !gotkey && !isVariable )
		{
			throw new PropertyFormatException(file.name(), lineNo, "Property key name missing");
		}

		switch ( ch )
		{
		case ' ':
		case '\t':
		case '=':
		case ':':
			// allowed
			break;

		default:
			if ( isVariable )
			{
				// '}' allowed to terminate variable name
				if ( ch != '}' )
				{
					throw new PropertyFormatException(file.name(), lineNo, "Character in variable name is not letter, digit, '_', '-' or '.'");
				}
			}
			else
			{
				// '+' allowed to terminate resource key name
				if ( ch != '+' )
				{
					throw new PropertyFormatException(file.name(), lineNo, "Character in " + (isVariable ? "variable" : "property key") + " name is not letter, digit, '_', '-' or '.'");
				}
			}
			break;
		}

		return index;
	}

	/**
	 * Process key name part of resource setting or variable name using unrestricted character set.
	 *
	 * @param      file    the ConfigEntry for the file containing the comment block
	 * @param      lineNo  the line number  in the containing file of the last line in the comment block
	 * @param      line    the input line to process
	 * @param      index   the index in line to start parsing
	 * @param      keyBuf  storage for the key name
	 * @param      isVariable true if processing property name in context of ${variable} name
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected int readPropertyKeyUnRestricted(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer keyBuf, boolean isVariable)
		throws PropertyFormatException
	{
		boolean gotkey = false;
		int length = line.length();
		char ch = '\n';

		for ( ; index < length && !gotkey; index++ )
		{
			ch = line.charAt(index);

			switch ( ch )
			{
			case ' ':
			case '\t':
			case '=':
			case ':':
				// end of key name
				gotkey = true;
				break;

			case '}':
				if ( isVariable )
				{
					// end of variable
					gotkey = true;
				}
				else
				{
					// allowed for resource key
					keyBuf.append(ch);
				}
				break;

			case '+':
				// possible auto-number indicator: ends key only if really is one
				if ( (index+1) < length && line.charAt(index+1) == '+' )
				{
					index++;

					// really got auto-number indicator
					if ( isVariable )
					{
						// '++' not allowed in variable name
						throw new PropertyFormatException(file.name(), lineNo, "Auto-number indicator in variable");
					}
					else
					{
						autoNumberPropertyKey(file, lineNo, line, index, keyBuf);
						index++;
					}

					// end of key
					gotkey = true;
				}
				else
				{
					// allowed for resource key
					keyBuf.append(ch);
				}
				break;

			case '$':
				// variable expansion - look for and replace value of ${name}
				index = readVariable(file, lineNo, line, index, keyBuf, VAR_SET_VALUE_ON_LOAD);
				break;

			case '\\':
				index = readEscapeSequence(file, lineNo, line, index, keyBuf, true);
				break;

			default:
				// allowed
				keyBuf.append(ch);
				break;
			}
		}

		if ( index == length )
		{
			// end of key
			gotkey = true;
		}

		if ( !gotkey && !isVariable )
		{
			throw new PropertyFormatException(file.name(), lineNo, "Property key name missing");
		}

		switch ( ch )
		{
		case ' ':
		case '\t':
		case '=':
		case ':':
			// allowed
			break;

		default:
			if ( isVariable )
			{
				// '+' not allowed in variable name
				if ( ch == '+' )
				{
					throw new PropertyFormatException(file.name(), lineNo, "Auto-number indicator in variable");
				}
			}
			break;
		}

		return index;
	}

	/**
	 * Process optional assignment operator part of resource setting
	 *
	 * @param      file    the ConfigEntry for the file containing the comment block
	 * @param      lineNo  the line number  in the containing file of the last line in the comment block
	 * @param      line    the input line to start parsing
	 * @param      index   the index in line to start parsing
	 *
	 * @return  updated index in line
	 */
	protected int readAssignmentOperator(ConfigEntry file, int lineNo, StringBuffer line, int index)
	{
		int length = line.length();
		char ch = '\n';

		// Skip whitespace
		for ( ; index < length; index++ )
		{
			ch = line.charAt(index);

			if ( !Character.isWhitespace(ch) )
				break;
		}

		// Skip optional '=' or ':'
		switch ( ch )
		{
		case '=':
		case ':':
			index++;
		}

		// Skip whitespace
		for ( ; index < length; index++ )
		{
			ch = line.charAt(index);

			if ( !Character.isWhitespace(ch) )
				break;
		}

		return index;
	}

	/**
	 * Process value part of resource setting
	 *
	 * @param	   key	   the property name
	 * @param      file    the ConfigEntry for the file containing the comment block
	 * @param      lineNo  the line number  in the containing file of the last line in the comment block
	 * @param      line    the input line to process
	 * @param      index   the index in line to start parsing
	 * @param      valBuf  storage for the value
	 * @param      logWarning if true, warn if key non-null and value has non-escaped trailing whitespace
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected int readPropertyValue(String key, ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer valBuf, boolean logWarning, int variableOption)
		throws PropertyFormatException
	{
		int length = line.length();
		boolean trailingWhitespace = false;

		for ( ; index < length; index++ )
		{
			char ch = line.charAt(index);
			trailingWhitespace = Character.isWhitespace( ch );

			switch ( ch )
			{
			case '$':
				// variable expansion - look for and replace value of ${name}
				index = readVariable(file, lineNo, line, index, valBuf, variableOption);
				break;

            // updated by lilang on 2004-01-08
            // Don't process escape charactor in value, if in AFE,
            // giving no chance to process escape charactor, so holding orignal charactor sequence.
            // If in AFE, then directly forward to default label, if in TI, then execute label below.
			case '\\':
                if (isEscapeSupportedInRead) {
                    // Not in AFE, in TI, need to process escape charactor
                    index = readEscapeSequence(file, lineNo, line, index, valBuf, false);
                    break;
                } else {
                    // In AFE, not breaking, directly forward to default label -->
                }
            // orinal codes which has been comment on
//            case '\\':
//                index = readEscapeSequence(file, lineNo, line, index, valBuf, false);
//                break;

			default:
				// allowed
				valBuf.append(ch);
				break;
			}
		}

		if ( logWarning && key != null && trailingWhitespace )
		{
//			cat.warn("Property " + setCharEscapesInKey(key) + " has trailing whitespace.");
		}

		return index;
	}

	protected int readPropertyValue(String key, ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer valBuf, boolean logWarning)
		throws PropertyFormatException
	{
		return readPropertyValue(key, file, lineNo, line, index, valBuf, logWarning, getVariableOption());
	}

	/**
	 * Process an escape sequence
	 *
	 * @param      file    the ConfigEntry for the file containing the escape sequence
	 * @param      lineNo  the line number in the containing file
	 * @param      line    the input line
	 * @param      index   the index in line to start parsing
	 * @param      valBuf  storage for the value
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected int readEscapeSequence(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer valBuf, boolean isKey)
		throws PropertyFormatException
	{
		// map escaped character value
		int length = line.length();
		index++;

		// assert index < length since continuation lines already folded
		if ( index >= length )
		{
			throw new PropertyFormatException(file.name(), lineNo, "Internal error - readEscapeSequence unexpected line continuation");
		}

		char ch = line.charAt(index);

		if ( isKey )
		{
			switch ( ch )
			{
			case '.':
				throw new PropertyFormatException(file.name(), lineNo, "Bad escape sequence '\\.' in property key");

			case '\\':
			case '*':
			case '?':
				// Retain escape notation for these characters - needed for ConfigData handling.
				valBuf.append('\\');
				break;
			}
		}

		index = mapEscapedChar(file, lineNo, line, index, valBuf, isKey);

		return index;
	}

	/**
	 * Process ${variable} reference when reading properties file, where variable is a property name.
	 * But treat as literal if trailing $ or $x where x is not {.
	 *
	 * @param      file      the ConfigEntry for the file containing the comment block
	 * @param      lineNo    the line number  in the containing file of the last line in the comment block
	 * @param      line      the input line to process
	 * @param      index     the index in line to start parsing
	 * @param      varBuf    storage for the reference property value
	 * @param      varOption the value of the variable handling option to use
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected int readVariable(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer varBuf, int varOption)
		throws PropertyFormatException
	{
		index++;

		if ( index >= line.length() || line.charAt(index) != '{' )
		{
			// trailing $ - treat as literal $
			// Due to a bug in previous version of SuperProperties, it did not throw an exception for unescaped $
			// if it happened to be the last character on a line, so existing users can have properties files
			// for example with regexp that has trailing $.
			varBuf.append( '$' );
			return index - 1;
		}

		index++;

		if ( index >= line.length() )
		{
			throw new PropertyFormatException(file.name(), lineNo, "Bad variable syntax - expecting variable name after '${'");
		}

		StringBuffer varName = new StringBuffer("");
		char ch = line.charAt(index);

		switch ( ch )
		{
		case '=':
		case ':':
		case '.':
			throw new PropertyFormatException(file.name(), lineNo, "Bad variable syntax - expecting variable name after '${'");

		default:
			if ( Character.isWhitespace(ch) )
			{
				throw new PropertyFormatException(file.name(), lineNo, "Bad variable syntax - expecting variable name after '${'");
			}
		}

		index = readPropertyKey(file, lineNo, line, index, varName, null, true) - 1;

		ch = line.charAt(index);

		if ( ch != '}' )
		{
			if ( varName.length() == 0 )
			{
				throw new PropertyFormatException(file.name(), lineNo, "Bad variable syntax - found '${' when expecting '${property}'");
			}
			else
			{
				throw new PropertyFormatException(file.name(), lineNo, "Bad variable syntax - expecting '}' after '${"+ varName + "'");
			}
		}

		if ( varName.length() == 0 )
		{
			throw new PropertyFormatException(file.name(), lineNo, "Bad variable syntax - missing variable name in '${}'");
		}

		String key = varName.toString();
		String varValue = null;

		switch ( varOption )
		{
		default:
		case VAR_SET_VALUE_ON_LOAD:
			varValue = getProperty(key);
			break;

		case VAR_SET_VALUE_ON_GET:
		case VAR_KEEP_REFERENCE:
			varValue = "${" + key + "}";
			break;
		}

		if ( varValue == null )
		{
			varValue = System.getProperties().getProperty(key);

			if ( varValue == null )
			{
				switch ( getUnsetVariableOption() )
				{
				default:
				case UNSETVAR_THROW_EXCEPTION:
					throw new PropertyFormatException(file.name(), lineNo, "Variable reference to unset property in '${"+ varName + "}'");

				case UNSETVAR_KEEP_REFERENCE:
					varValue = "${" + key + "}";
					break;

				case UNSETVAR_SET_EMPTY:
					varValue = "";
					break;
				}
			}
		}

		varBuf.append(varValue);

		return index;
	}

	/**
	 * Recursively process ${variable} references in value of getProperty() call, where variable is a property name.
	 *
	 * @param      resource    the resource whose value is being evaluated
	 * @param      varList     list of variables referenced (to detect recursion)
	 * @param      value       the buffer in which to expand variable values
	 * @param      start       the index in value to start processing
	 *
	 * @return the index immediately following the expanded variable value
	 *
	 * @exception  PropertyFormatException if the variable reference is badly formed
	 */
	protected int evaluateVariable(String resource, Hashtable varList, StringBuffer value, int start)
		throws PropertyFormatException
	{
		start += 2; // skip ${

		if ( start >= value.length() )
		{
			throw new PropertyFormatException(null, 0, resource, "Bad variable syntax - expecting variable name after '${'");
		}

		StringBuffer varName = new StringBuffer("");
		char ch = value.charAt(start);

		switch ( ch )
		{
		case '=':
		case ':':
		case '.':
			throw new PropertyFormatException(null, 0, resource, "Bad variable syntax - expecting variable name after '${'");

		default:
			if ( Character.isWhitespace(ch) )
			{
				throw new PropertyFormatException(null, 0, resource, "Bad variable syntax - expecting variable name after '${'");
			}
		}

		int end = readPropertyKey(null, 0, value, start, varName, null, true) - 1;

		ch = value.charAt(end);

		if ( ch != '}' )
		{
			if ( varName.length() == 0 )
			{
				throw new PropertyFormatException(null, 0, resource, "Bad variable syntax - found '${' when expecting '${property}'");
			}
			else
			{
				throw new PropertyFormatException(null, 0, resource, "Bad variable syntax - expecting '}' after '${"+ varName + "'");
			}
		}

		if ( varName.length() == 0 )
		{
			throw new PropertyFormatException(null, 0, resource, "Bad variable syntax - missing variable name in '${}'");
		}

		String key = varName.toString();

		if ( varList.contains( key ) )
		{
			throw new PropertyFormatException(null, 0, resource, "Bad variable reference - recursive evaluation of '${" + key + "}'");
		}

		varList.put( key, key );

		String varValue = getProperty(key, VAR_KEEP_REFERENCE);
		boolean evaluateNestedVariableReferences = (varValue != null);

		if ( !evaluateNestedVariableReferences )
		{
			varValue = System.getProperties().getProperty(key);

			if ( varValue == null )
			{
				switch ( getUnsetVariableOption() )
				{
				default:
				case UNSETVAR_THROW_EXCEPTION:
					throw new PropertyFormatException(null, 0, resource, "Variable reference to unset property in '${"+ varName + "}'");

				case UNSETVAR_KEEP_REFERENCE:
					varValue = "${" + key + "}";
					break;

				case UNSETVAR_SET_EMPTY:
					varValue = "";
					break;
				}
			}
		}

		// Replace the variable reference with its value
		start -= 2; // compensate to include ${ skipped earlier
		delete(value, start, end); // equivalent to value.delete(start, end) but needed for JDK 1.1 compatibility
		value.insert(start, varValue);
		end = start + varValue.length();

		if ( evaluateNestedVariableReferences )
		{
			int varAt = varValue.indexOf( "${" );

			if ( varAt != -1 )
			{
				end = evaluateVariable( resource, varList, value, start + varAt );
			}
		}

		return end;
	}

	/**
	 * Delete characters from StringBuffer.
	 */
	protected void delete(StringBuffer buf, int start, int end)
	{
		int length = buf.length();

		if ( start > length ) start = length;
		if ( start < 0 ) start = 0;

		if ( end > length ) end = length;
		if ( end < 0 ) end = 0;

		if ( start < end )
		{
			// Note: for JDK 1.1 and 1.2 we need to avoid creating toMove as a zero length array
			// since StringBuffer.getChars() needlessly throws a StringIndexOutOfBoundsException
			// even when the number of characters requested is zero. This is fixed since JDK 1.3.
			char[] toMove = null;

			end++;

			if ( end < length )
			{
				toMove = new char[length-end];
				buf.getChars(end, length, toMove, 0);
			}

			buf.setLength(start);

			if ( toMove != null )
			{
				buf.append(toMove);
			}
		}
	}

	/**
	 * Process auto-number indicator (trailing '++') in a property name
	 *
	 * @param      file    the ConfigEntry for the file containing the comment block
	 * @param      lineNo  the line number  in the containing file of the last line in the comment block
	 * @param      line    the input line to process
	 * @param      index   the index in line to start parsing
	 * @param      keyBuf  storage for the key
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the resource is badly formed
	 */
	protected void autoNumberPropertyKey(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer keyBuf)
		throws PropertyFormatException
	{
		// peek ahead to check next char is whitespace, '=' or ':'
		int length = line.length();

		if ( index+1 < length )
		{
			char ch = line.charAt(index+1);

			switch ( ch )
			{
			case '=':
			case ':':
				// allowed
				break;

			default:
				if ( !Character.isWhitespace(ch) )
				{
					throw new PropertyFormatException(file.name(), lineNo, "Property auto-number indicator not at end of key name");
				}
			}
		}

		// check not at start of key
		if ( keyBuf.length() == 0 || keyBuf.charAt(keyBuf.length()-1) == '.' )
		{
			throw new PropertyFormatException(file.name(), lineNo, "Property auto-number indicator with no key name");
		}

		// now generate next number in sequence for this key
		String key = keyBuf.toString();
		String number = (String) _autoNumberedKeys.get(key);

		if ( number == null )
		{
			number = "1";
		}
		else
		{
			number = "" + (Integer.parseInt(number) + 1);
		}

		_autoNumberedKeys.put(key, number);
		keyBuf.append(number);
	}

	/**
	 * Getter/Setter methods for _useRestrictedKeys
	 */
	public boolean getUseRestrictedKeys()
	{
		return _useRestrictedKeys;
	}

	public void setUseRestrictedKeys(boolean useRestrictedKeys)
	{
		_useRestrictedKeys = useRestrictedKeys;
	}

	/**
	 * Maps an escaped input character in an input key to its escaped value and returns the updated line index.
	 *
	 * @param      file    the ConfigEntry for the file containing the escape sequence
	 * @param      lineNo  the line number in the containing file
	 * @param      line    the input line
	 * @param      index   the index in line to start parsing
	 * @param      valBuf  storage for the value
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the escape sequence is badly formed
	 */
	protected int mapEscapedCharInKey(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer valBuf)
		throws PropertyFormatException
	{
		return mapEscapedChar(file, lineNo, line, index, valBuf, true);
	}

	/**
	 * Maps an escaped input character in an input value to its escaped value and returns the updated line index.
	 *
	 * @param      file    the ConfigEntry for the file containing the escape sequence
	 * @param      lineNo  the line number in the containing file
	 * @param      line    the input line
	 * @param      index   the index in line to start parsing
	 * @param      valBuf  storage for the value
	 *
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the escape sequence is badly formed
	 */
	protected int mapEscapedCharInValue(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer valBuf)
		throws PropertyFormatException
	{
		return mapEscapedChar(file, lineNo, line, index, valBuf, false);
	}

	/**
	 * Maps an escaped input character in an input line to its escaped value and returns the updated line index.
	 *
	 * @param      file    the ConfigEntry for the file containing the escape sequence
	 * @param      lineNo  the line number in the containing file
	 * @param      line    the input line
	 * @param      index   the index in line to start parsing
	 * @param      valBuf  storage for the value
	 * @param      isKey   true if the input is a key, false if it is a resource value
	 * @return  updated index in line
	 *
	 * @exception  PropertyFormatException if the escape sequence is badly formed
	 */
	protected int mapEscapedChar(ConfigEntry file, int lineNo, StringBuffer line, int index, StringBuffer valBuf, boolean isKey)
		throws PropertyFormatException
	{
		char ch = line.charAt(index); // assert: range check done by caller

		switch ( ch )
		{
		case 'f':
			ch = '\f';
			break;

		case 'n':
			ch = '\n';
			break;

		case 'r':
			ch = '\r';
			break;

		case 't':
			ch = '\t';
			break;

		case 'u':
			{
				int length = line.length();

				int value = 0;
				int i = 1;

				for ( ; i <= 4 && (index + i < length); i++ )
				{
					ch = line.charAt(index + i);

					if ( ch >= '0' && ch <= '9' )
						value = (value << 4) + ch - '0';
					else if ( ch >= 'a' && ch <= 'f' )
						value = (value << 4) + 10 + ch - 'a';
					else if ( ch >= 'A' && ch <= 'F' )
						value = (value << 4) + 10 + ch - 'A';
					else
						throw new PropertyFormatException(file.name(), lineNo, "Illegal \\uxxxx encoding: \\u" + line.toString().substring(index + 1, index + i));
				}

				if ( i <= 4 )
					throw new PropertyFormatException(file.name(), lineNo, "Illegal \\uxxxx encoding: \\u" + (i <= 1 ? "" : line.toString().substring(index + 1, index + i)));

				ch = (char) value;
				index += 4;
				break;
			}

		default:
			// map to self
			break;
		}

		valBuf.append(ch);

		return index;
	}

	/**
	 * Scans a string for characters that need to be converted
	 * to an escape sequence on output and maps then to the
	 * appropriate escape sequence.
	 */
	protected String setCharEscapesInKey(String value)
	{
		return setCharEscapes(value, true, null, false);
	}

	protected String setCharEscapesInKey(String value, String extraCharsToEscape)
	{
		return setCharEscapes(value, true, extraCharsToEscape, false);
	}

	protected String setCharEscapesInValue(String value)
	{
		return setCharEscapes(value, false, null, false);
	}

	protected String setCharEscapesInValue(String value, boolean xml)
	{
		return setCharEscapes(value, false, null, xml);
	}

	protected String setCharEscapesInValue(String value, String extraCharsToEscape, boolean xml)
	{
		return setCharEscapes(value, false, extraCharsToEscape, xml);
	}

	protected String setCharEscapes(String value, boolean isKey, String extraCharsToEscape, boolean xml)
	{
		if ( value == null )
			return null;

		StringBuffer valBuf = new StringBuffer("");
		int length = value.length();

		for ( int i = 0; i < length; i++ )
		{
			boolean escape = false;
			char ch = value.charAt(i);

			switch ( ch )
			{
			case '=':
			case ':':
			case '#':
			case '!':
				// NB: For compatibility with Properties.store(), always escape these
				// by setting charsToEscape in call to setCharEscapesInValue() in store() method
				escape = isKey || (extraCharsToEscape != null && extraCharsToEscape.indexOf(ch) != -1);
				break;

			case ' ':
				// in key need to esape in key always
				// in value only if leading space in classic properties file
				// but not in xml properties file (since xml:space=preserve is set by caller)
				if ( !xml || isKey )
					escape = (isKey /*|| i == 0*/);  // updated by lilang on 2004-01-08
				break;

			case '+':
				escape = isKey;
				break;

            // updated by lilang on 2004-01-08
            // Don't process escape charactor in value
            // When loading property file, has not processed escape charactors, so hold orignal charactor sequence
            // We only remove below codes for this idea.
//			case '\\':
//				escape = !isKey;
//				break;
//
//			case '\f':
//				escape = true;
//				ch = 'f';
//				break;
//
//			case '\n':
//				escape = true;
//				ch = 'n';
//			break;
//
//			case '\r':
//				escape = true;
//				ch = 'r';
//			break;
//
//			case '\t':
//				escape = true;
//				ch = 't';
//				break;

			case '*':
			case '?':
				// wildcards already escaped in key (see ConfigData)
				// so fall through to default

			default:
                // updated by lilang on 2004-01-08
                // Not to convert to unicode escape sequence
				if ( false && ( ch < 0x0020 || ch > 0x007E ) )
				{
					// output unicode escape sequence
					String hexDigits = "0123456789ABCDEF";

					valBuf.append('\\');
					valBuf.append('u');
					valBuf.append( hexDigits.charAt((ch >> 12) & 0xF) );
					valBuf.append( hexDigits.charAt((ch >> 8) & 0xF) );
					valBuf.append( hexDigits.charAt((ch >> 4) & 0xF) );
					ch = hexDigits.charAt(ch & 0xF);
					escape = false; // since output above
				}
				else
				{
					// map to self if not in charsToEscape
					escape = (extraCharsToEscape != null && extraCharsToEscape.indexOf(ch) != -1);
				}
				break;
			}

			if ( escape )
				valBuf.append('\\');

			valBuf.append(ch);
		}

		return valBuf.toString();
	}

    /**
     * Returns an enumeration of all the keys in this property list, including
     * the keys in the default property list.
     *
	 * Overrides Properties.propertyNames() because it calls private method enumerate()
	 * which in turn calls get() which SuperProperties now overrides.
	 *
     * @return  an enumeration of all the keys in this property list, including
     *          the keys in the default property list.
     * @see     java.util.Enumeration
     * @see     java.util.Properties#defaults
     */
    public Enumeration propertyNames()
	{
		Hashtable h = new Hashtable();

		if ( defaults != null )
		{
			for ( Enumeration e = defaults.propertyNames(); e.hasMoreElements(); )
			{
				String key = (String)e.nextElement();
				h.put(key, key);
			}
		}

		for ( Enumeration e = keys(); e.hasMoreElements(); )
	    {
	        String key = (String)e.nextElement();
			h.put(key, key);
		}

		return h.keys();
    }

	/**
	 * Finds the subset of properties, if any, starting with oldPrefix
	 * and substitutes newPrefix for oldPrefix in the found property keys.
	 * If the subset is empty, the returned SuperProperties object will contain no properties.
	 * <P>
	 * Important: the returned subset does not contain wildcard entries which might
	 * be matched in a call to getProperty(). If oldPrefix contains wildcard characters
	 * only the wildcard entries with exactly that prefix will be returned.
	 * <P>
	 * @param oldPrefix	Prefix to use to find the subset of properties
	 * @param newPrefix The new prefix to use in place of oldPrefix
	 * <P>
	 * @return SuperProperties object containing the requested subset.
	 */
	public synchronized SuperProperties getSubset(String oldPrefix, String newPrefix)
	{
		SuperProperties subset = new SuperProperties();

		int index = (oldPrefix == null ? 0 : oldPrefix.length());

		if ( newPrefix == null ) newPrefix = "";

		for ( Enumeration e = propertyNames(); e.hasMoreElements(); )
		{
			Object obj = e.nextElement();

			if ( obj instanceof String )
			{
				String key = (String) obj;

				if ( index == 0 || key.startsWith(oldPrefix) )
				{
					String subsetKey = newPrefix + key.substring(index);

					if ( subsetKey.startsWith( "." ) )
					{
						subsetKey = subsetKey.substring(1);
					}

					subset.setProperty(subsetKey, getProperty(key));
				}
			}
		}

		return subset;
	}

	/**
	 * Finds the subset of properties, if any, starting with prefix
	 * and strips prefix from the found property keys.
	 * If the subset is empty, the returned SuperProperties object will contain no properties.
	 * <P>
	 * Important: the returned subset does not contain wildcard entries which might
	 * be matched in a call to getProperty(). If oldPrefix contains wildcard characters
	 * only the wildcard entries with exactly that prefix will be returned.
	 * <P>
	 * @param prefix	Prefix to use to find the subset of properties
	 * <P>
	 * @return SuperProperties object containing the requested subset.
	 */
	public SuperProperties getSubset(String prefix)
	{
		return getSubset(prefix, "");
	}

    /**
     * Compares the specified Object with this SuperProperties for equality.
	 * Overrides Hashtable.equals() use of get() because SuperProperties
	 * now overrides get(). Note that this version of equals() should
	 * be usable with JDK 1.1.8.
	 *
     * @return true if the specified Object is equal to this SuperProperties.
     * @see java.util.Hashtable#equals(Object)
     */
    public synchronized boolean equals(Object o)
	{
		boolean match = (o == this);

		if ( !match && (o instanceof SuperProperties) && ((SuperProperties)o).size() == size() )
		{
			SuperProperties sp = (SuperProperties)o;
			match = true;

			for (Enumeration e = keys(); match && e.hasMoreElements(); )
			{
				Object key = e.nextElement();
				Object value = ( key instanceof String ? getProperty((String)key) : super.get(key) );
				Object otherValue = ( key instanceof String ? sp.getProperty((String)key) : sp.get(key) );
				match = (value == null ? (otherValue==null && sp.containsKey(key)) : value.equals(otherValue));
			}
		}

		return match;
    }

	/**
	 * Returns the properties list represented as an XML string.
	 */
	public String asXML()
		throws IOException
	{
		return asXML(false);
	}

	/**
	 * Returns the properties list represented as an XML string.
	 * <P>
	 * @param withComments	if true, include comments in the XML returned
	 */
	public String asXML(boolean withComments)
		throws IOException
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter( sw );

		//zhq modify
//		save(pw, "", withComments, false, true);

		return sw.toString();
	}

	/**
	 * Override Properties.store() to get required escape mappings.
	 *
	 * @param   out      the output stream.
	 * @param   header   optional comment
	 *
	 * @exception  IOException If error writing to output stream
	 */
	public synchronized void store(OutputStream out, String header)
		throws IOException
	{
	    PrintWriter prnt = new PrintWriter(out, true);

	    if ( header != null )
		{
	        prnt.println("#" + header);
		}

	    prnt.println("#" + new Date());

		String extraCharsToEscape = ":=!#";

	    for ( Enumeration e = keys(); e.hasMoreElements(); )
	    {
	        String key = (String)e.nextElement();
	        String value = (String)super.get(key);

	        prnt.println(setCharEscapesInKey(key) + "=" + setCharEscapesInValue(value, extraCharsToEscape, false));
	    }

	    prnt.flush();
	}

	/**
	 * Override Properties.save() method to get required escape mappings.
	 * Needed by JDK 1.1 users. Also, for compatibility with JDK 1.1
	 * does not throw IOException.
	 *
	 * @deprecated As of JDK 1.2. Use the <code>store(OutputStream out,
	 * String header)</code> method.
	 *
	 * @param   out      the output stream.
	 * @param   header   optional comment
	 */
	public synchronized void save(OutputStream out, String header)
	{
	    try
	    {
	        store(out, header);
	    }
	    catch ( IOException ioe )
	    {
	    }
	}

	/**
	 * Saves this property list to the specified file.
	 *
	 * @param   path              output path prefix.
	 * @param   filename          output filename.
	 * @param   withComments      if true saves comments.
	 * @param   preserveIncludes  if true preserves #include file structure
	 *                            otherwise #include'd values are saved as
	 *                            if they were read in the main properties file
	 * @param   asXML             if true, writes an xml format file
	 *
	 * @exception  IOException  if an error occurred when saving the output
	 */
	public void save (String path, String filename, boolean withComments, boolean preserveIncludes)
		throws IOException
	{
		save (path, filename, withComments, preserveIncludes, false);
	}

	public void save (String path, String filename, boolean withComments, boolean preserveIncludes, boolean asXML)
		throws IOException
	{
		PrintWriter prnt = null;
		boolean flush = false;

		if ( filename.equals("-") )
		{
			flush = true;
			prnt = new PrintWriter(System.out, flush);
		}
		else
		{
			File file = makeFilePath(filename, path);
			prnt = new PrintWriter(new BufferedWriter(new FileWriter(file)), true);
		}
		//zhq modify
//		save(prnt, path, withComments, preserveIncludes, asXML);

		if ( flush )
			prnt.flush();
		else
			prnt.close();
	}

	/**
	 * Saves this property list to the specified file.
	 *
	 * @param   prnt              the output writer
	 * @param   path              output path prefix.
	 * @param   withComments      if true saves comments.
	 * @param   preserveIncludes  if true preserves #include file structure
	 *                            otherwise #include'd values are saved as
	 *                            if they were read in the main properties file
	 * @param   asXML             if true, writes an xml format file
	 *
	 * @exception  IOException  if an error occurred when saving the output
	 */
/* zhq modify
	public synchronized void save (PrintWriter prnt, String path, boolean withComments, boolean preserveIncludes, boolean asXML)
		throws IOException
	{
		ConfigEntry from = cfgData.getChainHead();
		Element xml = null;

		if ( asXML )
		{
			xml = new Element("tongintegrator");
		}

		if ( from != null )
			save(from.next(), prnt, path, withComments, preserveIncludes, null, xml);

		if ( asXML )
		{
			XMLOutputter xmlout = new XMLOutputter("  ", true);
			xmlout.setExpandEmptyElements(true);
			xmlout.setEncoding(encoding);
			xmlout.output( new Document( xml ), prnt );
		}
	}
*/

	/*
	 * Create directories to filename, using the given path if filename is not already absolute.
	 *
	 * The SuperProperties convention is to use '/' for file separator in 'include directives
	 * on all platforms so that properties files with relative pathnames are portable.
	 * Recent JDK versions (e.g. 1.2.2, 1.3.0) seem to have a File.mkdirs() method
	 * which accepts incoming '/' in filename as a path separator on Windows platforms,
	 * but older JDK versions (e.g. 1.1.8) seem to require the platform specific File.separator.
	 * So if the File.separator is '\\' we map incoming '/' to '\\'.
	 * The file separator is assumed to be a single character.
	 *
	 * @param filename  the filename
	 * @param path      path to use if filename is not already absolute.
	 *
	 * @return File representing the file pathname
	 */
	protected File makeFilePath(String filename, String path)
		throws IOException
	{
		if ( File.separator.length() > 1 )
		{
			throw new IOException("SuperProperties: platform has unsupported File.separator='" + File.separator + "'");
		}

		if ( File.separatorChar != '/' )
		{
			filename = filename.replace('/', File.separatorChar);
		}

		File file = new File(filename);

		if ( !file.isAbsolute() && path != null  && path.length() > 0 )
		{
			if ( File.separatorChar != '/' )
			{
				path = path.replace('/', File.separatorChar);
			}

			file = new File(path + File.separatorChar + filename);
		}

		// Use File getParent() method for JDK 1.1 compatibility
		String parent = file.getParent();

		if ( parent != null )
		{
			File parentFile = new File(parent);

			if ( parentFile != null )
			{
				// try to ensure necessary directories exist
				parentFile.mkdirs();
			}
		}

		return file;
	}


	/**
	 * Saves a resource value. Variable references in the value are preserved
	 * provided VAR_SET_VALUE_ON_LOAD was not set when the properties file was read,
	 * otherwise the variable values are output. Variable references in the key are not preserved.
	 *
	 * @param prnt                output writer
	 * @param entry               the ConfigEntry for the resource to be saved
	 * @param stripPrefix         leading prefix to remove
	 */
	protected void saveProperty(PrintWriter prnt, ConfigEntry entry, String stripPrefix)
	{
		switch ( entry.type() )
		{
		case ConfigEntry.RESOURCE:
		case ConfigEntry.XML_CDATA:
		case ConfigEntry.XML_ATTRIBUTE:
			int len = entry.len();
			String name = "";

			for ( int i = 0; i < len; i++ )
			{
				name += setCharEscapesInKey(entry.getPos(i)) + ".";
			}

			if ( stripPrefix != null && name.length() > 0 && name.startsWith(stripPrefix) )
			{
				name = name.substring(stripPrefix.length()+1); // +1 since there is always a trailing '.'
			}

			if ( name.length() > 0 )
			{
				prnt.print(name);
			}

			prnt.print(setCharEscapesInKey(entry.name()));
			prnt.print(" = ");
			prnt.println(setCharEscapesInValue( entry.value() ));
			break;
		}
	}


	/**
	 * Saves a comment block.
	 *
	 * @param prnt  output writer
	 * @param entry the ConfigEntry for the comment block to be saved
	 *
	 * @exception  IOException  if an error occurred when saving the output
	 */
	protected void saveComment(PrintWriter prnt, ConfigEntry entry)
		throws IOException
	{
		BufferedReader rdr = null;
		String xmlPath = entry.value();     // if non-null was an xml comment, need to prefix with #
		String commentTag = (xmlPath != null ? "#" : "");

		switch ( entry.type() )
		{
		case ConfigEntry.XML_PROCESSING_INSTRUCTION:
			String data = entry.getPos(0);
			rdr = new BufferedReader( new StringReader(entry.name() + (data == null ? "" : " " + data)) );

			for ( String line = rdr.readLine(); line != null; line = rdr.readLine() )
			{
				prnt.println( commentTag + line );
			}
			break;

		case ConfigEntry.COMMENT:
			int len = entry.len();

			for ( int i = 0; i < len; i++ )
			{
				String text = entry.getPos(i);

				if ( text == null )
				{
					// blank line
					prnt.println( "" );
				}
				else if ( text.length() == 0 )
				{
					prnt.println( commentTag );
				}
				else
				{
					rdr = new BufferedReader( new StringReader(text) );

					for ( String line = rdr.readLine(); line != null; line = rdr.readLine() )
					{
						prnt.println( commentTag + line );
					}
				}
			}
			break;
		}
	}

	/**
	 * Determine whether a text string is empty or contains only whitespace.
	 *
	 * @param text  The string to check
	 * @return      true if string is empty or contains only whitespace
	 */
	boolean isWhitespace(String text)
	{
		if ( text == null )
			return true;

		int len = text.length();
		boolean isws = (len == 0 );

		for ( int i = 0; i < len && isws; i++ )
		{
			isws = Character.isWhitespace( text.charAt(i) );
		}

		return isws;
	}

    /**
     * Override Hashtable.get() to call getProperty() when the given key is a String.
	 * This method is deprecated. The getProperty() method should be used instead.
	 * It is provided only as a trap for the cases where people have erroneously
	 * coded calls to get() instead of to getProperty().
     *
	 * @param   key   the property key.
	 * @return  the value in this property list with the specified key value.
     * @see     java.util.Hashtable#get(Object)
	 * @deprecated Use getProperty()
     */
    public synchronized Object get(Object key)
	{
		Object value = null;

		if ( key instanceof String )
		{
			value = getProperty( (String) key );

			// Callers should not be using get(String).
			// For debugging, output a stack trace of calling location.
//			cat.debug(" SuperProperties.get() method called - use getProperty() instead");

			try
			{
				StringWriter sw = new StringWriter();
				Exception e = new Exception();
				e.printStackTrace( new PrintWriter( sw ) );
				LineNumberReader lnr = new LineNumberReader( new StringReader( sw.toString() ) );

				// Print the stack trace (skipping first line with Exception detail)
				for ( String line = lnr.readLine(); (line = lnr.readLine()) != null; )
				{
//					cat.debug("" + line);
				}
			}
			catch ( IOException ioe )
			{
			}
		}
		else
		{
			value = super.get( key );
		}

		return value;
    }

	/**
	 * Searches for the property with the specified key in this property list.
	 * If the key is not found in this hierarchical property list, the non-hierarchical
	 * property list, the default property list, and its defaults, recursively, are
	 * then checked. The method returns <code>null</code> if the property is not found.
	 *
	 * @param   key   the property key.
	 * @return  the value in this property list with the specified key value.
	 * @see     java.util.Properties#defaults
	 */
	public String getProperty(String key)
	{
		return getProperty(key, getVariableOption());
	}
    
    /**
     * ����Ƿ�ȥ������ֵ�����Ŀո�,��ȡ����ֵ
     * @param key ����������
     * @param ifDeleteLastSpace �Ƿ񷵻�ȥ�����
     * �ո��ַ������ֵ
     * @return ��������ֵ
     */
	public String getProperty(String key,boolean ifDeleteLastSpace)
    {
		return getProperty(key, getVariableOption(),ifDeleteLastSpace);
	}

	/**
	 * ����Ƿ�ȥ������ֵ�����Ŀո�,��ȡ����ֵ
	 * @param key ����������
	 * @param variableOption ����ѡ��
	 * @param ifDeleteLastSpace �Ƿ񷵻�ȥ�����
	 * �ո��ַ������ֵ
	 * @return ��������ֵ
	 */
	protected String getProperty(String key, int variableOption,boolean ifDeleteLastSpace)
	{
		String val = cfgData.getResource(key);        
		if ( val == null )
		{
			val = (String)super.get(key);
		}

		if ( val == null && defaults != null )
		{
			val = defaults.getProperty(key);
		}

		if ( val != null && variableOption == VAR_SET_VALUE_ON_GET )
		{
			int start = 0;
			int varAt = -1;
			StringBuffer valBuf = new StringBuffer( val );

			// When evaluateVariable calls getProperty() preserve nested variable references
			// since evaluateVariable will re-evaluate them.
			// Note: use valBuf.toString().substring() instead of valBuf.substring() for JDK 1.1 compatibility
			while ( (varAt = valBuf.toString().substring(start).indexOf( "${" )) != -1 )
			{
				Hashtable varList = new Hashtable();
				varList.put( key, key );
				start = evaluateVariable(key, varList, valBuf, start+varAt);
			}

			val = valBuf.toString();
		}
		//modified by chenli 05-02-23
		if(ifDeleteLastSpace)
			if(val!=null){
				
				char[] buf = val.toCharArray();
				char c = 0;
				if(buf.length>0)
				   c = buf[buf.length-1];
				if(c == ' ')
				  val = val.substring(0, buf.length - 1);
			}
		if((val!=null)&&(val.equals("")))
		   val = null;
		//modified by chenli 05-02-23
		return val;
	}

	/**
	 * Searches for the property with the specified key in this property list.
	 * If the key is not found in this hierarchical property list, the non-hierarchical
	 * property list, the default property list, and its defaults, recursively, are
	 * then checked. The method returns <code>null</code> if the property is not found.
	 *
	 * @param   key   the property key.
	 * @param   variableOption  a variableOption value
	 * @return  the value in this property list with the specified key value.
	 * @see     java.util.Properties#defaults
	 */
	protected String getProperty(String key, int variableOption)
	{
		String val = cfgData.getResource(key);
		if ( val == null )
		{
			val = (String)super.get(key);
		}

		if ( val == null && defaults != null )
		{
			val = defaults.getProperty(key);
		}

		if ( val != null && variableOption == VAR_SET_VALUE_ON_GET )
		{
			int start = 0;
			int varAt = -1;
			StringBuffer valBuf = new StringBuffer( val );

			// When evaluateVariable calls getProperty() preserve nested variable references
			// since evaluateVariable will re-evaluate them.
			// Note: use valBuf.toString().substring() instead of valBuf.substring() for JDK 1.1 compatibility
			while ( (varAt = valBuf.toString().substring(start).indexOf( "${" )) != -1 )
			{
				Hashtable varList = new Hashtable();
				varList.put( key, key );
				start = evaluateVariable(key, varList, valBuf, start+varAt);
			}

			val = valBuf.toString();
		}
		//modified by chenli 05-02-23
		if(val!=null){
			
			char[] buf = val.toCharArray();
			char c = 0;
			if(buf.length>0)
			   c = buf[buf.length-1];
			if(c == ' ')
			  val = val.substring(0, buf.length - 1);
		}
		if((val!=null)&&(val.equals("")))
		   val = null;
		//modified by chenli 05-02-23
        return val;
	}

	/**
	 * ����Ƿ�ȥ������ֵ�����Ŀո�,��ȡ����ֵ
	 * @param key ����������
	 * @param defaultValue ����Ĭ��ֵ
	 * @param ifDeleteLastSpace �Ƿ񷵻�ȥ�����
	 * �ո��ַ������ֵ
	 * @return ��������ֵ�ַ�
	 */
	public String getProperty(String key, String defaultValue,boolean ifDeleteLastSpace)
	{
		String val = getProperty(key);
		//modified by chenli 05-02-23
        if(ifDeleteLastSpace){
    		if(val!=null){
    			
    			char[] buf = val.toCharArray();
    			char c = 0;
    			if(buf.length>0)
    			   c = buf[buf.length-1];
    			if(c == ' ')
    			  val = val.substring(0, buf.length - 1);
    		}
			if((val!=null)&&(val.equals("")))
			  val = null;
        }
		//modified by chenli 05-02-23 
		
		return (val == null) ? defaultValue : val;
	}
	
	/**
	 * Searches for the property with the specified key in this property list.
	 * If the key is not found in this property list, the default property list,
	 * and its defaults, recursively, are then checked. The method returns the
	 * default value argument if the property is not found.
	 *
	 * @param   key            the hashtable key.
	 * @param   defaultValue   a default value.
	 *
	 * @return  the value in this property list with the specified key value.
	 * @see     java.util.Properties#defaults
	 */
	public String getProperty(String key, String defaultValue)
	{
		String val = getProperty(key);
		//modified by chenli 05-02-23
		if(val!=null){
			
			char[] buf = val.toCharArray();
			char c = 0;
			if(buf.length>0)
			   c = buf[buf.length-1];
			if(c == ' ')
			  val = val.substring(0, buf.length - 1);
		}
		if((val!=null)&&(val.equals("")))
		  val = null;
		//modified by chenli 05-02-23 
		
		return (val == null) ? defaultValue : val;
	}
	/**
	 * Returns comments associated with the specified key in this property list.
	 * The method returns <code>null</code> if the property is not found.
	 *
	 * @param   key   the property key.
	 * @return  the comments associated with this property
	 */
	public String[] getComments(String key)
	{
		return cfgData.getComments(key);
	}

	/**
	 * Adds a comment block to the ConfigEntry chain.
	 *
	 * @param file       the ConfigEntry for the file containing the comment block
	 * @param lineNo     the line number  in the containing file of the last line in the comment block
	 * @param commentv   the comment block
	 * @param path	     if non-null, the prefix the element path is associated with
	 */
	public void setComment(ConfigEntry file, int lineNo, Vector commentv, String path)
	{
		if ( commentv != null && commentv.size() > 0 )
		{
			ConfigEntry entry = new ConfigEntry(file, lineNo, ConfigEntry.COMMENT, commentv, path);
			cfgData.insertChainEntry(entry, file, null);
		}
	}

	/**
	 * Adds a comment string to the ConfigEntry chain.
	 *
	 * @param file       the ConfigEntry for the file containing the comment text
	 * @param lineNo     the line number  in the containing file of the last line in the comment block
	 * @param text       the comment string; if null, then means blank line in classic properties file
	 * @param path	     if non-null, the prefix the element path is associated with
	 */
	public void setComment(ConfigEntry file, int lineNo, String text, String path)
	{
		Vector commentv = new Vector();
		commentv.addElement(text);

		setComment(file, lineNo, commentv, path);
	}

	/**
	 * Adds an XML processing instruction to the ConfigEntry chain.
	 *
	 * @param file       the ConfigEntry for the file containing the comment block
	 * @param lineNo     the line number  in the containing file of the last line in the comment block
	 * @param name       the processsing instruction name
	 * @param data       if non-null, the processing instruction data
	 * @param path	     if non-null, the prefix the element path is associated with
	 */
	public void setProcessingInstruction(ConfigEntry file, int lineNo, String name, String data, String path)
	{
		Vector vec = new Vector();
		vec.addElement(data);
		ConfigEntry entry = new ConfigEntry(file, lineNo, ConfigEntry.XML_PROCESSING_INSTRUCTION, vec, name, path);
		cfgData.insertChainEntry(entry, file, null);
	}

	/**
	 * Sets a property key/value pair.
	 *
	 * @param	key	the property name
	 * @param	value	the property value
	 *
	 * @return	the previous value for this key (a String).
	 */
	public Object setProperty(String key, String value)
		throws IllegalArgumentException
	{
		return setProperty(key, value, false);
	}

	/**
	 * Sets a property key/value pair.
	 *
	 * @param	key	this propertys name
	 * @param	value	this propertys value
	 * @param	logWarning	if true, will log a warning message if this property displaces another
	 *
	 * @return	the previous value for this key (a String).
	 */
	public Object setProperty(String key, String value, boolean logWarning)
		throws IllegalArgumentException
	{
		return setProperty(key, value, null, 0, logWarning);
	}

	/**
	 * Sets a property key/value pair.
	 *
	 * @param	key	this propertys name
	 * @param	value	this propertys value
	 * @param	filename	the associated filename
	 * @param	lineno	the associated line number
	 * @param	logWarning	if true, will log a warning message if this property displaces another
	 * @param	type	the resource type (for xml)
	 *
	 * @return	the previous value for this key (a String).
	 */
	public Object setProperty(String key, String value, ConfigEntry file, int lineno, boolean logWarning)
		throws IllegalArgumentException
	{
		return setProperty(key, value, file, lineno, logWarning, ConfigEntry.RESOURCE);
	}

	public Object setProperty(String key, String value, ConfigEntry file, int lineno, boolean logWarning, int type)
		throws IllegalArgumentException
	{
		ConfigEntry newEntry = cfgData.setResource(null, file, lineno, key, value, type);
		String newKey = key;

		if ( newEntry != null )
		{
			newKey = newEntry.prefix(true);
			newKey += "." + newEntry.name();
		}

		Object oldValue = super.put(newKey, value);

		if ( logWarning && oldValue != null && !value.equals(oldValue.toString()) )
		{
//			cat.warn("Property " + setCharEscapesInKey(newKey) + " value changed from '" + setCharEscapesInValue(oldValue.toString()) + "' to '" + setCharEscapesInValue(value) + "'");
		}

		return oldValue;
	}

	/**
	 * Removes the property with the specified key from this property list.
	 * The key is also removed from the default property list.
	 *
	 * @param   key            the hashtable key.
	 */
	public void removeProperty(String key)
	{
		cfgData.removeResource(key);
		super.remove(key);
	}

	public synchronized Object clone ()
	{
		return null;
	}

	public int getVariableOption()
	{
		return _variableOption;
	}

	public int getUnsetVariableOption()
	{
		return _unsetVariableOption;
	}

	public void setVariableOption(int option)
		throws Exception
	{
		switch ( option )
		{
		case VAR_SET_VALUE_ON_GET:
		case VAR_SET_VALUE_ON_LOAD:
		case VAR_KEEP_REFERENCE:
			_variableOption = option;
			break;

		default:
			throw new Exception("SuperProperties: setVariableOption - invalid parameter [" + option + "]");
		}
	}

	public void setUnsetVariableOption(int option)
		throws Exception
	{
		switch ( option )
		{
		case UNSETVAR_SET_EMPTY:
		case UNSETVAR_KEEP_REFERENCE:
		case UNSETVAR_THROW_EXCEPTION:
			_unsetVariableOption = option;
			break;

		default:
			throw new Exception("SuperProperties: setUnsetVariableOption - invalid parameter [" + option + "]");
		}
	}

    // added by lilang on 2004-01-08
    /**
     * �� AFE �У�����֧��ת���ַ��д������ TI ��Ȼ���ֶ�ת���ַ��д��֧�֣�
     * �������һ������� TI ��ȡ����ʱ֧��ת���ַ�Ĵ��?
     * @param isEscapeSupportedInRead
     * @see #readPropertyValue(String, ConfigEntry, int, StringBuffer, int, StringBuffer, boolean, int)
     * @see #readPropertyValue(String, ConfigEntry, int, StringBuffer, int, StringBuffer, boolean)
     */
    public void setEscapeSupportedInRead(boolean isEscapeSupportedInRead) {
        this.isEscapeSupportedInRead = isEscapeSupportedInRead;
    }
	/**
	 * @param encoding Ҫ���õ� encoding��
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
