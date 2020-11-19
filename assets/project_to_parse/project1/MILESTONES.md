**Milestone 1 contents**
- Brief description of your planned DSL idea
- Notes of any important changes/feedback from TA discussion
- Any planned follow-up tasks or features still to design


Our planned DSL idea is to create a space invaders game generator, where the user will be able to define user-player settings and customize the spawning of enemies to generate a playable level.  Afterwards, the user will be able to play-test the game, with mechanics similar to the original space invaders game.

Our TA (Michelle) commented that the current DSL specifications might be too simple and suggested we add more complexity in the form adding macros of some sort.  In response to this, we will be adding the ability for the user to define enemy alien movement, and use said movement as function calls to apply to other enemies. 

**Milestone 2 contents** 
- Planned division of main responsibilities between team members
- Roadmap for what should be done when
- Summary of progress so far

Roadmap:
| Due Date   | Tasks                                                        | Responsible  |
| -----------|-------------                                                 | -----|
| Sept 28    | define DSL EBNF                                              | James |
| Oct 2      | base game mechanics done                                     | Andy, Juan, James |
| Oct 2      | User Study #1                                                | Mandy, John  |
| Oct 2      | Tokenization/Parsing                                         | Mandy, John |
| Oct 9      | add custom constructors for player, <br> alien, blockades    | Andy, Juan, James |
| Oct 9      | Validate/Evaluate (produce Settings object)                  | Mandy, John |
| Oct 14     | User Study #2                                                | James, Juan |
| Oct 14     | add custom constructors for game waves                       | Andy, Juan, James |  
| Oct 16     | Integration Stage; run game with Settings object             | Mandy, Andy, Juan, James |
| Oct 19     | Edit video                                                   | John |

Progress so far: established project specifications and workflow.  Project language chosen to be Java.  Devised roadmap and assigned responsibilities.  Basic implementation of base game and its mechanics started

**Milestone 3 contents** 
- Mockup of concrete language design (as used for your first user study)
- Notes about first user study 
- Any changes to original language design

See grammar.MD file for concrete language design / grammar  
See user_studies.MD for first user study notes  

Progress so far: on track with roadmap (finished implementation of base game, conducted first user study and finished tokenization / parsing steps)

**Milestone 4 contents**
- Status of implementation
- Plans for final user study 
- Planned timeline for the remaining days

The current status of the implementation is the base space-invaders game and it's mechanics and done.  Customizable 
alien, player and blockades are complete.  Evaluation and validation is complete as well.  Validation returns a Settings
object that will be used to extract constructor parameters of various objects.

The final user study is pushed back to allow users to get visual feedback, as they will be able to test launching the
game with different input settings.  This will be conducted when custom constructors for game waves is finished.

Please consult updated roadmap for timeline of remaining days
