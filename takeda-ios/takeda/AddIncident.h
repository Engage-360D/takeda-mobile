//
//  AddIncident.h
//  takeda
//
//  Created by Alexander Rudenko on 13.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RAlertView.h"

@interface AddIncident : VControllerExt
@property (nonatomic,retain) IBOutletCollection(UIButton) NSArray *incBtns;
@property (nonatomic, strong) IBOutlet UITextView *addComment;
@property (nonatomic, strong) IBOutlet UIButton *addIncident;
@property (nonatomic, strong) IBOutlet UIView *incidentsContainer;
@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;

@end
