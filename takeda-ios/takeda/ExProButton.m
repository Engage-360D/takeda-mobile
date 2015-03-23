//
//  ExProButton.m
//  iMedicum
//
//  Created by Alexander Rudenko on 17.09.14.
//  Copyright (c) 2014 cpcs. All rights reserved.
//

#import "ExProButton.h"

@implementation ExProButton

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)setText:(NSString*)text{
    [self setTitle:text forState:UIControlStateNormal];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
