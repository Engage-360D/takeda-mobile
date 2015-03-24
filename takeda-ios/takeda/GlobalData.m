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
            [[NSNotificationCenter defaultCenter] addObserver:objectInstance
                                                     selector:@selector(updateCalendarBadge)
                                                         name:kCalendarTasksChanged
                                                       object:nil];

        }
        return objectInstance;
    }
}

+(void)resetData{
    [[NSNotificationCenter defaultCenter] removeObserver:objectInstance];
    objectInstance = nil;
}


-(void)openDB{
    database = [FMDatabase databaseWithPath:[Global pathToDB]];
    [database open];
}

-(NSMutableArray*)arrayFromFMRS:(FMResultSet*)rs{
    NSMutableArray *array = [NSMutableArray new];
    while ([rs next]) {
        NSMutableDictionary *d = [NSMutableDictionary new];
        for (NSString *key in [rs.resultDictionary allKeys]){
            id object = [rs.resultDictionary objectForKey:key];
            if ([object isKindOfClass:[NSNull class]]||object == nil||object == (id)[NSNull null]){
                [rs.resultDictionary setValue:nil forKey:key];
            } else {
                [d setObject:object forKey:key];
            }
            
        }
        [array addObject:d];
        
    }
    
    return [Global recursiveMutable:array];
}

-(NSMutableDictionary*)dictionaryFromFMRS:(FMResultSet*)rs{
    NSMutableDictionary *dict = [NSMutableDictionary new];
    [rs next];
    dict = [NSMutableDictionary dictionaryWithDictionary:rs.resultDictionary];
    for (int i = 0; i<dict.allKeys.count; i++) {
        NSString *currentKey = dict.allKeys[i];
        id object = [rs.resultDictionary objectForKey:currentKey];
        if ([object isKindOfClass:[NSNull class]]||object == nil||object == (id)[NSNull null]){
            [dict removeObjectForKey:currentKey];
            i--;
        }
    }
    return [Global recursiveMutable:dict];
}


+(NSMutableArray*)regionsList{
    return [Global recursiveMutable: [[NSMutableArray alloc] initWithContentsOfFile:filePath(regionsListFile)]];
}

+(void)loadRegionsList:(void (^)(BOOL success, id result))completion{
}

+(void)saveRegions:(NSMutableArray*)regions{
    [regions writeToFile:filePath(regionsListFile) atomically:YES];
}

+(void)saveIncidents:(NSMutableDictionary*)incidents{
    [UserDefaults setValue:incidents forKey:@"incidents"];
    User.incidents = incidents;
    // [regions writeToFile:filePath(regionsListFile) atomically:YES];
    
    /*
     
     "data": {
     "hadBypassSurgery": true,
     "hadHeartAttackOrStroke": false,
     "hasDiabetes": false
     }

     */
    
}


