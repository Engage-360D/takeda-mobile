//
//  analizRiskCell.h
//  takeda
//
//  Created by Serg on 4/2/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "buttonWithID.h"

@interface analizRadioCell : UITableViewCell
@property (nonatomic,retain) IBOutlet UILabel *name;
@property (nonatomic,retain) IBOutlet UILabel *first_param;
@property (nonatomic,retain) IBOutlet UILabel *second_param;

@property (nonatomic,retain) IBOutlet buttonWithID *first_item;
@property (nonatomic,retain) IBOutlet buttonWithID *second_item;
@end
