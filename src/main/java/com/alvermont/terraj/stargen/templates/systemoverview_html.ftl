<table border='3' cellspacing='2' cellpadding='2' align='center'
    bgcolor='${BGTABLE}' width='90%'>

<tr>
<th colspan='6' bgcolor='${BGHEADER}' align='center'>
<font size='+2' color='${TXHEADER}'>System Overview</font>
</th>
</tr>

<tr align='center'>
<th>#</th><th colspan='2'>Type</th><th>Dist</th><th>Mass</th><th>Radius</th>
</tr>

<#list planets as planet>

<tr align='right'>
<td><a href='#${planet.number}'>${planet.number}</a></td>
<td colspan='2'>${planet.type.printText}</td>
<td>${planet.getA()?string("0.000")} AU</td>
<td>${(planet.mass * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} EM</td>
<td>${(planet.radius / constants.KM_EARTH_RADIUS)?string("0.000")} ER</td>
</tr>

</#list>

</table>