package cn.com.esrichina.gcloud.commons.license;



import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * ConfigData - maintains a searchable set of resources (name/value pairs).
 *
 * Resource names consist of words seperated by '.'s and may be wildcarded
 * as follows:
 *
 *    A wildcard character of * matches 1 or more simple words.
 *    A wildcard character of ? matches 1 simple word only.
 *
 *<p>
 * Valid resource names which can be set:
 *<p>
 *    x.y.z    x.y.?.a        x.*.b    *.x.*.b
 *<p>
 * Valid retrievable resource names:
 *<p>
 *    x.y.z        matches x.y.z
 *
 *    x.y.z.a      matches x.y.?.a
 *    x.y.a.a
 *    x.y.bb.a
 *
 *
 *    x.y.z.b      matches x.*.b
 *    x.z.b
 *    x.a.b.c.d.b
 *
 *
 *    q.x.y.z.b    matches *.x.*.b
 *
 * @author Jatinder Sangha
 *
 * Extended Jan 2001 by Colin Prosser to support managing comment and
 * #include file data.
 *
 * ConfigData now also maintains a chain of ConfigEntry for managing
 * SuperProperties file contents. By convention, the chain reflects the
 * order items are read in a properties files. Entries from #include files
 * are linked in order as if read in at the top level, however the entries
 * reflect details of the #include file name and line number. This information
 * is sufficient for an application to re-constitute a direct equivalent of
 * the original properties file, preserving comments, #include directives
 * and ordering of resource entries (see, for example, the SuperProperties
 * save() method).
 *
 * To insertChainEntry() and removeChainEntry() methods are used to manage
 * the chain of ConfigEntry. These methods DO NOT add or remove resources
 * from the searchable set, but they are called by setResource() and
 * removeResource() when a resource is added or removed from the
 * searchable set.
 *
 * @author Colin Prosser
 * @version    %version: 4 %
 */
