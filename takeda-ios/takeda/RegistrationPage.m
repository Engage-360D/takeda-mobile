//
//  RegistrationPage.m
//  takeda
//
//  Created by Serg on 3/27/14.
//  Copyright (c) 2014 organization. All rights reserved.
//

#import "RegistrationPage.h"
#import <QuartzCore/QuartzCore.h>



@interface RegistrationPage ()

@end

@implementation RegistrationPage
@synthesize scrollView;
@synthesize btn_register;
@synthesize bg_block;
@synthesize name_field;
@synthesize email_field;
@synthesize pass_field;



- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}



-(void)setFieldsSettings{
    for (UIView *view in self.bg_block) {
        CALayer *TopBorder = [CALayer layer];
        TopBorder.frame = CGRectMake(0.0f, 0.0f, view.frame.size.width, 1.0f);
        TopBorder.contents = (id)[UIImage imageNamed:@"bg_separator"].CGImage;
        [view.layer addSublayer:TopBorder];
        CALayer *BottomBorder = [CALayer layer];
        BottomBorder.frame = CGRectMake(0.0f, view.frame.size.height, view.frame.size.width, 1.0f);
        BottomBorder.contents = (id)[UIImage imageNamed:@"bg_separator"].CGImage;
        [view.layer addSublayer:BottomBorder];
    }
    
    [self.btn_register setBackgroundImage:[[UIImage imageNamed:@"button_arrow_bg"] resizableImageWithCapInsets:UIEdgeInsetsMake(10, 10, 10, 30)] forState:UIControlStateNormal];
    
    self.email_field.placeholderColor = RGB(53, 65, 71);
    self.email_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.pass_field.placeholderColor = RGB(53, 65, 71);
    self.pass_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    self.name_field.placeholderColor = RGB(53, 65, 71);
    self.name_field.placeholderFont = [UIFont fontWithName:@"SegoeWP" size:14.0];
    
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    self.title = @"";
    [self setNavImage];
    [self setNavigationPanel];
    [self setFieldsSettings];
    

    self.scrollView.frame = RectSetOrigin(self.view.frame, 0, 0);
    self.scrollView.frame = CGRectMake(0, self.btn_register.frame.size.height+2, 320, self.view.frame.size.height - self.btn_register.frame.size.height);
    [self.view addSubview:self.scrollView];
    [self.scrollView setContentSize:CGSizeMake(320, 670)];

}


-(void)goBack{
    [self.navigationController popViewControllerAnimated:YES];
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


#pragma mark -


#pragma mark - UITextFieldDelegate

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [textField resignFirstResponder];
    return YES;
}
#pragma mark -



@end
