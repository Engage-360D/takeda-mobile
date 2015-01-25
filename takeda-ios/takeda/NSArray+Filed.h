//
//  NSArray+Filed.h
//  takeda
//
//  Created by Alexander Rudenko on 25.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSArray (Filed)

-(void)saveTofile:(NSString*)fileName;
+(NSMutableArray*)readFromFile:(NSString*)fileName;


@end
