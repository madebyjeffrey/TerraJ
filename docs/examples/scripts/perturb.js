// Example script to perturb all vertices by a random amount
//
// $Id: perturb.js,v 1.1 2006/02/01 11:12:01 martin Exp $

var vertices = mesh.getVertexCount();

// loop over the current vertices

for (var v = 0; v < vertices; ++v)
{
	// get and update the height

	var h = mesh.getVertexHeight(v);

	var p = Math.random() - 0.5;

	mesh.setVertexHeight(v, h + (p / 50));
}

// now we need to make sure the display is updated and using correct colours
// for the new altitudes

control.recolour();
control.forceRedraw();
