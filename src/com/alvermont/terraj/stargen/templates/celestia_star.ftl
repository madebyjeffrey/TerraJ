###################
# $Id: celestia_star.ftl,v 1.3 2006/04/23 14:22:45 martin Exp $
###################

500000 "Stargen"
{
    RA ${star.rightAscension?c}
    Dec ${star.declination?c}
    Distance ${star.distance?c}
    SpectralType "${star.getSpectralClass()}${star.getSpectralSubclass()}${star.getLuminosityClass()}"
    AbsMag ${star.absoluteMagnitude?c}
}
