//
//  jsonInFile.h
//  Copyright (c) 2014 CPCS. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface jsonInFile : NSObject
+(void)writeJsonToFile:(NSData*)data fileName:(NSString*)fileName;
+(id)getDataFromFile:(NSString*)fileName;
+(void)removeAllData;
@end
