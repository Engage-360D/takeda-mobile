//
//  UsefulKnowPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "UsefulKnowPage.h"

@interface UsefulKnowPage ()

@end

@implementation UsefulKnowPage

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
    self.mainElement = self.scrollView;
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
}

-(void)setupInterface{
    UILabel *tL = [[UILabel alloc] initWithFrame:CGRectMake(_infInsInfo.titleLabel.x, 15, 200, 15)];
    tL.font = [UIFont fontWithName:@"SegoeWP-Light" size:10];
    tL.textColor = RGB(95, 95, 95);
    tL.backgroundColor = [UIColor clearColor];
    tL.text = @"Пошаговая инструкция";
    
    [_infInsInfo addSubview:tL];
    
    for (UIButton *btn in _btnsCollection){
        btn.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
        [btn setTitleColor:RGB(54, 65, 71) forState:UIControlStateNormal];
    }
    
    for (UILabel *lb in self.blockLabels){
        lb.font = [UIFont fontWithName:@"SegoeWP" size:14];
    }
    
    [self drawBorders:self.bg_block];
    [self.scrollView setup_autosize];
}


@end
