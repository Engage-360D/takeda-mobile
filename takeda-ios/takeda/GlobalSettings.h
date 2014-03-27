//
//  GlobalSettings.h
//

#import <Foundation/Foundation.h>


enum {
    State_Risk_Analysis = 1,
    State_Search_Institution = 2,
    State_Recomendation = 3,
    State_Analysis_Result = 4,
    State_Calendar = 5,
    State_Useful_Know = 6,
    State_Publication = 7,
    State_Reports = 8
};
typedef NSUInteger StateMenu;



@interface GlobalSettings : NSObject

+(GlobalSettings*)sharedInstance;
@property StateMenu stateMenu;




@end
