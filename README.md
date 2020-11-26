# MicroPost

Welcome to MicroPost!


MicroPost is a simple internet messaging program written in Java. There are two parts to this program: MPServer, which allows users to create messaging servers, and MPClient, which allows users to connect to servers and chat with one another. In its current form, it is sort of like an IRC messenger, though additional features are being planned to enhance the user experience. It has been tested on Linux (Ubuntu) and Windows for portability. This repository contains the executable .jar files and source .java files in the "jar" and "src" folders respectively. Here is a brief overview of these folders' contents:

"jar" folder - Contains two folders for each executable, MPClient and MPServer. MPClient opens a GUI, and can be run like any normal .jar executable. MPServer is strictly a command line program, so you must either run one of the included scripts to set up a command line interface (run_xterm.sh on Linux, run_cmd.bat on Windows), or open a command line interface and run the program manually.

"src" folder - Contains all of the source files in one folder. MPClient and MPClientGUI are used only in the MPClient program, MPServer is used only in the MPServer program, and Message and User are used by both programs. Java should compile all related files for MPClient and MPServer into their .class files automatically, but be sure to keep these .class files together if you need to reorganize anything.


Here is a list of potential new features:

User Search - search by user to return a list of this user's messages

Voting - voting system to automatically determine if messages are liked or disliked by users of a server, voting results are hidden from users

Autoban - automatically remove very unpopular messages based on votes

Autopin - automatically pin very popular messages to the server banner based on votes, identity of poster is hidden


Thanks for reading, and let me know if I should change or fix anything!
