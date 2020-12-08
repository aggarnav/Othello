=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: arnavagg
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Array: A 2D array was used to store the state of the game board. The use of a 2D array was
  appropriate since it resembled the shape of the 8x8 board in Othello. However, instead of using
  Booleans to store data in the array, I used integers where 0 represented a blank square, 1 a 
  black piece and 2 a white square. The 2D array helped me iterate over all pieces which was useful
  when implementing the game's logic

  2. Collections: A Deque of a list of array of integers was used to store the history of the game.
  The deque allowed me to implement LIFO which was useful for the undo functionality. I used a 
  linkedlist for the two lists. The inner list was first populated by the piece to be placed and
  then the set of pieces that were flipped because of it (I used a list since it was important
  that the piece that was placed would be first in the list since when performing undo that piece
  would be replaced by 0 but the other pieces in the list would be flipped; I didn't use an 
  Enrty/Map since they required a comparable input and integer arrays don't implement comparable).
  I implemented a collection of integer arrays through a HashMap for the set of pieces that were to
  be flipped. I didn't use a TreeSet since the TreeSet also required comparable input

  3. File I/O: Optionality to load and save the game state is provided where the user chooses where
  to save data and where to read data from. Upon loading the game from a saved state, there is no 
  difference and everything including the queue for undo is returned to the state it was when we
  stored the data

  4. AI:I implemented a simple AI algorithm where if the user clicks on hint then the computer
  calculates the move for which the net pieces flipped in favor of the computer over the next two
  turns (first the computer then the user then the computer) is maximized. This move is then
  automatically played. 

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  Game is the top level frame and widget for the GUI
  
  GameBoard serves as the controller and the view. It draws the board, implements a mouseclick
  listener and, performs load/save operations that are then passed on to Othello
  
  Othello is the server class and is completely independent of Game and GameBoard. Othello maintains
  the server's variables and interacts with the other classes through constructors and getters.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  It was difficult to implement an appropriate set of collections simply due to the constraints
  and range of functionality required. Additionally, given that Othello allows users to skip turns
  only when there are no available options and that one can't otherwise play a piece where no piece
  is flipped, it was difficult to implement AI since there were multiple cases that had to be 
  indididually implemented. 


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I would say that there is a good separation of functionality. However, given the chance, I would
  refactor my code to separate the view and controller aspect of GameBoard. I would also create a
  shape and use those a piece instead of consecutively drawing an oval. 
  
  While the private state is well encapsulated, I would split Othello(model) into multiple classes
  to distribute functionality and make the code easier to read and understand.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  None Used
