//
//  GlobalSettings.h
//

#import <Foundation/Foundation.h>

#define client_id @"3_5nsdsmqy3dc80gkk48s0g0ow8o44soc0o44cwkgsc8wgckss8"
#define client_secret @"20i8s224eqjo4804g8c0448co8g88o8occkkksgww4ksw4ksgs"
#define api_url @"http://46.182.24.175"


enum {
    State_Risk_Analysis = 1,
    State_Search_Institution = 3,
    State_Recomendation = 2,
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
