all: compile

compile:
	cd src/ && javac cli/*.java data/*.java keyboard/*.java ui/*.java ui/headerElements/*.java wordle/*.java

gui: compile
	cd src/ && java wordle.WordleGui

cli: compile
	cd src/ && java wordle.WordleCli

clean:
	- cd src/ && rm -r cli/*.class data/*.class keyboard/*.class ui/*.class ui/headerElements/*.class wordle/*.class