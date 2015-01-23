//
//  GlobalData.m
//  takeda
//
//  Created by Alexander Rudenko on 23.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "GlobalData.h"
#define filePath(fileName) [NSString stringWithFormat:@"%@/%@", [Path JSONFolder],fileName]
#define regionsListFile  @"regionsListFile"

@implementation GlobalData

static GlobalData *objectInstance = nil;
@synthesize database;

+(GlobalData*)sharedObject{
    @synchronized(self){
        if (!objectInstance) {
            objectInstance = [GlobalData new];
            [objectInstance openDB];
        }
        return objectInstance;
    }
}

-(void)openDB{
    return; //temp
    database = [FMDatabase databaseWithPath:[Global pathToDB]];
    [database open];
}

-(NSMutableArray*)regionsList{
    return [Global recursiveMutable: [[NSMutableArray alloc] initWithContentsOfFile:filePath(regionsListFile)]];
}

-(void)loadRegionsList:(void (^)(BOOL success, id result))completion{
}

-(void)saveRegions:(NSMutableArray*)regions{
    [regions writeToFile:filePath(regionsListFile) atomically:YES];
}

@end
