// Example script shows moving the camera and then putting it back
// after a delay
//
// $Id: camera.js,v 1.1 2006/02/01 11:12:01 martin Exp $

camera.moveBackward(-0.5);
java.lang.Thread.sleep(2000);
camera.moveBackward(0.5);
