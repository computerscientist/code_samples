COMP590 Course Project Part 2: Client Exploits

== About ==

GeoPhoto is the latest and greatest GeoTagging App!  This is a beta
release. Current features  include reading some metadata from any given JPG
image and adding a hidden watermark to the image. Try downloading an image
from the GeoPhoto website and parsing its metadata with GeoPhoto Beta
today!!

Now includes pwn.exe! Note that pwn only works properly when launched by
a shellcode.

== Installation ==

1) Unzip (you should have already done this) :) 

2) Open a command prompt, browse to this folder, execute e.g.:

    GeoPhoto.exe photo.jpg

    or

    GeoPhoto.exe photo.jpg --watermark

  Note that watermarking will permanently modify your image.

*) If you get an error about a missing DLL, download the VC++
redistributable package
http://www.microsoft.com/en-us/download/details.aspx?id=5555

== Project Deliverables ==

To receive full project credit you must write an exploit for each mode of
operation (e.g. with and without the watermark option).

1) In the mode of operation that does not involve watermarking, you must
write an exploit that overwrites SEH and utilizes a pop/pop/ret sequence to
return to your shellcode.  This shellcode should pop up a message when
executed that lets us know your team name and favorite quote. Your
deliverable is a JPG image that exploits GeoPhoto to give us your message.

2) In the watermarking mode of operation, you should deliver a JPG image
that launches the 'pwn.exe' program included in this zip package when the
'--watermark' option is given to GeoPhoto.  This vulnerability is not as
straight-forward as the first.  Apply the skills you have learned, as just
following the Lab instructions will not be enough for this exploit.

Extra kudos if you can package both exploits into a single JPG image.

Reminder: Ensure your exploits work outside of the debugger.  We will
verify they work both in Windows XP and Windows 7 (see Rules) with the
following commands:

    GeoPhoto.exe exploit1.jpg

    GeoPhoto.exe exploit2.jpg --watermark

== Rules ==

  1) You shall not hardcode any return address, SEH function pointer, or
  ANY other memory address, UNLESS that address is located in
  GeoPhotoLib.dll, which does not utilize ASLR.

  2) You shall not hardcode any API call pointers in your shellcode. Note
  that if you are using MetaSploit this is already covered.

  3) You shall not embed shellcode with malicious logic that performs
  actions other than delivering a message box or launching our version of
  'pwn.exe'.

If you follow the rules, your exploit should be compatible across all
Windows versions XP/Vista/7.

== Submitting ==

Use the website to submit your exploits:

http://normandy-vm-04.cs.unc.edu/submit.php

If you do not have access to the submit page, but have a working
exploit, just include that in your project submission directory 
(see web FAQ).