//
//  ResultRiskAnalDetail.m
//  takeda
//
//  Created by Alexander Rudenko on 16.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "ResultRiskAnalDetail.h"

@interface ResultRiskAnalDetail ()

@end

@implementation ResultRiskAnalDetail
@synthesize blockInfo;
@synthesize scrollView;
@synthesize stateIcon;
@synthesize subtitle;
@synthesize note;
@synthesize bannerInfo;
@synthesize bannerInfoContainer;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setupInterface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    scrollView.alpha = 0;
    [_indicator startAnimating];
    [self initData];
}

-(void)setupInterface{
    subtitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:17];
    note.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    bannerInfo.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    
    subtitle.textColor = RGB(53, 65, 71);
    note.textColor = RGB(53, 65, 71);
    bannerInfo.textColor = RGB(53, 65, 71);

    for (UIButton * btn in self.btnsCollection){
        btn.titleLabel.font = [UIFont fontWithName:@"SegoeWP-Light" size:14];
        [btn setTitleColor:RGB(53, 65, 71) forState:UIControlStateNormal];
    }
}

-(void)initData{
    NSString *url = [NSString stringWithFormat:@"account/test-results/%@/pages/%@",blockInfo[@"testId"],blockInfo[@"type"]];
    
    [GlobalData resultAnalBlock:url completition:^(BOOL success, id result){
        if (!success) {[self showMessage:@"Ошибка загрузки. Попробуйте позже" title:@"Ошибка" result:^{
            [self backAction];
        }];
        } else {
            [UIView animateWithDuration:0.3f delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
                scrollView.alpha = success?1.0f:0.f;
            } completion:^(BOOL finished){
                [_indicator stopAnimating];
            }];
            
            [self showInfo:result];
        }
    }];
}

-(void)showInfo:(NSMutableDictionary*)result{
    
    
    if (blockInfo[@"title"]&&[blockInfo[@"title"] isKindOfClass:[NSString class]]){
        self.vcSubTitle.text = blockInfo[@"title"];
    } else {
        self.vcSubTitle.text = @"";
    }
    
    stateIcon.image = [UIImage imageNamed:[NSString stringWithFormat:@"%@_bigRed",result[@"data"][@"state"]]];
//    stateIcon.image = [UIImage imageNamed:@"dangerRedIcon"]; // temp
    
    
    if (result[@"data"][@"title"]&&[result[@"data"][@"title"] isKindOfClass:[NSString class]]){
        subtitle.text = result[@"data"][@"title"];
    } else {
        subtitle.text = @"";
    }
    
    if (result[@"data"][@"subtitle"]&&[result[@"data"][@"subtitle"] isKindOfClass:[NSString class]]){
        note.text = result[@"data"][@"subtitle"];
    } else {
        note.text = @"";
    }
    
    if (result[@"data"][@"text"]&&[result[@"data"][@"text"] isKindOfClass:[NSString class]]){
        bannerInfo.text = result[@"data"][@"text"];
    } else {
        bannerInfo.text = @"";
    }


    subtitle.height = [Global heightLabel:subtitle];
    note.height = [Global measureHeightOfUITextView:note];
    bannerInfo.height = 0;
    note.y = subtitle.bottom+10;
    stateIcon.y = subtitle.bottom+10;
    
    bannerInfoContainer.y = MAX(note.bottom, stateIcon.bottom);
    [bannerInfoContainer setupAutosizeBySubviews];
    [self showHideCommonInfo:nil];
    [scrollView setup_autosize];
    
}

-(IBAction)showHideCommonInfo:(UIButton*)sender{
    sender.selected = !sender.selected;
    float h;
    float time = sender!=nil?0.3:0;
    if (sender.selected){
        h = [Global measureHeightOfUITextView:bannerInfo];
    } else {
        h = 0;
    }
    
    [UIView animateWithDuration: time delay:0.0f options:UIViewAnimationOptionCurveLinear animations:^{
        bannerInfo.height = h;
        [bannerInfoContainer arrangeViewsVerticallyWithInterval:-0.5f];
        [bannerInfoContainer setupAutosizeBySubviews];
        [scrollView setup_autosize];
    } completion:^(BOOL finished){
        
    }];
    
}

/*
 note = "\U041d\U0435\U043e\U0431\U0445\U043e\U0434\U0438\U043c\U043e \U0443\U043b\U0443\U0447\U0448\U0435\U043d\U0438\U0435";
 pageUrl = "/api/v1/account/test-results/18/pages/isAddingExtraSalt";
 state = attention;
 subtitle = "";
 title = "\U041f\U043e\U0442\U0440\U0435\U0431\U043b\U0435\U043d\U0438\U0435 \U0441\U043e\U043b\U0438";

*/





@end
