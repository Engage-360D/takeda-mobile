//
//  NSArray+Filt.m
//  iMedicum
//
//  Created by Alexander Rudenko on 01.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "NSArray+Filt.h"
#import <objc/runtime.h>

static char const * const origSKey = "OrigTag";
static char const * const filterSKey = "FilterTag";
static char const * const filterKeySKey = "FilterKeyTag";
@implementation NSArray (Filt)

@dynamic filteredArray;
@dynamic filter;
@dynamic filtK;
//
- (NSArray*)filteredArray {
    NSArray *arr = objc_getAssociatedObject(self, origSKey);
    if (!arr||self.filter.length==0||self.filtK.length==0) {
        return self;
    }
 
    return arr;
    
   // return [self filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"%K CONTAINS[cd] %@",self.filtK, self.filter]];

    

}

- (void)setFilteredArray:(NSArray*)newObjectTag {
    objc_setAssociatedObject(self, origSKey, newObjectTag, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}
//
- (id)filter {
    return objc_getAssociatedObject(self, filterSKey);
}

- (void)setFilter:(id)newObjectTag {
    objc_setAssociatedObject(self, filterSKey, newObjectTag, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
    [self filtArray];
}
//
- (id)filtK{
    return objc_getAssociatedObject(self, filterKeySKey);
}

- (void)setFiltK:(id)newObjectTag {
    objc_setAssociatedObject(self, filterKeySKey, newObjectTag, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}
//

-(void)filtArray{
    if (!self.filteredArray) self.filteredArray = [NSArray arrayWithArray:self];
    if (self.filter.length>0&&self.filtK.length>0){
        self.filteredArray = [self filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"%K CONTAINS[cd] %@",self.filtK, self.filter]];
    } else {
        self.filteredArray = self;
    }
}

@end
