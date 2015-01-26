//
//  NSDictionary+Filed.m
//  takeda
//
//  Created by Alexander Rudenko on 25.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "NSDictionary+Filed.h"

@implementation NSDictionary (Filed)

-(void)saveTofile:(NSString*)fileName{
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:self];
    [data writeToFile:fileName atomically:YES];
}

+(NSMutableDictionary*)readFromFile:(NSString*)fileName{
    NSData *data = [[NSData alloc] initWithContentsOfFile:fileName];
    NSArray *dictionary = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    return [Global recursiveMutable:dictionary];
}

@end
