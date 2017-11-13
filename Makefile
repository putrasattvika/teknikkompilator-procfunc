.SUFFIXES: .java .class

JFLAGS = -d .
JVM = java
JC = javac

JLEX_MAIN = JLex.Main
JAVACUP_MAIN = java_cup.Main

SCANNER_LEX = Scanner.lex
PARSER_CUP = Parser.cup

PARSER_FILES = \
	Scanner.lex.java \
	parser.java \
	sym.java

PACKAGE_NAME = TeknikKompilator


default: tekkom-gen-lang tekkom-machine

tekkom-gen-lang:
	echo '[+] Generating parser files..'
	$(JVM) $(JLEX_MAIN) $(SCANNER_LEX)
	$(JVM) $(JAVACUP_MAIN) $(PARSER_CUP)

tekkom-machine:
	echo '[+] Compiling machine..'
	$(JC) $(JFLAGS) *.java

clean:
	rm $(PARSER_FILES)
	rm -r $(PACKAGE_NAME)