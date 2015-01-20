//
//  StandartCombyCell.h
//  takeda
//
//  Created by Alexander Rudenko on 20.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    ctSimpleRightCaption = 1,
    ctLeftCaptionRightCaption = 2,
    ctLeftCaptionRightArrow = 3,
    ctCaptionSubtitleRightArrow = 4,
} CombyCellType;

@interface StandartCombyCell : UITableViewCell
@property (nonatomic, strong) IBOutlet UILabel *caption;
@property (nonatomic, strong) IBOutlet UILabel *subTitle;
@property (nonatomic, strong) IBOutlet UILabel *rightCaption;
@property (nonatomic, strong) IBOutlet UIImageView *rightArrow;

@property (nonatomic) CombyCellType cellType;


-(void)setupCellType:(CombyCellType)cellType;

@end
