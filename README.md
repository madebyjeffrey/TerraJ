Addendum by Jeffrey Drake
-------------------------

24 Feb 2013

The code as is will not compile and run. It is being updated to do so, but
on the Mac it does not run with OpenGL as of yet. 

The JOGL libraries were updated to the latest of the 1.1 series to enable the 
program to run, and compile. It is not compatible with JDK 7 on the Mac.

Next step is to convert to JOGL 2. Probably won't work any better afterwards.

25 Feb 2013

Uses JOGL, issues with creation of GL profile. Expecting some weird interaction.



TerraJ Readme
-------------

Martin Smith
martin at spamcop dot net

http://terraj.sourceforge.net

This file is an extract from the full documentation. Please refer to the main
document for full details.

TerraJ is a set of fractal terrain and solar system generation programs that 
have been ported from C/C++ to Java.

The port has included extensive refactoring, the creation of a documented API 
to enable reuse of parts of them in other programs and extensions to the 
originals such as new Swing user interfaces. Full javadoc for the classes in 
the project is available.

What Might it be Used For?

I hope that this software will be useful both for learning purposes and as part 
of other programs such as games and simulations. Go on, surprise me!

What is Included?

The TerraJ suite currently contains the following software:

Fracplanet – Generates fractal planets and terrains with a wide range of user 
controlled parameters. Has an OpenGL rendering preview using JOGL and can 
export to POVRay format for rendering. 

Stargen – Creates realistic planetary systems and stars using an accretion 
simulation. The resulting text and HTML output is template based and 
configurable.

Planetary Terrain Generator – A second fractal terrain generation program. 
This one does not do 3D rendering but can output images using many different 
map projections.

Dole Accretion – An older and slightly less complex star system generator 
included for reference and completeness.

What is Required to Run These Programs?

Most of the programs will run on any Java J2SE 1.5 runtime environment. 
Additionally to run Fracplanet you must have a platform that is supported by 
the JOGL OpenGL bindings library. This is currently Windows, Linux and OS X.
Some Linux systems will already have Java or you may have installed it to run 
other programs3. To download a Java runtime for Windows and Linux go to: 
http://java.com/en/download/index.jsp. To obtain Java 1.5 for OS X you will 
need to go to the Apple downloads site and search for the latest version.

To build and extend these programs you will need a suitable JDK. These can 
normally be downloaded from the same place as the JRE. 

Note to run Fracplanet you will need to make sure the JOGL native libraries 
are available on your library path. The details of how to do this vary by 
platform. If you get an error that mentions UnsatisfiedLinkException then this 
is probably what is wrong.

The program now attempts to extract the JOGL libraries from the jar file to 
the users home directory and load them from there. The files are then removed 
when the program exits. In most cases this should remove the need to specify 
absolute paths to native libraries, one of the biggest current annoyances 
in Java.

However, if these problems persist then the library directory property can be 
set explicitly from the command line. On Windows this might look something 
like:

java -jar stargen.jar -Djava.library.path=c:/java/projects/stargen/lib/natives-win32

On Linux it would probably be something like:

java -jar stargen -Djava.library.path=/opt/stargen/lib/natives-linux

Obviously the paths will depend on where the program has been installed.

To generate large terrains you may need to increase the amount of memory 
available to the Java runtime system. To do this include -Xmx=size in the
options passed to java. For example -Xmx=512m allocates up to 512 Mb. If you 
do not increase the memory size appropriately then you may get Out Of Memory 
errors with large terrains. This is not a bug in the program, honest!

What License is TerraJ Released Under?

TerraJ is released under the GNU General Public License. This is one of the 
most widely used free software licenses. Everyone is welcome to modify and 
extend the software as it stands and I hope some people will find uses for it 
in their own applications. Disclaimer: There is no warranty with this software 
and you use it at your own risk. For full details consult the license text at 
the end of this document.

Note: The original code was issued under a variety of licenses and some of it 
predates the existence of standard software licenses. If you wish to use the 
original C code you should refer to the corresponding original source files 
and documentation to ensure compliance.

Note: An exception to the license has been granted to allow use of this 
software with the necessary libraries. Refer to any of the Java source files
for details of this exception.

What about Contributions?

This is an open source project and contributions are welcome. Some of the 
things that would be good to have are:

o New features or enhancements to existing ones. Rings rendering needs to be
finished as a priority.
o Better integration of the programs
o Details of software that should be added to this collection, whether or not a 
  Java version already exists.
o More Junit test cases. There are some but it was just too much work to write 
  them for all the code. Help would be appreciated here.
o Fixes for the actual issues reported by Checkstyle, as opposed to the noise 
  it generates.
o Advice on fixing the Jalopy formatting. It stopped working for some reason.
o Offers of collaboration from the authors of related software

I Found a Bug ...

I'm sure there are quite a few. Its pretty unlikely to release 60,000 lines of 
code and for there not to be any problems with it. Testing this sort of 
application is quite hard both because of the complexity and because most of 
the output is based on things that are generated randomly. 

If you do find problems with it then let me know, particularly differences 
from the ranges of values produced by the C code for programs like Stargen.

Credits and Original Sources
These programs would not have been possible without the work of those who 
produced the original versions. I would like to thank them for making the 
source code of their work available and the inspiration it gave me to 
complete this project.

Stargen

Stargen was originally written by Jim Burrows. The main page for information 
and downloads of the C version is:

http://home.comcast.net/~brons/NerdCorner/StarGen/StarGen.html

Planetary Terrain Generator

The original version was written by Torben Ægidius Mogensen with contributions 
from Jim Burrows. Their pages are:

http://www.diku.dk/users/torbenm/
http://home.comcast.net/~brons/NerdCorner/Planet.html

Fracplanet

The C version was originally written by Tim Day. The home page including links 
to the source code is at:

http://www.bottlenose.demon.co.uk/share/fracplanet/index.htm

Other

Thanks to Stephen Manley for the original C++ version of the OpenGL Camera 
class that has been ported to form part of this application.

Thanks to Mike M for making changes to Rings to improve the support for 
rendering large terrains. I'm sorry I haven't managed to get a properly 
working version of Rings support in this release and I hope to do so soon.

Thanks to the people behind FreeMarker for coming up with a template library 
that did exactly what I needed. When I started I'd assumed I'd have to write 
a lot of tedious code for formatting but it's all in the template, including 
the maths for converting units.

Thanks to the other library authors who provided useful functionality and made 
this easier to write. 
