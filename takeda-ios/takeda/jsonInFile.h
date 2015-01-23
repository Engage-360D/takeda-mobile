//
//  jsonInFile.h
//  Copyright (c) 2014 CPCS. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Path.h"

@interface jsonInFile : NSObject
+(void)writeJsonToFile:(NSData*)data fileName:(NSString*)fileName;
+(id)getDataFromFile:(NSString*)fileName;
+(void)removeAllData;

+(void)createUser:(NSString*)login userInfo:(NSMutableDictionary*)userInfo accessToken:(NSString*)access_token;
+(NSMutableDictionary*)getUserInfo:(NSString*)login;

@end
