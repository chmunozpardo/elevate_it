#include <jni.h>
#ifndef _Included_Wiegand
#define _Included_Wiegand
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     Wiegand
 * Method:    readCard
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_cl_dreamit_elevateit_Hardware_Wiegand_readCard
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
