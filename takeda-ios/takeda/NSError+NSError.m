//
//  NSError+NSError.m
//  takeda
//
//  Created by Alexander Rudenko on 23.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "NSError+NSError.h"

@implementation NSError (NSError)

-(BOOL)answerOk{
    if (self.code==200||self.code == 201) return YES;
    return NO;
}

@end
