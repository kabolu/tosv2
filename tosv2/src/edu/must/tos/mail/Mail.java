package edu.must.tos.mail;

import javax.servlet.ServletContext;

import org.apache.commons.mail.SimpleEmail;

import edu.must.tos.util.ToolsOfString;

public class Mail {

	private SimpleEmail smail;
    private String hostName = null;
    private Integer port = null;
    private String mailUserid = null;
    private String mailPasswd = null;

    public Mail(ServletContext servletContext) {
        this.smail = new SimpleEmail();
        this.hostName = servletContext.getInitParameter("mailHost");
        this.port = new Integer(servletContext.getInitParameter("mailPort"));
        this.mailUserid = servletContext.getInitParameter("mailUserid");
        this.mailPasswd = servletContext.getInitParameter("mailPasswd");
    }

    protected boolean send(String subject, String content, String toAddr, String toName, String sender) {
        return send(subject, content, toAddr, toName, sender, null, null);
    }

    protected boolean send(String subject, String content, String toAddr, String toName, String sender, String ccAddr, String bccAddr) {
        boolean flag = false;
        try {
            this.smail.setCharset("UTF-8");
            this.smail.setAuthentication(this.mailUserid, this.mailPasswd);
            this.smail.setHostName(this.hostName);
            this.smail.setSmtpPort(this.port);
            this.smail.setFrom(sender);
            if(ToolsOfString.isNull(toName))
                this.smail.addTo(toAddr);
            else
                this.smail.addTo(toAddr, toName);
            this.smail.setSubject(subject);
            this.smail.setContent(content, this.smail.TEXT_HTML);
            if(!ToolsOfString.isNull(ccAddr)){
                if(ccAddr.indexOf(";") > 0){
                    String[] ccAddrs = ccAddr.split(";");
                    for(int i = 0; i<ccAddrs.length; i++){
                        this.smail.addCc(ccAddrs[i]);
                    }
                } else {
                    this.smail.addCc(ccAddr);
                }
            }
            if(!ToolsOfString.isNull(bccAddr))
                this.smail.addBcc(bccAddr);

            this.smail.send();
            flag = true;
        }
        catch(Exception e) {
            System.out.print("Email expection Error:" + e.getMessage());
        }
        return flag;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setMailPasswd(String mailPasswd) {
        this.mailPasswd = mailPasswd;
    }

    public void setMailUserid(String mailUserid) {
        this.mailUserid = mailUserid;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
