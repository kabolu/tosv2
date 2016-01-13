package edu.must.tos.mail;

import java.sql.Connection;

import javax.servlet.ServletContext;

import edu.must.tos.bean.Email;
import edu.must.tos.impl.EmailDAOImpl;
import edu.must.tos.util.ToolsOfString;

public class SendEmail extends Mail {
    private Connection conn;
    private String content, sender, subject, toAddr, toName, cc, bcc;

    public SendEmail(ServletContext servletContext, Connection conn) {
        super(servletContext);
        this.conn = conn;
    }

    public boolean sendTestEmailInfo(String emailType, String name, String bookTable, String toEmail) throws Exception {
        boolean flag = false;
        if(ToolsOfString.isNull(toEmail))
            return flag;
        toAddr = toEmail;
        if(getMailTemplate(emailType)) {
            String[] replace = {"studName", name,
            		            "bookList", bookTable
            		           };
            flag = replaceAndSend(replace);
        }
        return flag;
    }

    //========================= private function start here =====================
    private boolean getMailTemplate(String mailType) throws Exception {
        EmailDAOImpl eMailDao = new EmailDAOImpl();
        Email eMailBean = eMailDao.getEmail(conn, mailType);
        if(eMailBean != null) {
            content = ToolsOfString.chkNullString(eMailBean.getEmContent());
            sender = ToolsOfString.chkNullString(eMailBean.getEmSender());
            subject = ToolsOfString.chkNullString(eMailBean.getEmSubject());
            if(ToolsOfString.isNull(cc))
                cc = ToolsOfString.chkNullString(eMailBean.getEmCc());
            bcc = ToolsOfString.chkNullString(eMailBean.getEmBcc());
            return true;
        }
        return false;
    }

    private boolean replaceAndSend(String[] replace) {
        for(int i = 0; i < replace.length - 1; i += 2) {
            content = content.replaceAll("\\$\\{" + replace[i] + "}", replace[i + 1]);
        }
        return sendMail();
    }

    private boolean sendMail() {
        if(ToolsOfString.isNull(toAddr))
            return false;
        return super.send(subject, content, toAddr, toName, sender, cc, bcc);
    }

}
