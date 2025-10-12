## Information Retrieval Project - Bhargav Alimili.

## Overview
1.It is a project that deploys a simple Information Retrieval system in Java. It includes:
2.Thokenizer Tokenizes and elimites stopwords.  
3.Porter  Stemmer  Filters words to root form.  
4.WordDictionary & FileDictionary  Keeps term and document dictionaries.  
5.TextParser  Parsing and building dictionaries.  
6.Stopword List  A list of stopwords to be ignored as a text file.  
7.parseroutput.txt  Sample dictionary output that was produced after parsing.

## Requirements
Java 8+ (JDK must be installed)  

## Verify installation:
java -version
javac -version

## Project Structure
FileDictionary.java
Porter.java
Tokenizer.java
TextParser.java
WordDictionary.java
stopwordlist.txt
output of the parsing (generated output) **parser_output.txt**

## How to Compile
Open the terminal and execute the below commands

javac *.java

This summarizes all the files of .Java and produces files of .class

## How to Run
Run TextParser program and give it the path of the input file as an argument:

java TextParser <inputFile>

## Example (using ft911_1):
java TextParser ft911_1

## For multiple files:
java TextParser ft911/*

## Generated Output:
The indexed word dictionary is in the parseroutput.txt file as the words ID.