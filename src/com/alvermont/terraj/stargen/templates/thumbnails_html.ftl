<tr bgcolor='${BGTABLE}'>
<td colspan='2'>
<table border='0' cellspacing='0' cellpadding='3' bgcolor='${BGSPACE}' width='100%'>
<tr>
<td colspan='${(planets?size +2)}' bgcolor='${BGHEADER}' align='center'>
<font size='+1' color='${TXHEADER}'><b>${planets?size} Planets</b></font>
<font size='-1' color='${TXHEADER}'>size proportional to Sqrt(Radius)</font>
</td>
</tr>

<tr valign='middle' bgcolor='${BGSPACE}'>
<td bgcolor='${BGSPACE}'><img alt='Sun' src='${imageroot}Sun.png' width='15' height='63' border='0'></td>

<#list planets as p>

<#assign eval = evaluators[p.number - 1]>
<#assign pixels = uiutils.getSizeInPixels(p)>

<td bgcolor='${BGSPACE}'>
<a href='#${p.number}' title='#${p.number} - ${uiutils.getInfo(p)}'>
<img alt='${p.type.printText}' src='${imageroot}${uiutils.getHtmlImageName(p)}' width='${pixels}'
    height='${pixels}' border='0'></a>
</td>

</#list>

<td bgcolor='${BGSPACE}' align='right' valign='bottom'>
<a href='key.html'><font size='-3' color='${TXSPACE}'>See<br>Key</font></a>
</td>
</tr>

</table>
</td>
</tr>

