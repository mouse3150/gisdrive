package cn.com.esrichina.gisdrive.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * This class provides a number of supporting String methods which are
 * otherwise not found in the java.util package. They are
 * implemented as static methods which take a String as argument and/or
 * provide a String as return value. This class can be divided into the
 * following sections:
 * <ul>
 * <li>General String and String manipulation methods.
 *     <ul>
 *     <li>getSpaces() returns an empty String
 *     <li>getZeros() returns a zero-filled String
 *     <li>padLeft() left-pads a String to a given width
 *     <li>padRight() right-pads a String to a given width
 *     <li>padZero() left-pads a String with zeros
 *     </ul>
 * <li>Formatted output of primitive data types (like printf in C/C++).
 *     <ul>
 *     <li>getFormatted() takes a String and a value argument and returns
 *         a composed String according to the printf formatting rules.
 *         This method is available for values of type int, double, char
 *         and String.
 *     </ul>
 * </ul>
 * <p>
 * getFormatted mostly follows the conventions used in C/C++ printf
 * formatting, thoufg there are some slight differences. If you're not
 * familar with printf, you may consider to take a look in any good
 * C reference. Here are the rules for out format String:
 * <ul>
 * <li>There may be an arbibtrary number of format specifiers.
 *     Only the first one is used to obtain the format information.
 * <li>The format specifier must have the form:
 *     <pre>
 *     %[-][+][0-9][.0-9][lL][dxXuofeEgGcs]
 *     ||  |  |    |     |   |
 *     ||  |  |    |     |   +- format char, see below
 *     ||  |  |    |     +----- long modifier, ignored
 *     ||  |  |    +----------- decimals
 *     ||  |  +---------------- field length
 *     ||  +------------------- plus sign
 *     |+---------------------- leftalign
 *     +----------------------- percent sign, starts format specifier
 *     </pre>
 *     <ul>
 *     <li>The percent sign always starts the format specifiert. Two
 *         consecutive %'s could be used to literally generate a single %
 *     <li>A "-" aligns output left (usually, it's right-aligned).
 *     <li>A "+" outputs a plus sign for positive numbers (usually, it is
 *         supressed.
 *     <li>The field length specifies the overall field length. If the
 *         formatted value is shorter, it will be padded with blanks,
 *         if it longer, it will remain unchanged.
 *     <li>The number of decimals specifies the length of the fractional
 *         part for floating point numbers. If ommitted, the default is 6.
 *     </ul>
 *     The format char determines the base type for the formatted value
 *     and has a big impact on how the result appears:
 *     <ul>
 *     <li>"d": integer value in decimal format.
 *     <li>"x": integer value in hexadecimal format (letters in lowercase).
 *     <li>"X": integer value in hexadecimal format (letters in uppercase).
 *     <li>"u": absolute integer value in decimal format. Result will always
 *              be positive.
 *     <li>"o": integer value in octal format.
 *     <li>"f": floating point value in fixed format (xxx.yyyyyy).
 *     <li>"e": floating point value in scientific format (0.yyyyyye+zzz).
 *     <li>"E": floating point value in scientific format (0.yyyyyyE+zzz).
 *     <li>"g": same as "f" for absolute values not smaller than 0.001 and
 *              not greater or equal than 1000. Otherwise, same as "e".
 *     <li>"G": same as "f" for absolute values not smaller than 0.001 and
 *              not greater or equal than 1000. Otherwise, same as "E".
 *     <li>"c": single character.
 *     <li>"s": String.
 *     </ul>
 *     Here are some additional hints:
 *     <ul>
 *     <li>For the "e" and "E" format chars the following is true:
 *         <ul>
 *         <li>The mantissa is normalized to its canonical form, i.e. it's
 *             always smaller than 1.0 and greater than or equal to 0.1
 *         <li>The exponent has always three digits.
 *         <li>The exponent always shows a sign, even for positive values.
 *         <li>The length of the frational part is subject to the number
 *             of decimals specified in the format string. If omitted, it
 *             defaults to 6.
 *         </ul>
 *     <li>The fractional part is subject to rounding for the last visible
 *         digit. Overflows could be propagated into the integer part or
 *         even into the exponent.
 *     <li>The "u" format char behaves different as in C/C++. Here, it always
 *         displays the absolute value of the argument in decimal format.
 *     <li>The "l" and "L" modfiers are completely ignored.
 *     <li>The number of decimals is applied to all format chars except "c"
 *         and "s". For integer values, the fractional part is always 0.
 *     </ul>
 * </ul>
 *
 * @version     1.0, 97/12/25
 * @author      Guido Krueger
 */
public class StringUtil {
  //---Constants-------------------------------------------------------
  static final int MAXSPACES = 2560;
  static final int MAXZEROS = 2560;
  static final int DEFAULTDECIMALS = 6;
  static final int EXPONENTLEN = 3;

  //---Pseudo Constants------------------------------------------------
  static String SPACES = "                                        ";
  static int SPACESLEN = 40;
  static String ZEROS = "0000000000000000000000000000000000000000";
  static int ZEROSLEN = 40;
  static Object lock = new Object();

  //---Public general String handling methods--------------------------

  /**
   *
   * @param str
   * @return
   */
  public static boolean isEmpty(String str) {
    return (str == null) || str.trim().equals("");
  }
  public static boolean isnotEmpty(String str) {
	    return (str != null) &&! str.trim().equals("");
	  }
  /**
   * Returns an empty String (i.e. a String which consists of only blanks)
   * of the specified length.
   *
   * @param       len length of String to return.
   * @return      Blank String of the given length.
   * @exception   StringIndexOutOfBoundsException if len is larger than
   *              MAXSPACES (currently 2560).
   */
  static public String getSpaces(int len) {
    if (len > SPACESLEN && SPACESLEN < MAXSPACES) {
      //aquire lock only when neccessary
      synchronized (lock) {
        while (len > SPACESLEN && SPACESLEN < MAXSPACES) {
          SPACES += SPACES;
          SPACESLEN += SPACESLEN;
        }
      }
    }
    return SPACES.substring(0, len);
  }

  /**
   * Returns a String of the specified length which consists of only
   * Zeros (ASCII 48).
   *
   * @param       len length of String to return.
   * @return      Zero-filled String of the given length.
   * @exception   StringIndexOutOfBoundsException if len is larger than
   *              MAXZEROS (currently 2560).
   */
  static public String getZeros(int len) {
    if (len > ZEROSLEN && ZEROSLEN < MAXZEROS) {
      //aquire lock only when neccessary
      synchronized (lock) {
        while (len > ZEROSLEN && ZEROSLEN < MAXZEROS) {
          ZEROS += ZEROS;
          ZEROSLEN += ZEROSLEN;
        }
      }
    }
    return ZEROS.substring(0, len);
  }

  /**
   * Pads the String s to the given length by inserting blanks at the left
   * side. If s is longer than len, the String remains unchanged.
   *
   * @param       s String to pad
   * @param       len length of resulting String
   * @return      The padded String.
   */
  static public String padLeft(String s, int len) {
    return padLeft(s, len, false);
  }

  /**
   * Pads the String s to the given length by inserting blanks at the left
   * side. If s is longer than len and trim is set to true, the result is
   * truncated to the given length.
   *
   * @param       s String to pad
   * @param       len length of resulting String
   * @param       trim truncates s if longer then len
   * @return      The padded String.
   */
  static public String padLeft(String s, int len, boolean trim) {
    int slen = s.length();
    String ret;
    if (slen < len) {
      ret = getSpaces(len - slen) + s;
    }
    else if (slen > len && trim) {
      ret = s.substring(0, len);
    }
    else {
      ret = s;
    }
    return ret;
  }

  /**
   * Pads the String s to the given length by inserting blanks at the right
   * end. If s is longer than len, the String remains unchanged.
   *
   * @param       s String to pad
   * @param       len length of resulting String
   * @return      The padded String.
   */
  static public String padRight(String s, int len) {
    return padRight(s, len, false);
  }

  /**
   * Pads the String s to the given length by inserting blanks at the right
   * end. If s is longer than len and trim is true, the result is
   * truncated to the given length.
   *
   * @param       s String to pad
   * @param       len length of resulting String
   * @param       trim truncates s if longer then len
   * @return      The padded String.
   */
  static public String padRight(String s, int len, boolean trim) {
    int slen = s.length();
    String ret;
    if (slen < len) {
      ret = s + getSpaces(len - slen);
    }
    else if (slen > len && trim) {
      ret = s.substring(0, len);
    }
    else {
      ret = s;
    }
    return ret;
  }

  /**
   * Left-pads the String with zeros to the given length.
   *
   * @param       s String to pad
   * @param       len length of resulting String
   * @return      The padded value as a String.
   */
  static public String padZero(String s, int len) {
    int slen = s.length();
    String ret;
    if (slen < len) {
      ret = getZeros(len - slen) + s;
    }
    else {
      ret = s;
    }
    return ret;
  }

  /**
   * Converts the integer value into a String which is left-padded with
   * zeros to the given length.
   *
   * @param       value integer value to pad
   * @param       len length of resulting String
   * @return      The padded value as a String.
   */
  static public String padZero(int value, int len) {
    String s = "" + value;
    int slen = s.length();
    String ret;
    if (slen < len) {
      ret = getZeros(len - slen) + s;
    }
    else {
      ret = s;
    }
    return ret;
  }

  //---Public String formatting methods--------------------------------

  /**
   * Formats the value according to the given format specifier. The
   * format char may be any character out of "dxXoufeEgGc". If the
   * format char is in "feEgG", the value is casted to a double, if
   * it is "c", it is casted to a char, before actually calling the
   * corresponding formatting method.
   *
   * @param       format format specifier according to the rules
   *              mentioned above
   * @param       value integer value which should be formatted as
   *              specified in the format argument
   * @return      The formatted value as a String. If the format char
   *              is "s", the return value is the error
   *              message "<***cannot convert value***>".
   */
  public static String getFormatted(String format, int value) {
    String ret;
    FormatParas fp = new FormatParas();
    parseFormatString(format, fp);
    ret = fp.prefix.toString();
    if (fp.specfound) {
      String valstring;
      if (fp.basetype == 'i') {
        valstring = toString(value, fp);
      }
      else if (fp.basetype == 'f') {
        valstring = toString( (double) value, fp);
      }
      else if (fp.basetype == 'c') {
        valstring = toString( (char) value, fp);
      }
      else {
        valstring = "<***cannot convert value***>";
      }
      ret += valstring + fp.suffix.toString();
    }
    return ret;
  }

  /**
   * Formats the value according to the given format specifier. The
   * format char may be any character out of "dxXoufeEgG". If the
   * format char is in "dxXou", the value is casted to an int before
   * actually calling the corresponding formatting method.
   *
   * @param       format format specifier according to the rules
   *              mentioned above
   * @param       value double value which should be formatted as
   *              specified in the format argument
   * @return      The formatted value as a String. If the format char
   *              is "c" or "s", the return value is the error
   *              message "<***cannot convert value***>".
   */
  public static String getFormatted(String format, double value) {
    String ret;
    FormatParas fp = new FormatParas();
    parseFormatString(format, fp);
    ret = fp.prefix.toString();
    if (fp.specfound) {
      String valstring;
      if (fp.basetype == 'f') {
        valstring = toString(value, fp);
      }
      else if (fp.basetype == 'i') {
        valstring = toString( (int) value, fp);
      }
      else {
        valstring = "<***cannot convert value***>";
      }
      ret += valstring + fp.suffix.toString();
    }
    return ret;
  }

  /**
   * Formats the value according to the given format specifier. The
   * format char may be any character out of "csdxXou". If the
   * format char is "s", the value is considered a String of length 1.
   * If it is in "dxXou", the value is casted to an int before
   * actually calling the corresponding formatting method.
   *
   * @param       format format specifier according to the rules
   *              mentioned above
   * @param       value character which should be formatted as
   *              specified in the format argument
   * @return      The formatted value as a String. If the format char
   *              is in "feEgG", the return value is the error
   *              message "<***cannot convert value***>".
   */
  public static String getFormatted(String format, char value) {
    String ret;
    FormatParas fp = new FormatParas();
    parseFormatString(format, fp);
    ret = fp.prefix.toString();
    if (fp.specfound) {
      String valstring;
      if (fp.basetype == 'c') {
        valstring = toString(value, fp);
      }
      else if (fp.basetype == 's') {
        valstring = toString("" + value, fp);
      }
      else if (fp.basetype == 'i') {
        valstring = toString( (int) value, fp);
      }
      else {
        valstring = "<***cannot convert value***>";
      }
      ret += valstring + fp.suffix.toString();
    }
    return ret;
  }

  /**
   * Formats the value according to the given format specifier. The
   * format char has to be the character "s".
   *
   * @param       format format specifier according to the rules
   *              mentioned above
   * @param       value String value which should be formatted as
   *              specified in the format argument
   * @return      The formatted value as a String. If the format char
   *              is different from "s", the return value is the error
   *              message "<***cannot convert value***>".
   */
  public static String getFormatted(String format, String value) {
    String ret;
    FormatParas fp = new FormatParas();
    parseFormatString(format, fp);
    ret = fp.prefix.toString();
    if (fp.specfound) {
      String valstring;
      if (fp.basetype == 's') {
        valstring = toString(value, fp);
      }
      else {
        valstring = "<***cannot convert value***>";
      }
      ret += valstring + fp.suffix.toString();
    }
    return ret;
  }

  /**
   * This method could be used to run the format parser on the first
   * argument and then return the format specification as a String.
   * It is thought for debugging purposes and need not be called
   * under normal circumstances. It's declared public because the
   * test engine is in a different package and thus can not call
   * package level methods.
   *
   * @param       format format specifier according to the rules
   *              mentioned above
   * @return      The parsed format String in a human-readable format.
   */
  public static String testFormatParser(String format) {
    FormatParas fp = new FormatParas();
    parseFormatString(format, fp);
    return "\nFormat String....: " + format + "\n" + fp.toString();
  }

  //---Private methods-------------------------------------------------
  /**
   * Converts the integer value into a String according to the format
   * encoded in fp. This method supports the format specifiers
   * "d", "u", "x" and "o". It interprets the modifiers "alignleft",
   * "plussign", "fieldlen", "padzero" and "decimals".
   */
  private static String toString(int value, FormatParas fp) {
    String ret = "";
    int signlen = 0;
    char sign = ' ';
    //Value conversion
    if (fp.convchar == 'd' || fp.convchar == 'u') {
      if (fp.convchar == 'u') {
        value = Math.abs(value);
      }
      if (value < 0) {
        ret = "" + ( -value);
        signlen = 1;
        sign = '-';
      }
      else {
        ret = "" + value;
        if (fp.plussign) {
          signlen = 1;
          sign = '+';
        }
      }
      if (fp.decimals > 0) {
        ret += "." + getZeros(fp.decimals);
      }
    }
    else if (fp.convchar == 'x') {
      ret = Integer.toHexString(value);
    }
    else if (fp.convchar == 'X') {
      ret = Integer.toHexString(value).toUpperCase();
    }
    else if (fp.convchar == 'o') {
      ret = Integer.toOctalString(value);
    }
    //Padding
    if (fp.fieldlen > 0) {
      if (fp.padzero) {
        ret = padZero(ret, fp.fieldlen - signlen);
        if (signlen > 0) {
          ret = sign + ret;
        }
      }
      else {
        if (signlen > 0) {
          ret = sign + ret;
        }
        if (fp.alignleft) {
          ret = padRight(ret, fp.fieldlen);
        }
        else {
          ret = padLeft(ret, fp.fieldlen);
        }
      }
    }
    else if (signlen > 0) {
      ret = sign + ret;
    }
    return ret;
  }

  /**
   * Converts the double value into a String according to the format
   * encoded in fp. This method supports the format specifiers
   * "f", "e", "E", "g" nd "G". It interprets the modifiers "alignleft",
   * "plussign", "fieldlen", "padzero" and "decimals".
   *
   * The floating point formatter always transforms the value into
   * the normalized canonical form "0.xy" where the whole part
   * is 0 and the first digit x of the fractional part is not 0. The
   * implementation relies on the fact that the implicit Java String
   * conversion for normalized values always returns a String in the
   * format mentioned above.
   */
  private static String toString(double value, FormatParas fp) {
    String ret = "";
    char convchar = fp.convchar;
    //Sign
    int signlen = 0;
    char sign = ' ';
    if (value < 0) {
      signlen = 1;
      sign = '-';
      value = -value;
    }
    else {
      if (fp.plussign) {
        signlen = 1;
        sign = '+';
      }
    }
    //Modify convchar
    if (fp.convchar == 'g' || fp.convchar == 'G') {
      if (value >= 1000.0 || value < 0.001) {
        convchar = (fp.convchar == 'g') ? 'e' : 'E';
      }
      else {
        convchar = 'f';
      }
    }
    //Compute exponent and canonical form
    int exp;
    String canonical;
    if (value == 0.0) {
      exp = 0;
      canonical = "0.0";
    }
    else {
      exp = (int) (Math.floor(Math.log(value) / Math.log(10)) + 1);
      //Checking for possible floating value representation errors
      //Occure if value is a multiple of 1000
      double normalized = value / Math.pow(10.0, exp);
      if (normalized >= 1.0) {
        //exp too small, multiplied by 10
        exp += 1.0;
        normalized = value / Math.pow(10.0, exp);
      }
      if (normalized < 0.1) {
        //exp too big, divided by 10
        exp -= 1.0;
        normalized = value / Math.pow(10.0, exp);
      }
      //Creating canonical form
      canonical = "" + normalized;
    }
    if (!canonical.startsWith("0.")) {
      ret = "***unexpected canonical form: " + ret;
    }
    else {
      //Value conversion
      canonical = canonical.substring(2);
      String s1, s2, s3;
      if (convchar == 'f') {
        if (exp > 0) {
          s1 = (canonical + getZeros(exp)).substring(0, exp);
          s2 = (canonical + getZeros(exp)).substring(exp);
        }
        else if (exp < 0) {
          s1 = "0";
          s2 = getZeros( -exp) + canonical;
        }
        else {
          s1 = "0";
          s2 = canonical;
        }
        s3 = "";
      }
      else { //convchar is 'e' or 'E'
        s1 = "0";
        s2 = canonical;
        if (exp >= 0) {
          s3 = "" + convchar + "+" + padZero(exp, EXPONENTLEN);
        }
        else {
          s3 = "" + convchar + "-" + padZero( -exp, EXPONENTLEN);
        }
      }
      //Correcting number of decimals and rounding
      int decimals = (fp.decimals == -1) ? DEFAULTDECIMALS : fp.decimals;
      if (decimals == 0) {
        if (s2.charAt(0) >= '5') {
          s1 = incrementString(s1);
        }
        s2 = "";
      }
      else {
        s2 += getZeros(decimals + 1);
        if (s2.charAt(decimals) >= '5') {
          s2 = incrementString(s2.substring(0, decimals));
          if (s2.length() > decimals) { //overflow in fractional part
            if (convchar == 'f') {
              //set frac part to zero and propagate overflow to integer part
              s2 = s2.substring(1);
              s1 = incrementString(s1);
            }
            else {
              //normalize frac part and propagate overflow to exponent
              s2 = "1" + s2.substring(2);
              int newexp = exp + 1;
              if (newexp >= 0) {
                s3 = "" + convchar + "+" + padZero(newexp, EXPONENTLEN);
              }
              else {
                s3 = "" + convchar + "-" + padZero( -newexp, EXPONENTLEN);
              }
            }
          }
        }
        else {
          s2 = s2.substring(0, decimals);
        }
      }
      //Building raw version
      ret = s1 + (decimals > 0 ? ("." + s2) : "") + s3;
      //Padding
      if (fp.fieldlen > 0) {
        if (fp.padzero) {
          ret = padZero(ret, fp.fieldlen - signlen);
          if (signlen > 0) {
            ret = sign + ret;
          }
        }
        else {
          if (signlen > 0) {
            ret = sign + ret;
          }
          if (fp.alignleft) {
            ret = padRight(ret, fp.fieldlen);
          }
          else {
            ret = padLeft(ret, fp.fieldlen);
          }
        }
      }
      else if (signlen > 0) {
        ret = sign + ret;
      }
    }
    return ret;
  }

  /**
   * Converts the char value into a String according to the format
   * encoded in fp. This method supports the format specifier "c" with
   * the "fieldlen" and "alignleft" modifiers.
   */
  private static String toString(char value, FormatParas fp) {
    String ret = "" + value;
    if (fp.fieldlen > 0) {
      if (fp.alignleft) {
        ret = padRight(ret, fp.fieldlen);
      }
      else {
        ret = padLeft(ret, fp.fieldlen);
      }
    }
    return ret;
  }

  /**
   * Formats the String value according to the format encoded in fp. This
   * method supports the format specifier "s" with the "fieldlen" and
   * "alignleft" modifiers.
   */
  private static String toString(String value, FormatParas fp) {
    if (fp.fieldlen > 0) {
      if (fp.alignleft) {
        value = padRight(value, fp.fieldlen);
      }
      else {
        value = padLeft(value, fp.fieldlen);
      }
    }
    return value;
  }

  /**
   * This method is used to parse the format String and to split it up
   * into three parts:
   * <ol>
   * <li>All characters which occur before the first format specifier
   *     will be stored in the "prefix" component.
   * <li>All characters which occur after the end of the first format
   *     specifier will be stored in the "suffix" component.
   * <li>All charactcers which comprise the first format specifiert are
   *     scanned and stored in a structured way.
   * </ol>
   * All three parts are returned in the fp argument which is described
   * in more detail below.
   */
  private static void parseFormatString(String format, FormatParas fp) {
    int specpos = 0;
    int formatlen = format.length();
    while (formatlen > 0) {
      specpos = format.indexOf("%");
      if (specpos == -1 || specpos >= formatlen - 1) {
        //% not found or last char in format
        if (!fp.specfound) {
          fp.prefix.append(format);
        }
        else {
          fp.suffix.append(format);
        }
        format = "";
        formatlen = 0;
      }
      else if (format.charAt(specpos + 1) == '%') {
        //found two consecutive %'s
        if (!fp.specfound) {
          fp.prefix.append(format.substring(0, specpos + 1));
        }
        else {
          fp.suffix.append(format.substring(0, specpos + 1));
        }
        format = format.substring(specpos + 2);
        formatlen -= (specpos + 2);
      }
      else if (fp.specfound) {
        //found single % in suffix
        fp.suffix.append(format.substring(0, specpos + 1));
        format = format.substring(specpos + 1);
        formatlen -= (specpos + 1);
      }
      else {
        //found first single %
        fp.prefix.append(format.substring(0, specpos));
        format = format.substring(specpos + 1);
        formatlen -= (specpos + 1);
        fp.specfound = true;
        //now scanning format specifier for -+lL0123456789.dxXoufeEgGcs chars
        int i = 0, numpos = 0;
        int num[] = {
            -1, -1};
        boolean firstdigit = true;
        while (i < formatlen) {
          char c = format.charAt(i);
          if (c == '-') {
            fp.alignleft = true;
          }
          else if (c == '+') {
            fp.plussign = true;
          }
          else if (c == 'l' || c == 'L') {
            fp.aslong = true;
          }
          else if (c >= '0' && c <= '9') {
            if (num[numpos] == -1) {
              num[numpos] = 0;
            }
            num[numpos] = 10 * num[numpos] + c - '0';
            if (c == '0' && firstdigit) {
              fp.padzero = true;
            }
            firstdigit = false;
          }
          else if (c == '.') {
            numpos = 1;
          }
          else if ("dxXou".indexOf(c) != -1) {
            fp.convchar = c;
            fp.basetype = 'i';
            ++i;
            break;
          }
          else if ("feEgG".indexOf(c) != -1) {
            fp.convchar = c;
            fp.basetype = 'f';
            ++i;
            break;
          }
          else if (c == 'c') {
            fp.convchar = c;
            fp.basetype = 'c';
            ++i;
            break;
          }
          else if (c == 's') {
            fp.convchar = c;
            fp.basetype = 's';
            ++i;
            break;
          }
          else {
            ++i;
            break;
          }
          ++i;
        }
        fp.fieldlen = num[0];
        fp.decimals = num[1];
        //specifier completely scanned
        format = format.substring(i);
        formatlen -= i;
      }
    }
  }

  /**
   * Treats the String as character-encoded positive integer value and
   * increments it by 1. The return value is larger than s if and only
   * if s completely consisted of "9" digits. This case could be
   * considered an overflow.
   */
  private static String incrementString(String s) {
    int i;
    StringBuffer sb = new StringBuffer(s);
    for (i = sb.length() - 1; i >= 0; --i) {
      char c = sb.charAt(i);
      if (c < '9') {
        sb.setCharAt(i, (char) (c + 1));
        break;
      }
      else {
        sb.setCharAt(i, '0');
      }
    }
    return ( (i == -1) ? "1" : "") + sb.toString();
  }

  public static String format(String sFormat, String[] obj) {
    int len = obj.length;
    String sResult = sFormat;

    for (int i = 0; i < len; i++) {
      sResult = getFormatted(sResult, obj[i]);
    }
    return sResult;
  }

  public static void main(String sArgv[]) {
//    String format = "%d %c%s %s";
//    String[] sArgs = new String[4];
//    sArgs[0] = "20";
//    sArgs[1] = "A";
//    sArgs[2] = "GE";
//    sArgs[3] = " is not good";
//    String sStr = Str.format(format, sArgs);
//    cat.debug("STR IS:" + sStr);
  }

  private static final char cKanJiSpace = '丂';

  /**
   * replace sub string
   */
  public static String replaceStr(String src, String sFnd, String sRep) {
    String sTemp = "";
    int endIndex = 0;
    int beginIndex = 0;
    if (src == null || sFnd == null) {
      return src;
    }
    do {
      endIndex = src.indexOf(sFnd, beginIndex);
      if (endIndex >= 0) { //mean found it.
        sTemp += src.substring(beginIndex, endIndex) + sRep;
        beginIndex = endIndex + sFnd.length();
      }
      else if (beginIndex >= 0) {
        sTemp += src.substring(beginIndex);
        break;
      }
    }
    while (endIndex >= 0);

    return sTemp;
//      return src;
  }

  //---------------------------------------------------------------
  //婡擻		敿妏婰崋仺慡妏婰崋曄姺
  //堷悢		str		曄姺偝傟傞暥帤楍
  //栠傝抣				曄姺屻偺暥帤楍
  //---------------------------------------------------------------
  public static String chgHkigo2Zkigo(String str) {

    String tmp = "";
    int i;
    String Z_kigo;
    String H_kigo;

    Z_kigo = "両亹亅仈仐丏丵乫丆乭";
    H_kigo = "!$-#@._/,\"";

    tmp = str;
    for (i = 0; i < H_kigo.length(); i++) {
      tmp = tmp.replace(H_kigo.charAt(i), Z_kigo.charAt(i));
    }

    return tmp;

  }

  //---------------------------------------------------------------
  //婡擻		敿妏僇僫仺慡妏僇僫曄姺
  //堷悢		str		曄姺偝傟傞暥帤楍
  //栠傝抣				曄姺屻偺暥帤楍
  //---------------------------------------------------------------
  public static String chgHkana2Zkana(String str) {

    String tmp;
    int i;
    String Z_kana;
    String H_kana;

    Z_kana = "傾僀僂僄僆僇僉僋働僐僒僔僗僙僜僞僠僣僥僩僫僯僰僱僲僴僸僼僿儂儅儈儉儊儌儎儐儓儔儕儖儗儘儚儝儞";
    H_kana = "辈炒刀犯购患骄坷谅媚牌侨墒颂臀闲岩釉罩棕仝圮";

    String Z_kana2 = "掇忿皋罐恨晦嫁睫巨哭擂赁罗棉霓兽宿剔娃无蔬诉踢瓦芜";
    String H_kana2 = "僈僊僌僎僑僓僕僘僛僝僟僡僤僨僪僶價僽儀儃僷僺僾儁億";

    tmp = str;
    //戺壒丒敿戺壒傪曄姺;
    for (i = 0; i < H_kana2.length(); i++) {
      tmp = tmp.replace(H_kana2.charAt(i), Z_kana2.charAt(i));
    }

    //捠忢偺僇僫傪曄姺;
    for (i = 0; i < H_kana.length(); i++) {
      tmp = tmp.replace(H_kana.charAt(i), Z_kana.charAt(i));
    }

    return tmp;

  }

  //---------------------------------------------------------------
  //婡擻		vbcrlf,vbcr,vblf,vbtab仺敿妏僗儁乕僗曄姺
  //堷悢		str		曄姺偝傟傞暥帤楍
  //栠傝抣				曄姺屻偺暥帤楍
  //---------------------------------------------------------------
  public static String chgCRLFTAB2SPC(String str) {

    String tmp;
    tmp = str;

    tmp = replaceStr(tmp, "\r\n", " ");
    tmp = replaceStr(tmp, "\r", " ");
    tmp = replaceStr(tmp, "\n", " ");
    tmp = replaceStr(tmp, "\b", " ");

    return tmp;

  }

  //-----------------------------------------------------------------------------
  //婡擻	暥帤楍偵敿妏丒慡妏偺偐側丒僇僫偑娷傑傟偰偄傞偐僠僃僢僋
  //堷悢	strValue	擖椡暥帤楍
  //栠抣	false丗娷傑傟偰偄側偄
  //丂丂丂true丗娷傑傟偰偄傞
  //-----------------------------------------------------------------------------
  public static boolean hasKana(String str) {

    String kana;
    int i;

    kana = "傾僀僂僄僆僇僉僋働僐僒僔僗僙僜僞僠僣僥僩僫僯僰僱僲僴僸僼僿儂儅儈儉儊儌儎儐儓儔儕儖儗儘儚儝儞";
    kana += "僈僊僌僎僑僓僕僘僛僝僟僡僤僨僪僶價僽儀儃僷僺僾儁億傽傿僁僃僅儠儢僢儍儏儑儙";
    kana += "偁偄偆偊偍偐偒偔偗偙偝偟偡偣偦偨偪偮偰偲側偵偸偹偺偼傂傆傊傎傑傒傓傔傕傗備傛傜傝傞傟傠傢傪傫";
    kana += "偑偓偖偘偛偞偠偢偤偧偩偫偯偱偳偽傃傇傋傏傁傄傉傌傐偀偂偅偉偋偭傖傘傚傡乕";
    kana += "辈炒刀犯购患骄坷谅媚牌侨墒颂臀闲岩釉罩棕仝圮Ж┆甙";

    for (i = 0; i < str.length(); i++) {
      if (kana.indexOf(str.substring(i, i + 1)) >= 0) {
        return true;
      }
    }

    return false;
  }

  // 暥帤楍偵慡晹偱敿妏丒慡妏偺偐側丒僇僫偐僠僃僢僋
  public static boolean chkKana(String str) {

    String kana;
    int i;

    kana = "傾僀僂僄僆僇僉僋働僐僒僔僗僙僜僞僠僣僥僩僫僯僰僱僲僴僸僼僿儂儅儈儉儊儌儎儐儓儔儕儖儗儘儚儝儞";
    kana += "僈僊僌僎僑僓僕僘僛僝僟僡僤僨僪僶價僽儀儃僷僺僾儁億傽傿僁僃僅儠儢僢儍儏儑儙";
    kana += "偁偄偆偊偍偐偒偔偗偙偝偟偡偣偦偨偪偮偰偲側偵偸偹偺偼傂傆傊傎傑傒傓傔傕傗備傛傜傝傞傟傠傢傪傫";
    kana += "偑偓偖偘偛偞偠偢偤偧偩偫偯偱偳偽傃傇傋傏傁傄傉傌傐偀偂偅偉偋偭傖傘傚傡乕";
    kana += "辈炒刀犯购患骄坷谅媚牌侨墒颂臀闲岩釉罩棕仝圮Ж┆甙 丂";

    for (i = 0; i < str.length(); i++) {
      if (kana.indexOf(str.substring(i, i + 1)) < 0) {
        return false;
      }
    }

    return true;
  }

  //-----------------------------------------------------------------------------
  //婡擻	暥帤楍偵慡妏暥帤偑娷傑傟偰偄傞偐僠僃僢僋
  //堷悢	strValue	擖椡暥帤楍
  //栠抣	true 丗娷傑傟偰偄側偄
  //丂丂丂false丗娷傑傟偰偄傞
  //-----------------------------------------------------------------------------
  public static boolean hasZenkaku(String str) {

    int i;
    String sTemp;
    byte[] bTmp;

    for (i = 0; i < str.length(); i++) {
      sTemp = str.substring(i, i + 1);
      bTmp = sTemp.getBytes();
      if (bTmp.length == 2) {
        return true;
      }
    }

    return false;
  }

  public static String GBToISO88591(String org) {
    if (org == null || org.equals("")) {
      return org;
    }
    try {
      byte[] buf = org.getBytes("GBK");
      return new String(buf, "ISO8859_1");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static String ISO88591ToGB(String org) {

    if (org == null || org.equals("")) {
      return org;
    }
    // ISO88591 -> GBK
    try {
      byte[] buf = org.getBytes("ISO8859_1");
      return new String(buf, "GBK");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  public static String[] ISO88591ToGB(String sOrgs[]) {
    if (sOrgs == null) {
      return null;
    }
    for (int i = 0; i < sOrgs.length; i++) {
      sOrgs[i] = ISO88591ToGB(sOrgs[i]);
    }

    return sOrgs;
  }

  public static String ISO88591ToSJIS(String sOrg) {
    return convertString(sOrg);
  }

  public static String[] ISO88591ToSJIS(String sOrg[]) {

    return convertString(sOrg);
  }

  /**
   *  ISO8859-1 ==> SJIS
   */
  public static String convertString(String sOrg) {

    if (sOrg == null || sOrg.equals("")) {
      return null;
    }

    try {
      byte[] buf = sOrg.getBytes("ISO8859_1");
      return new String(buf, "Shift_JIS");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /**
   *  String[] ISO8859-1 ==> SJIS
   */
  public static String[] convertString(String[] sOrgs) {

    if (sOrgs == null) {
      return null;
    }
    for (int i = 0; i < sOrgs.length; i++) {
      sOrgs[i] = convertString(sOrgs[i]);
    }

    return sOrgs;
  }

  /* 暥帤楍偵悢帤傪僠僃僢僋
   *
   */
  public static boolean isNumber(String str) {
    int i;
    for (i = 0; i < str.length(); i++) {
      if (!java.lang.Character.isDigit(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /*暥帤楍偵慡妏悢帤塸暥偑娷傑傟偰偄傞偐僠僃僢僋
   *
   */
  public static boolean containsZenNum(String str) {
    int i;
    String zenNum = "侽侾俀俁係俆俇俈俉俋";
    zenNum += "倎倐們倓倕倖倗倛倝倞倠倢倣値倧倫倯倰倱倲倳倴倵倶倷倸";
    zenNum += "俙俛俠俢俤俥俧俫俬俰俲俴俵俶俷俹俻俼俽俿倀倁倂倃倄倅";
    for (i = 0; i < str.length(); i++) {
      if (zenNum.indexOf(str.substring(i, i + 1)) >= 0) {
        return true;
      }
    }
    return false;
  }

  public static String nvl(String sIn) {
    return (sIn == null) ? "" : sIn;
  }

  public static String nvl(String sIn, String sDef) {
    return (sIn == null) ? sDef : sIn;
  }

  public static String fmtDate(String sYear, String sMonth, String sDay) {
    if (sMonth.length() == 1) {
      sMonth = "0" + sMonth;
    }
    if (sDay.length() == 1) {
      sDay = "0" + sDay;
    }
    return sYear + sMonth + sDay;
  }

  public static String convDate(String sDate, String sSep) {
    int pos = 0;
    String str = sDate;
    int len = str.length();
    if ( (len < 8) || (len > 10)) {
      return str;
    }
    else if (str.indexOf(sSep) == 4) {
      pos = str.indexOf(sSep, 5);
      if (pos == 6) {
        if (len == 8) {
          return str.substring(0, 4) + "0" + str.substring(5, 6) + "0" +
              str.substring(7, 8);
        }
        else {
          return str.substring(0, 4) + "0" + str.substring(5, 6) +
              str.substring(7, 9);
        }
      }
      else if (pos == 7) {
        if (len == 9) {
          return str.substring(0, 4) + str.substring(5, 7) + "0" +
              str.substring(8, 9);
        }
        else {
          return str.substring(0, 4) + str.substring(5, 7) +
              str.substring(8, 10);
        }
      }
      else {
        return str;
      }
    }
    else {
      return str;
    }
  }

  public static boolean chkDate(String date) {
    int[] months = {
        1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    int[] leapdays = {
        31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    int[] days = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    int year, month, day;
    int i;

    if (date == null) {
      return false;
    }
    try {
      if (date.length() == 10) {
        date = formatDate(date);
      }

      if (date.length() != 8 && date.length() != 6) {
        return false;
      }
      if (date.length() == 8) {
        year = java.lang.Integer.parseInt(date.substring(0, 4));
        month = java.lang.Integer.parseInt(date.substring(4, 6));
        day = java.lang.Integer.parseInt(date.substring(6, 8));
        if (month < 1 || month > 12 || day < 1) {
          return false;
        }

        if (isLeapYear(year)) {
          for (i = 0; i < months.length; i++) {
            if (months[i] == month && day > leapdays[i]) {
              return false;
            }
          }
        }
        else {
          for (i = 0; i < months.length; i++) {
            if (months[i] == month && day > days[i]) {
              return false;
            }
          }
        }
      }
      else if (date.length() == 6) {
        year = java.lang.Integer.parseInt(date.substring(0, 4));
        month = java.lang.Integer.parseInt(date.substring(4, 6));
        if (month < 0 || month > 12) {
          return false;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public static boolean isLeapYear(int year) {
    if (year % 100 != 0 && year % 4 == 0) {
      return true;
    }
    else if (year % 400 == 0) {
      return true;
    }
    return false;
  }

  public static boolean chkMail(String mail) {
    int i;
    int len = mail.length();
    int aPos = mail.indexOf("@");
    int dPos = mail.indexOf(".");
    int aaPos = mail.indexOf("@@");
    int adPos = mail.indexOf("@.");
    int ddPos = mail.indexOf("..");
    int daPos = mail.indexOf(".@");
    String lastChar = mail.substring(len - 1, len);
    String chkStr =
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_@.";
    if ( (aPos <= 0) || (aPos == len - 1) || (dPos <= 0) || (dPos == len - 1) ||
        (adPos > 0) || (daPos > 0) ||
        (lastChar.equals("@")) || (lastChar.equals(".")) || (aaPos > 0) ||
        (ddPos > 0)) {
      return false;
    }
    if (mail.indexOf("@", aPos + 1) > 0) {
      return false;
    }
    while (aPos > dPos) {
      dPos = mail.indexOf(".", dPos + 1);
      if (dPos < 0) {
        return false;
      }
    }
    for (i = 0; i < len; i++) {
      if (chkStr.indexOf(mail.charAt(i)) < 0) {
        return false;
      }
    }
    return true;
  }

  public static String removeChar(String str, String rc) {
    if (str == null) {
      return null;
    }

    int i = str.indexOf(rc);
    while (i >= 0) {
      str = str.substring(0, i) + str.substring(i + 1, str.length());
      i = str.indexOf(rc);
    }
    return str;
  }

  /**
   * divide the source String into several String array,
   * the numbers of the array is the separator numbers+1
   */
  public static String[] divideStr(String src, String separator) {
    if (src == null) {
      return null;
    }
    if (separator == null || separator == "") {
      String[] result = {
          src};
      return result;
    }

    int part = 0;
    int end = src.indexOf(separator);
    int begin = end + separator.length();
    while (end >= 0 && begin <= src.length()) {
      part++;
      end = src.indexOf(separator, begin);
      begin = end + separator.length();
    }

    //the src is divided into n+1 parts by the n separators.
    String[] result = new String[part + 1];
    part = 0;
    begin = 0;
    end = src.indexOf(separator);
    while ( (end >= 0) && (end + separator.length()) <= src.length()) {
      result[part++] = src.substring(begin, end);
      begin = end - 1;
      src = src.substring(0, end) +
          src.substring(end + separator.length(), src.length());
      end = src.indexOf(separator, ++begin);
    }
    result[part] = src.substring(begin, src.length());
    return result;
  }

  public static boolean chkPhone(String phone) {
    int i = phone.indexOf("--");
    int len = phone.length();
    if (i >= 0) {
      return false;
    }
    i = phone.indexOf("-");
    if ( (i == 0) || (i == len - 1)) {
      return false;
    }
    else if (i > 0) {
      i = phone.lastIndexOf("-");
      if (i == len - 1) {
        return false;
      }
      phone = removeChar(phone, "-");
    }
    if (!isNumber(phone)) {
      return false;
    }
    return true;
  }

  public static String formatDate(String sDate) {
    return formatDate(sDate, null, null);
  }

  public static String formatDate(String sDate, String sFmtTo) {
    return formatDate(sDate, null, sFmtTo);
  }

  public static String formatDate(String sDate, String sFmtFrom, String sFmtTo) {
    SimpleDateFormat sdfFrom = null;
    SimpleDateFormat sdfTo = null;
    java.util.Date dt = null;
    int nLen = 0;

    if (sDate == null) {
      return sDate;
    }

    try {
      nLen = sDate.length();

      if (sFmtFrom == null) {
        if (nLen == 8) {
          sFmtFrom = "yyyyMMdd";
        }
        else if (nLen == 10) {
          sFmtFrom = "yyyy/MM/dd";
        }
      }

      if (sFmtTo == null) {
        if (nLen == 8) {
          sFmtTo = "yyyy/MM/dd";
        }
        else if (nLen == 10) {
          sFmtTo = "yyyyMMdd";
        }
      }

      sdfFrom = new SimpleDateFormat(sFmtFrom);
      dt = sdfFrom.parse(sDate);

      sdfTo = new SimpleDateFormat(sFmtTo);

      return sdfTo.format(dt);

    }
    catch (Exception e) {
      return sDate;
    }

  }

  public static int compareDate(String sDate1, java.util.Date dt2) {
    SimpleDateFormat sdf = null;
    SimpleDateFormat sdfNormalized = new SimpleDateFormat("yyyyMMdd");

    String sFmt = "";
    java.util.Date dt1 = null;
    int nLen = 0;

    if (sDate1 == null || dt2 == null) {
      return 0;
    }

    try {
      if (sDate1.length() == 6) {
        sDate1 = sDate1 + "01";
      }

      nLen = sDate1.length();

      if (nLen == 8) {
        sFmt = "yyyyMMdd";
      }
      else if (nLen == 10) {
        sFmt = "yyyy/MM/dd";
      }

      sdf = new SimpleDateFormat(sFmt);
      dt1 = sdf.parse(sDate1);

      String sDt1 = sdfNormalized.format(dt1);
      String sDt2 = sdfNormalized.format(dt2);

      return sDt1.compareTo(sDt2);

    }
    catch (Exception e) {
      return 0;
    }

  }

  public static int compareDate(String sDate1, String sDate2) {
    SimpleDateFormat sdf = null;
    String sFmt = "";
    java.util.Date dt2 = null;
    int nLen = 0;

    if (sDate1 == null || sDate2 == null) {
      return 0;
    }

    try {

      if (sDate2.length() == 6) {
        sDate2 = sDate2 + "01";
      }

      nLen = sDate2.length();

      if (nLen == 8) {
        sFmt = "yyyyMMdd";
      }
      else if (nLen == 10) {
        sFmt = "yyyy/MM/dd";
      }

      sdf = new SimpleDateFormat(sFmt);
      dt2 = sdf.parse(sDate2);

      return compareDate(sDate1, dt2);

    }
    catch (Exception e) {
      return 0;
    }

  }

  public static boolean isToday(String sDate) {
    return (compareDate(sDate, new java.util.Date()) == 0);
  }

  public static boolean beforeToday(String sDate) {
    return (compareDate(sDate, new java.util.Date()) < 0);
  }

  public static boolean afterToday(String sDate) {
    return (compareDate(sDate, new java.util.Date()) > 0);
  }

  public static String toDBSelStr(String sIn) {
    if (sIn == null) {
      return sIn;
    }

    String sOut = sIn;

    sOut = replaceStr(sOut, "~", "~~");
    sOut = replaceStr(sOut, "%", "~%");
    sOut = replaceStr(sOut, "_", "~_");

    sOut = replaceStr(sOut, "'", "''");

    return sOut;
  }

  public static String toDBInsStr(String sIn) {
    return toDBInsStr(sIn, -1);
  }

  public static String toDBInsStr(String sIn, int nMaxLen) {
    if (sIn == null) {
      return sIn;
    }

    String sOut = sIn;

    sOut = replaceStr(sOut, "\"", "乭");

    if (nMaxLen != -1) {
      byte[] bs = sOut.getBytes();
      if (bs.length > nMaxLen) {
        sOut = new String(bs, 0, nMaxLen);
      }
    }

    sOut = replaceStr(sOut, "'", "''");

    return sOut;
  }

  public static String toHTMLOutStr(String sIn) {
    if (sIn == null) {
      return sIn;
    }
    String sOut = sIn;

    sOut = replaceStr(sOut, "<", "&lt;");
    sOut = replaceStr(sOut, ">", "&gt;");

    return sOut;
  }

//向html页面发送固定长度的字符串适合宽度固定的<TD>  length为中文字的长度，林金钟添加
  public static String getHtmlBRString(int length, String sIn) {
    if (sIn == null) {
      return "";
    }
    int strlen = sIn.length();
    if (strlen <= length) {
      return sIn;
    }
    else {
      String outStr = "";
      String tempStr = "";
      int i = 0;
      for (; i < strlen / length; i++) {
        tempStr = sIn.substring(i * length, i * length + length);
        tempStr = tempStr + "<br>";
        outStr = outStr + tempStr;
      }
      tempStr = sIn.substring(i * length, strlen);
      outStr = outStr + tempStr;
      return outStr;
    }

  }

  public static boolean isSpace(char c) {
    return (c == ' ' || c == '\t' || c == '丂');
  }

  public static boolean isSpace(String s) {
    if (s == null) {
      return false;
    }

    for (int i = 0; i < s.length(); i++) {
      if (!isSpace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static String filter(String value) {

    if (value == null) {
      return (null);
    }

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < value.length(); i++) {
      char ch = value.charAt(i);
      if (ch == '<') {
        result.append("&lt;");
      }
      else if (ch == '>') {
        result.append("&gt;");
      }
      else if (ch == '&') {
        result.append("&amp;");
      }
      else if (ch == '"') {
        result.append("&quot;");
      }
      else if (ch == '\r') {
        result.append("<BR>");
      }
      else if (ch == '\n') {
        if (i > 0 && value.charAt(i - 1) == '\r') {

        }
        else {
          result.append("<BR>");
        }
      }
      else if (ch == '\t') {
        result.append("&nbsp;&nbsp;&nbsp;&nbsp");
      }
      else if (ch == ' ') {
        result.append("&nbsp;");
      }
      else {
        result.append(ch);
      }
    }
    return (result.toString());
  }

  public static String filter2(String value) {

    if (value == null) {
      return (null);
    }

    StringBuffer result = new StringBuffer();
    for (int i = 0; i < value.length(); i++) {
      char ch = value.charAt(i);
      if (ch == '<') {
        result.append("&lt;");
      }
      else if (ch == '>') {
        result.append("&gt;");
      }
      else if (ch == '&') {
        result.append("&amp;");
      }
      else if (ch == '"') {
        result.append("&quot;");
      }
      else if (ch == '\r') {
        result.append("<BR>");
      }
      else if (ch == '\t') {
        result.append("&nbsp;&nbsp;&nbsp;&nbsp");
      }
      else {
        result.append(ch);
      }
    }
    return (result.toString());
  }

  /**
   * 将数据库中取出的日期字符串格式化 1、只要日期，不要时间 2、如果时1900年，说明日期为空
   * @param date
   * @return
   */
  public static String getDateString(String date) {
    String result = "";
    if (date != null && date.length() > 10) {
      result = date.substring(0, 10);
    }
    if (result.startsWith("1900")) {
      result = "";
    }
    return result;
  }

  public static String trim(String s) {
    if (s == null) {
      return null;
    }
    int begin, end;
    for (begin = 0; (begin < s.length()) && isSpace(s.charAt(begin)); begin++) {
      ;
    }
    for (end = s.length() - 1; (end >= 0) && isSpace(s.charAt(end)); end--) {
      ;
    }
    if (end < begin) {
      return "";
    }
    return s.substring(begin, end + 1);
  }

  /*
       public static String trimKanJiSpace(String s)
       {
      if ( s == null) return null;
      s = s.trim();
      int nLen = s.length();
      int nBegin = 0;
      int nEnd = nLen;
      for ( int i = 0; i < nLen; i++){
          nBegin = i;
          if (s.charAt(i) != cKanJiSpace){
                  break;
          }
      }
      for ( int i = nLen-1; i > 0; i--){
          if (s.charAt(i) != cKanJiSpace){
              nEnd = i + 1;
              break;
          }
      }
      if ( nBegin == nEnd) return "";
      s = s.substring(nBegin, nEnd);
      return s.trim();
       }
   */
  /**
   * Convert a string like "243,434,343" to long like 243434343
   */
  public static long unFormatNum(String input) {
    if (input == null || input.equals("")) {
      return 0;
    }
    String tmp = removeChar(input, ",");
    if (isNumber(tmp)) {
      long output;
      try {
        output = java.lang.Long.parseLong(tmp);
      }
      catch (Exception e) {
        return -1;
      }
      return output;
    }
    else {
      return -1;
    }
  }

  /**
   * Convert a long number like 123456789 to a format String "123,456,789"
   */
  public static String formatNum(int input) {
    if (input < 0) {
      return "";
    }
    String tmp = java.lang.String.valueOf(input);
    String output = "";
    int len = tmp.length();
    int i = len;
    while (i > 0) {
      if (i < 3) {
        output = tmp.substring(0, i) + output;
        break;
      }
      else {
        i = i - 3;
        output = tmp.substring(i, i + 3) + output;
        if (i > 0) {
          output = "," + output;
        }
      }
    }
    return output;
  }

  public static String formatNum(long input) {
    if (input < 0) {
      return "";
    }
    String tmp = java.lang.String.valueOf(input);
    String output = "";
    int len = tmp.length();
    int i = len;
    while (i > 0) {
      if (i < 3) {
        output = tmp.substring(0, i) + output;
        break;
      }
      else {
        i = i - 3;
        output = tmp.substring(i, i + 3) + output;
        if (i > 0) {
          output = "," + output;
        }
      }
    }
    return output;
  }

  public static String formatNum(String input) {
    if (input == null) {
      return null;
    }
    if (input.trim().length() == 0) {
      return null;
    }
    //String tmp = java.lang.String.valueOf(input);
    String tmp = input;
    String output = "";
    int len = tmp.length();
    int i = len;
    while (i > 0) {
      if (i < 3) {
        output = tmp.substring(0, i) + output;
        break;
      }
      else {
        i = i - 3;
        output = tmp.substring(i, i + 3) + output;
        if (i > 0) {
          output = "," + output;
        }
      }
    }
    return output;
  }

  public static int length(String str) {
    if (str == null) {
      return 0;
    }
    else {
      return str.getBytes().length;
    }
  }

  /**
   *将字符串转换成整数，如果是无效字符，则返回0、或者null。
   *@param num 待转换字符
   *@return 转换后的整数
   */
  public static int getInt(String num) {
    int id = 0;
    if (num == null || num.equals("")) {
      return id;
    }
    else {
      try {
        id = Integer.parseInt(num);
        return id;
      }
      catch (Exception e) {
        //e.printStackTrace();
        return id;
      }
    }
  }

  /**
   *将Double型数字转换成整数
   */
  public static int doubleToInt(double num) {
    return (int) num;
  }

  /**
   *将Double型四舍五入，只保留小数点两位
   */
  public static double doubleToDouble(double num) {
    NumberFormat form = NumberFormat.getInstance();
    if (form instanceof DecimalFormat) {
      ( (DecimalFormat) form).applyPattern("#0.0#");
      ( (DecimalFormat) form).setMaximumFractionDigits(2);
    }
    String value = form.format(num);
    return Double.parseDouble(value);
  }

  public static String intToString(int num) {
    String result = "0";
    try {
      result = Integer.toString(num);
    }
    catch (Exception e) {

    }
    return result;
  }

  public static String floatToString(float num) {
    String result = "0";
    try {
      result = Float.toString(num);
    }
    catch (Exception e) {
    }
    return result;
  }

  public static String doubleToString(double num) {
    String result = "0";
    try {
      result = Double.toString(num);
    }
    catch (Exception e) {
    }
    return result;
  }
}

/**
 * This class is used to hold the properties of a single format specifier.
 * It is filled by the parseFormatString method and is used by several
 * other methods.
 *
 * @version     1.0, 97/12/21
 * @author      Guido Krueger
 */
class FormatParas {
  boolean specfound; //A valid format specifier was found
  int fieldlen; //Length of field (-1 if not specified)
  int decimals; //Number of decimals (-1 if not specified)
  boolean alignleft; //Left align inside output field
  boolean padzero; //Pad with leading zeros
  boolean plussign; //Output plus sign for positive numbers
  boolean aslong; //"long" modifier found
  char convchar; //Conversion character
  char basetype; //i=integer f=floatingpoint s=String c=char
  StringBuffer prefix; //Substring on the left of the specifier
  StringBuffer suffix; //Substring on the right of the specifier

  /**
   * Initializes all members to their default  values.
   */
  public FormatParas() {
    specfound = false;
    fieldlen = -1;
    decimals = -1;
    alignleft = false;
    padzero = false;
    plussign = false;
    aslong = false;
    convchar = ' ';
    prefix = new StringBuffer("");
    suffix = new StringBuffer("");
  }

  /**
   * Returns a String representation of the object's state.
   */
  public String toString() {
    String ret = "";
    ret += "specfound........: " + specfound + "\n";
    ret += "fieldlen.........: " + fieldlen + "\n";
    ret += "decimals.........: " + decimals + "\n";
    ret += "alignleft........: " + alignleft + "\n";
    ret += "padzero..........: " + padzero + "\n";
    ret += "plussign.........: " + plussign + "\n";
    ret += "aslong...........: " + aslong + "\n";
    ret += "convchar.........: " + convchar + "\n";
    ret += "prefix...........: " + prefix + "\n";
    ret += "suffix...........: " + suffix + "\n";
    return ret;
  }

  public static boolean chkSpecialCharacter(String str) {
    return false;
  }

  public static String hankaku2Zenkaku(String str) {
    return str;
  }

  public static String zenkaku2Hankaku(String str) {
    return str;
  }

  /**
   * 按照指定长度截取中英文字符串
   * @param str 需要截取的字符串
   * @param size 指定长度，为字节的长度，按照中文2个英文数字1个计算
   * @return
   */
  public static String substring(String str) {
    int size = 50;
    if (str == null || str.length() < 1 || size <= 2) {
      return str;
    }

    String returnStr = "";

    for (int i = 0, j = 0; j < size && j < str.getBytes().length; ) {
      String ch = str.charAt(i++) + "";

      if (returnStr.getBytes().length == size - 1 && ch.getBytes().length > 1) {
        returnStr = returnStr + ".";
      }
      else {
        returnStr = returnStr + ch;
      }

      j = j + ch.getBytes().length;
    }

    return returnStr;
  }

  /**
   * 按照指定长度截取中英文字符串
   * @param str 需要截取的字符串
   * @param size 指定长度，为字节的长度，按照中文2个英文数字1个计算
   * @return
   */
  public static String substring(String str, int size) {

    if (str == null || str.length() < 1 || size <= 2) {
      return str;
    }

    String returnStr = "";

    for (int i = 0, j = 0; j < size && j < str.getBytes().length; ) {
      String ch = str.charAt(i++) + "";

      if (returnStr.getBytes().length == size - 1 && ch.getBytes().length > 1) {
        returnStr = returnStr + ".";
      }
      else {
        returnStr = returnStr + ch;
      }

      j = j + ch.getBytes().length;
    }

    return returnStr;
  }

}
