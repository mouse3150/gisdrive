package cn.com.esrichina.gcloud.commons.license;


import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;


public class NumberdPropertyKey {

        public String key = null;

        public int keyNo = -1;

        public NumberdPropertyKey(String k, int n)
        {
                key = k;
                keyNo = n;
        }


        public static NumberdPropertyKey[] getNumberdPropertyKeys(Properties myprops, String prefix,String postfix)
        {
                Vector routeRuleKeys = new Vector();
                Vector routeRuleNos = new Vector();
                if(prefix == null)
                        prefix = "";
                if(myprops != null)
                {
                        Enumeration allkeys = myprops.propertyNames();
                        while(allkeys.hasMoreElements())
                        {
                                String pkey = (String)allkeys.nextElement();
                                int postfixIndex = pkey.length();
                                if(postfix != null)
                                  postfixIndex = pkey.lastIndexOf(postfix);

                                if(pkey.startsWith(prefix))
                                {
                                        try
                                        {
                                                String rulenokey = pkey.substring((prefix).length(),postfixIndex);

                                                int ruleno = Integer.parseInt(rulenokey);
                                                int i;
                                                for(i=routeRuleNos.size()-1;i>=0;i--)
                                                {
                                                        int pruleno = ((Integer)routeRuleNos.get(i)).intValue();
                                                        if(ruleno > pruleno)
                                                                break;
                                                }
                                                routeRuleKeys.add(i+1,pkey);
                                                routeRuleNos.add(i+1,new Integer(ruleno));
                                        }
                                        catch(IndexOutOfBoundsException e)
                                        {
                                                continue;
                                        }
                                        catch(NumberFormatException e)
                                        {
                                                continue;
                                        }
                                }
                        }
                }

                NumberdPropertyKey[] keys = new NumberdPropertyKey[routeRuleKeys.size()];
                for(int i=0;i<routeRuleKeys.size();i++)
                {
                        keys[i] = new NumberdPropertyKey(
                                                                (String)(routeRuleKeys.get(i)),
                                                                ((Integer)(routeRuleNos.get(i))).intValue());
                }

                return keys;
        }


        public static NumberdPropertyKey[] getNumberdPropertyKeys(Properties myprops, String prefix)
        {
          return getNumberdPropertyKeys(myprops,prefix,null);
        }

}
