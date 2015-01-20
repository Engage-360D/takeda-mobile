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
@synthesize data_banner;
@synthesize data_page;
@synthesize scrol;



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
    [self clearScrollView];
    if (self.data_banner) {
        if ([self.data_banner objectForKey:@"title"]) {
            self.vcSubTitle.text = [self.data_banner objectForKey:@"title"];
        }else{
            self.vcSubTitle.text  = @"";
        }

    }
    
    [self makeResultation];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setNavImage];
    [self setNavigationPanel];
}



-(void)clearScrollView{
    for (UIView *view in [self.scrol subviews]) {
        [view removeFromSuperview];
    }
}



-(void)makeResultation{
    float Y_pos = 10;
    
    if ([data_banner objectForKey:@"state"] && ![[data_banner objectForKey:@"state"] isEqual:[NSNull null]]) {
        UIImageView *img = [[UIImageView alloc] initWithFrame:CGRectMake(320/2-50/2, Y_pos, 50, 50)];
        img.image = [UIImage imageNamed:[self getNameImage:[data_banner objectForKey:@"state"]]];
        img.contentMode = UIViewContentModeCenter;
        [self.scrol addSubview:img];
        Y_pos = Y_pos + img.image.size.height + 20;
    }
    
    
    if ([data_page objectForKey:@"title"] && ![[data_page objectForKey:@"title"] isEqual:[NSNull null]]) {
        UILabel *title = [[UILabel alloc] init];
        title.text = [data_page objectForKey:@"title"];
        title.font = [self getFontTitle];
        title.frame = CGRectMake(20, Y_pos, 280, [Helper heightText:title.text withFont:title.font withWidth:280]);
        title.numberOfLines = 0;
        title.textColor = RGB(53, 65, 71);
        [self.scrol addSubview:title];
        Y_pos = Y_pos + title.frame.size.height + 30;
    }
    
    if ([data_page objectForKey:@"subtitle"] && ![[data_page objectForKey:@"subtitle"] isEqual:[NSNull null]]) {
        UILabel *subtitle = [[UILabel alloc] init];
        subtitle.text = [data_page objectForKey:@"subtitle"];
        subtitle.font = [self getFontSubTitle];
        subtitle.frame = CGRectMake(20, Y_pos, 280, [Helper heightText:subtitle.text withFont:subtitle.font withWidth:280]);
        subtitle.textColor = RGB(53, 65, 71);
        subtitle.numberOfLines = 0;
        [self.scrol addSubview:subtitle];
        Y_pos = Y_pos + subtitle.frame.size.height + 30;
    }
    
    if ([data_page objectForKey:@"text"] && ![[data_page objectForKey:@"text"] isEqual:[NSNull null]]) {
        UILabel *title_text = [[UILabel alloc] init];
        title_text.text = [data_page objectForKey:@"text"];
        title_text.font = [self getFontText];
        title_text.numberOfLines = 0;
        title_text.frame = CGRectMake(20, Y_pos, 280, [Helper heightText:title_text.text withFont:title_text.font withWidth:280]);
        title_text.textColor = RGB(53, 65, 71);
        [self.scrol addSubview:title_text];
        Y_pos = Y_pos + title_text.frame.size.height + 30;
    }

    [self.scrol setContentSize:CGSizeMake(320, Y_pos + 20)];
    
}






-(UIFont*)getFontTitle{
    return [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
}

-(UIFont*)getFontSubTitle{
    return [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
}

-(UIFont*)getFontText{
    return [UIFont fontWithName:@"SegoeUI-Light" size:17.0];
}


-(NSString*)getNameImage:(NSString*)state{
    if ([state isEqual:[NSNull null]]) {
        return @"";
    }
    SWITCH(state){
        CASE(@"attention"){
            return @"danger_icon_red";
            break;
        }
        CASE(@"ok"){
            return @"good_result_icon_red";
            break;
        }
        CASE(@"bell"){
            return @"bell_icon_red";
            break;
        }
        CASE(@"doctor"){
            return @"doc_tools_icon_red";
            break;
        }
        CASE(@"ask"){
            return @"undefined_result_icon_red";
            break;
        }
        DEFAULT{
            return @"";
            break;
        }
    }
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
