//
//  AddPills.h
//  takeda
//
//  Created by Alexander Rudenko on 17.02.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DPicker.h"


typedef enum {
    aCreate = 1,
    aUpdate = 2,
    aPreview = 3
} ActionType;

@interface AddPills : VControllerExt

@property (nonatomic,retain) IBOutletCollection(UIButton) NSArray *incBtns;
@property (nonatomic,retain) IBOutletCollection(PLTextField) NSArray *textFields;
@property (nonatomic,retain) IBOutletCollection(UILabel) NSArray *labels;

@property (nonatomic, strong) IBOutlet UIScrollView *scrollView;
@property (nonatomic, strong) IBOutlet PLTextField *drugName;
@property (nonatomic, strong) IBOutlet UILabel *repeatLabel;
@property (nonatomic, strong) IBOutlet UILabel *timeLabel;
@property (nonatomic, strong) IBOutlet UILabel *sinceDateLabel;
@property (nonatomic, strong) IBOutlet UILabel *tillDateLabel;
@property (nonatomic, strong) IBOutlet UILabel *quantityLabel;

@property (nonatomic) ActionType actionType;

@property (nonatomic, strong) NSMutableDictionary *drug;



@end
