//
//  GlobalSettings.h
//

#import <Foundation/Foundation.h>



enum {
    State_MainPage = 1,
    State_Risk_Analysis = 2,
    State_Search_Institution = 3,
    State_Recomendation = 4,
    State_Analysis_Result = 5,
    State_Calendar = 6,
    State_Useful_Know = 7,
    State_Publication = 8,
    State_Reports = 9
};
typedef NSUInteger StateMenu;



@interface GlobalSettings : NSObject

+(GlobalSettings*)sharedInstance;
@property StateMenu stateMenu;




@end
