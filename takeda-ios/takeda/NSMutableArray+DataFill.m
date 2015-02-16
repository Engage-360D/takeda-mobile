//
//  NSMutableArray+DataFill.m
//  takeda
//
//  Created by Alexander Rudenko on 13.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "NSMutableArray+DataFill.h"

@implementation NSMutableArray (DataFill)

-(void)fillIntegerFrom:(int)start to:(int)finish step:(int)step{
    [self removeAllObjects];
    for (int i = start; i<=finish; i+=step){
        [self addObject:[NSNumber numberWithInt:i]];
    }
}

-(void)fillFloatFrom:(float)start to:(float)finish step:(float)step{
    [self removeAllObjects];
    for (float i = start; i<=finish; i+=step){
        [self addObject:[NSNumber numberWithFloat:i]];
    }
}

@end