+(void)clearFiles{

    NSFileManager *fileManager = [NSFileManager defaultManager];
    [fileManager removeItemAtPath:resultAnalysesFile error:nil];
    [fileManager removeItemAtPath:resultDietFile error:nil];
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


+(void)cleanOldResults:(NSMutableArray*)results{
    NSDate *lastReset = [Global parseDateTime:User.userData[@"resetAt"]];
    BOOL params = YES;
    if (!results){
        params = NO;
        results = [self resultAnalyses];
    }
    
    for (int i = 0; i<results.count; i++){
        if ([[Global parseDateTime:results[i][@"createdAt"]]isLaterThanDate:lastReset]){
            [results removeObjectAtIndex:i];
            i--;
        }
    }

    if (!params){
        [results saveTofile:resultAnalysesFile];
    }
    
}

+(void)saveResultAnalyses:(id)result{
    
    int lastId = 0;
    NSString *fileN = resultAnalysesFile;
    NSMutableArray *arr = [NSMutableArray new];
    NSMutableArray *tArr = [self resultAnalyses];
    
    [self cleanOldResults:tArr];
    
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

+(NSDate*)lastloadCitiesDate{
    return (NSDate*)[UserDefaults valueForKey:@"lastloadCitiesDate"];
}

+(void)writeLastloadCitiesDate:(NSDate*)lId{
    [UserDefaults setObject:lId forKey:@"lastloadCitiesDate"];
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


//-(void)setIncidentTo:(NSMutableArray*)inc incident:(IncidentType)incType comment:(NSString*)comment{
//    [self deleteAllIncidents:inc];
//    [self addIncidentTo:inc incident:incType comment:comment];
//}

-(void)addIncidentTo:(NSMutableDictionary*)inc incident:(NSString*)incidentType comment:(NSString*)comment{
    [inc setObject:@YES forKey:incidentType];
}

-(void)deleteIncident:(NSMutableDictionary*)inc incident:(NSString*)incidentType{
    [inc setObject:@NO forKey:incidentType];
}

-(void)deleteAllIncidents:(NSMutableDictionary*)inc{
    [inc removeAllObjects];
    [inc addEntriesFromDictionary:[GlobalData incidentModel]];
}

+(NSMutableDictionary*)incidentModel{
    return [[NSMutableDictionary alloc] initWithDictionary:@{@"hadBypassSurgery":[NSNumber numberWithBool:NO],@"hadHeartAttackOrStroke":[NSNumber numberWithBool:NO],@"hasDiabetes":[NSNumber numberWithBool:NO]}];
}

+(NSDictionary*)incidents{
    return @{[NSNumber numberWithInt:inInsultInfarct]:@"hadHeartAttackOrStroke", [NSNumber numberWithInt:inCoronar]:@"hadBypassSurgery", [NSNumber numberWithInt:inDiabet]:@"hasDiabetes"};
}

+(void)resultAnalBlock:(NSString*)url completition:(void (^)(BOOL success, id result))completion{
    id res = [self cashedRequest:url needInternet:YES];
    if (res){
        completion(YES, res);
        return;
    };
    
    [ServData resultAnalBlock:url completition:^(BOOL success, id result){
        completion(success, result);
    }];
    
}

+(void)loadAnalysisFromServerWithLastId:(int)lastId completion:(void (^)(BOOL success, NSError* error, id result))completion{
    
    [ServData loadAnalysisFromServerWithLastId:lastId completion:^(BOOL success, NSError *error, id result) {
        {
            if ([error answerOk]){
                    [self saveResultAnalyses:result[@"data"]];
                    completion(YES,error, result);
            } else {
                completion(NO,error, result);
            }
        }
    }];
}


+(void)loadMyCityByLocation:(CLLocation*)location fromCashe:(BOOL)fromCashe copml:(void (^)(BOOL success, id result))completion{
        if ([UserDefaults valueForKey:@"lastCity"]&&fromCashe){
            completion(YES, [UserDefaults valueForKey:@"lastCity"]);
        } else {
            [ServData loadMyCityByLocation:location copml:^(BOOL success, id result) {
                if (success){
                    if (result[@"data"]&&[result[@"data"] isKindOfClass:[NSDictionary class]]){
                        if (result[@"data"][@"id"]){
                            NSString *city = result[@"data"][@"id"];
                            [UserDefaults setObject:city forKey:@"lastCity"];
                            completion(success, result[@"data"][@"id"]);
                        } else {
                            completion(NO, result);
                        }
                    }
                } else {
                    completion(NO, result);
                }
            }];
        }
}

-(void)loadLPUSListForCity:(NSString*)city spec:(NSString*)spec copml:(void (^)(BOOL success, id result))completion{
    [ServData loadLPUsListForCity:city spec:spec copml:^(BOOL success, id result){
        completion(success, result);
    }];
    
}

-(void)loadLPUSListWithCasheForCity:(NSString*)city spec:(NSString*)spec firstCashe:(BOOL)firstCache copml:(void (^)(BOOL success, id result))completion{
    NSString *url = [NSString stringWithFormat:@"%@%@?parsedTown=%@&specialization=%@",kServerURL,kInstitutionsList,city, spec];

    if (firstCache){
        id res = [GlobalData cashedRequest:url needInternet:NO];
        if (res){
            completion(YES, res);
            return;
        } else {
            [self loadLPUSListForCity:city spec:spec copml:^(BOOL success, id result){
                if (success){
                    [GlobalData casheRequest:result fromUrl:url];
                }
                completion(success, result);
            }];
        };

    } else {
        [self loadLPUSListForCity:city spec:spec copml:^(BOOL success, id result){
            if (success){
                [GlobalData casheRequest:result fromUrl:url];
            } else {
                id res = [GlobalData cashedRequest:url needInternet:NO];
                if (res){
                    completion(YES, res);
                    return;
                };
            }
            completion(success, result);
        }];
    }

    

}



+(void)updateISP:(void (^)(BOOL success, id result))completion{
    [ServData loadISPCompletition:^(BOOL success, id result){
            if (success&&result[@"data"]&&[result[@"data"] isKindOfClass:[NSDictionary class]]&&[result[@"data"] hasKey:@"id"]){
                NSString *isp = result[@"data"][@"id"];
                [UserDefaults setObject:isp forKey:@"ISP"];
                completion(success, result[@"data"][@"id"]);
            } else {
                completion(NO, nil);
            }
    }];

}

+(NSString*)ISP{
    return [UserDefaults valueForKey:@"ISP"];
}

+(void)loadTimelineCompletition:(void (^)(BOOL success, id result))completion{

    [ServData loadTimelineCompletition:^(BOOL success, id result){
        
        [GlobalData casheTimelineTasks:[Global recursiveMutable:[result[@"linked"][@"tasks"] groupByKey:@"id"]]];
        [GlobalData casheTimeline:result[@"data"]];

        completion(success, result);
    }];
    
}


-(void)loadCitiesList:(void (^)(BOOL success, id result))completion{
    [ServData loadCitiesCompletition:^(BOOL success, id result){
        NSArray *cities = result[@"data"];
        [database executeUpdate:@"DELETE FROM cities"];
        [database beginTransaction];
        for (int i = 0; i<cities.count; i++){
            [database executeUpdate:@"INSERT INTO cities VALUES (?)",[NSString stringWithFormat:@"%@",cities[i][@"id"]]];
            if (i%100==0){
                [database commit];
                [database beginTransaction];
            }
        }
        [database commit];
        completion(success, result);
    }];
    
}

-(void)loadSpecializationsList:(void (^)(BOOL success, id result))completion{
    [ServData loadSpecializationsCompletition:^(BOOL success, id result){
        NSArray *spec = result[@"data"];
        [database executeUpdate:@"DELETE FROM specs"];
        [database beginTransaction];
        for (int i = 0; i<spec.count; i++){
            [database executeUpdate:@"INSERT INTO specs VALUES (?)",[NSString stringWithFormat:@"%@",spec[i][@"id"]]];
            if (i%100==0){
                [database commit];
                [database beginTransaction];
            }
        }
        [database commit];
        completion(success, result);
    }];
    
}


-(NSMutableArray*)citiesTerm:(NSString*)term{
    int limit = 100;
    NSString *query = [NSString stringWithFormat:@"SELECT * FROM cities WHERE UPPER(%@) LIKE UPPER('%@%@%@') limit %i",@"name", @"%", term,@"%",limit];
    FMResultSet *rs = [database executeQuery:query];
    return [self arrayFromFMRS:rs];
}

-(NSMutableArray*)specializationsTerm:(NSString*)term{
    int limit = 100;
    NSString *query = [NSString stringWithFormat:@"SELECT * FROM specs WHERE UPPER(%@) LIKE UPPER('%@%@%@') limit %i",@"name", @"%", term,@"%",limit];
    FMResultSet *rs = [database executeQuery:query];
    return [self arrayFromFMRS:rs];
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

+(id)cashedRequest:(NSString*)url needInternet:(BOOL)needIternet{
    if (appDelegate.hostConnection != NotReachable&&needIternet) {
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
    [[NSNotificationCenter defaultCenter] postNotificationName:kCalendarTasksChanged object:nil];
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

//-(NSNumber*)missedEventsCount{
//    
//}

-(NSNumber*)missedEventsCount{
    if (_missedEventsCount==nil){
        [self updateCalendarBadge];
    }
    return _missedEventsCount;
}

-(void)updateCalendarBadge{
    if (!User.userData) return;
    NSMutableDictionary *tasks;
    tasks = [GlobalData cashedTimelineTasks];
    NSArray *tasksArray = tasks.allValues;
    NSArray *missedTasksArray = [tasksArray filteredArrayUsingPredicate:[NSPredicate predicateWithFormat:@"SELF.isCompleted !=null"]];
    _missedEventsCount = [NSNumber numberWithInt:tasksArray.count - missedTasksArray.count];
}


@end
