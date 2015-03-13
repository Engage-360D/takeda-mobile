//
//  NSArray+CaseInsensitiveIndexing.m
//  iMedicum
//
//  Created by Alexander Rudenko on 02.10.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "NSArray+CaseInsensitiveIndexing.h"

@implementation NSArray (CaseInsensitiveIndexing)

- (NSUInteger)indexOfCaseInsensitiveString:(NSString *)aString {
    NSUInteger index = 0;
    for (NSString *object in self) {
        if ([object caseInsensitiveCompare:aString] == NSOrderedSame) {
            return index;
        }
        index++;
    }
    return NSNotFound;
}


@end
