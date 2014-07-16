<#list planets as p>
<#assign eval = evaluators[p.number - 1]>
<#if eval.isTerrestrialPlanet()>
#${p.number} ${p.type.printText}: ${eval.planetShortDescription}
</#if>
</#list>
