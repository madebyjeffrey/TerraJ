// Example script changes the colour of all the vertices to random values
//
// $Id: colours.js,v 1.1 2006/02/01 11:12:01 martin Exp $s

var vertices = mesh.getVertexCount();

for (var v = 0; v < vertices; ++v)
{
	mesh.getVertices().get(v).
		setColour(0, Math.random(), Math.random(), Math.random());
}

// now we need to make sure the display is updated and using correct colours
// for the new altitudes

control.recolour();
control.forceRedraw();
