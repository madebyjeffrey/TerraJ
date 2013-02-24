${star.name}

<#include "stardetails_text.ftl">
<#include "terrestrials_text.ftl">

<#include "systemoverview_text.ftl">

<#list planets as p>
<#global planet = p>
<#global evaluator = evaluators[planet.number - 1]>
<#include "planetdetails_text.ftl">

</#list>
