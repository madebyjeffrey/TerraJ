<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/1999/REC-html401-19991224/loose.dtd">
<html>
<head>
    <title>${star.name}</title>
</head>
<body>
<#include "colours.ftl">
<table border='3' cellspacing='2' cellpadding='2' align='center'
    bgcolor='${BGTABLE}' width='90%'>
<tr>
<th colspan='2' bgcolor='${BGTABLE}' align='center'>
<font size='+2' color='${TXTABLE}'>${star.name}</font>
</th>
</tr>
<#include "stardetails_html.ftl">
</table>
</body>
</html>