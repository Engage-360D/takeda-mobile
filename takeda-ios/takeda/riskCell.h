//
//  riskCell.h
//  takeda
//
//  Created by Serg on 4/6/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface riskCell : UITableViewCell
@property (nonatomic,retain) IBOutlet UILabel *text_data;
@property (nonatomic,retain) IBOutlet UIImageView *img_icon;

@property(nonatomic,retain) IBOutlet UIImageView *top_separator;
@property(nonatomic,retain) IBOutlet UIImageView *bottom_separator;
@end
