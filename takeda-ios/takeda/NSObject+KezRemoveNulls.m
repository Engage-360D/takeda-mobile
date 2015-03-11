// NSObject+KezRemoveNulls.m

#import "NSObject+KezRemoveNulls.h"

@implementation NSObject (KezRemoveNulls)

- (void)Kez_removeNulls {
    // nothing to do
}

@end

@implementation NSMutableArray (KezRemoveNulls)

- (void)Kez_removeNulls {
    [self removeObject:[NSNull null]];
    for (NSObject *child in self) {
        [child Kez_removeNulls];
    }
}

@end

@implementation NSMutableDictionary (KezRemoveNulls)

- (void)Kez_removeNulls {
    NSNull *null = [NSNull null];
    for (NSObject *key in self.allKeys) {
        NSObject *value = self[key];
        if (value == null) {
            [self removeObjectForKey:key];
        } else {
            [value Kez_removeNulls];
        }
    }
}


@end
