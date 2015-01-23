//
//  inetRequests.h
//  takeda
//
//  Created by Serg on 4/4/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "analizData.h"

@interface inetRequests : NSObject
+(void)authUserWithLogin:(NSString*)login
                password:(NSString*)password
              completion:(void (^)(BOOL result, NSError* error))completion;

+(void)getUserDataWithCompletion:(void (^)(BOOL result, NSError* error))completion;
+(void)registrationUserWithData:(NSDictionary*)params  completion:(void (^)(BOOL result, NSError* error ,NSString* textError))completion;

+(void)reсoverPassword:(NSString*)email  completion:(void (^)(BOOL result, NSString* error))completion;


+(void)sendAnalysisToServer:(NSDictionary*)analysisData completion:(void (^)(BOOL result, NSError* error))completion;



@end
