package com.example.helloworld;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * RequestWrapper allows us to change what some request methods do or what they return.
 */
public class RequestWrapper extends HttpServletRequestWrapper {
  private String iBlock;
  private int iBLockLength;
  private String iBlockUIDL;
  private int iBLockLengthUIDL;
  
  public RequestWrapper(HttpServletRequest aHttpServletRequest, String aBlock) {
    super(aHttpServletRequest);
    iBlock = aBlock;
    if (aBlock == null) {
      iBlockUIDL = null;
      iBLockLength = -1;
      iBLockLengthUIDL = -1;
    } else {
      iBlockUIDL = "/UIDL"+aBlock;
      iBLockLength = iBlock.length();
      iBLockLengthUIDL = iBlockUIDL.length();
    }
  }

  // Remove session part from path info
  public String getPathInfo() {
    String result = super.getPathInfo();
    if ((result != null) && (iBlock != null)) {
      int length = result.length();
      if (result.startsWith(iBlock)) {
        result = (length == iBLockLength) ? null : result.substring(iBLockLength);
      } else {
        if (result.startsWith(iBlockUIDL)) {
          result = (length == iBLockLengthUIDL) ? "/UIDL/" : "/UIDL"+result.substring(iBLockLengthUIDL);
        }
      }
    }
    return result;
  }

  // Add session part to servlet path
  public String getServletPath() {
    String result = super.getServletPath();
    if (iBlock != null) {
      if (result.length() == 0) {
        result = iBlock;
      } else {
        result += iBlock;
      }
    }
    return result;
  }
}
