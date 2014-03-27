

#import "GlobalSettings.h"

@implementation GlobalSettings
@synthesize stateMenu;

static GlobalSettings *sharedInst = NULL;


+(GlobalSettings*)sharedInstance{
    if (!sharedInst || sharedInst == NULL) {
        sharedInst = [GlobalSettings new];
    }
    return sharedInst;
}

@end
