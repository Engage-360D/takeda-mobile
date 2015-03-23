//
//  UsefulKnowCell.h
//  takeda
//
//  Created by Alexander Rudenko on 19.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface UsefulKnowCell : UITableViewCell

@property (nonatomic, strong) IBOutlet UIImageView *img;
@property (nonatomic, strong) IBOutlet UILabel *headText;
@property (nonatomic, strong) IBOutlet UITextView *bodyText;
@property (nonatomic, strong) IBOutlet UIView *sharePanel;
@property (nonatomic, strong) IBOutletCollection(UIButton) NSArray *shareBtns;

-(void)setupCell:(NSMutableDictionary*)info;
-(float)heightCell:(NSMutableDictionary*)info;

@end
