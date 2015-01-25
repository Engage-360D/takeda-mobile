//
//  NSArray+Filed.m
//  takeda
//
//  Created by Alexander Rudenko on 25.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "NSArray+Filed.h"

@implementation NSArray (Filed)

-(void)saveTofile:(NSString*)fileName{
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:self];
    [data writeToFile:fileName atomically:YES];
}

+(NSMutableArray*)readFromFile:(NSString*)fileName{
    NSData *data = [[NSData alloc] initWithContentsOfFile:fileName];
    NSArray *array = [NSKeyedUnarchiver unarchiveObjectWithData:data];
    return [Global recursiveMutable:array];
}

@end
