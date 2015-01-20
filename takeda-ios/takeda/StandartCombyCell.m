//
//  StandartCombyCell.m
//  takeda
//
//  Created by Alexander Rudenko on 20.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "StandartCombyCell.h"

@implementation StandartCombyCell
@synthesize caption;
@synthesize subTitle;
@synthesize rightCaption;
@synthesize rightArrow;

- (void)awakeFromNib {
    
    caption.font = [UIFont fontWithName:@"SegoeWP" size:14];
    subTitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:10];
    rightCaption.font = [UIFont fontWithName:@"SegoeWP" size:14];
    
//    UIView *selectedView = [UIView new];
//    selectedView.backgroundColor = [UIColor clearColor];
//    self.selectedBackgroundView = selectedView;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    
    // Configure the view for the selected state
}

-(void)setCellType:(CombyCellType)cellType{
    [self setupCellType:cellType];
    _cellType = cellType;
}

-(CombyCellType)getCellType{
    return _cellType;
}

-(void)setupCellType:(CombyCellType)cellType{
    
    caption.frame = CGRectMake(12, 8, self.width - 12, 21);
    caption.hidden = NO;
    subTitle.hidden = YES;
    rightCaption.hidden = YES;
    rightArrow.hidden = YES;

    switch (cellType) {
        case ctSimpleRightCaption:{
            
            break;
        }
        case ctLeftCaptionRightCaption:{
            rightCaption.hidden = NO;

            break;
        }
        case ctLeftCaptionRightArrow:{
            rightArrow.hidden = NO;
            break;
        }
        case ctCaptionSubtitleRightArrow:{
            caption.frame = CGRectMake(12, 2, self.width - 12, 20);

            subTitle.hidden = NO;
            rightArrow.hidden = NO;
            
            break;
        }
    }
}

@end

