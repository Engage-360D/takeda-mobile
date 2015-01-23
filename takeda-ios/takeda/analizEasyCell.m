//
//  analizEasyCell.m
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "analizEasyCell.h"

@implementation analizEasyCell
@synthesize name;
@synthesize value;
@synthesize description;
@synthesize description_1;
@synthesize description_2;


- (void)awakeFromNib
{
//    self.top_separator.height = 0.5;
//    self.bottom_separator.height = 0.5;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
