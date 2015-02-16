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
#define resultAnalysesFile [NSString stringWithFormat:@"%@/%@", [Path JResultsFolder],@"analize_results"]
#define userSettingsFile [NSString stringWithFormat:@"%@/%@", [Path JResultsFolder],@"user"]


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

+(NSMutableArray*)regionsList{
    return [Global recursiveMutable: [[NSMutableArray alloc] initWithContentsOfFile:filePath(regionsListFile)]];
}

+(void)loadRegionsList:(void (^)(BOOL success, id result))completion{
}

+(void)saveRegions:(NSMutableArray*)regions{
    [regions writeToFile:filePath(regionsListFile) atomically:YES];
}

+(void)clearFiles{
    NSFileManager *fileManager = [NSFileManager defaultManager];
    [fileManager removeItemAtPath:resultAnalysesFile error:nil];
    [fileManager removeItemAtPath:userSettingsFile error:nil];
}

+(void)saveResultAnalyses:(id)result{
    int lastId = 0;
    NSString *fileN = resultAnalysesFile;
    NSMutableArray *arr = [NSMutableArray new];
    NSMutableArray *tArr = [self resultAnalyses];
    [arr addObjectsFromArray:tArr];
   
    if ([result isKindOfClass:[NSDictionary class]]){
        if (![Global dictionaryWithValue:result[@"id"] ForKey:@"id" InArray:tArr]){
            [arr addObject:result];
            lastId = [result[@"id"] intValue];

        }
    } else if ([result isKindOfClass:[NSArray class]]){
        for (NSMutableDictionary* res in result){
            if (![Global dictionaryWithValue:res[@"id"] ForKey:@"id" InArray:tArr]){
                [arr addObject:res];
                lastId = [res[@"id"] intValue];
            }
        }
    }
    
    [arr sortUsingComparator: ^(id obj1, id obj2) {
        return [obj1[@"id"]intValue]>[obj2[@"id"]intValue];
    }];

    
    if (![User checkForRole:tDoctor]){
        if (arr.count>0){
            [arr removeObjectsInRange:NSMakeRange(0, arr.count-1)];
        }
    }


    
    [arr saveTofile:fileN];
    [self writeLastResultDataId:lastId];
}

+(NSMutableArray*)resultAnalyses{
    NSString *fileN = resultAnalysesFile;
    NSMutableArray *arr = [NSMutableArray readFromFile:fileN];
    return arr;
}

+(int)lastResultDataId{
    return (int)[UserDefaults integerForKey:uKey(@"lastResultDataId")];
}

+(void)writeLastResultDataId:(int)lId{
    [UserDefaults setInteger:lId forKey:uKey(@"lastResultDataId")];
}

+(NSDate*)lastResultDate{
    NSMutableDictionary *results_data;
    NSMutableArray *allResults = [GlobalData resultAnalyses];
    if (allResults.count>0){
        results_data = [[GlobalData resultAnalyses] lastObject];
        NSDate *fromDate = [Global parseDateTime:results_data[@"createdAt"]];
        return fromDate;
    }
    
    return nil;
}

-(void)setIncidentTo:(NSMutableArray*)inc incident:(IncidentType)incType comment:(NSString*)comment{
    [self deleteAllIncidents:inc];
    [self addIncidentTo:inc incident:incType comment:comment];
}

-(void)addIncidentTo:(NSMutableArray*)inc incident:(IncidentType)incType comment:(NSString*)comment{
    [inc addObject:[Global recursiveMutable:@{@"type":[NSNumber numberWithInt:incType],@"comment":comment}]];
}

-(void)deleteIncident:(NSMutableArray*)inc incident:(IncidentType)incType{
    
}

-(void)deleteAllIncidents:(NSMutableArray*)inc{
    [inc removeAllObjects];
}

+(void)resultAnalBlock:(NSString*)url completition:(void (^)(BOOL success, id result))completion{
    id res = [self cashedRequest:url];
    if (res){
        completion(YES, res);
        return;
    };
    
    [ServData resultAnalBlock:url completition:^(BOOL success, id result){
        completion(success, result);
    }];
    
    
}

+(id)cashedRequest:(NSString*)url{
    return nil;
}

+(void)casheRequest:(id)res{
    
}

@end
