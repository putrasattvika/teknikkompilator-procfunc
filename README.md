Teknik Kompilator
=================


## How to's
- Generate parser files and compile the machine
	```
    $ make
	```

- Clean workspace (delete generated java files and compiled classes)
	```
    $ make clean
    ```

- Compile tekkom's source code (e.g., tests/proc1.tk)
	```
    $ ./compile tests/proc1.tk > tests/proc1.asm
	```
    
- Execute tekkom's compiled source code (e.g., tests/proc1.asm)
	```
    $ ./run tests/proc1.asm
	```
