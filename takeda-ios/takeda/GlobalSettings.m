

#import "GlobalSettings.h"

@implementation GlobalSettings
@synthesize stateMenu;
@synthesize last_stateMenu;

static GlobalSettings *sharedInst = NULL;


+(GlobalSettings*)sharedInstance{
    if (!sharedInst || sharedInst == NULL) {
        sharedInst = [GlobalSettings new];
    }
    return sharedInst;
}

+(void)resetData{
    sharedInst = nil;
}


@end
