//
//  analizEasyCell.h
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface analizEasyCell : UITableViewCell
@property (nonatomic,retain) IBOutlet UILabel *name;
@property (nonatomic,retain) IBOutlet UILabel *value;
@property (nonatomic,retain) IBOutlet UILabel *description;

@property(nonatomic,retain) IBOutlet UIImageView *top_separator;
@property(nonatomic,retain) IBOutlet UIImageView *bottom_separator;
@end
