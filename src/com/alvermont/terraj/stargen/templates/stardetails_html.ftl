<tr>
<td colspan='2' bgcolor='${BGHEADER}' align='center'>
<font size='+2' color='${TXHEADER}'><B>Stellar Characteristics</B></font>
</td>
</tr>

<tr>
<td>Stellar Mass</td>
<td>${star.mass?string("0.00")} solar masses</td>
</tr>

<tr>
<td>Stellar Luminosity</td>
<td>${star.luminosity?string("0.00")} (Sol = 1.00)</td>
</tr>

<tr>
<td>Age</td>
<td>${(star.age / 1000000000)?string("0.000")} billion years<br>
(${((star.life - star.age) / 1000000000)?string("0.000")} billion left on main sequence</td>
</tr>

<tr>
<td>Habitable Ecosphere Radius</td>
<td>${star.REcosphere?string("0.000")} AU</td>
</tr>
