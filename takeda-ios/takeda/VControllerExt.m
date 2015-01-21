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
    [self setupData];
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

-(void)setupData{
    _danger_text.text = @"Имеются противопоказания \n необходимо ознакомиться с инструкцией по применению";
}

-(void)setup_interface{
    self.danger_text.numberOfLines = 0;
    self.danger_text.font = [UIFont fontWithName:@"SegoeWP" size:10];
    self.danger_text.textColor = RGB(55, 64, 76);
    self.vcTitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:25];
    self.vcSubTitle.font = [UIFont fontWithName:@"SegoeWP-Light" size:12];
    for (UIImageView *separ in self.separators_collection){
        separ.height = 0.5f;
        separ.y += 0.5;
    }

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
    

    self.navigationItem.rightBarButtonItems = @[[self peopleButton],[self alarmButton]];
    if (self.isRootVC){
        self.navigationItem.leftBarButtonItem = [self menuButton];
    } else {
        self.navigationItem.leftBarButtonItem = [self backBtn];
    }

    
}

-(void)openLeftMenu{
    if ([self.slideMenuController isMenuOpen]) {
        [self.slideMenuController closeMenuAnimated:YES completion:nil];
    }else{
        [self.slideMenuController openMenuAnimated:YES completion:nil];
    }
}

-(void)backAction{
    [self.navigationController popViewControllerAnimated:YES];
}

-(UIImageView*)separatorLine{
    UIImageView *sp = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.view.width, 0.5f)];
    sp.height = 0.5f;
    sp.backgroundColor = RGB(170, 170, 170);
    return sp;
}

-(UIBarButtonItem*)menuButton{
    UIImage *menuImage = [UIImage imageNamed:@"menu_icon"];
    UIButton *aButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [aButton setImage:menuImage forState:UIControlStateNormal];
    aButton.frame = CGRectMake(0.0,0.0,menuImage.size.width+20,menuImage.size.height);
    aButton.contentEdgeInsets = (UIEdgeInsets){.left=-20};
    [aButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:aButton];
}

-(UIBarButtonItem*)peopleButton{
    UIImage *peopleImage = [UIImage imageNamed:@"people_icon"];
    UIButton *bButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [bButton setImage:peopleImage forState:UIControlStateNormal];
    bButton.frame = CGRectMake(0.0,0.0,peopleImage.size.width+10,peopleImage.size.height);
    bButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    //[bButton addTarget:self action:@selector(openLeftMenu) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:bButton];
}

-(UIBarButtonItem*)alarmButton{
    UIImage *alarmImage = [UIImage imageNamed:@"alarm_icon"];
    UIButton *cButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [cButton setImage:alarmImage forState:UIControlStateNormal];
    cButton.frame = CGRectMake(0.0,0.0,alarmImage.size.width+10,alarmImage.size.height);
    cButton.contentEdgeInsets = (UIEdgeInsets){.left=5};
    return [[UIBarButtonItem alloc] initWithCustomView:cButton];
}

-(UIBarButtonItem*)backBtn{
    UIButton *leftButtonItem = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 48, 30)];
    [leftButtonItem setImage:[UIImage imageNamed:@"back_btn"] forState:UIControlStateNormal];
    leftButtonItem.contentHorizontalAlignment = UIControlContentHorizontalAlignmentLeft;
    [leftButtonItem addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    return [[UIBarButtonItem alloc] initWithCustomView:leftButtonItem];
}

-(BOOL)isRootVC{
    return  self == [self.navigationController.viewControllers objectAtIndex: 0];
}


@end
