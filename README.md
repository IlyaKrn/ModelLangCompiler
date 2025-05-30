# Compiler of Pascal-like programming language
### Description
This repository contains my сoursework on the subject "Theoty of compilation". This program written on Java and use theory of formal languages, grammars and machines to translate Pascal-like programm to POLIZ and execute it with error handle.
### Program structure
Colpiler contains 3 main parts:

**Lexical analyzer (Lexer)** - it part perform analyze program text as regular grammar and create tables of lexemes

**Parser** - it part perform analyze tables of lexemes as context-free grammar (Syntax analyze), context-sensitive grammar (Semantic analyze) and generate POLIZ of program

**Interpreter** - this part execute POLIZ of program
