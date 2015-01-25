//
//  NSDictionary+Filed.h
//  takeda
//
//  Created by Alexander Rudenko on 25.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSDictionary (Filed)

-(void)saveTofile:(NSString*)fileName;
+(NSMutableDictionary*)readFromFile:(NSString*)fileName;

@end
