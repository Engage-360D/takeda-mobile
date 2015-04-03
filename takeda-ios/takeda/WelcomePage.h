//
//  WelcomePage.h
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WelcomePage : UIViewController
@property (nonatomic,retain) IBOutlet UILabel *welcome_text_1;
@property (nonatomic,retain) IBOutlet UILabel *welcome_text_2;
@property (nonatomic,retain) IBOutlet UILabel *welcome_text_3;
@property (nonatomic,strong) NSMutableDictionary *theUser;

@property (nonatomic) BOOL is_loading;
@property (nonatomic) BOOL is_authorized;
@property (nonatomic) BOOL taked;

-(void)start;

@end
