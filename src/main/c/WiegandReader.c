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
#include "WiegandReader.h"

// GPIOs used for the Wiegand Reader
#define D0_PIN 5
#define D1_PIN 7

// Convert to MIFARE format
#define MIFARE(value)       (value & 0x000000FF) << 24 | \
                            (value & 0x0000FF00) <<  8 | \
                            (value & 0x00FF0000) >>  8 | \
                            (value & 0xFF000000) >> 24

// Convert to HID format
#define HID_CODE1(value)    (value >> (17 + 6)) & 0x00FF
#define HID_CODE2(value)    (value >> ( 1 + 6)) & 0xFFFF

// Struct to save data
struct wiegandGet{
    uint32_t code_1;
    uint32_t code_2;
    uint8_t  cardType;
};

// Routine in charge of reading the data from the Wiegand device
void detectWiegandRead(void *ctrlArg){

    struct timeval stop, start;
    struct wiegandGet *ctrlThread = (struct wiegandGet*)ctrlArg;

    int count = 0;
    bool detecting = false;
    bool old_D0 = true;
    bool old_D1 = true;
    uint64_t value = 0;
    uint64_t MAX_TIME = 250000;

    pinMode(D0_PIN, INPUT);
    pinMode(D1_PIN, INPUT);

    gettimeofday(&start, NULL);
    while(true) {
        gettimeofday(&stop, NULL);
        bool new_D0 = (digitalRead(D0_PIN) == 1);
        bool new_D1 = (digitalRead(D1_PIN) == 1);
        if(old_D0 && !new_D0) old_D0 = false;
        if(old_D1 && !new_D1) old_D1 = false;
        if(detecting) {
            if((stop.tv_usec - start.tv_usec) > MAX_TIME){
                uint8_t cardType;
                uint32_t code_1;
                uint32_t code_2;
                if(count == 26){
                    cardType = 2;
                    code_1   = HID_CODE1(value);
                    code_2   = HID_CODE2(value);
                } else if(count == 32) {
                    cardType = 5;
                    code_1   = MIFARE(value);
                    code_2   = 0;
                } else {
                    cardType = count;
                    code_1   = (value >> 32) & 0xFFFFFFFF;
                    code_2   = (value >>  0) & 0xFFFFFFFF;
                }
                ctrlThread->code_1 = code_1;
                ctrlThread->code_2 = code_2;
                ctrlThread->cardType = cardType;
                count = 0;
                value = 0;
                detecting = false;
                break;
            }
        }
        if(!old_D0 && new_D0) { // D0 on ground?
            old_D0 = true;
            detecting = true;
            ++count;
            gettimeofday(&start, NULL);
        }
        if(!old_D1 && new_D1) { // D1 on ground?
            old_D1 = true;
            detecting = true;
            value |= (1 << (31 - count));
            ++count;
            gettimeofday(&start, NULL);
        }
    }
}

// Implementation of the native method readCard()
JNIEXPORT void JNICALL Java_cl_dreamit_elevateit_Hardware_Wiegand_readCard(JNIEnv *env, jobject thisObj) {

    // Setup WiringPI
    if (wiringPiSetup () < 0) {
        fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));
        return;
    }

    // Struct to save from the Wiegand Reader
    struct wiegandGet ctrlArg = {0,0,0};

    // Loop to detect cards
    detectWiegandRead(&ctrlArg);

    // We set the data we read into the Object back to Java
    jclass thisClass = (*env)->GetObjectClass(env, thisObj);
    jfieldID fidCard_1 = (*env)->GetFieldID(env, thisClass, "card_1", "I");
    if (NULL == fidCard_1) return;
    jfieldID fidCard_2 = (*env)->GetFieldID(env, thisClass, "card_2", "I");
    if (NULL == fidCard_2) return;
    jfieldID fidCardType = (*env)->GetFieldID(env, thisClass, "cardType", "I");
    if (NULL == fidCardType) return;

    (*env)->SetIntField(env, thisObj, fidCard_1, ctrlArg.code_1);
    (*env)->SetIntField(env, thisObj, fidCard_2, ctrlArg.code_2);
    (*env)->SetIntField(env, thisObj, fidCardType, ctrlArg.cardType);
}