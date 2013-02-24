<tr>
<td colspan='2'>
<table width='100%'>

<#list planets as p>

<#assign eval = evaluators[p.number - 1]>

<#if eval.isTerrestrialPlanet()>

<tr>
<td align='right' width='5%'>
<a href='#${p.number}'><small>#${p.number}</small></a>
</td>

<td><small>${p.type.printText}: </small>${eval.planetShortDescription}
</td>
</tr>

</#if>

</#list>

</table>
</td>
</tr>