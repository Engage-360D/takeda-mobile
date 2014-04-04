//
//  Helper.h
//  takeda
//
//  Created by Serg on 4/3/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Helper : NSObject
+(float)heightText:(NSString*)text withFont:(UIFont*)font withWidth:(float)width;
+(void)fastAlert:(NSString*)text;
@end

@interface NSDictionary (Helper)
- (BOOL)hasKey:(NSString*)key;
@end
