//
//  NSArray+Filt.h
//  iMedicum
//
//  Created by Alexander Rudenko on 01.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface NSArray (Filt)

@property (nonatomic, retain) NSArray *filteredArray;
@property (nonatomic, retain) NSString *filtK;
@property (nonatomic, retain) NSString *filter;

-(void)filtArray;

@end
