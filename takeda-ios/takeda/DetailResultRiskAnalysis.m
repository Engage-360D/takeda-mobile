//
//  DetailResultRiskAnalysis.m
//  takeda
//
//  Created by Serg on 4/9/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "DetailResultRiskAnalysis.h"

@interface DetailResultRiskAnalysis ()

@end

@implementation DetailResultRiskAnalysis
@synthesize data_result;
@synthesize titleResult;
@synthesize data_banner;
@synthesize data_page;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    if (self.data_banner) {
        if ([self.data_banner objectForKey:@"title"]) {
            self.titleResult.text = [self.data_banner objectForKey:@"title"];
        }else{
            self.titleResult.text  = @"";
        }

    }
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setNavImage];
    [self setNavigationPanel];
}


#pragma mark - navigation panel
-(void)setNavImage{
    UINavigationBar *navBar = [[self navigationController] navigationBar];
    UIImage *backgroundImage;
    backgroundImage = [[UIImage imageNamed:@"nav_bar_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 10)] ;
    [navBar setBackgroundImage:backgroundImage forBarMetrics:UIBarMetricsDefault];
}

-(void)setNavigationPanel{
    UIButton *back_btn = [UIButton buttonWithType:UIButtonTypeCustom];
    back_btn.frame = CGRectMake(0, 0, 80, 40);
    back_btn.titleLabel.font = [UIFont fontWithName:@"Helvetica-Light" size:17.0];
    [back_btn addTarget:self action:@selector(goBack) forControlEvents:UIControlEventTouchDown];
    
    [back_btn setTitle:@"Назад" forState:UIControlStateNormal];
    CALayer *back_arrow = [CALayer layer];
    back_arrow.frame = CGRectMake(0.0f, back_btn.frame.size.height/2 - 7.5, 8, 15.0f);
    back_arrow.contents = (id)[UIImage imageNamed:@"left_white_arrow"].CGImage;
    [back_btn.layer addSublayer:back_arrow];
    UIBarButtonItem *back_item = [[UIBarButtonItem alloc] initWithCustomView:back_btn];
    self.navigationItem.leftBarButtonItem = back_item;
    
    
    
    UIImage *logoImage = [UIImage imageNamed:@"title_logo"];
    UIImageView *img_logo = [[UIImageView alloc] initWithFrame:CGRectMake(40, 8, logoImage.size.width, logoImage.size.height)];
    img_logo.image = logoImage;
    self.navigationItem.titleView = img_logo;
}


-(void)goBack{
    [self.navigationController popViewControllerAnimated:YES];
}
#pragma mark -





@end
