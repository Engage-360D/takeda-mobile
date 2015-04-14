
#define uKey(key)                           [NSString stringWithFormat:@"%@",key]//[NSString stringWithFormat:@"%@_%@",key,User.user_login]
#define aKey(key)                           [NSString stringWithFormat:@"%@_analiz",key]



typedef enum {
    tUser = 1,
    tDoctor = 2,
} UserType;


typedef enum {
    sMan = 1,
    sWoman = 2
} Sex;

typedef enum {
    sVK = 1,
    sFB = 2,
    sOK = 3,
    sGp = 4
} SocialServices;

typedef enum {
    inInsultInfarct = 1,
    inCoronar = 2,
    inDiabet = 3
} IncidentType;

typedef enum {
    dNew = 1,
    dFilled = 2
} DIndex;


