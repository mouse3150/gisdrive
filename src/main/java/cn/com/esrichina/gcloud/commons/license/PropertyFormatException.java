package cn.com.esrichina.gcloud.commons.license;


	/**
	 * PropertyNotSetException - encapsulates the exception
	 * raised when a mandatory property is not set.
	 */
public class PropertyFormatException
	extends IllegalArgumentException
	{
	public PropertyFormatException (String filename, int lineNo, String resName, String msg)
		{
		super(filename!=null && filename.length()>0
				? filename + "(" + lineNo + "): Resource '" + resName + "' " + msg
				: "Resource '" + resName + "' " + msg);
		}


	public PropertyFormatException (String filename, int lineNo, String msg)
		{
		super(filename!=null && filename.length()>0
				? filename + "(" + lineNo + "): " + msg
				: msg);
		}
	}
