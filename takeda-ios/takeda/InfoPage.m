//
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "InfoPage.h"

@interface InfoPage ()

@end

@implementation InfoPage
@synthesize insultInfoTxtView;
@synthesize lifeInfoTxtView;
@synthesize btnsContainer;


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
    
//    _infInsInfo.selected = YES;
//    _LifeStyle.selected = YES;
    [self showHideInsultInfo:nil];
    [self showHideCommonInfo:nil];
}

-(void)setupInterface{

    insultInfoTxtView.editable = NO;
    lifeInfoTxtView.editable = NO;
    
    insultInfoTxtView.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    insultInfoTxtView.textColor = RGB(53, 65, 71);
    
    lifeInfoTxtView.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    lifeInfoTxtView.textColor = RGB(53, 65, 71);

    
    UILabel *tL = [[UILabel alloc] initWithFrame:CGRectMake(_infInsInfo.titleLabel.x, 17, 200, 15)];
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


-(IBAction)showHideInsultInfo:(UIButton*)sender{
    sender.selected = !sender.selected;
    float h;
    float time = sender!=nil?0.2:0;
    if (sender.selected){
        h = [Global measureHeightOfUITextView:insultInfoTxtView];
    } else {
        h = 0;
    }
    
    [UIView animateWithDuration: time delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
        insultInfoTxtView.height = h;
        [btnsContainer arrangeViewsVerticallyWithInterval:-0.5f];
        [btnsContainer setupAutosizeBySubviews];
        [_scrollView setup_autosize];
    } completion:^(BOOL finished){
        
    }];
    
}


-(IBAction)showHideCommonInfo:(UIButton*)sender{
    sender.selected = !sender.selected;
    float h;
    float time = sender!=nil?0.2:0;
    if (sender.selected){
        h = [Global measureHeightOfUITextView:lifeInfoTxtView];
    } else {
        h = 0;
    }
    
    [UIView animateWithDuration: time delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
        lifeInfoTxtView.height = h;
        [btnsContainer arrangeViewsVerticallyWithInterval:-0.5f];
        [btnsContainer setupAutosizeBySubviews];
        [_scrollView setup_autosize];
    } completion:^(BOOL finished){
        
    }];
    
}



@end
