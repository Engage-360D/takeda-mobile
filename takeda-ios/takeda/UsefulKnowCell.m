//
//  UsefulKnowCell.m
//  takeda
//
//  Created by Alexander Rudenko on 19.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UsefulKnowCell.h"

@implementation UsefulKnowCell
@synthesize headText;
@synthesize bodyText;
@synthesize img;

- (void)awakeFromNib {
    // Initialization code
    
    bodyText.editable = NO;
    bodyText.scrollEnabled = NO;
    
    headText.font = [UIFont fontWithName:@"SegoeWP-Light" size:17];
    headText.textColor = RGB(95, 95, 95);
   
    bodyText.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    bodyText.textColor = RGB(54, 65, 71);

}

-(void)setupCell:(NSMutableDictionary*)info{
    headText.text = info[@"title"];
    bodyText.text = info[@"text"];
    if (info[@"image"]) {self.img.image = [UIImage imageNamed:info[@"image"]];}
    
    float hh = [info[@"hh"] floatValue]; // [Global measureHeightOfUITextView:headText];
    float bh = [info[@"bh"] floatValue]; //[Global measureHeightOfUITextView:bodyText];
    
    hh = MAX(65, hh);
    img.height = hh;
    
    headText.y = img.y;
    headText.height = hh;
    
    bodyText.y = headText.bottom;
    bodyText.height = bh;
    self.sharePanel.y = bodyText.bottom;
}



-(float)heightCell:(NSMutableDictionary*)info{
    headText.text = info[@"title"];
    bodyText.text = info[@"text"];
    
    float h = 0;
    
    float hh = [Global heightLabel:headText]; // [Global measureHeightOfUITextView:headText];
    float bh = [Global measureHeightOfUITextView:bodyText];
    
    [info setObject:[NSNumber numberWithFloat:hh] forKey:@"hh"];
    [info setObject:[NSNumber numberWithFloat:bh] forKey:@"bh"];

    hh = MAX(65, hh);
    img.height = hh;
    
    headText.y = img.y;
    headText.height = hh;
    
    bodyText.y = headText.bottom;
    bodyText.height = bh;
    
    h = bodyText.bottom +45;
    h = MAX(h, 125);

    return h;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

