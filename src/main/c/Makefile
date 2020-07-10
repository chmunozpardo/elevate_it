LIBS:=-lwiringPi -lpthread -lcrypt -lm -lrt
JAVA_HOME:=${JAVA_HOME}
INCLUDES:=-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux

C_SOURCES:=WiegandReader.c
OUTPUT:=libwiegandreader.so
FLAGS:=-fPIC
WARNINGS:=-Wall

all:
	gcc $(WARNINGS) $(FLAGS) $(INCLUDES) -shared -o $(OUTPUT) $(C_SOURCES) $(LIBS)