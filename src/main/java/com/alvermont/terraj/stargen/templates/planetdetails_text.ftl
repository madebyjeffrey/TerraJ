<#-- Template For Planet Details as text -->
<#-- $Id: planetdetails_text.ftl,v 1.4 2006/04/23 09:54:30 martin Exp $ -->
Planet Type:            ${planet.type}
                        ${evaluator.planetShortDescription}
Distance From Primary:  ${(planet.getA() * constants.KM_PER_AU)?string("0.000E0")} KM ${planet.getA()?string("0.000")} AU
Mass:                   ${(planet.mass * constants.SUN_MASS_IN_EARTH_MASSES * constants.EARTH_MASS_IN_GRAMS / 1000.0)?string("0.000E0")} KG ${(planet.getMass() * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} Earth masses
<#if (planet.dustMass * constants.SUN_MASS_IN_EARTH_MASSES > 0.0006) &&
    (planet.gasMass * constants.SUN_MASS_IN_EARTH_MASSES > 0.0006)>
Dust Mass:              ${(planet.dustMass * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} Earth masses
Gas Mass:               ${(planet.gasMass * constants.SUN_MASS_IN_EARTH_MASSES)?string("0.000")} Earth masses
</#if>
<#function ctof x>
    <#return x * 1.8 + 32.0>
</#function>
<#if !planet.type.isGasGiant()>
<#assign celsius = (planet.surfaceTemperature - constants.FREEZING_POINT_OF_WATER)>
<#assign fahrenheit = ctof(celsius)>
<#assign fahrenheit_avg = ctof(constants.EARTH_AVERAGE_CELSIUS)>
Surface Gravity:        ${planet.surfaceAcceleration?string("0.0")} cm/sec squared ${planet.surfaceGravity?string("0.00")} Earth G 
Surface Pressure:       ${planet.surfacePressure} millibars ${(planet.surfacePressure / constants.EARTH_SURF_PRES_IN_MILLIBARS)?string("0.000")} Earth atmospheres
Surface Temperature:    ${celsius} degrees C ( ${(celsius - constants.EARTH_AVERAGE_CELSIUS)?string("+0.0;-0.0")} degrees C Earth temperature) 
                        ${fahrenheit} degrees F ( ${(fahrenheit - fahrenheit_avg)?string("+0.0;-0.0")} degrees F Earth temperature) 
<#if (planet.greenhouseRise > 0.1)>
                        ${planet.greenhouseRise?string("0.0")} degrees C greenhouse effect <#if (planet.isGreenhouseEffect() && planet.surfacePressure > 0.0)> (GH)</#if>
</#if>
<#if (math.abs(planet.highTemperature - planet.maxTemperature) > 10) ||
        (math.abs(planet.lowTemperature - planet.minTemperature) > 10)>
Night Temperature:      ${(planet.lowTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} deg C ${(ctof(planet.lowTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} deg F                            
Day Temperature:        ${(planet.highTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} deg C ${(ctof(planet.highTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} deg F                            
</#if>
Min Temperature:        ${(planet.minTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} deg C ${(ctof(planet.minTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} deg F                            
Max Temperature:        ${(planet.maxTemperature - constants.FREEZING_POINT_OF_WATER)?string("0.0")} deg C ${(ctof(planet.maxTemperature - constants.FREEZING_POINT_OF_WATER))?string("0.0")} deg F                            
</#if>
Equatorial Radius:      ${planet.radius?string("0.0")} KM ${(planet.radius / constants.KM_EARTH_RADIUS)?string("0.000")} Earth radii
Density:                ${planet.density?string("0.00")} grams/cc ${(planet.density / constants.EARTH_DENSITY)?string("0.00")} Earth densities
Orbital Eccentricity:   ${planet.getE()}
Escape Velocity:        ${(planet.escapeVelocity / constants.CM_PER_KM)?string("0.0")} KM/sec 
Molec Weight Retained:  ${planet.molecularWeight?string("0.0")} and above
<#if (evaluator.retainedElements?size > 0)>
                        <#list evaluator.retainedElements as x>${x.symbol}<#if x_has_next>,</#if></#list>  
</#if>
<#if (evaluator.atmosphericGases?size > 0)>
<#list evaluator.atmosphericGases as gas>
                        ${gas.element.name} ${(gas.surfacePressure * 100.0 / planet.surfacePressure)?string("0.0")}% {$gas.surfacePressure?string("0")} (ipp: ${enviro.getInspiredPartialPressure(planet.surfacePressure, gas.surfacePressure)?string("0")})
</#list>
</#if>
Axial Tilt:             ${planet.axialTilt?string("0")} degrees
Albedo:                 ${planet.albedo?string("0.00")}
Exospheric Temp:        ${planet.exosphericTemperature?string("0.00")} K ${(planet.exosphericTemperature - constants.EARTH_EXOSPHERE_TEMP)?string("+0.00;-0.00")} deg C Earth temperature
Year Length:            ${planet.orbitalPeriod?string("0.00")} Earth Days ${(planet.orbitalPeriod * 24.0 / planet.day)?string("0.00")} Local Days
<#if (planet.orbitalPeriod > constants.DAYS_IN_A_YEAR)>
                        ${(planet.orbitalPeriod / constants.DAYS_IN_A_YEAR)?string("0.00")} Earth years
</#if>
Day Length:             ${planet.day?string("0.00")} Hours
<#if !planet.type.isGasGiant()>
Water Boiling Point:    ${(planet.boilingPoint - constants.FREEZING_POINT_OF_WATER)?string("0.0")} deg C ${(ctof(planet.boilingPoint - constants.FREEZING_POINT_OF_WATER))?string("0.0")} deg F 
Hydrosphere Percent:    ${planet.hydrosphere?string.percent}
Cloud Cover Percent:    ${planet.cloudCover?string.percent}
Ice Cover Percent:      ${planet.iceCover?string.percent}
</#if>
