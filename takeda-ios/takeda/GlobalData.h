//
//  GlobalData.h
//  takeda
//
//  Created by Alexander Rudenko on 23.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Synchronizer.h"

@interface GlobalData : NSObject

+(GlobalData*)sharedObject;
+(void)resetData;

@property (nonatomic, strong) FMDatabase *database;
@property (nonatomic, strong) NSNumber* missedEventsCount;

+(NSMutableArray*)regionsList;
+(void)loadRegionsList:(void (^)(BOOL success, id result))completion;
+(void)saveRegions:(NSMutableArray*)regions;
+(void)saveIncidents:(NSMutableDictionary*)incidents;
+(void)saveResultAnalyses:(NSMutableDictionary*)result;
+(void)cleanOldResults:(NSMutableArray*)results;
+(NSMutableArray*)resultAnalyses;
+(int)lastResultDataId;
+(void)writeLastResultDataId:(int)lId;
+(NSDate*)lastResultDate;
+(NSDate*)lastloadCitiesDate;
+(void)writeLastloadCitiesDate:(NSDate*)lId;
+(void)saveResultDiet:(NSMutableDictionary*)result testId:(int)testId;
+(NSMutableDictionary*)resultDietForTestId:(int)testId;
-(void)loadCitiesList:(void (^)(BOOL success, id result))completion;
-(void)loadSpecializationsList:(void (^)(BOOL success, id result))completion;
//-(void)setIncidentTo:(NSMutableArray*)inc incident:(IncidentType)incType comment:(NSString*)comment;
+(void)loadAnalysisFromServerWithLastId:(int)lastId completion:(void (^)(BOOL success, NSError* error, id result))completion;
+(void)updateISP:(void (^)(BOOL success, id result))completion;
+(NSString*)ISP;
-(void)addIncidentTo:(NSMutableDictionary*)inc incident:(NSString*)incidentType comment:(NSString*)comment;
-(void)deleteIncident:(NSMutableDictionary*)inc incident:(NSString*)incidentType;
-(void)deleteAllIncidents:(NSMutableDictionary*)inc;
+(NSMutableDictionary*)incidentModel;
-(void)loadLPUSListForCity:(NSString*)city spec:(NSString*)spec copml:(void (^)(BOOL success, id result))completion;
-(void)loadLPUSListWithCasheForCity:(NSString*)city spec:(NSString*)spec firstCashe:(BOOL)firstCache copml:(void (^)(BOOL success, id result))completion;
-(NSMutableArray*)citiesTerm:(NSString*)term;
-(NSMutableArray*)specializationsTerm:(NSString*)term;
+(NSDictionary*)incidents;
+(void)resultAnalBlock:(NSString*)url completition:(void (^)(BOOL success, id result))completion;
+(void)loadMyCityByLocation:(CLLocation*)location fromCashe:(BOOL)fromCashe copml:(void (^)(BOOL success, id result))completion;
+(void)loadTimelineCompletition:(void (^)(BOOL success, id result))completion;
+(NSMutableArray*)pills;
+(NSMutableDictionary*)pillById:(int)pillId;
+(NSMutableDictionary*)pillsDict;
+(void)updatePill:(NSMutableDictionary*)pill;
+(void)deletePill:(NSMutableDictionary*)pill;
+(void)loadPillsCompletition:(void (^)(BOOL success, id result))completion;
+(void)clearFiles;
+(void)casheTimelineTasks:(NSMutableDictionary*)tasks;
+(NSMutableDictionary*)cashedTimelineTasks;
+(void)casheTimeline:(NSMutableArray*)tasks;
+(NSMutableArray*)cashedTimeline;

+(id)cashedRequest:(NSString*)url needInternet:(BOOL)needIternet;
+(void)casheRequest:(id)res fromUrl:(NSString*)url;


@end
