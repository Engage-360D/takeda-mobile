//
//  DietTestTableCell.m
//  takeda
//
//  Created by Alexander Rudenko on 26.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "DietTestTableCell.h"

@implementation DietTestTableCell

- (void)awakeFromNib {
    [self updateSegmentLabel];
    
}

-(void)updateSegmentLabel{
    self.segment.height = 50;
    
    for (id segment in [self.segment subviews]) {
        
        for (id label in [segment subviews]) {
            
            if ([label isKindOfClass:[UILabel class]]) {
                
                UILabel *titleLabel = (UILabel *) label;
                titleLabel.frame = CGRectMake(0, 0, 97, 50);
                titleLabel.numberOfLines = 0;
            }
        }
    }

}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
