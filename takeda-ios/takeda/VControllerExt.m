//
//  VControllerExt.m
//  takeda
//
//  Created by Alexander Rudenko on 12.01.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "VControllerExt.h"

@interface VControllerExt ()

@end

@implementation VControllerExt

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    [self setNavigationPanel];
    [self setup_interface];
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self setNavImage];
    self.navigationController.navigationBarHidden = NO;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)drawBorders:(NSArray*)array{
    float sepH = 0.5f;
    for (UIView *view in array) {
        CALayer *TopBorder = [CALayer layer];
        TopBorder.frame = CGRectMake(0.0f, 0.0f, view.frame.size.width, sepH);
        TopBorder.backgroundColor = RGB(178, 178, 178).CGColor;
        [view.layer addSublayer:TopBorder];
        CALayer *BottomBorder = [CALayer layer];
        BottomBorder.frame = CGRectMake(0.0f, view.frame.size.height-sepH, view.frame.size.width, sepH);
        BottomBorder.backgroundColor = RGB(178, 178, 178).CGColor;
        [view.layer addSublayer:BottomBorder];
    }

}

-(void)setup_interface{
    self.vcTitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:25];
    self.vcSubTitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];

    //SegoeWP Light
}

#pragma mark - navigation panel
-(void)setNavImage{
    UINavigationBar *navBar = [[self navigationController] navigationBar];
    UIImage *backgroundImage;
    backgroundImage = [[UIImage imageNamed:@"nav_bar_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 10)] ;
    [navBar setBackgroundImage:backgroundImage forBarMetrics:UIBarMetricsDefault];
}

-(void)setNavigationPanel{
    UIView *view = [[UIView alloc] initWithFrame:[self.navigationController.navigationBar frame]];
    
    UIImage *logoImage = [UIImage imageNamed:@"title_logo"];
    
    
    UIImageView *img_logo = [[UIImageView alloc] initWithFrame:CGRectMake(40, 8, logoImage.size.width, logoImage.size.height)];
    img_logo.image = logoImage;
    [view addSubview:img_logo];
    view.backgroundColor = [UIColor clearColor];
    self.navigationItem.titleView = view;
    
    UIImage *menuImage = [UIImage imageNamed:@"menu_icon"];
    UIButton *aButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [aButton setImage:menuImage forState:UIControlStateNormal];
    aButton.frame = CGRectMake(0.0,0.0,menuImage.size.width+20,menuImage.size.height);
    aButton.contentEdgeInsets = (UIEdgeInsets){.left=-20};
    [aButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *menuButton = [[UIBarButtonItem alloc] initWithCustomView:aButton];
    self.navigationItem.leftBarButtonItem = menuButton;
    
    
    UIImage *peopleImage = [UIImage imageNamed:@"people_icon"];
    UIButton *bButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [bButton setImage:peopleImage forState:UIControlStateNormal];
    bButton.frame = CGRectMake(0.0,0.0,peopleImage.size.width+10,peopleImage.size.height);
    bButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    //[bButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *peopleButton = [[UIBarButtonItem alloc] initWithCustomView:bButton];
    
    
    UIImage *alarmImage = [UIImage imageNamed:@"alarm_icon"];
    UIButton *cButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cButton setImage:alarmImage forState:UIControlStateNormal];
    cButton.frame = CGRectMake(0.0,0.0,alarmImage.size.width+10,alarmImage.size.height);
    cButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    //[cButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *alarmButton = [[UIBarButtonItem alloc] initWithCustomView:cButton];
    
    self.navigationItem.rightBarButtonItems = @[peopleButton,alarmButton];
    
}

-(void)openLeftMenu{
    if ([self.slideMenuController isMenuOpen]) {
        [self.slideMenuController closeMenuAnimated:YES completion:nil];
    }else{
        [self.slideMenuController openMenuAnimated:YES completion:nil];
    }
}


@end
