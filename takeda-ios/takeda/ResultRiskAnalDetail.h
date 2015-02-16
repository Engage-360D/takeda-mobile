//
//  ResultRiskAnalDetail.h
//  takeda
//
//  Created by Alexander Rudenko on 16.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ResultRiskAnalDetail : VControllerExt

@property (nonatomic, strong) IBOutletCollection(UIButton) NSMutableArray *btnsCollection;
@property (nonatomic, strong) IBOutlet UIActivityIndicatorView *indicator;
@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (nonatomic, strong) IBOutlet UIImageView *stateIcon;
@property (nonatomic, strong) IBOutlet UILabel *subtitle;
@property (nonatomic, strong) IBOutlet UITextView *note;
@property (nonatomic, strong) IBOutlet UITextView *bannerInfo;
@property (nonatomic, strong) IBOutlet UIView *bannerInfoContainer;

@property (nonatomic, strong) NSMutableDictionary *blockInfo;

@end
