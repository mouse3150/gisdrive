package cn.com.esrichina.gcloud.commons.license;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * Various conversion methods.
 * These methods are mostly used to convert internal java data
 * fields into byte arrays or strings for use over the network.
 *
 *             
 */
public class Base64Encoder {

    /**
     * Performs RFC1521 style Base64 decoding of Base64 encoded data.
     * The output is a byte array containing the decoded binary data.
     * The input is expected to be a normal Unicode String object.
     * 
     * @param s The Base64 encoded string to decode into binary data.
     * @return An array of bytes containing the decoded data.
     */
    protected static final byte[] fromBase64String(String s) {
	try {
	    StringCharacterIterator iter = new StringCharacterIterator(s);
	    ByteArrayOutputStream   bytestr = new ByteArrayOutputStream();
	    DataOutputStream	    outstr = new DataOutputStream(bytestr);
	    char		    c;
	    int			    d, i, group;
	    int[]		    bgroup = new int[4];
	    decode: for (i=0, group=0, c = iter.first();
			 c != CharacterIterator.DONE;
			 c = iter.next())
	    {
		switch (c) {
		    case 'A': d =  0; break; case 'B': d =  1; break;
		    case 'C': d =  2; break; case 'D': d =  3; break;
		    case 'E': d =  4; break; case 'F': d =  5; break;
		    case 'G': d =  6; break; case 'H': d =  7; break;
		    case 'I': d =  8; break; case 'J': d =  9; break;
		    case 'K': d = 10; break; case 'L': d = 11; break;
		    case 'M': d = 12; break; case 'N': d = 13; break;
		    case 'O': d = 14; break; case 'P': d = 15; break;
		    case 'Q': d = 16; break; case 'R': d = 17; break;
		    case 'S': d = 18; break; case 'T': d = 19; break;
		    case 'U': d = 20; break; case 'V': d = 21; break;
		    case 'W': d = 22; break; case 'X': d = 23; break;
		    case 'Y': d = 24; break; case 'Z': d = 25; break;
		    case 'a': d = 26; break; case 'b': d = 27; break;
		    case 'c': d = 28; break; case 'd': d = 29; break;
		    case 'e': d = 30; break; case 'f': d = 31; break;
		    case 'g': d = 32; break; case 'h': d = 33; break;
		    case 'i': d = 34; break; case 'j': d = 35; break;
		    case 'k': d = 36; break; case 'l': d = 37; break;
		    case 'm': d = 38; break; case 'n': d = 39; break;
		    case 'o': d = 40; break; case 'p': d = 41; break;
		    case 'q': d = 42; break; case 'r': d = 43; break;
		    case 's': d = 44; break; case 't': d = 45; break;
		    case 'u': d = 46; break; case 'v': d = 47; break;
		    case 'w': d = 48; break; case 'x': d = 49; break;
		    case 'y': d = 50; break; case 'z': d = 51; break;
		    case '0': d = 52; break; case '1': d = 53; break;
		    case '2': d = 54; break; case '3': d = 55; break;
		    case '4': d = 56; break; case '5': d = 57; break;
		    case '6': d = 58; break; case '7': d = 59; break;
		    case '8': d = 60; break; case '9': d = 61; break;
		    case '+': d = 62; break; case '/': d = 63; break;
		    case '_': d = 62; break; case '-': d = 63; break;
		    default:
			// Any character not in Base64 alphabet is treated
			// as end of data.  This includes the '=' (pad) char.
			break decode;   // Skip illegal characters.
		}
		bgroup[i++] = d;
		if (i >= 4) {
		    i = 0;
		    group = ((bgroup[0] & 63) << 18)+((bgroup[1] & 63) << 12)+
			    ((bgroup[2] & 63) << 6) + (bgroup[3] & 63);
		    outstr.writeByte(((group >> 16) & 255));
		    outstr.writeByte(((group >> 8) & 255));
		    outstr.writeByte(group & 255);
		}
	    }
	    // Handle the case of remaining characters and
	    // pad handling.  If input is not a multiple of 4
	    // in length, then '=' pads are assumed.
	    switch (i) {
		case 2:
		    // One output byte from two input bytes.
		    group = ((bgroup[0] & 63) << 18)+((bgroup[1] & 63) << 12);
		    outstr.writeByte(((group >> 16) & 255));
		    break;
		case 3:
		    // Two output bytes from three input bytes.
		    group = ((bgroup[0] & 63) << 18)+((bgroup[1] & 63) << 12)+
			    ((bgroup[2] & 63) << 6);
		    outstr.writeByte(((group >> 16) & 255));
		    outstr.writeByte(((group >> 8) & 255));
		    break;
		default:
		    // Any other case, including correct 0, is treated as
		    // end of data.
		    break;
	    }
	    outstr.flush();
	    return bytestr.toByteArray();
	}
	catch (IOException e) {} // Won't happen. Return null if it does.
	return null;
    }
}

