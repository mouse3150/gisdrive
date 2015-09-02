package cn.com.esrichina.gcloud.commons.license;


import java.io.Serializable;
import java.util.Vector;

/**
 * A single configuration file entry - this is a list of the components
 * which make up the resource path, and the resource name.
 *
 * A string is used to keep the score when a match is being attempted, the
 * greater the string value (on ASCII sort), the greater the score.
 *
 * The function minLen returns the minimum number of resource path
 * components which must be present for a match to be possible. For example,
 * if the path is a*b.c*d and the resource name is x, then resName will
 * be 'x' and _words will be { a, *, b, c, *, e } (six elements), and minLen
 * will return 4 since this pattern will match a.b.c.d (which has four
 * components).
 *
 * The _words, _len, and _name members don't change for * the lifetime of
 * the ConfigEntry object, the others (_pos, _score) are used during matching
 * attempts.
 *
 * @author Jatinder Sangha
 *
 * Extended Jan 2001 by Colin Prosser to support COMMENT and FILE data.
 *
 * The new _next and _prev fields and associated methods are used by ConfigData
 * to maintain a chain of ConfigEntry for managing SuperProperties file
 * contents. By convention, the chain reflects the order items are read
 * in a properties files. Entries from #include files are linked in order
 * as if read in at the top level, however the _file and _lineNo entries
 * reflect details of the #include file. This information is sufficient
 * for an application to re-constitute a direct equivalent of the original
 * properties file, preserving comments, #include directives and ordering
 * of resource entries (see, for example, the SuperProperties save() method).
 *
 * The new _type field supports FILE and COMMENT as well as the previous
 * RESOURCE entries. This allows an application access to comment and #include
 * file data in addition to resource names and values.
 *
 * The _filename String is replaced by the_file ConfigEntry. COMMENT data
 * is stored in the _words vector. Use getPos() to access individual comment
 * lines in a block comment. FILE entries are used to store information
 * about a SuperProperties top-level file and #include files. The filename
 * is stored in the _val field and the optional prefix is stored in
 * the first element of the _words vector.
 *
 * @author Colin Prosser
 */
