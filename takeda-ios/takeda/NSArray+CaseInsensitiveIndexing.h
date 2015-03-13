//
//  NSArray+CaseInsensitiveIndexing.h
//  iMedicum
//
//  Created by Alexander Rudenko on 02.10.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSArray (CaseInsensitiveIndexing)

- (NSUInteger)indexOfCaseInsensitiveString:(NSString *)aString;

@end
