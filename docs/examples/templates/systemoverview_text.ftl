System Overview

#   Type            Dist            Mass            Radius

<#list planets as planet>
${planet.number}    ${planet.type.printText}            ${planet.getA()?string("0.000")} AU         ${(planet.mass * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} EM            ${(planet.radius / constants.KM_EARTH_RADIUS)?string("0.000")} ER
</#list>
