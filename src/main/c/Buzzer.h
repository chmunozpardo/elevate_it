#include <jni.h>
#ifndef _Included_Buzzer
#define _Included_Buzzer
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     Buzzer
 * Method:    buzz
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cl_dreamit_elevateit_Hardware_Buzzer_buzz
  (JNIEnv *, jobject, jdouble, jint);

#ifdef __cplusplus
}
#endif
#endif
