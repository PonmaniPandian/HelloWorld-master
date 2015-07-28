package com.example.helloworld;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.terminal.gwt.server.ApplicationServlet;

/**
 * TestServlet allows to override default servlet processing. Note that we also 
 * have to change web.xml and specify this class as servlet-class.
 * 
 * <p>&lt;servlet-class&gt;com.example.helloworld.TestServlet&lt;/servlet-class&gt;</p>
 */
public class TestServlet extends ApplicationServlet {
  private static final long serialVersionUID = 0;
  private static long sSession = System.currentTimeMillis();

  private synchronized void setSession(StringBuffer aBuffer) {
    long id = sSession++;
    if (aBuffer.charAt(aBuffer.length()-1) != '/') {
      aBuffer.append("/");
    }
    aBuffer.append("session").append(String.valueOf(id));
  }
  
  private static String getSessionID(String aBuffer, String aPattern) {
    String sid = null;
    int idx = aBuffer.indexOf(aPattern);
    if (idx >= 0) {
      int idy = aBuffer.indexOf("/", idx+aPattern.length());
      if (idy < 0) {
        idy = aBuffer.length();
      }
      sid = aBuffer.substring(idx+8, idy);
    }
    return sid;
  }

  /**
   * Most important logic is here. This overrides Vaadin servlet's service() method.
   */
  public void service(HttpServletRequest aRequest, HttpServletResponse aResponse) throws ServletException, IOException {
    StringBuffer url = aRequest.getRequestURL();
    String pathInfo = aRequest.getPathInfo();
    boolean uidl = (pathInfo != null) && pathInfo.startsWith("/UIDL/");
    boolean vaadin = (pathInfo != null) && pathInfo.startsWith("/VAADIN/");
    String sid = getSessionID(url.toString(), "/session");
    // Redirect other than Ajax UIDL requests if URL does not contains "/sessionNNNNN" part
    if (!uidl && !vaadin && (sid == null)) {
      setSession(url);
      String qstring = aRequest.getQueryString();
      if ((qstring != null) && (qstring.length() > 0)) {
        url.append("?").append(qstring);
      }
      aResponse.sendRedirect(url.toString());
    } else {
      // Otherwise wrap the request, because we have to change getPathInfo() and getServletPath()
      String sessionPath = (sid == null) ? null : "/session"+sid;
      RequestWrapper wrapper = new RequestWrapper(aRequest, sessionPath);
      // Now pass request to Vaadin
      super.service(wrapper, aResponse);
    }
  }
}
