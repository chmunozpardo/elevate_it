LIBS:=-lwiringPi -lpthread -lcrypt -lm -lrt
JAVA_HOME:=${JAVA_HOME}
INCLUDES:=-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux
OUTPUT:=/usr/lib/libelevateit.so

SOURCES:=WiegandReader.c Buzzer.c
OBJECTS=$(SOURCES:.c=.o)

all: $(OBJECTS)
	gcc -Wall -fPIC -shared -o $(OUTPUT) $(OBJECTS) $(LIBS)

%.o: %.c
	gcc -fPIC $(INCLUDES) -c $< -o $@

.PHONY: clean

clean:
	rm -f *.o