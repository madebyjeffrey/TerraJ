<#list planets as planet>

"Planet${planet.number}" "Stargen"
{
    Texture "${planet.type.texture}"
    Radius ${planet.radius?c}
    Mass ${(planet.mass * constants.SUN_MASS_IN_EARTH_MASSES)?c}

    <#if (planet.type == "ONEFACE" || planet.type == "ROCK" || planet.type == "ASTEROIDS")>
    Color   [ 0.52 0.47 0.42 ]
    BlendTexture true
    <#elseif (planet.type == "ICE")>
    Color [ 1.0 0.9 0.75 ]
    HazeColor [ 0.2 0.5 1 ]
    HazeDensity 1

    Atmosphere 
    {
        Height 60
        Lower [ 0.8 0.4 0.1 ]
        Upper [ 0.0 0.0 0.9 ]
        Sky [ 0.8 0.4 0.1 ]
    }
    <#elseif (planet.type == "MARTIAN")>
    Color   [ 1 0.75 0.7 ]
    HazeColor [ 1 1 1 ]
    HazeDensity 0.45

    Atmosphere 
    {
        Height 30
        Lower [ 0.8 0.6 0.6 ]
        Upper [ 0.7 0.3 0.3 ]
        Sky [ 0.83 0.75 0.65 ]
    }
    <#elseif (planet.type == "TERRESTRIAL")>
    Color [ 0.85 0.85 1.0 ]
    SpecularColor [ 0.5 0.5 0.55 ]
    SpecularPower 25.0
    HazeColor [ 1 1 1 ]
    HazeDensity 0.3

    Atmosphere 
    {
        Height 60
        Lower [ 0.5 0.5 0.65 ]
        Upper [ 0.3 0.3 0.6 ]
        Sky [ 0.3 0.6 0.9 ]
        CloudHeight 7
        CloudSpeed 65
        CloudMap \"earth-clouds.png\"
    }
    <#elseif (planet.type == "WATER")>
    Color [ 0.75 0.75 1.0 ]
    SpecularColor [ 0.5 0.5 0.55 ]
    SpecularPower 25.0
    HazeColor [ 1 1 1 ]
    HazeDensity 0.3

    Atmosphere 
    {
        Height 90
        Lower [ 0.4 0.4 0.7 ]
        Upper [ 0.2 0.2 0.6 ]
        Sky [ 0.4 0.7 0.9 ]
    }
    <#elseif (planet.type == "VENUSIAN")>
    HazeColor [ 0.5 0.35 0.2 ]
    HazeDensity 0.35

    Atmosphere 
    {
        Height 60
        Lower [ 0.8 0.8 0.5 ]
        Upper [ 0.6 0.6 0.6 ]
        Sky [ 0.8 0.8 0.5 ]
    }
    <#elseif (planet.type == "SUBGASGIANT" || planet.type == "SUBSUBGASGIANT")>
    Color [ 0.75 0.85 1.0 ]
    HazeColor [ 0.5 0.8 1.0 ]
    HazeDensity 0.2
    <#elseif (planet.type == "GASGIANT")>
    HazeColor [ 0.4 0.45 0.5 ]
    HazeDensity 0.3
    </#if>

    EllipticalOrbit 
    {
        Period            ${(planet.orbitalPeriod / constants.DAYS_IN_A_YEAR)?c} # years
        SemiMajorAxis     ${planet.getA()?c} # AU
        Eccentricity      ${planet.getE()?c}
        Inclination       0.0
        AscendingNode     0
        LongOfPericenter  0
        MeanLongitude 0
    }

    RotationPeriod    ${planet.day?c}
    Obliquity         ${planet.axialTilt?c}
    Albedo            ${planet.albedo?c}
}

</#list>