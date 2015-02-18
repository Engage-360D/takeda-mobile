//
//  StandartCombyCell.h
//  takeda
//
//  Created by Alexander Rudenko on 20.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "buttonWithID.h"

typedef enum {
    ctSimpleRightCaption = 1,
    ctLeftCaptionRightCaption = 2,
    ctLeftCaptionRightArrow = 3,
    ctCaptionSubtitleRightArrow = 4,
    ctCaptionSubtitleChecked = 5,
    ctCaptionChecked = 6,
    ctLeftCaptionRightBadgeArrow = 7,
    ctLeftCaptionRightCaptionArrow = 8

} CombyCellType;

@interface StandartCombyCell : UITableViewCell
@property (nonatomic, strong) IBOutlet UILabel *caption;
@property (nonatomic, strong) IBOutlet UILabel *subTitle;
@property (nonatomic, strong) IBOutlet UILabel *rightCaption;
@property (nonatomic, strong) IBOutlet UIImageView *rightArrow;
@property (nonatomic, strong) IBOutlet buttonWithID *checkBtn;

@property (nonatomic) BOOL checked;


@property (nonatomic) CombyCellType cellType;


-(void)setupCellType:(CombyCellType)cellType;

@end
