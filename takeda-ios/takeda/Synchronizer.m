//
//  Synchronizer.m
//  takeda
//
//  Created by Alexander Rudenko on 02.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "Synchronizer.h"

@implementation Synchronizer{

}

static Synchronizer *sharedInst = NULL;

@synthesize completedJobsCount;
@synthesize tasks;

+(Synchronizer*)sharedInstance{
    if (!sharedInst || sharedInst == NULL) {
        sharedInst = [Synchronizer new];
    }
    return sharedInst;
}

-(void)startSynchronizeCompletition:(void (^)(BOOL success, id result))completion{
    self.resultBlock = completion;
//    [self loadRiskAnalResults];
//    [self loadPills];
    tasks = [NSMutableArray arrayWithObjects:[NSValue valueWithPointer:@selector(loadRiskAnalResults)],[NSValue valueWithPointer:@selector(loadPills)], nil];
    [self startTasksMachine];
}

-(void)startTasksMachine{
    completedJobsCount = 0;
    for (int i = 0; i<tasks.count; i++){
        SEL selector = [tasks[i] pointerValue];
        IMP imp = [self methodForSelector:selector];
        void (*func)(id, SEL) = (void *)imp;
        func(self, selector);
    }
}


-(void)loadRiskAnalResults{
    NSLog(@"start loadRisk anal");
    int lastId = [GlobalData lastResultDataId];
    [ServData loadAnalysisFromServerWithLastId:lastId completion:^(BOOL success, NSError *error, id result) {
        NSLog(@"finish loadRisk anal");
        if (success) {
            [GlobalData saveResultAnalyses:result[@"data"]];
        } else {
        }
        [self finishJob];
    }];

}

-(void)loadPills{
    NSLog(@"start pills");
    [GlobalData loadPillsCompletition:^(BOOL completition, id result){
        NSLog(@"finish pills");
        [self finishJob];
    }];
}

-(void)finishJob{
    completedJobsCount++;
    NSLog(@"compl jobs count = %i",completedJobsCount);
    if (completedJobsCount == tasks.count){
        completedJobsCount = 0;
        [self finishSynchronize];
    }
}

-(void)finishSynchronize{
    NSLog(@"finish");
    self.resultBlock(YES,nil);
}

@end
