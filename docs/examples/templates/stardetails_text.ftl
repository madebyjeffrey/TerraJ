Stellar Characteristics

Stellar Mass:       ${star.mass?string("0.00")} solar masses
Stellar Luminosity: ${star.luminosity?string("0.00")} (Sol = 1.00)
Age:                ${(star.age / 1000000000)?string("0.000")} billion years
                    (${((star.life - star.age) / 1000000000)?string("0.000")} billion left on main sequence
Habitable Radius:   ${star.REcosphere?string("0.000")} AU