public class ConfigEntry
	implements Serializable
{
	static final long serialVersionUID = -3761076331346530260L;

	public String name ()
	{
		return _name;
	}

	public ConfigEntry file ()
	{
		return _file;
	}

	public ConfigEntry next ()
	{
		return _next;
	}

	public ConfigEntry prev ()
	{
		return _prev;
	}

	public ConfigEntry next (ConfigEntry n)
	{
		return _next = n;
	}

	public ConfigEntry prev (ConfigEntry p)
	{
		return _prev = p;
	}

	public String value ()
	{
		return _val;
	}

	public int type ()
	{
		return _type;
	}

	public void reset ()
	{
		_pos = 0;
		_score = null;
	}

	public int pos ()
	{
		return _pos;
	}

	public int pos (int delta)
	{
		return (_pos += delta);
	}

	public int len ()
	{
		return _len;
	}

	public String getPos (int p)
	{
		return (String)_words.elementAt(p);
	}

	public void scoreHit (char c)
	{
		switch ( _type )
		{
		case RESOURCE:
		case XML_ATTRIBUTE:
		case XML_CDATA:
			if (_score == null)
			{
				_score = "" + c;
			}
			else
			{
				_score = _score + c;
			}
			break;
		}
	}

	public String score ()
	{
		return _score==null ? "" : _score;
	}

	/**
	 * Constructor is passed full LHS value (including resourceName and the
	 * RHS value in the configuration file).
	 */
	public ConfigEntry (ConfigEntry file, int lineNo, int ttype, Vector w, String v)
	{
		this(file, lineNo, ttype, w, v, null);
	}

	public ConfigEntry (ConfigEntry file, int lineNo, int ttype, Vector w, String v, String p)
	{
		_file = file;
		_lineNo = lineNo;
		_type = ttype;

		_prev = null;
		_next = null;

		reset();

		switch ( _type )
		{
		default:
			_val = null;
			_words = null;
			_len = 0;
			_name = null;
			break;

		case FILE:
		case XML_PROCESSING_INSTRUCTION:
			_val = p;
			_words = w;
			_len = 0;
			_name = v;
			break;

		case COMMENT:
			_val = v;
			_words = w;
			_len = (w != null ? w.size() : 0);
			_name = null;
			break;

		case RESOURCE:
		case XML_ATTRIBUTE:
		case XML_CDATA:
			String vv = (v != null ? v : "");
			int vlen = vv.length();

			_len = (w != null ? w.size() : 0);

			/*
			 * Get total length of strings (including null terminators)
			 */
			int lmax = vlen + 1;
			for ( int i = 0;  i < _len;  ++i )
			{
				lmax += (1 + ((String)w.elementAt(i)).length());
			}

			/*
			 * Total space for _len pointers + characters counted above
			 */
			_words = new Vector();
			for ( int j = 0;  j < _len;  ++j )
			{
				_words.addElement ((String)w.elementAt (j));
			}

			/*
			 * Adjust len_ to not include final resource name component
			 */
			_len -= 1;

			/*
			 * resource name is last component
			 */
			_name = (_len >= 0 ? (String)_words.elementAt(_len) : null);
			_val  = v;
			break;
		}

		if ( _debug )
		{
			String filename = (_file == null ? "null" :_file.name());

			switch ( _type )
			{
			default:
				System.out.print("[" + filename + ", " + _lineNo + ", " + _type + "]");
				System.out.println("** unknown type=" + _type);
				break;

			case FILE:
				// FIXME: add prefix
				System.out.print("[" + filename + ", " + _lineNo + ", " + _type + "]");
				System.out.println("#include " + _name);
				break;

			case COMMENT:
				if ( _words != null )
				{
					int delta = _len;
					for ( int i = 0;  i < _len;  i++, delta--)
					{
						System.out.print("[" + filename + ", " + (_lineNo-delta) + ", " + _type + "]");
						System.out.println(_words.elementAt(i).toString());
					}
				}
				break;

			case RESOURCE:
			case XML_ATTRIBUTE:
			case XML_CDATA:
				if ( _words != null )
				{
					for ( int i = 0; i < _len; i++)
					{
						System.out.print(_words.elementAt(i).toString());
						System.out.print(".");
					}
					System.out.println(_name + "=" + _val);
				}
				break;
			}
		}
	}

	public boolean equals (Object o)
	{
		ConfigEntry e = (ConfigEntry)o;

		if ( e._type != _type )
			return false;

		switch ( _type )
		{
		case FILE:
		case RESOURCE:
		case XML_ATTRIBUTE:
		case XML_CDATA:
		case XML_PROCESSING_INSTRUCTION:
		if ( !e._name.equals(_name) )
				return false;
			break;
		}

		if ( e._len != _len )
			return false;

		for ( int i = 0;  i < _len;  ++i )
		{
			if ( !e._words.elementAt(i).equals(_words.elementAt(i)) )
			{
				return false;
			}
		}

		return true;
	}


	public int hashCode ()
	{
		if ( !_gotHashCode )
		{
			switch ( _type )
			{
			default:
				_hashCode = "".hashCode();
				break;

			case FILE:
			case RESOURCE:
			case XML_ATTRIBUTE:
			case XML_CDATA:
			case XML_PROCESSING_INSTRUCTION:
				_hashCode = _name.hashCode();
				break;
			}

			for ( int i = 0;  i < _len;  ++i )
			{
				_hashCode ^= _words.elementAt(i).hashCode();
			}

			_gotHashCode = true;
		}

		return _hashCode;
	}

	public String toString ()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(_name);
		sb.append(" [type=");
		sb.append(""+_type);
		sb.append("] [pos=");
		sb.append(""+_pos);
		sb.append("] [len=");
		sb.append(""+_len);
		sb.append("] [minLen=");
		sb.append("" + minLen ());
		sb.append("]\t");
		for ( int i = 0;  i < _len;  ++i )
		{
			if ( i > 0 ) sb.append(" ");
			sb.append(getPos(i));
		}

		return sb.toString();
	}

	/**
	 * Return the *minimum* number of components left in the path from the
	 * current position - simply the number of non-'*' components
	 */
	public int minLen ()
	{
		int min = 0;

		switch ( _type )
		{
		case RESOURCE:
		case XML_ATTRIBUTE:
		case XML_CDATA:
			for ( int i = _pos;  i < _len;  ++i )
			{
				if ( ((String)_words.elementAt(i)).charAt(0) != '*' )
				{
					++min;
				}
			}
			break;
		}

		return min;
	}

	public boolean isComment()
	{
		return _type == COMMENT;
	}

	public boolean isFile()
	{
		return _type == FILE;
	}

	public boolean isResource()
	{
		return _type == RESOURCE;
	}

	public boolean isXMLAttribute()
	{
		return _type == XML_ATTRIBUTE;
	}

	public boolean isXMLCDATA()
	{
		return _type == XML_CDATA;
	}

	public boolean isXMLProcessingInstruction()
	{
		return _type == XML_PROCESSING_INSTRUCTION;
	}

	public String prefix( boolean getNearest )
	{
		String path = null;

		switch ( _type )
		{
		case COMMENT:
		case XML_PROCESSING_INSTRUCTION:
		case FILE:
			path = value();

			if ( path == null && getNearest )
			{
				path = nearestPrefix();
			}
			break;

		case RESOURCE:
		case XML_ATTRIBUTE:
		case XML_CDATA:
			path = xmlPrefix( getNearest );
			break;
		}

		return path;
	}

	public String nearestPrefix()
	{
		// find nearest following resource
		String path = null;
		ConfigEntry resEntry = next();

		while ( resEntry != null && resEntry.type() != ConfigEntry.RESOURCE )
		{
			resEntry = resEntry.next();
		}

		if ( resEntry != null )
		{
			path = resEntry.prefix(false);
		}

		return path;
	}

	public String nearestXmlPrefix()
	{
		// find nearest following resource
		String path = null;
		ConfigEntry resEntry = next();

		while ( resEntry != null && resEntry.type() != ConfigEntry.RESOURCE )
		{
			resEntry = resEntry.next();
		}

		if ( resEntry != null )
		{
			path = resEntry.xmlPrefix(false);
		}

		return path;
	}

	public String xmlPrefix( boolean getNearest )
	{
		String path = null;

		switch ( _type )
		{
		case FILE:
		case RESOURCE:
		case XML_ATTRIBUTE:
		case XML_CDATA:
 			if ( _len > 0 )
 			{
 				path = "";

				for ( int i = 0; i < _len; i++ )
				{
					if ( path.length() > 0 )
						path += ".";

					path += getPos(i);
				}
			}
			break;
		}

		if ( path == null && getNearest )
		{
			path = nearestXmlPrefix();
		}

		return path;
	}

	static private boolean _debug = false;

	static final public int FILE = 1;		// also xml <?include ...?>
	static final public int RESOURCE = 2;	// and xml tag
	static final public int COMMENT = 4;	// and xml comment

	// need additonal XML_xxx types to reproduce xml format properties files
	static final public int XML_ATTRIBUTE = 8;
	static final public int XML_CDATA = 16;
	static final public int XML_PROCESSING_INSTRUCTION = 32;	// except <?include ...?>

	private ConfigEntry _file;
	private ConfigEntry _next;
	private ConfigEntry _prev;
	private int         _lineNo;
	private int         _type;
	private String      _name;
	private String      _val;
	private int         _len;
	private int         _pos;
	private String      _score;
	private Vector      _words;
	private int         _hashCode;
	private boolean     _gotHashCode = false;
};
