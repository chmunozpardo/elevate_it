#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <stdint.h>
#include <stdbool.h>
#include <stdlib.h>
#include <sys/time.h>
#include <errno.h>
#include <wiringPi.h>
#include <pthread.h>
#include "Buzzer.h"

// GPIOs used for the Buzzer output
#define BUZZER_PIN 20

// Implementation of the native method readCard()
JNIEXPORT void JNICALL Java_cl_dreamit_elevateit_Hardware_Buzzer_buzz(
    JNIEnv *env,
    jobject thisObj,
    jdouble frequency,
    jint duration) {

    // Setup WiringPI
    if (wiringPiSetup () < 0) {
        fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));
        return;
    }

    uint64_t period = (uint64_t) (1000000.0/frequency)/2.0;
    struct timeval stop, start;
    struct timeval freq_stop, freq_start;
    pinMode(BUZZER_PIN, OUTPUT);
    gettimeofday(&start, NULL);
    gettimeofday(&freq_start, NULL);
    int buzzValue = 0;
    while(true){
        gettimeofday(&stop, NULL);
        if((stop.tv_sec - start.tv_sec) > duration){
            digitalWrite(BUZZER_PIN, 0);
            return;
        } else {
            gettimeofday(&freq_stop, NULL);
            if((freq_stop.tv_usec - freq_start.tv_usec) > period){
                buzzValue = (buzzValue + 1)%2;
                digitalWrite(BUZZER_PIN, buzzValue);
                gettimeofday(&freq_start, NULL);
            }
        }
    }
}