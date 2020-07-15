LIBS:=-lwiringPi -lpthread -lcrypt -lm -lrt
JAVA_HOME:=${JAVA_HOME}
INCLUDES:=-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
OUTPUT:=/usr/lib/libelevateit.so

all: WiegandReader.o Buzzer.o
	gcc -Wall -fPIC -shared -o $(OUTPUT) $^ $(LIBS)

WiegandReader.o: WiegandReader.c
	gcc -fPIC $(INCLUDES) -c $< -o $@

Buzzer.o: Buzzer.c
	gcc -fPIC $(INCLUDES) -c $< -o $@

.PHONY: clean

clean:
	rm -f *.o