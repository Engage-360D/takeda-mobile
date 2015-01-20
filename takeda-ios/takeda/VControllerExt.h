//
//  VControllerExt.h
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VControllerExt : UIViewController

@property(nonatomic, strong) IBOutlet UILabel *vcTitle;
@property(nonatomic, strong) IBOutlet UILabel *vcSubTitle;

-(void)drawBorders:(NSArray*)array;
-(void)setNavigationPanel;

@end
