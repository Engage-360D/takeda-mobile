//
//  analizeCheckCell.h
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "buttonWithID.h"

@interface analizeCheckCell : UITableViewCell
@property (nonatomic,retain) IBOutlet UILabel *name;

@property(nonatomic,retain) IBOutlet UIImageView *top_separator;
@property(nonatomic,retain) IBOutlet UIImageView *bottom_separator;

@property (nonatomic,retain) IBOutlet buttonWithID *selected_item;
@end
