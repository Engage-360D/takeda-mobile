//
//  VControllerExt.h
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VControllerExt : UIViewController

@property (nonatomic, strong) IBOutletCollection(UIImageView) NSArray *separators_collection;
@property(nonatomic, strong) IBOutlet UILabel *vcTitle;
@property(nonatomic, strong) IBOutlet UILabel *vcSubTitle;
@property (nonatomic,retain) IBOutlet UILabel *danger_text;

- (BOOL)isRootVC;

-(void)drawBorders:(NSArray*)array;
-(void)setNavigationPanel;
-(void)backAction;

-(UIImageView*)separatorLine;
-(UIBarButtonItem*)menuButton;
-(UIBarButtonItem*)peopleButton;
-(UIBarButtonItem*)alarmButton;
-(UIBarButtonItem*)backBtn;

@end
