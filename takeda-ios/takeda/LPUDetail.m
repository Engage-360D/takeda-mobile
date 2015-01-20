//
//  LPUDetail.m
//  takeda
//
//  Created by Alexander Rudenko on 20.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "LPUDetail.h"

@interface LPUDetail ()

@end

@implementation LPUDetail
@synthesize lpu;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self showInfo];
}

-(void)showInfo{
    self.lpuName.text = lpu[@"name"];
    self.lpuAddress.text = lpu[@"address"];
}



@end
