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
#define resultDietFile [NSString stringWithFormat:@"%@/%@", [Path JResultsFolder],@"diet_results"]
#define userSettingsFile [NSString stringWithFormat:@"%@/%@", [Path JResultsFolder],@"user"]
#define cashFile(url) [NSString stringWithFormat:@"%@/%@", [Path CasheFolder],url]
#define userPills [NSString stringWithFormat:@"%@/%@", [Path JSONFolder],@"pills"]
#define userTimelineTasksFile [NSString stringWithFormat:@"%@/%@", [Path JSONFolder],@"timelineTasks"]
#define userTimelineFile [NSString stringWithFormat:@"%@/%@", [Path JSONFolder],@"timeline"]


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
    [fileManager removeItemAtPath:userTimelineTasksFile error:nil];
    [fileManager removeItemAtPath:userTimelineFile error:nil];
    [fileManager removeItemAtPath:userPills error:nil];

    [self removeFilesFromDir:[Path JSONFolder]];
    [self removeFilesFromDir:[Path CasheFolder]];

}

+(void)removeFilesFromDir:(NSString*)directory{
    NSFileManager *fm = [NSFileManager defaultManager];
    NSError *error = nil;
    for (NSString *file in [fm contentsOfDirectoryAtPath:directory error:&error]) {
        BOOL success = [fm removeItemAtPath:[NSString stringWithFormat:@"%@%@", directory, file] error:&error];
        if (!success || error) {
            // it failed.
        }
    }
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
    
//    [arr sortUsingComparator: ^(id obj1, id obj2) {
//        return [obj1[@"id"]intValue]>[obj2[@"id"]intValue];
//    }];
//    
    [arr sortUsingComparator: ^(id obj1, id obj2) {
        NSNumber *numb1 = [NSNumber numberWithInteger:[obj1[@"id"] integerValue]];
        NSNumber *numb2 = [NSNumber numberWithInteger:[obj2[@"id"] integerValue]];
        return [numb1 compare:numb2];
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


+(void)saveResultDiet:(NSMutableDictionary*)result testId:(int)testId{
    NSMutableDictionary *dietResults = [NSMutableDictionary readFromFile:resultDietFile];
    if (dietResults==nil) dietResults = [NSMutableDictionary new];
    if (result!=nil){
        [dietResults setObject:result forKey:[NSNumber numberWithInt:testId]];
    }
    [dietResults saveTofile:resultDietFile];
}

+(NSMutableDictionary*)resultDietForTestId:(int)testId{
    NSMutableDictionary *dietResults = [NSMutableDictionary readFromFile:resultDietFile];
    if (dietResults==nil) return nil;
    return [dietResults objectForKey:[NSNumber numberWithInt:testId]];
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

+(NSDictionary*)incidents{
    return @{[NSNumber numberWithInt:inInsultInfarct]:@"hadHeartAttackOrStroke", [NSNumber numberWithInt:inCoronar]:@"hadBypassSurgery", [NSNumber numberWithInt:inDiabet]:@"hasDiabetes"};
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

+(void)loadTimelineCompletition:(void (^)(BOOL success, id result))completion{

    [ServData loadTimelineCompletition:^(BOOL success, id result){
        
        completion(success, result);
    }];
    
}

+(NSMutableArray*)pills{
    NSMutableArray *arr = [NSMutableArray readFromFile:userPills];
    return arr;
}

+(NSMutableDictionary*)pillById:(int)pillId{
    NSMutableArray *arr = [self pills];
    NSMutableDictionary *pill = [Global dictionaryWithValue:[NSNumber numberWithInt:pillId] ForKey:@"id" InArray:arr];
    return pill;
}

+(NSMutableDictionary*)pillsDict{
    NSMutableArray *arr = [self pills];
    return [Global recursiveMutable: [arr groupByKey:@"id"]];
}

+(void)updatePill:(NSMutableDictionary*)pill{
    NSMutableDictionary *pills = [self pillsDict];
    [pills setObject:pill forKey:pill[@"id"]];
    [pills.allKeys saveTofile:userPills];
}

+(void)deletePill:(NSMutableDictionary*)pill{
    NSMutableDictionary *pills = [self pillsDict];
    [pills removeObjectForKey:pill[@"id"]];
    [pills.allKeys saveTofile:userPills];
}


+(void)loadPillsCompletition:(void (^)(BOOL success, id result))completion{
    [ServData loadPillsCompletition:^(BOOL success, id result){
        if (success){
            NSMutableArray *pills = result[@"data"];
            [pills saveTofile:userPills];
        }
        completion(success, result);
    }];
    
}



+(id)cashedRequest:(NSString*)url{
    if (appDelegate.hostConnection != NotReachable) {
        return nil;
    }
    NSString *fileName = cashFile([Global PathFromUrl:url]);
    NSData *data = [[NSData alloc] initWithContentsOfFile:fileName];
    return [Global recursiveMutable:[NSKeyedUnarchiver unarchiveObjectWithData:data]];
}

+(void)casheRequest:(id)res fromUrl:(NSString*)url{
    NSString *fileName = cashFile([Global PathFromUrl:url]);
    NSData *data = [NSKeyedArchiver archivedDataWithRootObject:res];
    [data writeToFile:fileName atomically:YES];

}

+(void)casheTimelineTasks:(NSMutableDictionary*)tasks{
    [tasks saveTofile:userTimelineTasksFile];
}

+(NSMutableDictionary*)cashedTimelineTasks{
    return [NSMutableDictionary readFromFile:userTimelineTasksFile];
}

+(void)casheTimeline:(NSMutableArray*)tasks{
    [tasks saveTofile:userTimelineFile];
}

+(NSMutableArray*)cashedTimeline{
    return [NSMutableArray readFromFile:userTimelineFile];
}

@end
