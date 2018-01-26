<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="mytag"  uri="/WEB-INF/my.tld" %>
<c:set var="ctx" value="<%=request.getContextPath() %>"/>
<c:set var="ctxajax" value="<%=request.getContextPath() + \"/ajax\" %>"/>
<c:set var="ctxweb" value="<%=request.getContextPath() + \"/web\" %>"/>
<c:set var="ctxsts" value="<%=request.getContextPath() + \"/sts\" %>"/>