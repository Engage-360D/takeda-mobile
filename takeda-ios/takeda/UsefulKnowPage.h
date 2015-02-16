//
//  UsefulKnowPage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UsefulKnowPage : VControllerExt

@property (nonatomic, strong) IBOutletCollection(UIButton) NSMutableArray *btnsCollection;
@property (nonatomic,retain) IBOutletCollection(UILabel) NSArray *blockLabels;

@property (nonatomic, strong) IBOutlet UIButton *infInsInfo;
@property (nonatomic, strong) IBOutlet UIButton *LifeStyle;
@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@end
