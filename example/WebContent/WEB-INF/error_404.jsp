<%@ page isErrorPage="true" import="java.io.PrintWriter" contentType="text/plain"%><%@ page import="java.io.StringWriter"%><%@ page import="java.util.Enumeration"%>


<%
  StringWriter stringWriter = new StringWriter();
  PrintWriter printWriter = new PrintWriter(stringWriter);
  if(exception != null){
    printWriter.write("Message:<br/>");
    printWriter.write(exception.getMessage()+"<br/>");
    printWriter.write("StackTrace:<br/>");
    exception.printStackTrace(printWriter);
    System.out.println(stringWriter);
    printWriter.close();
    stringWriter.close();
  }
%>
页面不存在
