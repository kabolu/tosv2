package edu.must.tos.util;

import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContext;

import edu.must.tos.util.ToolsOfString;

import org.apache.log4j.Logger;

public class Authenticate {
	Hashtable env = null;
    String ID = null;
    String usergroups = null;
    String userName = null;
    String type = null;
    private Logger logger = Logger.getLogger(this.getClass());
    public Authenticate() {
        env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
    }

    public boolean validate(String user, String pass, ServletContext context) {
        boolean flag = true;

        if(ToolsOfString.isNull(user) ||
            ToolsOfString.isNull(pass) ||
            context == null)
            flag = false;

        String userid = user.trim();
        String authType = context.getInitParameter("authtype");

        flag = this.validate_staff(userid, pass, context);

        return flag;
    }

    private boolean validate_staff(String user, String pass, ServletContext context) {
        ID = user;
        type = "STAFF";
        return validate_by_ad(user, pass, context);
    }

    private boolean validate_by_ad(String user, String pass, ServletContext context) {
        boolean isValid = false;
        DirContext ctx = null;

        String authSvr = context.getInitParameter("authsvr");
        String authDomain = context.getInitParameter("authdomain");
        authDomain = authDomain.replaceAll("@", "");
        String[] dc = authDomain.split("\\.");
        String searchBase = "";
        for(int i = 0; i < dc.length; i++)
            searchBase = ToolsOfString.join(searchBase, "dc=" + dc[i], ",");

        try {
            env.put(Context.SECURITY_PRINCIPAL, user + "@" + authDomain);
            env.put(Context.SECURITY_CREDENTIALS, pass);
            env.put(Context.PROVIDER_URL, "ldap://" + authSvr + ":389");

            ctx = new InitialDirContext(env);

            SearchControls sCtrls = new SearchControls();
            sCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String[] strAttributes = {"displayName", "memberOf"};
            String FILTER = "(&(objectClass=user)(sAMAccountName=" + user + "))";

            sCtrls.setReturningAttributes(strAttributes);
            NamingEnumeration results = ctx.search(searchBase, FILTER, sCtrls);

            if(results != null && results.hasMoreElements()) {
                SearchResult srchResult = (SearchResult)results.next();

                Attribute attr = srchResult.getAttributes().get("memberOf");
                if(attr != null && attr.size() > 0) {
                    for(int i = 0; i < attr.size(); i++) {
                        String att = (String)attr.get(i);
                        usergroups = ToolsOfString.join(usergroups, att.substring(3, att.indexOf(',')), ",");
                    }
                }

                attr = srchResult.getAttributes().get("displayName");
                if(attr != null && attr.size() > 0) {
                    userName = (String)attr.get(0);
                }
                Date date = new Date();
                System.out.println("user : " + userName + " login on " + date);
                logger.info("user : " + userName + " login on " + date);

                isValid = true;
            }
        }
        catch(NamingException e) {
            System.err.println("Naming Exception:" + e);
        }
        catch(Exception e1) {
            System.err.println("Exception:" + e1);
        }
        finally {
            try {
                if(ctx != null)
                    ctx.close();
            }
            catch(NamingException e) {
                System.err.println("Naming Exception:" + e);
            }
        }

        return isValid;
    }

    public String getID() {
        return ID;
    }

    public String getUserGroups() {
        return usergroups;
    }

    public String getUserName() {
        return userName;
    }

    public String getType() {
        return type;
    }
}
