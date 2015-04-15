//
//  Synchronizer.h
//  takeda
//
//  Created by Alexander Rudenko on 02.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface Synchronizer : NSObject

#define jLoadRiskAnalResults    @"loadRiskAnalResults"
#define jLoadPills              @"loadPills"
#define jLoadCities             @"loadCities"
#define jLoadSpecializations    @"loadSpecializations"
#define jLoadIncidents          @"loadIncidents"
#define jLoadISP                @"loadISP"
#define jLoadTimeLine           @"loadTimeLine"


+(Synchronizer*)sharedInstance;
+(void)resetData;

@property (nonatomic, strong) NSArray *tasks;
@property (nonatomic) int completedJobsCount;

@property (nonatomic, strong) void (^resultBlock) (BOOL success, id result);

-(void)startSynchronizeTasks:(NSArray*)tasksArray completition:(void (^)(BOOL success, id result))completion;
-(void)startSynchronizeCompletition:(void (^)(BOOL success, id result))completion;

@end
