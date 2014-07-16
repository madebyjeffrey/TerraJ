<#-- Template For Planet Details as HTML -->
<#-- $Id: planetdetails_html.ftl,v 1.6 2006/04/24 09:59:09 martin Exp $ -->
<table border=3 cellspacing=2 cellpadding=2 align=center bgcolor='${BGTABLE}' WIDTH='90%>'
<colgroup span=1 align=left valign=middle>
<colgroup span=2 align=left valign=middle>

<tr>
<th colspan=3 bgcolor='${BGHEADER}' align=center>
<font size='+2' color='${TXHEADER}'>
Planet ${planet.number} Statistics
</font>
</th>
</tr>

<tr>
<td>Planet Type</td>
<td colspan='2'>${planet.type} ${evaluator.planetShortDescription}</td>
</tr>

<tr>
<td>Distance From Primary</td>
<td>${(planet.getA() * constants.KM_PER_AU)?string("0.000E0")} KM</td>
<td>${planet.getA()?string("0.000")} AU</td>
</tr>

<tr>
<td>Mass</td>
<td>${(planet.mass * constants.SUN_MASS_IN_EARTH_MASSES * constants.EARTH_MASS_IN_GRAMS / 1000.0)?string("0.000E0")} KG</td> 
<td>${(planet.getMass() * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} Earth masses<br>
<#if (planet.dustMass * constants.SUN_MASS_IN_EARTH_MASSES > 0.0006) &&
    (planet.gasMass * constants.SUN_MASS_IN_EARTH_MASSES > 0.0006)>
${(planet.dustMass * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} Earth masses dust
${(planet.gasMass * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} Earth masses gas
</#if>
</td>
</tr>

<#function ctof x>
    <#return (x * 1.8) + 32.0>
</#function>
<#if !planet.type.isGasGiant()>
<#assign celsius = (planet.surfaceTemperature - constants.FREEZING_POINT_OF_WATER)>
<#assign fahrenheit = ctof(celsius)>
<#assign fahrenheit_avg = ctof(constants.EARTH_AVERAGE_CELSIUS)>

<tr>
<td>Surface Gravity</td>
<td>${planet.surfaceAcceleration?string("0.0")} cm/sec squared</td>
<td>${planet.surfaceGravity?string("0.00")} Earth G</td>
</tr> 

<tr>
<td>Surface Pressure</td>
<td>${planet.surfacePressure} millibars</td>
<td>${(planet.surfacePressure / constants.EARTH_SURF_PRES_IN_MILLIBARS)?string("0.000")} Earth atmospheres</td>
</tr>

<tr>
<td>Surface Temperature</td>
<td>${celsius} &deg; C<br>${fahrenheit} &deg; F</td>
<td rowspan='2' valign='top'>
${(celsius - constants.EARTH_AVERAGE_CELSIUS)?string("+0.0;-0.0")} &deg; C Earth temperature<br> 
${(fahrenheit - fahrenheit_avg)?string("+0.0;-0.0")} &deg; F Earth temperature 
<#if (planet.greenhouseRise > 0.1)>
<br>${planet.greenhouseRise?string("0.0")} &deg; C greenhouse effect <#if (planet.isGreenhouseEffect() && planet.surfacePressure > 0.0)> (GH)</#if>
</#if>
</td>
</tr>

<tr>
<td>Normal Temperature Range</td>
<td>
<center>
<table>
<#if (math.abs(planet.highTemperature - planet.maxTemperature) > 10) ||
        (math.abs(planet.lowTemperature - planet.minTemperature) > 10)>
<tr><th>Night</th><th></th><th>Day</th></tr>
<tr><td>${(planet.lowTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} &deg; C<br>
${(ctof(planet.lowTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} &deg; F                            
</td.
<td> - </td>
<td>${(planet.highTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} &deg; C<br>
${(ctof(planet.highTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} &deg; F                            
</td></tr>
</#if>

<tr><th>Min</th><th></th><th>Max</th></tr>
<tr>
<td>${(planet.minTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} &deg; C<br>
${(ctof(planet.minTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} &deg; F    
</td>
<td> - </td>                        
<td>${(planet.maxTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} &deg; C<br>
${(ctof(planet.maxTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} &deg; F                            
</td>
<tr>

</table>
</center>
</td>
</tr>

</#if>

<tr>
<td>Equatorial Radius:</td>
<td>${planet.radius?string("0.0")} KM</td>
<td>${(planet.radius / constants.KM_EARTH_RADIUS)?string("0.000")} Earth radii</td>
</tr>

<tr>
<td>Density</td>
<td>${planet.density?string("0.00")} grams/cc</td>
<td>${(planet.density / constants.EARTH_DENSITY)?string("0.00")} Earth densities</td>
</tr>

<tr>
<td>Orbital Eccentricity</td>
<td>${planet.getE()}</td><td></td>
</tr>

<tr>
<td>Escape Velocity</td>
<td>${(planet.escapeVelocity / constants.CM_PER_KM)?string("0.0")} KM/sec</td><td></td>
</tr>
 
<tr>
<td>Molec Weight Retained</td>
<td>${planet.molecularWeight?string("0.0")} and above</td>
<td>
<#if (evaluator.retainedElements?size > 0)>
<#list evaluator.retainedElements as x>${x.htmlSymbol}<#if x_has_next>,</#if></#list>  
</#if>
<#if (evaluator.atmosphericGases?size > 0)>
<table border='0' cellspacing='0' cellpadding='0'>
<#list evaluator.atmosphericGases as gas>
<tr>
<th align='left'>${gas.element.name}&nbsp;</th> 
<td align='right'>${(gas.surfacePressure * 100.0 / planet.surfacePressure)?string("0.0")}%&nbsp;</td>
<td align='right'>${gas.surfacePressure?string("0")}&nbsp;</td>
<td align='right'>(ipp: ${enviro.getInspiredPartialPressure(planet.surfacePressure, gas.surfacePressure)?string("0")})</td>
</tr>
</#list>
</table>
</#if>
</td>
</tr>

<tr>
<td>Axial Tilt</td>
<td>${planet.axialTilt?string("0")} &deg;</td><td></td>
</tr>

<tr>
<td>Albedo</td>
<td>${planet.albedo?string("0.00")}</td><td></td>
</tr>

<tr>
<td>Exospheric Temp</td>
<td>${planet.exosphericTemperature?string("0.00")} &deg; K</td> 
<td>${(planet.exosphericTemperature - constants.EARTH_EXOSPHERE_TEMP)?string("+0.00;-0.00")} &deg; C Earth temperature</td>
</tr>

<tr>
<td>Year Length</td>
<td>${planet.orbitalPeriod?string("0.00")} Earth Days</td>
<td>${(planet.orbitalPeriod * 24.0 / planet.day)?string("0.00")} Local Days
<#if (planet.orbitalPeriod > constants.DAYS_IN_A_YEAR)>
<br>${(planet.orbitalPeriod / constants.DAYS_IN_A_YEAR)?string("0.00")} Earth years
</#if>
</td>
</tr>

<tr>
<td>Day Length</td>
<td>${planet.day?string("0.00")} Hours</td><td></td>

<#if !planet.type.isGasGiant()>
<tr>
<td>Water Boiling Point</td>
<td>${(planet.boilingPoint - constants.FREEZING_POINT_OF_WATER)?string("0.0")} &deg; C<br>
${(ctof(planet.boilingPoint - constants.FREEZING_POINT_OF_WATER))?string("0.0")} &deg; F</td>
<td></td>
</tr>

<tr>
<td>Hydrosphere Percent</td>
<td>${planet.hydrosphere?string.percent}</td><td></td>
</tr>

<tr>
<td>Cloud Cover Percent</td>
<td>${planet.cloudCover?string.percent}</td><td></td>
</tr>

<tr>
<td>Ice Cover Percent</td>
<td>${planet.iceCover?string.percent}</td><td></td>
</tr>

</#if>
</table>
