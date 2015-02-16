//
//  Synchronizer.m
//  takeda
//
//  Created by Alexander Rudenko on 02.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "Synchronizer.h"

@implementation Synchronizer

static Synchronizer *sharedInst = NULL;


+(Synchronizer*)sharedInstance{
    if (!sharedInst || sharedInst == NULL) {
        sharedInst = [Synchronizer new];
    }
    return sharedInst;
}

-(void)startSynchronize{
    [self loadRiskAnalResults];
}

-(void)loadRiskAnalResults{
    int lastId = [GlobalData lastResultDataId];
    [ServData loadAnalysisFromServerWithLastId:lastId completion:^(BOOL success, NSError *error, id result) {
        if (success) {
                [GlobalData saveResultAnalyses:result[@"data"]];
        } else{
        }
    }];

}



@end
