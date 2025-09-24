Matthew Vanhoomissen
Student ID: 20789223

I am attempting an A

You run the program by putting the following into the terminal: 
javac --module-path "C:\Users\mike_\Desktop\javafx-sdk-21.0.8\lib" --add-modules javafx.controls,javafx.media,javafx.swing *.java

then: javac --module-path "C:\Users\mike_\Desktop\javafx-sdk-21.0.8\lib" --add-modules javafx.controls,javafx.media,javafx.swing Main

This will start the GUI program. Most actions are clear however to add 
a new song or search for a song, you must cleat the Text boxes
And for the top left, if you want to switch playlist, make sure the menu
selection is on menu selection

The biggest hurdle was 1. finding out how to implement all of the JComponents. There were
a bunch of them and this took a decent amount of time.
Specifically the hardest part was making the Transferable Handler to move the list items around. I had to research online on places like stack overflow for example code of how you actually make the class

Checklist:
A: 
- required fields
- SongNode constructor
- Getter and setter methods
- add first and last
- remove first and last
- get(index)
- display forwards and backward
- next and previous song
- has next/prev
- position tracker
- jump to song
- 800 x 400 window
- Now playing and buttons
- add or remove buttons
- mouse clicking activation
- Highlight playing song
- enable/disable buttons
B: 
- insertBefore and after
- swapNodes
- can reverse using the prev
- file and playlist menu
- volume slider
- progress bar
- search box and view menu
- queue and playlist history
A: 
- Drag and drop
- smart shuffle
- switch between playlist
- cross playlist add or copying
- time display
- shuffle/repeat
- speed control
- sortedInsert
- Duplicate removing button
- 15 test methods
Extra credit:
- user can loop one song
- Can loop back to start once playlist is done