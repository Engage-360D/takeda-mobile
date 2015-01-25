//
//  Personal.m
//  takeda
//
//  Created by Alexander Rudenko on 24.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "Personal.h"

@interface Personal ()

@end

@implementation Personal

- (void)viewDidLoad {
    [super viewDidLoad];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    UIBarButtonItem *mS = [self personalButton];
    mS.enabled = NO;
    self.navigationItem.rightBarButtonItems = @[mS,[self alarmButton]];
    
    NSLog(@"%@",User.userData);
}


@end
