//
//  AnalisisResultPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "AnalisisResultPage.h"
#import "ResultRiskAnal.h"

@interface AnalisisResultPage ()

@end

@implementation AnalisisResultPage

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (self.isFromMenu){
        [self goToRes:nil];
    }
}

-(IBAction)goToRes:(id)sender{
    ResultRiskAnal *rr = [ResultRiskAnal new];
    [self.navigationController pushViewController:rr animated:YES];
}


@end
