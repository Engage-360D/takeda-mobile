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
@property (nonatomic, strong) FMDatabase *database;

+(NSMutableArray*)regionsList;

+(void)loadRegionsList:(void (^)(BOOL success, id result))completion;

+(void)saveRegions:(NSMutableArray*)regions;

+(void)saveResultAnalyses:(NSMutableDictionary*)result;
+(NSMutableArray*)resultAnalyses;
+(int)lastResultDataId;
+(void)writeLastResultDataId:(int)lId;
+(NSDate*)lastResultDate;

-(void)setIncidentTo:(NSMutableArray*)inc incident:(IncidentType)incType comment:(NSString*)comment;
-(void)addIncidentTo:(NSMutableArray*)inc incident:(IncidentType)incType comment:(NSString*)comment;
-(void)deleteIncident:(NSMutableArray*)inc incident:(IncidentType)incType;
-(void)deleteAllIncidents:(NSMutableArray*)inc;
+(void)resultAnalBlock:(NSString*)url completition:(void (^)(BOOL success, id result))completion;

+(void)clearFiles;

+(id)cashedRequest:(NSString*)url;
+(void)casheRequest:(id)res fromUrl:(NSString*)url;
@end