public class ConfigData
	implements Serializable
{
	static final long serialVersionUID = 7967530998804212261L;

	//static Category cat = Category.getInstance(ConfigData.class.getName());

	private Hashtable _entries = new Hashtable();

	/*
	 * Head and tail of chain of ConfigEntry.
	 */
	private ConfigEntry _head = null;
	private ConfigEntry _tail = null;

	/**
	 * Default Constructor.
	 */
	public ConfigData()
	{
	}

	/*
	 * --------------------------------------------------------------------------
	 * Characters used in building match strings:
	 *   SC_MATCH_STAR    is used when a * was used to make a match, and the
	 *                    next component didn't match the name required
	 *   SC_MATCH_LOOSE   is used when a match was made over a '*', ie *.X
	 *                    matched the name X
	 *   SC_MATCH_ANYWORD is used when a name matched a ? character
	 *   SC_MATCH_TIGHT   is used when a name matched exactly
	 * --------------------------------------------------------------------------
	 */
	private static final char SC_MATCH_STAR    = '0';
	private static final char SC_MATCH_LOOSE   = '1';
	private static final char SC_MATCH_ANYWORD = '5';
	private static final char SC_MATCH_TIGHT   = '9';

	/**
	 * Returns the value of the named resource.
	 *
	 * @param    resName    the name of the resource of which to get the value.
	 *
	 * @return    the string value or null if the resource hasn't been set.
	 */
	public String getResource(String resName)
	{
		ConfigEntry entry = getEntry(resName);

		return (entry == null ? null : entry.value());
	}

	/**
	 * Returns comments associated with the named resource.
	 * The convention is to return the block of comments immediately
	 * preceding the resource entry in the properties file. Comments in
	 * an #include file are associated only with resource entries in the
	 * same #include file. To access trailing comments at the end of a
	 * file, with no following resource entry, examine the entries
	 * in the ConfigEntry chain.
	 *
	 * @param    resName    the name of the resource.
	 *
	 * @return   the string[] values or null if the resource hasn't been set.
	 */
	public String[] getComments(String resName)
	{
		String [] comments = null;
		ConfigEntry entry = getEntry(resName);

		if ( entry != null )
		entry = entry.prev();

		if ( entry != null && entry.isComment() && entry.len() > 0 )
		{
			comments = new String[entry.len()];

			for ( int i = 0; i < comments.length; i++ )
				comments[i] = entry.getPos(i);
		}

		return comments;
	}

	/**
	 * Returns the entry for the named resource.
	 *
	 * @param    resName    the name of the resource of which to get the value.
	 *
	 * @return   the entry or null if the resource hasn't been set.
	 */
	public ConfigEntry getEntry(String resName)
	{
		/*
		 * Check resource name passed is vaguely ok
		 */
		Vector spec = new Vector();
		int rlen = resName.length ();
		if ( rlen == 0  ||  resName.charAt(0) == '.'  ||  resName.charAt(rlen-1) == '.' )
		{
			return null;
		}

        // updated by lilang on 2004-04-08
        // since it uses 32.44% cpu that repeatly joining word string with charactors,
        // so we replace string with string buffer
        //////////////////////////////////////////////////////
		/*
		 * break resource name into component words
		 */
        StringBuffer wordBuf = new StringBuffer(rlen); // resName's length is max capacity of buffer
        for ( int c = 0;  c < rlen;  ++c )
        {
            char cc = resName.charAt(c);

            if (cc != '.')
            {
                wordBuf.append(cc);
            }
            else
            {
                if ( wordBuf.length() == 0 )
                {
                    return null;
                }
                spec.addElement(wordBuf.toString());
                wordBuf.setLength(0);
            }
        }
        int specLen = spec.size();

//		String word = "";
//		for ( int c = 0;  c < rlen;  ++c )
//		{
//			char cc = resName.charAt(c);
//
//			if (cc != '.')
//			{
//				word += cc;
//			}
//			else
//			{
//				if ( word.length() == 0 )
//				{
//					return null;
//				}
//				spec.addElement(word);
//				word = "";
//			}
//		}
//		int specLen = spec.size();

		/*
		 * Final component of spec is the real resource name - it won't have
		 * been gobbled in the while loop since we know that the spec doesn't end
		 * in a '.'
		 */
        String specResName = wordBuf.toString();

//        String specResName = word;
        //////////////////////////////////////////////////////

		//cat.debug("searching for " + specResName + "\n");

		/*
		 * As a simple first filter, place all pointers to all entries which
		 * match the resource name in a work array - these are the initial match
		 * candidates
		 */
		Hashtable work = null;
		Hashtable ceHt = (Hashtable)_entries.get(specResName);


		/*
		 * No matches ? Bail out ...
		 */
		if ( ceHt == null  ||  ceHt.size() == 0 )
		{
			//cat.debug("** no matches found");
			return null;
		}
		work = (Hashtable)ceHt.clone();

		for ( Enumeration values = work.elements();
		      values.hasMoreElements();
		      ((ConfigEntry)values.nextElement()).reset() )
			;


		/*
		 * Now work through each matched compoment eliminating those which don't
		 * match, and keeping a running score of those which do ...
		 *
		 * For each component in the resource spec ...
		 */
		for ( int specPos = 0;  specPos < specLen;  ++specPos )
		{
			String specBit = (String)spec.elementAt(specPos);
			//cat.debug ("spec item " + specPos + "[" + specBit + "]");

			/*
			 * ... check each of the remaining entries ...
			 */
			for ( Enumeration keys = work.keys();  keys.hasMoreElements();  )
			{
				Object key = keys.nextElement();

				ConfigEntry e = (ConfigEntry)work.get(key);

				boolean bumpedStar = false;

				/*
				 * is entry too long ?
				 */
				int eMinLen = e.minLen();

				if ( eMinLen > specLen - specPos )
				{
					/*
					 * there are too many components left in the match spec
					 * for a match to be possible
					 */
					//cat.debug("chuck it - too long " + e);
					work.remove(key);
					continue;
				}
				else if ( eMinLen == specLen - specPos )
				{
					/*
					 * We *have* to match this time - i.e. we have exactly N
					 * specified components left, one for each of the remaining
					 * spec items. If the front item is a '*', blow it off
					 */
					if ( e.getPos(e.pos()).equals("*") )
					{
						/*
						 * Advance to next item, and remember that this is what
						 * we have done
						 */
						e.pos(1);
						bumpedStar = true;
					}
				}

				/*
				 * Have we overflowed ?
				 */
				if ( e.pos() >= e.len() )
				{
					//cat.debug("chuck it - too few bits " + e);
					//cat.debug("e.pos = " + e.pos() + ", e.len=" + e.len());
					work.remove(key);
					continue;
				}

				/*
				 * Get current name piece from match candidate
				 */
				String se = e.getPos(e.pos ());

				if ( se.equals("*") )
				{
					/*
					 * Matched a loosely bound '*' ...
					 */
					char score = SC_MATCH_STAR;
					if ( e.pos()+1 < e.len() )
					{
						/*
						 * if the next position is a match for this string,
						 * bump both the '*' and the next position
						 */
						if ( e.getPos(e.pos()+1).equals(specBit) )
						{
							/*
							 * yes : do it ...
							 */
							e.pos(2);
							score = SC_MATCH_LOOSE;
						}
					}
					e.scoreHit(score);
				}
				else if ( se.equals("?") )
				{
					/*
					 * Matched a single word replacement wildcard
					 */
					e.pos(1);
					e.scoreHit(bumpedStar ? SC_MATCH_LOOSE : SC_MATCH_ANYWORD);
				}
				else
				{
					/*
					 * it's a tightly bound piece [a word] ...
					 */
					//cat.debug("Comparing " + se + " and " + specBit);
					if ( !se.equals(specBit) )
					{
						/*
						 * ... and non-matching ... chuck it away
						 */
						//cat.debug("chuck it - tight non-match " + e);
						work.remove(key);
						continue;
					}
					else
					{
						/*
						 * ... and matching - advance to next component
						 */
						e.pos(1);
						e.scoreHit(bumpedStar ? SC_MATCH_LOOSE : SC_MATCH_TIGHT);
					}
				}
			}
		}

		/*
		 * No matches ? Bail out ....
		 */
		if ( work.size() == 0 )
		{
			return null;
		}

		/*
		 * Final scoring pass: if matching entries have an '*' left, then they
		 * were loose-bound to the resource, otherwise tight-bound ...
		 */
		for ( Enumeration values = work.elements();  values.hasMoreElements();  )
		{
			ConfigEntry e = (ConfigEntry)values.nextElement();

			if ( e.pos() < e.len() )
			{
				/*
				 * stuff left ...
				 */
				e.scoreHit(SC_MATCH_LOOSE);
			}
			else
			{
				e.scoreHit(SC_MATCH_TIGHT);
			}
		}

		/*
		 * Find the highest score, and make sure there's only one such entry
		 */
		ConfigEntry maxEntry = null;
		String maxScore = "";
		int    maxCount = 0;
		for ( Enumeration values = work.elements();  values.hasMoreElements();  )
		{
			ConfigEntry e = (ConfigEntry)values.nextElement();

			String score = e.score();

			//cat.debug("score " + score + " - " + e);

			if ( score.compareTo(maxScore) > 0 )
			{
				maxScore = score;
				maxEntry = e;
				maxCount = 1;
			}
			else if ( score == maxScore )
			{
				++maxCount;
			}
		}

		if ( maxCount != 1 )
		{
			//cat.debug(" ** more than one entry with max score");
			return null;
		}

		// Eliminate plain foo matching prefix.foo
		if ( spec.size() == 0 && maxEntry.len() > 0 )
		{
			return null;
		}

		//cat.debug("value = " + maxEntry.resourceValue ());
		return maxEntry;
	}

	/**
	 * Sets a resource name/value pair.
	 *
	 * @param    resName   the resource name
	 * @param    resVal    the resources value
	 *
	 * @return   the previous entry for the resource or null
	 *
	 * @exception PropertyFormatException    if the resource name is invalid
	 */
	public ConfigEntry setResource (String resName, String resVal)
		throws PropertyFormatException
	{
		return setResource(null, null, 0, resName, resVal);
	}

	/**
	 * Sets a resource name/value pair.
	 *
	 * @param   prev    link this entry after prev in the ConfigEntry chain
	 * @param   file    the file associated with this entry
	 * @param   lineNo  the linenumber in file for this entry
	 * @param   resName the resource name
	 * @param   resVal  the resources value
	 * @param   type    the resource type (for xml)
	 *
	 * @return  the previous entry for the resource or null
	 *
	 * @exception PropertyFormatException    if the resource name is invalid
	 */
	public ConfigEntry setResource (ConfigEntry prev, ConfigEntry file, int lineNo, String resName, String resVal)
            throws PropertyFormatException
        {
		return setResource( prev, file, lineNo, resName, resVal, ConfigEntry.RESOURCE );
	}

	public ConfigEntry setResource (ConfigEntry prev, ConfigEntry file, int lineNo, String resName, String resVal, int type)
		throws PropertyFormatException
	{
		Vector lhs_ok = parseResourceName(resName, file, lineNo);
		ConfigEntry ce_new = new ConfigEntry(file, lineNo, type, lhs_ok, resVal);

		/*
		 * Find where to put the new entry - do we replace an old, identical, one
		 * or append it as a new entry to the end of the list
		 */
		String word = (String) lhs_ok.lastElement();
		boolean added = false;
		Hashtable ceHt = (Hashtable)_entries.get(word);
		if ( ceHt == null )
		{
			ceHt = new Hashtable();
			_entries.put(word, ceHt);
		}

		insertChainEntry(ce_new, file, prev);

		ConfigEntry displacedCE = (ConfigEntry)ceHt.put(ce_new, ce_new);

		return displacedCE;
	}

	/*
	 * Returns the head of the ConfigEntry chain.
	 */
	public ConfigEntry getChainHead()
	{
		if ( _head == null && _tail != null )
		{
			_head = _tail;

			for ( ConfigEntry h = _head.prev(); h != null; h = h.prev() )
			{
				_head = h;
			}
		}

		return _head;
	}

	/*
	 * Returns the tail of the ConfigEntry chain.
	 */
	public ConfigEntry getChainTail()
	{
		if ( _tail == null && _head != null )
		{
			_tail = _head;

			for ( ConfigEntry t = _tail.next(); t != null; t = t.next() )
			{
				_tail = t;
			}
		}

		return _tail;
	}

	/*
	 * Initializes the head and tail of the ConfigEntry chain.
	 *
	 * @param file  ConfigEntry for the file containing the entry to be added
	 * @param prev  if not null, used as an existing chain entry
	 */
	private void initChain(ConfigEntry file, ConfigEntry entry)
	{
		ConfigEntry h = getChainHead();
		ConfigEntry t = getChainTail();

		if ( h == null && entry != null )
		{
			_head = entry;

			for ( h = _head.prev(); h != null; h = h.prev() )
			{
				_head = h;
			}
		}

		if ( _head == null )
		{
			String filename = (file == null ? null : file.name());
			_head = new ConfigEntry(null, 0, ConfigEntry.FILE, null, filename);
		}

		if ( t == null && entry != null )
		{
			_tail = entry;

			for ( t = _tail.next(); t != null; t = t.next() )
			{
				_tail = t;
			}
		}

		if ( _tail == null )
			_tail = _head;
	}

	/*
	 * Inserts an entry in the ConfigEntry chain.
	 * Updates the head and tail of the chain if necessary.
	 *
	 * @param e     the ConfigEntry to add
	 * @param file  ConfigEntry for the file containing the entry to be added
	 * @param prev  chain the new entry after prev; if null, add to tail
	 */
	public void insertChainEntry(ConfigEntry e, ConfigEntry file, ConfigEntry prev)
	{
		if ( e == null )
			return;

		initChain(file, prev);

		if ( prev == null )
			prev = _tail;

		ConfigEntry next = (prev != null ? prev.next() : null);

		e.prev(prev);
		e.next(next);

		if ( prev != null )
			prev.next(e);

		if ( next != null )
			next.prev(e);

		if ( _tail == prev )
			_tail = e;
	}

	/*
	 * Removes an entry from the ConfigEntry chain.
	 * Updates the head and tail of the chain if necessary.
	 *
	 * @param e     the ConfigEntry to remove
	 */
	public void removeChainEntry(ConfigEntry e)
	{
		if ( e == null )
			return;

		ConfigEntry prev = e.prev();
		ConfigEntry next = e.next();

		if ( prev != null )
			prev.next(next);

		if ( next != null )
			next.prev(prev);

		if ( _tail == e )
			_tail = prev;

		if ( _head == e )
			_head = next;
	}

	/**
	 * Removes a resource name/value pair.
	 *
	 * @param    resName    the resource name
	 *
	 * @exception PropertyFormatException    if the resource name is invalid
	 */
	public void removeResource (String resName)
		throws PropertyFormatException
	{
		Vector lhs_ok = parseResourceName(resName, null, 0);
		ConfigEntry ce_remove = new ConfigEntry(null, 0, ConfigEntry.RESOURCE, lhs_ok, null);

		/*
		 * Remove equivalent entries.
		 */
		String word = (String) lhs_ok.lastElement();
		Hashtable ceHt = (Hashtable)_entries.get(word);
		if ( ceHt != null )
		{
			Hashtable ceHt_new = new Hashtable();
			for ( Enumeration values = ceHt.elements();  values.hasMoreElements();  )
			{
				ConfigEntry e = (ConfigEntry)values.nextElement();
				if ( !e.equals(ce_remove) )
				{
					ceHt_new.put(e, e);
				}
				else
				{
					removeChainEntry(e);
				}
			}

			if (  ceHt_new.size() > 0 )
				_entries.put (word, ceHt_new);
			else
				_entries.remove (word);
		}
	}

	/**
	 * Removes comments associated with a resource.
	 *
	 * @param    resName    the resource name
	 *
	 * @exception PropertyFormatException    if the resource name is invalid
	 */
	public void removeComments (String resName)
		throws PropertyFormatException
	{
		Vector lhs_ok = parseResourceName(resName, null, 0);
		ConfigEntry ce_remove = new ConfigEntry(null, 0, ConfigEntry.RESOURCE, lhs_ok, null);

		/*
		 * Find equivalent entries and remove comments.
		 */
		String word = (String) lhs_ok.lastElement();
		Hashtable ceHt = (Hashtable)_entries.get(word);
		if ( ceHt != null )
		{
			for ( Enumeration values = ceHt.elements();  values.hasMoreElements();  )
			{
				ConfigEntry e = (ConfigEntry)values.nextElement();
				if ( e.equals(ce_remove) )
				{
					ConfigEntry prev = e.prev();

					if ( prev != null && prev.isComment() )
					{
						removeChainEntry(prev);
					}
				}
			}
		}
	}

	/**
	 * Parses a resource name.
	 *
	 * @param    resName    the resource name
	 * @return   Vector of resource name components
	 *
	 * @exception PropertyFormatException    if the resource name is invalid
	 */
	protected Vector parseResourceName (String resName, ConfigEntry file, int lineNo)
		throws PropertyFormatException
	{
		String filename = (file == null ? "" : file.name());

		/*
		 * Now break up LHS into component parts...
		 */
		int ls_len = resName.length();

		if ( ls_len < 1 )
		{
			throw new PropertyFormatException(filename, lineNo, "empty name");
		}

		// check that last character is legal
		char ch = resName.charAt(ls_len-1);

		switch ( ch )
		{
		case '.':
			throw new PropertyFormatException(filename, lineNo, resName, "ends with word separator '" + ch + "' (require word as last component of resource name)");

		case '*':
		case '?':
			if ( ls_len < 2 || resName.charAt(ls_len-2) == '.' )
				throw new PropertyFormatException(filename, lineNo, resName, "ends with wildcard '" + ch + "' (require word as last component of resource name)");
		}

		final int NEED_SPEC = 1;
		final int IN_SPEC   = 2;
		final int NEED_DOT  = 3;

		int state = NEED_SPEC;
		int err   = 0;

		String word = "";
		Vector lhs_ok = new Vector();    // Of string

		/*
		 * break into constituent words : legal values are:
		 *     [ spec ] { . spec }
		 * where spec is either '*' or a word
		 */
		for ( int p = 0;  p < ls_len;  ++p )
		{
			char c = resName.charAt(p);
			if ( c == '\\' )
			{
				if ( ++p < ls_len )
				{
					c = resName.charAt(p);

					switch ( c )
					{
					case '\\':
					case '*':
					case '?':
						if ( state == NEED_DOT )
						{
							throw new PropertyFormatException(filename, lineNo, resName, "has escaped character '" + c + "' when expecting word separator '.' (require word; or wildcard on its own except in last component)");
						}
						else if ( state == NEED_SPEC  ||  state == IN_SPEC )
						{
							word += '\\';
							word += c;
							state = IN_SPEC;
						}
						else
						{
							throw new PropertyFormatException(filename, lineNo, resName, "Internal error - found escaped '" + c + "' when state=" + state);
						}
						break;

					default:
						throw new PropertyFormatException(filename, lineNo, resName, "Internal error - found escaped '" + c + "' in ConfigData when state=" + state);
					}
				}
			}
			else if ( c == '*'  ||  c == '?' )
			{
				word = "";
				if ( state == NEED_SPEC )
				{
					/*
					 * Don't push '*' if previous entry was '*'
					 */
					String ot = lhs_ok.size() > 0 ? (String)lhs_ok.elementAt(lhs_ok.size()-1) : null;
					if ( ot == null || !ot.equals("*") )
					{
						lhs_ok.addElement("" + c);
					}
					state = NEED_DOT;
				}
				else if ( state == IN_SPEC )
				{
					throw new PropertyFormatException(filename, lineNo, resName, "has word containing wildcard '" + c + "' (require word; or wildcard on its own except in last component)");
				}
				else if ( state == NEED_DOT )
				{
					throw new PropertyFormatException(filename, lineNo, resName, "has wildcard '" + c + "' when expecting '.' (require word; or wildcard on its own except in last component)");
				}
				else
				{
					throw new PropertyFormatException(filename, lineNo, resName, "Internal error - found wildcard '" + c + "' when state=" + state);
				}
			}
			else if ( c == '.' )
			{
				if ( state == NEED_SPEC )
				{
					throw new PropertyFormatException(filename, lineNo, resName, "has word separator '" + c + "' when expecting word or wildcard");
				}
				else if ( state == IN_SPEC  ||  state == NEED_DOT )
				{
					if ( word.length() > 0 )
					{
						lhs_ok.addElement(word);
						word = "";
					}
					state = NEED_SPEC;
				}
				else
				{
					throw new PropertyFormatException(filename, lineNo, resName, "Internal error - found word separator '" + c + "' when state=" + state);
				}
			}
			else
			{
				if ( state == NEED_DOT )
				{
					throw new PropertyFormatException(filename, lineNo, resName, "has character '" + c + "' when expecting word separator '.' (require word; or wildcard on its own except in last component)");
				}
				else if ( state == NEED_SPEC  ||  state == IN_SPEC )
				{
					word += c;
					state = IN_SPEC;
				}
				else
				{
					throw new PropertyFormatException(filename, lineNo, resName, "Internal error - found '" + c + "' when state=" + state);
				}
			}
		}

		if ( state != IN_SPEC  ||  word.length() == 0 )
		{
			throw new PropertyFormatException(filename, lineNo, resName, "has missing word");
		}

		//cat.debug("Adding " + word);
		lhs_ok.addElement(word);

		return lhs_ok;
	}
}
