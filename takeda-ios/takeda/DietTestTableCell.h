//
//  DietTestTableCell.h
//  takeda
//
//  Created by Alexander Rudenko on 26.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DietTestTableCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UISegmentedControl *segment;
@property (nonatomic, strong) IBOutlet UILabel *title;

-(void)updateSegmentLabel;

@end
