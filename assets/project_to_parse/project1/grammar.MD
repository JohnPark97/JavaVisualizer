CPSC410 - DSL Grammar

## Grammar  
**PROGRAM ::=** **VAR*** , [**PLAYER**] , [**BLOCKADE**] , **WAVE***  
**VARS ::=** "VARIABLES:" , {**VAR_NAME** , "=" , **VAR_VALUE**}  
**VAR_NAME ::==** *STRING*  
**VAR_VALUE ::==** *BLOCKADE* | *MOVEMENT*  | *SPEED*  
**PLAYER ::=**  "PLAYER:" ,"[", **HP** ,"|", (**SPEED** | **VAR_NAME**) "] "   
**BLOCKADE ::=**  ("BLOCKADE:" , **HP** , **SPAWN** ) | **VAR_VALUE**     
**WAVE ::=** "WAVE:" , **ENEMY**+,[**MOVEMENTS**]      
**ENEMY ::=**   **ENEMYVALUE** , **SPAWN** , **ROWS**   
**ENEMYVALUE ::=** "[" , **E_SPRITE** , **HP** ,"|", (**SPEED** | **VAR_NAME**) , **POINTS** , "] "  
**E_SPRITE ::=** "ALIEN"    
**HP ::=**	"HP" , "=" , *NUMBER*  
**SPEED ::=** "SPEED" , "=" , *NUMBER* | **F(t)**	  
**SPAWN ::=** "SPAWN" , "=" , *NUMBER*  
**ROWS ::=** "ROWS" , "=" , *NUMBER*  
**POINTS ::=** "POINTS" , "=" , *NUMBER*  
**F(t) ::=** *STRING* (* Function of time *)  
**MOVEMENTS ::=** "MOVEMENT:" , (**MOVEMENT** | **VAR_NAME**)+         
**MOVEMENT ::=** **MOVEMENTVALUE**+, *[LOOP]*   
**MOVEMENTVALUE ::=** **DIRECTION** , "=" , *NUMBER*   
**DIRECTION ::=** "LEFT" | "RIGHT" | "UP" | "DOWN"  
**LOOP ::=** "LOOP" , "=" , *NUMBER*

## Table of Symbols   
definition =  
concatentation ,  
termination ;  
alternation |
optional [ ... ]   
terminal string " ... "  
comment (* ... *)        
exception -  
one or more +          
zero or more *   
                                         
**Note:** For VAR_NAME, avoid using any keywords ex. WAVE ,HP or spaces           
## Example input:  
VARIABLE: MOVE1=LEFT=3000 RIGHT=6000 UP=2000 LOOP=0      
VARIABLE: BLOCK1=BLOCKADE: HP=5 SPAWN=4        
PLAYER: [HP=3 SPEED=700] BLOCK1           
WAVE: [ALIEN: HP=1 SPEED=500 POINTS=20] SPAWN=13 ROWS=4          
MOVEMENT: MOVE1            
WAVE: [ALIEN: HP=15 SPEED=400 POINTS=500] SPAWN=1 ROWS=1            
[ALIEN: HP=1 SPEED=300(sin(t) + 2) POINTS=20] SPAWN=13 ROWS=3      
MOVEMENT: LEFT=3000 LOOP=0      
